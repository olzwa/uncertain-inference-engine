/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest;

import pl.poznan.put.cie.oculus.dbentries.internal.GrfIrf;

import java.util.Arrays;

/**
 *
 * @author Kamil
 */
public class Premise {

    
    final private String[] premise;
    private GrfIrf grfIrf;
    
    public Premise(String premise) {
        this.premise = premise.split(" ");//TODO should also handle multiple spaces case
    }
    public Premise(String[] premise) {
        this.premise = premise;
    }
    
    public void setGrfIrf(GrfIrf grfIrf){
        this.grfIrf = grfIrf;
    }
    
    public GrfIrf getGrfIrf(){
        return grfIrf;
    }

    public String[] getBody() {
        return premise;
    }
    
    @Override
    public String toString(){
        return Arrays.toString(this.getBody());
    }

    public int getLength() {
        return premise.length;
    }
}
