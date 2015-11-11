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
public class Assembling extends Operation {
    private int bottomPackage;
    private int topPackage;
    
    public Assembling(int orderId, int orderBottomPkg, int orderTopPkg, int qty, Calendar checkInTime) 
    {
       //checkInTime = cal;
       setId(orderId);
       setCheckInTime(checkInTime);
       setQuantity(qty);
       bottomPackage = orderBottomPkg;
       topPackage = orderTopPkg;
       
       setState(INIT);
    }
}
