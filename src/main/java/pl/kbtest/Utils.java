/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest;

import java.math.BigDecimal;

/**
 *
 * @author Kamil
 */
public class Utils {
    private Utils(){
        
    }
    
    public static BigDecimal getMax(BigDecimal one, BigDecimal two){
        return (one.compareTo(two) < 0) ? two   : one;
    }
    
     public static BigDecimal getMin(BigDecimal one, BigDecimal two){
        return (one.compareTo(two) < 0) ? one   : two;
    }

    
}
