/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Queue;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.cellState;

/**
 *
 * @author David
 */
public class Manager {
    /**
     * Chooses from the operations list the next to be executed
     *
     * @return Operation Next operation to be executed
     */
    public void doNextOperation(Queue<Operation> waitingOps) 
    {
        int cell = -1 ;
        
        Modbus modbusCom = new Modbus();
        
        // Percorrer a lista de determinar a próxima operacao a ser executada
        System.out.println("ListIterator Approach: ");
        
        for(Iterator<Operation> i = waitingOps.iterator(); i.hasNext() ; ) 
        {
            Operation item = i.next();
            if ( item.getQuantity() == 0 ) // se tenho uma ordem que ja foi completada
                i.remove();
            
            if( item.getType() == 'T' )  //se for uma transformaçao... vai ver qual a celula Serie...Paralela, Ambas... Nenhuma
            {
                if(cellDestination(item.getStartPkg(), item.getEndPkg()) == -1) //caso estejam todas a ser usadas
                    continue;
                cell = cellDestination(item.getStartPkg(), item.getEndPkg());
                item.setQuantity(item.getQuantity()-1);
                modbusCom.sendOp(item.getStartPkg(), item.getEndPkg(), cell); //envia operação
            }
            
            else if ( item.getType() == 'U' ) //se for descarga
            {
                //if ( !cellState[5] && item.get) //pusher 1 livre e eu quero enviar para o pusher 1
            }
            
            else if ( item.getType() == 'M' && cellState[4] == 0 ) //se for montagem e se o robot 3D estiver livre
                modbusCom.sendOp(item.getStartPkg(), item.getEndPkg(), 4); //envia operação para o robot 3D (4)
            
            System.out.println(item.getId());
        }
        
        
    }
    
    public int cellDestination(int startPkg, int endPkg)
    {
        //char c = Trabalho_InformaticaIndustrial.transformationMatrix[startPkg-1][endPkg-1];
        
        return -1;
    }
   
}
