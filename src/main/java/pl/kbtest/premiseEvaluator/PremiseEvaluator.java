/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest.premiseEvaluator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kamil
 */
public class PremiseEvaluator {

    final private Map<String, String> variables;

    private String[] premise;
    private String[] fact;
    private boolean negate;

    public PremiseEvaluator() {
        this.variables = new HashMap<>();
    }

    public void setPair(String[] premiseBody, String[] factBody) {
        this.premise = premiseBody;
        this.fact = factBody;
    }

    public boolean equals() {
        boolean result = true;
        if (premise.length != fact.length) {
            return false;
        }
        for (int i = 0; i < premise.length; i++) {
            if (!premise[i].equals(fact[i]) && !premise[i].startsWith("?") && !premise[i].startsWith("!")) {
                result = false;
                break;
            }
            if (premise[i].startsWith("?")) {
                result = true;
                variables.put(premise[i], fact[i]);
            }
            if (premise[i].startsWith("!")) {
                if (premise[i].substring(1).equals(fact[i])) {
                    result = true;
                } else {
                    result = false;
                    break;
                }
            }

        }
        return result;
    }

    public Map<String, String> getVariables() {
        return this.variables;
    }

}
