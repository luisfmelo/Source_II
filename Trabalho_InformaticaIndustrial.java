/*
 *      by DdS
 */
package trabalho_informaticaindustrial;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class Trabalho_InformaticaIndustrial {
    public static Queue<Operation> listOps = new LinkedList<Operation>();
    public static Queue<Operation> waitingOps = new LinkedList<Operation>();
    public static int[] cellState = new int[10];     //!< Estado das células
    public static char[][] transformationMatrix = new char[][]
        {
            {'-','P','P','P','A','S','S','S','S'},
            {'X','-','A','P','X','X','X','X','X'},
            {'X','X','-','P','X','X','X','X','X'},
            {'X','X','X','-','X','X','X','X','X'},
            {'X','X','X','X','-','S','S','S','S'},
            {'X','X','X','X','X','-','2','X','X'},
            {'X','X','X','X','X','X','-','X','X'},
            {'X','X','X','X','X','X','P','-','S'},
            {'X','X','X','X','X','X','X','X','-'}
        }; // -: same Pkg; X: NotPossible; A: Any Cell; P:Parallel cell; S: Serie Cell 
    public static Calendar theBeginningOfTimes;
    public static Modbus modbusCom = new Modbus();
    public static gui SuperGui = new gui();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //MachineStatistics lol = new MachineStatistics();
        
        //lol.addTransformation(1-1, 4-1, 1);
        
        theBeginningOfTimes = Calendar.getInstance();
        
        for(int i = 0; i < 7; i++)
            cellState[i]=0;
         
        SuperGui.setVisible(true);
        
        SuperGui.addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                try {
                    getReadyToQuit();
                } catch (IOException ex) {
                    Logger.getLogger(Trabalho_InformaticaIndustrial.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            }
        });
                
        //SuperGui.addNewTransformation(1, 5, 3, 5, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 3);
        //SuperGui.addNewTransformation(2, 10, 1, 2, Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance(), 3);
        Modbus modbusCom = new Modbus();
        //Statistics SuperStatistics = new Statistics();
        
        modbusCom.start("127.0.0.1", 6009);
        
        UDP UdpThread = new UDP();
                
        (new Thread(UdpThread)).start();
        
        Manager SuperManager = new Manager();
        
        SimpleDateFormat horadechegada = new SimpleDateFormat("HH:mm:ss|dd.MM.yyyy");
        System.out.println(horadechegada.format(theBeginningOfTimes.getTime()));
        
        //System.out.println("id: " + HeadOp.getId());
        
        //modbusCom.test();
        
        int result = modbusCom.sendOp(3, 1, 6);
        cellState[5] = 1;
        
        System.out.print("cellState:");
        for(int x=0; x<7; x++) {
                System.out.print(cellState[x] + " ");
        }
         System.out.println(";");
        
        System.out.println("Resultado do envio da Op:" + result);
        
        
        
        while(true) 
        {
            modbusCom.updateCellState(cellState);
/*            if(UdpThread.ordersSize() > 0) 
            {
                String received = UdpThread.getUdpOrder();
                
                Operation op = stringToOperation(received);

                listOps.add(op);
                waitingOps.add(op);
                
                System.out.println("Ordem lida no MES:" + (listOps.peek()).getId());
                
                modbusCom.sendOp( (listOps.peek()).getStartPkg(), 
                                  (listOps.peek()).getEndPkg(), 
                                  operationToCell((listOps.peek()).getType()));

            }
            if(modbusCom.isWarehouseFree() == 1)  //se o 1º tapete está livre (registo do codesys)            MUDAR!!!!!!
            {
                SuperManager.doNextOperation(waitingOps); //recebe operação que é para enviar
            }
*/
        }
    }
    
    public static int operationToCell(char ordertype) {
        
        switch(ordertype)
        {
            case('T'):  return 1;

            case('U'):  return 6;

            case('M'):  return 5;

            default:    return 0;

        }
    }
    
    public static Operation stringToOperation(String order) {
        
        //if(order != null) {
            char ordertype  = order.charAt(0);
            int ordernumber = Integer.parseInt(order.substring(1, 4));
            int originpkg   = Integer.parseInt(order.substring(4, 5));
            int finalpkg    = Integer.parseInt(order.substring(5, 6));
            int qty         = Integer.parseInt(order.substring(6, 8));
            
            System.out.println("tipo:" + ordertype + "  numb:" + ordernumber + "  inicial:" + originpkg + "  final:" + finalpkg + " quant:" + qty);
            
            Calendar cal = Calendar.getInstance();
    
            //Unload op1 = new Unload(69, 666, 6, 9, cal);
            
            switch(ordertype)
            {
                case('T'):  System.out.println("Transformation");
                            break;
                    
                case('U'):  System.out.println("Unload");
                            //return new Unload(ordernumber, originpkg, finalpkg, qty, cal);
                            return new Operation(ordertype, ordernumber, originpkg, finalpkg, qty);

                case('M'):  System.out.println("Assembling");
                            break;
                    
                default:    System.out.println("Error!");

            }
        //}
        
        return null;
    }
    
    /**
     *  Create new file to Operator
     */
    public static void getReadyToQuit() throws IOException
    {
        try 
        {
            
            Calendar theEndOfTimes = Calendar.getInstance();
            String content = "Factory Working since " + theBeginningOfTimes.getTime() + ".\r\n";
            content += "Transformations: GET DATA\r\n";
            content += "Factory close at " + theEndOfTimes.getTime() + ".\r\n\r\n";
            double totalTime = theEndOfTimes.getTimeInMillis() - theBeginningOfTimes.getTimeInMillis();
            content += "Total Time Working: " + totalTime + " milisegundos.\r\n\r\n";
            content += "® David Sousa & Luís Melo";// e-mail: <" + SuperGui.getEmail() + ">\r\n\r\n";
            
            File file = new File("LogFile.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                    file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(content);
                bw.close();
            }

            System.out.println("Finally!!!!!");

        } 
        catch (IOException e) 
        {
                e.printStackTrace();
        }
       
      /*  File myFile = new File("Statistics.txt");
        if(!myFile.exists()) 
        {
            myFile.createNewFile();
        } 

        FileOutputStream oFile = new FileOutputStream(myFile, false); 
        oFile.write("dataToWrite");
        System.out.println("Done!");
    }*/
    
    }
}
