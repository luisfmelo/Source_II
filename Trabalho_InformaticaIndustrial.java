/*
 *      by DdS
 */
package trabalho_informaticaindustrial;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

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
    
    public static Modbus modbusCom = new Modbus();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        for(int i = 0; i < 7; i++)
            cellState[i]=0;
        
        modbusCom.start("127.0.0.1", 6009);
        
        UDP UdpThread = new UDP();
                
        (new Thread(UdpThread)).start();
        
        Manager SuperManager = new Manager();
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat horadechegada = new SimpleDateFormat("HH:mm:ss|dd.MM.yyyy");
        System.out.println(horadechegada.format(cal.getTime()));
        
        //System.out.println("id: " + HeadOp.getId());
        
        //modbusCom.test();
        
        modbusCom.sendOp(5, 6, 1);
        
        while(true) 
        {
            if(UdpThread.ordersSize() > 0) 
            {
                String received = UdpThread.getUdpOrder();
                
                Operation op = stringToOperation(received);

                listOps.add(op);
                waitingOps.add(op);
                
                System.out.println("Ordem lida no MES:" + (listOps.peek()).getId());
                
/*                modbusCom.sendOp( (listOps.peek()).getStartPkg(), 
                                  (listOps.peek()).getEndPkg(), 
                                  operationToCell((listOps.peek()).getType()));
*/
            }
            if(true)//se o 1º tapete está livre (registo do codesys)            MUDAR!!!!!!
            {
                SuperManager.doNextOperation(waitingOps); //recebe operação que é para enviar
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
}
