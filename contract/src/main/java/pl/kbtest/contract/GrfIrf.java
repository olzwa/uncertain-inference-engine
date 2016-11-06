/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrfIrf grfIrf = (GrfIrf) o;
        return Objects.equals(grf, grfIrf.grf) &&
                Objects.equals(irf, grfIrf.irf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grf, irf);
    }
}
