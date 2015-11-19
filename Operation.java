/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trabalho_informaticaindustrial;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 *
 * @author David
 */
public class Operation {
    private int id;
    private OpState state;
    private int quantity;
    //private char type;
    private int ongoingPackages;
    private int finishedPackages;
    private Calendar checkInTime;
    private Calendar intialTime;
    private Calendar finalTime;
    
    /*************************************************/
    /* Alteração temporária: Não estão a ser usadas  */
    /* as subclasses por dificuldades técnicas.      */
    /*************************************************/
    
    private char type;
    private int arg1;
    private int arg2;
    
    public Operation(char orderType, int orderId, int orderStartPkg, int orderEndPkg, int orderQty) {
        type = orderType;
        id = orderId;
        arg1 = orderStartPkg;
        arg2 = orderEndPkg;
    }
    
    public char getType() {
        return type;
    }
    
    public int getArg1() {
        return arg1;
    }

    public int getArg2() {
        return arg2;
    }
    
    /***********************************************/
    /******************    End    ******************/
    /**********@return get*************/
    /***********************************************/
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OpState getState() {
        return state;
    }

    public void setState(OpState state) {
        this.state = state;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOngoingPackages() {
        return ongoingPackages;
    }

    public void setOngoingPackages(int ongoingPackages) {
        this.ongoingPackages = ongoingPackages;
    }

    public int getFinishedPackages() {
        return finishedPackages;
    }

    public void setFinishedPackages(int finishedPackages) {
        this.finishedPackages = finishedPackages;
    }

    public Calendar getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Calendar checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Calendar getIntialTime() {
        return intialTime;
    }

    public void setIntialTime(Calendar intialTime) {
        this.intialTime = intialTime;
    }

    public Calendar getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(Calendar finalTime) {
        this.finalTime = finalTime;
    }
    
}