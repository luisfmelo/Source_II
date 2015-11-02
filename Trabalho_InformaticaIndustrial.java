/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;

import net.wimpi.modbus.facade.ModbusTCPMaster;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.io.*;
import net.wimpi.modbus.net.*;
import net.wimpi.modbus.util.*;

/**
 *
 * @author David
 */
public class Trabalho_InformaticaIndustrial {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ModbusTCPMaster modbusTCPMaster = new ModbusTCPMaster("127.0.0.1", 6009);
        
        try {
            modbusTCPMaster.connect();
        } catch(Exception seila) {
            System.out.println("Not connected!");
        }
        
        System.out.println("Connected to PLC!");
        
        InputRegister[] res = null;
        
        while(true){
            try {
                Register fuku = new SimpleRegister(609);
                Register[] valores = new Register[3];
                InputRegister readvalues = new SimpleInputRegister();
                //fuku.setValue(5);
                
                try {
                    for(int j=0; j < 3; j++) {
                        valores[j] = new SimpleRegister(69);
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
     /*           
                for (int i=0; i < 10; i++)  {
                    try {
                        modbusTCPMaster.writeSingleRegister(i, fuku);
                    } catch(Exception writeS) {
                        System.out.println("Error writing to:" + i);
                    }
                }
  */              
                //valores[0].setValue(69);
                
                //modbusTCPMaster.writeMultipleRegisters(0, valores);
                
                res = modbusTCPMaster.readInputRegisters(0, 5);
                
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
}
