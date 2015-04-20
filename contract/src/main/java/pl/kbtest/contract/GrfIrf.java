/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.math.BigDecimal;

/**
 *
 * @author Kamil
 */
public final class GrfIrf {
    
    private final BigDecimal grf;
    private final BigDecimal irf;
    
    public GrfIrf(BigDecimal grf, BigDecimal irf){
        this.grf = grf;
        this.irf = irf;
    }
    
    public BigDecimal getGrf(){
        return this.grf;
    }
    
    public BigDecimal getIrf(){
        return this.irf;
    }
    
    @Override
    public String toString(){
        return "irf: "+irf.toString()+" grf: "+grf.toString();
    }
            
    
}
