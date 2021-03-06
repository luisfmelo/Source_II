/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;

import java.net.ConnectException;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.facade.ModbusTCPMaster;
import net.wimpi.modbus.*;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.ANSI_RED;
import static trabalho_informaticaindustrial.Trabalho_InformaticaIndustrial.ANSI_YELLOW;

public class Modbus {
    
    private ModbusTCPMaster modbusTCPMaster;
    
    /**
     * Inicia o servidor Modbus.
     * 
     * @param ip
     * @param port 
     */
    public void start(String ip, int port) throws Exception {
        modbusTCPMaster = new ModbusTCPMaster(ip, port);
        
        try {
            modbusTCPMaster.connect();
        } catch(ConnectException notconnected) {
            System.out.println("Error connecting to PLC! Please start remote client first...\n" + notconnected);
            System.exit(0);
        }
                
        System.out.println("Connected to PLC!");
    }
    
    /**
     * Envia uma ordem para o PLC.
     * 
     * @param arg1 Peça inicial
     * @param arg2 Peça final/destino
     * @param cellDestination Célula destino
     * @return 0 se foi enviada com successo
     *         1 se ocorreu um erro
     */
    public int sendOp(int arg1, int arg2, int cellDestination) {
        Register[] valores = new Register[4];
        
        // Espera enquanto o armazem fica livre
        while(isWarehouseFree() != 1);
        
        valores[0] = new SimpleRegister(arg1);
        valores[1] = new SimpleRegister(arg2);
        valores[2] = new SimpleRegister(cellDestination);
        valores[3] = new SimpleRegister(1);
        
        try {
            modbusTCPMaster.writeMultipleRegisters(0, valores);
        } catch(Exception multiplewriteerror) {
            System.out.println("Error writeMultipleRegisters");
            return 1;
        }
        
        // Espera que o warehouse inicie o processo
        while(isWarehouseFree() != 0);
        
        // Faz reset aos dados
        valores[0] = new SimpleRegister(0);
        valores[1] = new SimpleRegister(0);
        valores[2] = new SimpleRegister(0);
        valores[3] = new SimpleRegister(0);
        
        try {
            modbusTCPMaster.writeMultipleRegisters(0, valores);
        } catch(Exception multiplewriteerror) {
            System.out.println("Error writeMultipleRegisters");
            return 1;
        }
        
        System.out.println("Writed:" + arg1 + ":" + arg2 + ":" + cellDestination);
        return 0;
    }
    
    /**
     * Lê os estados das células do PLC.
     * 
     * @return 
     */
    public int[] getCellState() throws InterruptedException {
        int[] cellStatePLC = new int[9];
        
        InputRegister[] readvalue = null;
        Register ack = new SimpleRegister(1);
        
        // Lê do PLC
        try {
            readvalue = modbusTCPMaster.readInputRegisters(20, 10);
        } catch(Exception multiplereaderror) {
            System.out.println("Error readMultipleRegisters updateCellState");
            return null;
        }
        
        // Iteracção para cada célula
        for(int i=0; i < 8; i++) {
                             
            // Ver se célula terminou
            if((readvalue[i+1].getValue() > 0 && ((i+1) <= 5)) || (readvalue[i+1].getValue() > 1 && ((i+1) == 6 || (i+1) == 7))) {

                System.out.println("A célula " + i + " terminou o processamento!");

                // O ack às vezes não chega ao PLC por isso tentamos 10 vezes
                for(int attempt = 0; attempt < 99999; attempt++) {
                
                    // Dar o ack do acontecimento
                    try {
                        ack.setValue(1);
                        modbusTCPMaster.writeSingleRegister(4+i, ack);
                    } catch(Exception multiplereaderror) {
                        System.out.println("Error readMultipleRegisters updateCellState");
                    }

                    System.out.println("Acknoledge enviado para a célula " + i + ".");

                    // Verificar que o ack foi recebido
                    try {
                        readvalue = modbusTCPMaster.readInputRegisters(20, 10);
                    } catch(Exception multiplereaderror) {
                        System.out.println("Error readMultipleRegisters updateCellState");
                    }

                    // Se foi recebido continua
                    if((readvalue[i+1].getValue() == 0 && (i+1) < 6) || (readvalue[i+1].getValue() < 2 && (i+1) == 6 || (i+1) == 7))
                        break;
                    
                    // Esperar 500 ms entre tentativas
                    Thread.sleep(500);
                    
                    System.out.println(ANSI_YELLOW + "Não foi possível confirmar que o PLC recebeu o acknowledge.");
                }
                              
                if((i+1) < 6) cellStatePLC[i] = 1;          // A célula já está livre
                else if((i+1) == 6 || (i+1) == 7) cellStatePLC[i] = 2;    // A célula de descarga está livre
                
                System.out.println("O acknoledge foi recebido pela célula " + i + ".");
                    
                // Limpar o ack
                try {
                    ack.setValue(0);
                    modbusTCPMaster.writeSingleRegister(4+i, ack);
                } catch(Exception multiplereaderror) {
                    System.out.println("Error readMultipleRegisters updateCellState");
                }
            } else {
                cellStatePLC[i] = 0;
            }
            
            // Caso seja um pusher e esteja ocupado
            if((readvalue[i+1].getValue() == 1 && ((i+1) == 6 || (i+1) == 7)) && cellStatePLC[i] != 2) {
                cellStatePLC[i] = 1;
            }
            
            // Célula CT3
            if((readvalue[i+1].getValue() == 1 && (i+1) == 8)) {
                cellStatePLC[i] = 1;
            }
        }
        
        return cellStatePLC;
    }
    
    public int[] readPLCState() {
        InputRegister[] readvalues = null;
        int[] intvalues = new int[10];
                
        try {
            readvalues = modbusTCPMaster.readInputRegisters(20, 10);
        } catch(Exception multiplereaderror) {
            System.out.println("Error readMultipleRegisters");
        }
      
        System.out.print("Cells State:");
        
        for(int i = 0; i < 10; i++) {
            intvalues[i] = readvalues[i].getValue();
        
            System.out.print(" " + intvalues[i]);
        }
        
        return intvalues;
    }
    
    public int isWarehouseFree() {
        InputRegister[] readvalue = null;
        
        try {
            readvalue = modbusTCPMaster.readInputRegisters(20, 1);
            
            return readvalue[0].getValue();
        } catch(Exception warehousefree) {
            System.out.println(ANSI_RED + "Erro modbus: não foi possível ler o estado do armazém.");
        }
        
        return -1;
    }
    
    public void test() {
        try {
            InputRegister[] res = null;

            Register fuku = new SimpleRegister(69);
            Register[] valores = new Register[3];
            InputRegister readvalues = new SimpleInputRegister();
            //fuku.setValue(5);

            try {
                for(int j=0; j < 3; j++) {
                    valores[j] = new SimpleRegister(j+100);
                    System.out.println("Writed to array");
                }
            } catch(Exception writingarray) {
                System.out.println("Error writing to array");
            }

            try {
                modbusTCPMaster.writeMultipleRegisters(10, valores);
            } catch(Exception picas) {
                System.out.println("Error writeMultipleRegisters");
            }

            for (int i=0; i < 25; i++)  {
                try {
                    fuku.setValue(i);
                    modbusTCPMaster.writeSingleRegister(i, fuku);
                } catch(Exception writeS) {
                    System.out.println("Error writing to:" + i);
                }
            }

            //valores[0].setValue(69);

            //modbusTCPMaster.writeMultipleRegisters(0, valores);
            try {
            res = modbusTCPMaster.readInputRegisters(20, 5);
            } catch(Exception seila) {
                System.out.println("Error reading: " + seila);
            }
            System.out.println("Update");                

            System.out.print("Data:");

            for (int i=0; i < 5; i++)  {
                 System.out.print(res[i].getValue() + " ");
            }
        } catch(Exception seila) {System.out.println("Error updating");}


        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            // Handle the exception
        }
    }
}
