/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;

import java.util.Calendar;

/**
 *
 * @author Toshiba
 */
public class MachineStatistics {
    private String name;
    private int workingTime; //in seconds
    private int[] numberOfPackages = new int[8];
    
    private int[][] cellTimeStats = new int[4][2];      //!< Array que vai guardar o tempo de funcinamento de cada máquina
    private int[][][] cellPkgStats = new int[4][2][9];   //!< Array que vai guardar o nº de peças processadas de cada máquina por peça inicial
    
    public static char[][] transformationsTime = new char[][] {
        { 0, 25,  0,  0, 25,  0,  0,  0,  0},
        { 0,  0, 20,  0,  0,  0,  0,  0,  0},
        { 0,  0,  0, 10,  0,  0,  0,  0,  0},
        { 0,  0,  0,  0,  0,  0,  0,  0,  0},
        { 0,  0,  0,  0,  0, 25,  0, 25,  0},
        { 0,  0,  0,  0,  0,  0, 25,  0,  0},
        { 0,  0,  0,  0,  0,  0,  0,  0,  0},
        { 0,  0,  0,  0,  0,  0,  0,  0, 20},
        { 0,  0,  0,  0,  0,  0,  0,  0,  0}
    };
    
    public static char[][] transformationsMachine = new char[][] {
        { 0,  3,  0,  0,  1,  0,  0,  0,  0},
        { 0,  0,  1,  0,  0,  0,  0,  0,  0},
        { 0,  0,  0,  3,  0,  0,  0,  0,  0},
        { 0,  0,  0,  0,  0,  0,  0,  0,  0},
        { 0,  0,  0,  0,  0,  2,  0,  2,  0},
        { 0,  0,  0,  0,  0,  0,  1,  0,  0},
        { 0,  0,  0,  0,  0,  0,  0,  0,  0},
        { 0,  0,  0,  0,  0,  0,  0,  0,  2},
        { 0,  0,  0,  0,  0,  0,  0,  0,  0}
    };
    
    /**
     * Adiciona às estatisticas os dados
     * 
     * @param pkgStart
     * @param pkgEnd
     * @param cellId 1:Paralela 2:SérieA 3:SérieB 4:SérieC
     */
    public void addTransformation(int pkgStart, int pkgEnd, int cellId)
    {
        int[][] tempTranf = new int[5][2];  //!< Serve para guardar a transformações intermédias
   
        // Se a transformação é directa
        if(transformationsTime[pkgStart][pkgEnd] > 0) {
            // Adiciona o tempo de processamento da transformação
            cellTimeStats[cellId-1][getCellMachine(cellId, transformationsMachine[pkgStart][pkgEnd])] += transformationsTime[pkgStart][pkgEnd];
            // Adiciona ao nº de peças processadas
            cellPkgStats[cellId-1][getCellMachine(cellId, transformationsMachine[pkgStart][pkgEnd])][pkgStart]++;
            System.out.println(pkgStart+1 + "->" + (pkgEnd+1));
        }
        else {
            // Procurar a peça que dê para transformar na final
            int k = 0;
            int tempend = pkgEnd;
            int tempstart = 0;
            boolean found = false;
            
            while(tempstart < 9 & found != true) {
                
                // Peça encontrada
                if(transformationsTime[tempstart][tempend] > 0) {
                    
                    // Se a peça for igual à inicial
                    if(tempstart == pkgStart) {
                        //!!! Adiciona estatisticas e termina
                        k--;
                        while(k >= 0) {
                            // Adiciona o tempo de processamento da transformação
                            cellTimeStats[cellId-1][getCellMachine(cellId, transformationsMachine[tempTranf[k][0]][tempTranf[k][1]])] += transformationsTime[tempTranf[k][0]][tempTranf[k][1]];
                            // Adiciona ao nº de peças processadas
                            cellPkgStats[cellId-1][getCellMachine(cellId, transformationsMachine[tempTranf[k][0]][tempTranf[k][1]])][tempTranf[k][0]]++;
                            k--;
                        }
                        
                        // Adiciona o tempo de processamento da transformação
                        cellTimeStats[cellId-1][getCellMachine(cellId, transformationsMachine[tempstart][tempend])] += transformationsTime[tempend][tempstart];
                        // Adiciona ao nº de peças processadas
                        cellPkgStats[cellId-1][getCellMachine(cellId, transformationsMachine[tempstart][tempend])][tempstart]++;

                        System.out.println(tempend+1 + "<-" + (tempstart+1));
                        found = true;
                        return;
                    }
                    else {
                        //!!! Adiciona estatisticas temporarias
                        tempTranf[k][0] = tempstart;
                        tempTranf[k][1] = tempend;
                        
                        System.out.println(tempend+1 + "<-" + (tempstart+1));
                        tempend = tempstart;
                        tempstart = 0;
                        k++;
                    }
                }
                else {
                    tempstart++;
                }
            }
            
            //!!! Transformação Impossível
            //return Error;
            System.out.println("Impossível");
        }    
    }
    
    /**
     * Usada para converter a máquina de 0..3 da matriz transformationsMachine para 0..1 para a cellStats
     * 
     * @param cellId 1..4
     * @param rawMachine
     * @return  -1 Error
     *         >=0 Máquina
     */
    private int getCellMachine(int cellId, int rawMachine) {
        if(cellId < 1 || cellId > 3) {
            System.out.println("A função \"getCellMachine\" recebeu o parâmetro cellId errado:" + cellId);
            return -1;
        }
        
        if(cellId == 1) {
            if(rawMachine == 1)
                return 0;
            else if(rawMachine == 3)
                return 1;
            else {
                System.out.println("A função \"getCellMachine\" recebeu o parâmetro rawMachine errado:" + rawMachine + " quando cellId era:" + cellId);
                return -1;
            }
        } 
        else if(cellId == 2 || cellId ==3 || cellId == 4) {
            if(rawMachine == 1)
                return 0;
            else if(rawMachine == 2)
                return 1;
            else {
                System.out.println("A função \"getCellMachine\" recebeu o parâmetro rawMachine errado:" + rawMachine + " quando cellId era:" + cellId);
                return -1;
            }
        }
        
        System.out.println("Ocorreu um erro na função \"getCellMachine\" rawMachine:" + rawMachine + " cellId:" + cellId);
        return -1;
    }
    
    public void printStats() {
        //...
    } 
}
