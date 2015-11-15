/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;

import java.util.Queue;

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
    public Operation getNextOperation(Queue<Operation> listOps) {
        
        // Percorrer a lista de determinar a próxima operacao a ser executada
        
        // DEBUG: Neste caso vamos considerar a é a primeira da fila
        return listOps.peek();
    }
    
    
}
