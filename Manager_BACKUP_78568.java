/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;

import java.util.*;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.cellState;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.modbusCom;

interface Cells {
    public static final int Parallel = 1;
    public static final int Series1  = 2;
    public static final int Series2  = 3;
    public static final int Series3  = 4;
    public static final int Assembly = 5;
    public static final int Unload1  = 6;
    public static final int Unload2  = 7;
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
    public void doNextOperation(Queue<Operation> waitingOps) 
    {
        int cell = -1 ;
        
<<<<<<< HEAD
        Modbus modbusCom = new Modbus();
        
        // Percorrer a lista de determinar a próxima operacao a ser executada
        //System.out.println("ListIterator Approach: ");
        
=======
        // Lê os estados das células no PLC
        cellState = modbusCom.readPLCState();
    
>>>>>>> origin/master
        for(Iterator<Operation> i = waitingOps.iterator(); i.hasNext() ; ) 
        {
            Operation item = i.next();
            
            if ( item.getQuantity() == 0 ) // se tenho uma ordem que ja foi completada
                i.remove();
            
            if( item.getType() == 'T' )  //se for uma transformaçao... vai ver qual a celula Serie...Paralela, Ambas... Nenhuma
            {
                if(cellDestination(item.getArg1(), item.getArg2()) == -1) //caso estejam todas a ser usadas
                    continue;
                else if(cellDestination(item.getArg1(), item.getArg2()) == -2) //caso retorne X
                {
                    i.remove();
                    continue;
                }
                cell = cellDestination(item.getArg1(), item.getArg2());
                if (cell >= 0)
                    cellState[cell] = 0;
                    
                item.setQuantity(item.getQuantity()-1);
                modbusCom.sendOp(item.getArg1(), item.getArg2(), cell); //envia operação
            }
            
            else if ( item.getType() == 'U' ) //se for descarga
            {
                if ( cellState[Cells.Unload1] == 0 && item.getArg2() == 1) //pusher 1 livre e eu quero enviar para o pusher 1
                {
                    cell = Cells.Unload1;
                    modbusCom.sendOp(item.getArg1(), item.getArg2(), cell); //envia operação
                    cellState[Cells.Unload1] = 0;
                }
                else if ( cellState[Cells.Unload2] == 0 && item.getArg2() == 2) //pusher 1 livre e eu quero enviar para o pusher 1
                {
                    cell = Cells.Unload1;
                    modbusCom.sendOp(item.getArg1(), item.getArg2(), cell); //envia operação
                    cellState[Cells.Unload2] = 0;
                }
            }
            
            else if ( item.getType() == 'M' && cellState[4] == 0 ) //se for montagem e se o robot 3D estiver livre
            {
                modbusCom.sendOp(item.getArg1(), item.getArg2(), 4); //envia operação para o robot 3D (4)
                cellState[4] = 0;
            }
            //System.out.println(item.getId());
        }
    }
    
    /**
     * Determina onde a TRANSFORMAÇÃO pode ser executada
     * 
     * @param startPkg
     * @param endPkg
     * @return 0 .. 3 Célula de destino
     *         -1     Células ocupadas
     *         -2     Transformação para a própria peça
     */
    public int cellDestination(int startPkg, int endPkg)
    {
        char c = Trabalho_InformaticaIndustrial.transformationMatrix[startPkg-1][endPkg-1];
        
        if(c == '-') // nao e preciso transformação vai direto para o armazem pela 1ª celula serie livre
        {
            if(cellState[1] == 0)
                return 1;
            else if(cellState[2] == 0)
                return 2;
            else if(cellState[3] == 0)
                return 3;
            else
                return -1;
        }
        else if(c == 'X') // nao e possivel a transformação
        {
            return -2;
        }
        else if(c == 'P') // Paralelo
        {
            if(cellState[0] == 0)
                return 0;
            else
                return -1;
        }
        else if(c == 'S') // Serie
        {
            if(cellState[1] == 0)
                return 1;
            else if(cellState[2] == 0)
                return 2;
            else if(cellState[3] == 0)
                return 3;
            else
                return -1;
        }
        else if(c == 'A') // Ambos... dou prioridade a fazer se nas série
        {
            if(cellState[1] == 0)
                return 1;
            else if(cellState[2] == 0)
                return 2;
            else if(cellState[3] == 0)
                return 3;
            else if(cellState[0] == 0)
                return 0;
            else
                return -1;
        }

        return -1;
    }
   
}
