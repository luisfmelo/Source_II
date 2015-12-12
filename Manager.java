package trabalho_informaticaindustrial;

import java.util.*;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.ANSI_GREEN;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.ANSI_RED;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.ANSI_YELLOW;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.cellState;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.modbusCom;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.updateArrived;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.updateMachines;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.updateOngoing;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.updatePushers;

interface Cells {
    public static final int Parallel = 0;
    public static final int Series1  = 1;
    public static final int Series2  = 2;
    public static final int Series3  = 3;
    public static final int Assembly = 4;
    public static final int Unload1  = 5;
    public static final int Unload2  = 6;
}

/**
 * @author LMelo
 * @author David
 */
public class Manager {
    /**
     * Chooses from the operations list the next to be executed
     *
     * @param waitingOps
     */
    public void doNextOperation(LinkedList<Operation> waitingOps, int[][] cellState, Modbus modbusCom) 
    {
        int cell = -1 ;
        
        // Percorrer a lista de determinar a próxima operacao a ser executada
        //System.out.println("ListIterator Approach: ");
        
        for(Iterator<Operation> i = waitingOps.iterator(); i.hasNext() ; ) 
        {
            Operation item = i.next();
            
            System.out.println("qty: " + item.getQuantity() + " finished:" + item.getFinishedPackages());
            
            // Se a ordem que ja foi completada é removida
            if ( (item.getQuantity() - item.getFinishedPackages() - item.getOngoingPackages()) <= 0 ) {
                
                if(item.getQuantity() - item.getFinishedPackages() <= 0) {
                    System.out.format(ANSI_GREEN + "A operação %c%03d foi completada!", item.getType(), item.getId());
                    System.out.println("qty: " + item.getQuantity() + " finished:" + item.getFinishedPackages());
                
                    i.remove();
                }
                
                continue;
            }
            
            if( item.getType() == 'T' )  //se for uma transformaçao... vai ver qual a celula Serie...Paralela, Ambas... Nenhuma
            {
                if(cellDestination(item.getArg1(), item.getArg2(), cellState) == -1) {// Caso estejam todas a ser usadas
                    System.out.println(ANSI_YELLOW + "Debug: células ocupadas.");
                    continue;
                }
                else if(cellDestination(item.getArg1(), item.getArg2(), cellState) == -2) // Caso a transfromação seja para a própria peça
                {
                    //i.remove();
                    
                    /* FALTA:   quando a transformação é para a própria peça
                                tem de se enviar na mesma para o armazém.
                    */
                    System.out.println(ANSI_YELLOW + "Debug: transformação desnecessária.");
                    continue;
                }
                
                // A célula livre em que a peça vai ser transformação
                cell = cellDestination(item.getArg1(), item.getArg2(), cellState);
                
                if(cell < 1) {
                    System.out.println(ANSI_RED + "Erro Manager: não foi possível atribuir uma célula à operação.");
                    continue;
                }
                
                // Enviar a operação para o PLC
                modbusCom.sendOp(item.getArg1(), item.getArg2(), cell); //aqui é que vai acontecer toda a ação
                updateMachines(cell, item.getArg1(), item.getArg2());
                
                item.incrementOngoingPackages();
                updateOngoing('T', item.getId());
                
                // A célula passa a estar ocupada
                cellState[cell-1][0] = 1;
                cellState[cell-1][1] = item.getId();
                
                System.out.println("Enviada operação Transformação para a célula: " + cell);
            }
            
            else if ( item.getType() == 'U' ) //se for descarga
            {
                if ( cellState[Cells.Unload1][0] == 0 && item.getArg2() == 1) //pusher 1 livre e eu quero enviar para o pusher 1
                {
                    cell = Cells.Unload1;
                    modbusCom.sendOp(item.getArg1(), item.getArg2(), cell+1); //envia operação
                    //addOneToPusher(cell+1);  //para a GUI
                    updatePushers(cell+1);

                    cellState[Cells.Unload1][0] = 2;

                    cellState[Cells.Unload1][1] = item.getId();
                    item.incrementOngoingPackages();
                    updateOngoing('U', item.getId());
                    
                    System.out.println("Enviada peça para o Pusher 1");
                }
                else if ( cellState[Cells.Unload2][0] == 0 && item.getArg2() == 2) //pusher 1 livre e eu quero enviar para o pusher 1
                {
                    cell = Cells.Unload1;
                    modbusCom.sendOp(item.getArg1(), item.getArg2(), cell+1); //envia operação
                    cellState[Cells.Unload2][0] = 2;
                    cellState[Cells.Unload2][1] = item.getId();
                    item.incrementOngoingPackages();
                    updateOngoing('U', item.getId());
                    //addOneToPusher(cell+1);  //para a GUI
                    updatePushers(cell+1);                               //para a GUI
                    System.out.println("Enviada peça para o Pusher 2");
                }
                
                System.out.println("Célula de descraga ocupada: " + cellState[Cells.Unload1][0] + " | " + cellState[Cells.Unload2][0]);
            }
            
            else if ( item.getType() == 'M' && cellState[4][0] == 0 ) //se for montagem e se o robot 3D estiver livre
            {
                modbusCom.sendOp(item.getArg1(), item.getArg2(), 4); //envia operação para o robot 3D (4)
                cellState[4][0] = 1;
                cellState[4][1] = item.getId();
                item.incrementOngoingPackages();
                updateOngoing('U', item.getId());
            }
        }
    }
    
    public void updateCellState(LinkedList<Operation> waitingOps, int[][] cellState, Modbus modbusCom) throws InterruptedException
    {
        int[] cellStatePLC;
        char cellType = 'N';
        
        // Vê se alguma célula terminou o processamento
        cellStatePLC = modbusCom.getCellState();
        
        for(int i=0; i < 8; i++) {
            
            // Se a célula terminou o processamento
            if(cellStatePLC[i] > 0 && (i<5)) {
                
                if((i+1) > 0 && (i+1) <= 4)
                    cellType = 'T';
                else if((i+1) == 5)
                    cellType = 'M';

                // Atualizar operação
                System.out.println("Atualizar operação index: " + i);
                int idx = waitingOps.indexOf(findOp(cellType, cellState[i][1], waitingOps));
                
                waitingOps.get(idx).incrementFinishedPackages();
                waitingOps.get(idx).decrementOngoingPackages(); 
                //updateArrived(cellType, idx);
                updateArrived(cellType, cellState[i][1]);
                // Atualizar estado interno das células
                cellState[i][0] = 0;
                cellState[i][1] = 0;
            }
            else if(cellStatePLC[i] > 1 && (i>=5) && (cellState[i][0] == 2)) {
                
                cellType = 'U';
                
                if(cellStatePLC[i] == 2) {
                    // Atualizar operação
                    System.out.println("Atualizar operação de descarga index: " + i);
                    int idx = waitingOps.indexOf(findOp(cellType, cellState[i][1], waitingOps));

                    waitingOps.get(idx).incrementFinishedPackages();
                    waitingOps.get(idx).decrementOngoingPackages();
                    
                    // Atualizar GUI
                    updateArrived(cellType,  cellState[i][1]);
                    
                    // Atualizar estado interno das células
                    cellState[i][0] = 1;
                    cellState[i][1] = 0;
                } 
                else if(cellStatePLC[i] == 1) {
                    cellState[i][0] = cellStatePLC[i];
                }
            }
            else if((i>=5) && (cellState[i][0] != 2)) {
                cellState[i][0] = cellStatePLC[i];
            }
        }
    }
    
    /**
     * Determina onde a TRANSFORMAÇÃO pode ser executada
     * 
     * @param startPkg
     * @param endPkg
     * @param cellState Array com estado de ocupação das células
     * @return 0 .. 3 Célula de destino
     *         -1     Células ocupadas
     *         -2     Transformação para a própria peça
     */
    public int cellDestination(int startPkg, int endPkg, int[][] cellState)
    {
        char c = Trabalho_InformaticaIndustrial.transformationMatrix[startPkg-1][endPkg-1];
        
        System.out.println("cellDestination: " + c);
        
        if(c == '-') // nao e preciso transformação vai direto para o armazem pela 1ª celula serie livre
        {
            if(cellState[0][0] == 0)
                return 1;
            else if(cellState[1][0] == 0)
                return 2;
            else if(cellState[2][0] == 0)
                return 3;
            else if(cellState[3][0] == 0)
                return 4;
            else
                return -1;
        }
        else if(c == 'X') // nao e possivel a transformação
        {
            return -2;
        }
        else if(c == 'P') // Paralelo
        {
            if(cellState[0][0] == 0)
                return 1;
            else
                return -1;
        }
        else if(c == 'S') // Serie
        {
            if(cellState[1][0] == 0)
                return 1;
            else if(cellState[2][0] == 0)
                return 2;
            else if(cellState[3][0] == 0)
                return 3;
            else
                return -1;
        }
        else if(c == 'A') // Ambos... dou prioridade a transformar nas série
        {
            System.out.println("A operação pode ser feita em ambos os tipos de células!");
            
            if(cellState[0][0] == 0)            //MUDEI ISTO POR CAUSA DA PRIORIDADE
                return 1;
            else if(cellState[1][0] == 0)
                return 2;
            else if(cellState[2][0] == 0)
                return 3;
            else if(cellState[3][0] == 0)
                return 4;
            else
                return -1;
        }

        return -1;
    }
    
    public Operation findOp(char type, int id, Queue<Operation> waitingOps)
    {
        Iterator<Operation> i = waitingOps.iterator();
        
        System.out.println("A procurar a operação do tipo " + type + " com id " + id + ".");
        
        while(i.hasNext()) {
            Operation item = i.next();
            
            if(item.getType() == type && item.getId() == id) {
                System.out.println("Operação encontrada.");
                return item;
            }
        }
        
        System.out.println(ANSI_RED + "Erro Manager: não foi possível localizar a operação desejada.");
        
        return null;
    }

    private void addOneToPusher(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}