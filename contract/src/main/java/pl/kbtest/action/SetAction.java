/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.action;

import java.util.Map;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;

/**
 *
 * @author Kamil
 */
public interface SetAction {
    String getOutput(Map<String,String> wildcardFillers);
    SetFact execute(Map<String,String> wildcardFillers, GrfIrf grfIrf, boolean conj);
    boolean isConjunction();
}
