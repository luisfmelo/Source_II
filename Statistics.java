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
public class Statistics {
    private String name;
    private int elapsedTime; //in seconds
    private int numberOfPackages;
    
    public void addTime(int t)
    {
        this.elapsedTime += t;
    }
    
    public void addPackage()
    {
        this.numberOfPackages += 1;
    }
}
