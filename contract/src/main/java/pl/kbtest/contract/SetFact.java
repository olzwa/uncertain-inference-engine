/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest.contract;

import java.math.BigDecimal;
import java.util.Set;

/**
 *
 * @author Kamil
 */
public interface SetFact {

    String getHead();
    Set<String> getSet();
    GrfIrf getGrfIrf();
    int getLength();
    BigDecimal getWParamter();
    boolean isAxiom();
    boolean isConjunction();
    boolean isNegated();
}
