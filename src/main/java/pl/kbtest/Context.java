/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 * @author Kamil
 */
public class Context {
    
      public ConcurrentLinkedDeque<Rule> rules;
      public ConcurrentLinkedDeque<Fact> facts;
      
      public Context(){
          this.rules = new ConcurrentLinkedDeque<>();
          this.facts = new ConcurrentLinkedDeque<>();
      }
}
