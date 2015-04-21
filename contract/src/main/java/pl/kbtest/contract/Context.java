/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetRule;

/**
 *
 * @author Kamil
 */
public class Context {
    
    private final Deque<SetRule> rules;
    private final Deque<SetFact> facts;

    public Context(Deque<SetFact> facts, Deque<SetRule> rules) {
        this.rules = rules;
        this.facts = facts;
    }
    
    public Deque<SetRule> getRules(){
        return this.rules;
    }
    
    public Deque<SetFact> getFacts(){
        return this.facts;
    }
}
