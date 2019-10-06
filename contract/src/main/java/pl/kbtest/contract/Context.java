/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetRule;

/**
 *
 * @author Kamil
 */
public class Context {
    private final List<SetRule> rules;
    private final List<SetFact> facts;

    public Context(List<SetFact> facts, List<SetRule> rules) {
        this.rules = rules;
        this.facts = facts;
    }
    
    public List<SetRule> getRules(){
        return this.rules;
    }
    
    public List<SetFact> getFacts(){
        return this.facts;
    }
}
