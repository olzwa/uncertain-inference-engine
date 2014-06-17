/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uncertainexpertsystem4.action;

import java.util.Map;
import uncertainexpertsystem4.Fact;
import uncertainexpertsystem4.GrfIrf;

/**
 *
 * @author Kamil
 */
public interface Action {
    String getOutput(Map<String,String> wildcardFillers);
    Fact execute(Map<String,String> wildcardFillers, GrfIrf grfIrf);
}
