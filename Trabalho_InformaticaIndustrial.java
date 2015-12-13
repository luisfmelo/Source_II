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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class Trabalho_InformaticaIndustrial {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss dd MMM yyyy");
    
    public static LinkedList<Operation> listOps = new LinkedList<Operation>();
    public static LinkedList<Operation> waitingOps = new LinkedList<Operation>();
    public static int[][] cellState = new int[10][2];     //!< Estado das células (a segunda dimensão indica o id da operação em execução)
    public static char[][] transformationMatrix = new char[][]
        {
            {'-','P','P','P','A','S','S','S','S'},
            {'X','-','A','P','X','X','X','X','X'},
            {'X','X','-','P','X','X','X','X','X'},
            {'X','X','X','-','X','X','X','X','X'},
            {'X','X','X','X','-','S','S','S','S'},
            {'X','X','X','X','X','-','A','X','X'},
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
    public static void main(String[] args) throws InterruptedException {
        
        //MachineStatistics lol = new MachineStatistics();
        
        //lol.addTransformation(1-1, 4-1, 1);
        
        theBeginningOfTimes = Calendar.getInstance();
        
        for(int i = 0; i < 7; i++) {
            cellState[i][0]=0; // Estado da célula           
            cellState[i][1]=0; // Id da Operação em execução
        }
        
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
        
        //int result = modbusCom.sendOp(3, 1, 6);
        //cellState[5] = 1;
        
        System.out.print("cellState:");
        for(int x=0; x<7; x++) {
                System.out.print(cellState[x][0] + " ");
        }
         System.out.println(";");
        
        //modbusCom.test();
        
        
         
        //System.out.println("Resultado do envio da Op:" + result);
        
        while(true) 
        {
            //modbusCom.updateCellState(cellState);
            
            if(UdpThread.ordersSize() > 0) 
            {
                String received = UdpThread.getUdpOrder();
                
                Operation op = stringToOperation(received);

                if(!listOps.add(op))
                    System.out.println("Não foi possível adicionar operação!");
                if(!waitingOps.add(op))
                    System.out.println("Não foi possível adicionar operação!");
                    
                System.out.println("Adicionada nova operação! Lista das operações:");
                
                Iterator<Operation> i = waitingOps.iterator();
                while(i.hasNext()) {
                    Operation item = i.next();
                    System.out.println(item.getType()+ "(" + item.getId() + "): " + item.getArg1() + " -> " + item.getArg2());
                }                
                
                System.out.println("Ordem lida no MES:" + (listOps.element()).getId());
                
                /*modbusCom.sendOp( (listOps.peek()).getStartPkg(), 
                                  (listOps.peek()).getEndPkg(), 
                                  operationToCell((listOps.peek()).getType()));
                */
            }

            
            SuperManager.updateCellState(waitingOps, cellState, modbusCom);
            
            if(modbusCom.isWarehouseFree() == 1)  //se o 1º tapete está livre (registo do codesys)            MUDAR!!!!!!
            {
                SuperManager.doNextOperation(waitingOps, cellState, modbusCom); //recebe operação que é para enviar
            }
            

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
            
            System.out.println("tipo:" + ordertype + "  numb:" + ordernumber + "  inicial:" + originpkg + " final:" + finalpkg + " quant:" + qty);
            
            Calendar cal = Calendar.getInstance();
            
            switch(ordertype)
            {
                case('T'):  System.out.println("Transformation");
                            SuperGui.addNewTransformation(ordernumber, qty, originpkg, finalpkg);
                            return new Operation(ordertype, ordernumber, originpkg, finalpkg, qty);
                    
                case('U'):  System.out.println("Unload");
                            SuperGui.addNewUnload(ordernumber, qty, originpkg, finalpkg);
                            //return new Unload(ordernumber, originpkg, finalpkg, qty, cal);
                            return new Operation(ordertype, ordernumber, originpkg, finalpkg, qty);

                case('M'):  System.out.println("Assembling");
                            SuperGui.addNewAssemble(ordernumber, qty, originpkg, finalpkg);
                            return new Operation(ordertype, ordernumber, originpkg, finalpkg, qty);
                    
                default:    System.out.println("Error!");
                            return null;
            }
        //}
    }
    
    /**
     *  Create new file to Operator
     */
    public static void getReadyToQuit() throws IOException
    {
        try 
        {
            
            Calendar theEndOfTimes = Calendar.getInstance();
            //Initialize
            String content = "Factory Working since " + dateFormat.format(theBeginningOfTimes.getTime()).toString() + ".\r\n";
            
            //Get DATA
            content += SuperGui.getStatistics();
            
            //Finalize
            content += "Factory closed since " + dateFormat.format(theEndOfTimes.getTime()).toString() + ".\r\n\r\n";
            double totalTime = theEndOfTimes.getTimeInMillis() - theBeginningOfTimes.getTimeInMillis();
            int sec  = (int)(totalTime/ 1000) % 60 ;
            int min  = (int)((totalTime/ (1000*60)) % 60);
            int hr   = (int)((totalTime/ (1000*60*60)) % 24);
            content += "Total Time Working: " + hr + "h " + min+ "m " + sec + "s.\r\n\r\n";
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
    
    public static void updateOngoing(char type, int id)
    {
        if ( type == 'T')
            SuperGui.oneTransformationGoing(id);
        else if ( type == 'U')
            SuperGui.oneUnloadGoing(id);
        else if ( type == 'M')
            SuperGui.oneAssembleGoing(id);
    }
    
    public static void updateArrived(char type, int id)
    {
        if ( type == 'T')
            SuperGui.oneTransformationArrived(id);
        else if ( type == 'U')
            SuperGui.oneUnloadArrived(id);
        else if ( type == 'M')
            SuperGui.oneAssembleArrived(id);
    }
    
    public static void updateMachines(int cell, int pkgInit, int pkgFinal)
    {
        if ( pkgInit != pkgFinal)
        {
            if (cell == 1 ) //falemos da maquina paralela
            {
                if (pkgInit == 1 || pkgInit == 3 )
                {
                    SuperGui.addOneToMachines(cell, 'c', pkgInit);
                    pkgInit ++;
                    updateMachines(cell, pkgInit, pkgFinal);
                }
                else if ( pkgInit == 8)
                {
                    SuperGui.addOneToMachines(cell, 'c', pkgInit);
                    pkgInit --; //so pode ir para a 7
                    updateMachines(cell, pkgInit, pkgFinal);
                }  
                else
                {
                    SuperGui.addOneToMachines(cell, 'a', pkgInit);
                    pkgInit ++;
                    updateMachines(cell, pkgInit, pkgFinal);
                }
            }
            else //falemos agora das maquina série
            {
                if (pkgInit == 6 )
                {
                    SuperGui.addOneToMachines(cell, 'a', pkgInit);
                    pkgInit ++;
                    updateMachines(cell, pkgInit, pkgFinal);
                }
                else if (pkgInit == 1 )
                {
                    SuperGui.addOneToMachines(cell, 'a', pkgInit);
                    pkgInit = 5;
                    updateMachines(cell, pkgInit, pkgFinal);
                }
                else if ( pkgInit == 5 && pkgFinal == 8)
                {
                    SuperGui.addOneToMachines(cell, 'b', pkgInit);
                    pkgInit = 8;
                    updateMachines(cell, pkgInit, pkgFinal);
                }
                else if ( pkgInit == 5 || pkgInit == 8)
                {
                    SuperGui.addOneToMachines(cell, 'b', pkgInit);
                    pkgInit ++;
                    updateMachines(cell, pkgInit, pkgFinal);
                }
            }
        }
        
        
        //SuperGui.addOneToMachines(cell, machine, pkg);
    }
    
    public static void updatePushers(int n)
    {
        SuperGui.addOneToPusher(n);
    }
    
}
