/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.action;

import java.util.Map;
import pl.kbtest.Fact;
import pl.kbtest.GrfIrf;

/**
 *
 * @author Kamil
 */
public interface Action {
    String getOutput(Map<String,String> wildcardFillers);
    Fact execute(Map<String,String> wildcardFillers, GrfIrf grfIrf);
}
