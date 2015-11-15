/*
 *      by DdS
 */
package trabalho_informaticaindustrial;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        
        UDP UdpThread = new UDP();
        
        (new Thread(UdpThread)).start();
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat horadechegada = new SimpleDateFormat("HH:mm:ss|dd.MM.yyyy");
        System.out.println(horadechegada.format(cal.getTime()));
        
        //System.out.println("id: " + HeadOp.getId());
        
        while(true) {
            if(UdpThread.ordersSize() > 0) {
                String received = UdpThread.getUdpOrder();
                
                Operation op = stringToOperation(received);
                
                System.out.println("Ordem lida no MES:" + op.getId());
            }
        }
        
/*        
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
                
                for (int i=0; i < 10; i++)  {
                    try {
                        modbusTCPMaster.writeSingleRegister(i, fuku);
                    } catch(Exception writeS) {
                        System.out.println("Error writing to:" + i);
                    }
                }
                
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
        */
    }
    
    public static Operation stringToOperation(String order) {
        
        //if(order != null) {
            String ordertype   = order.substring(0, 1);
            int ordernumber = Integer.parseInt(order.substring(1, 4));
            int originpkg   = Integer.parseInt(order.substring(4, 5));
            int finalpkg    = Integer.parseInt(order.substring(5, 6));
            int qty         = Integer.parseInt(order.substring(6, 8));
            
            System.out.println("tipo:" + ordertype + "  numb:" + ordernumber + "  inicial:" + originpkg + "  final:" + finalpkg + " quant:" + qty);
            
            Calendar cal = Calendar.getInstance();
    
            Unload op1 = new Unload(69, 666, 6, 9, cal);
            
            switch(ordertype)
            {
                case("T"):  System.out.println("Transformation");
                            break;
                    
                case("U"):  System.out.println("Unload");
                            return new Unload(ordernumber, originpkg, finalpkg, qty, cal);

                case("M"):  System.out.println("Assembling");
                            break;
                    
                default:    System.out.println("Error!");

            }
        //}
        
        return null;
    }
}
