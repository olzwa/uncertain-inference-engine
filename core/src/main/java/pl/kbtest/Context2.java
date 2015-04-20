/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest;

import java.util.concurrent.ConcurrentLinkedDeque;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetRule;

/**
 *
 * @author Kamil
 */
public class Context2 {
    
      public ConcurrentLinkedDeque<SetRule> rules;
      public ConcurrentLinkedDeque<SetFact> facts;
      
      public Context2(){
          this.rules = new ConcurrentLinkedDeque<>();
          this.facts = new ConcurrentLinkedDeque<>();
      }
}
