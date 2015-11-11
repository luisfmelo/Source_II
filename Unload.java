/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;

import java.util.Calendar;
import static trabalho_informaticaindustrial.OpState.*;

/**
 *
 * @author David
 */
public class Unload extends Operation {
    private int destination;
    private int pkg;
    Calendar cal = Calendar.getInstance();
    
    public Unload(int orderId, int orderPkg, int dest, int qty, Calendar checkInTime) 
    {
       //checkInTime = cal;
       setId(orderId);
       setCheckInTime(checkInTime);
       setQuantity(qty);
       destination = dest;
       pkg = orderPkg;
       
       setState(INIT);
    }
}

