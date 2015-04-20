/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;

/**
 *
 * @author Kamil
 */
public class UncertainRules4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

//        Context context = new Context();
        Context2 context2 = new Context2();

      
//        SimpleFact f1 = SimpleFact.FactFactory.getInstance("wydzial rodzimy informatyka", new GrfIrf(new BigDecimal(1.0), new BigDecimal(1.0)));
//        SimpleFact f2 = SimpleFact.FactFactory.getInstance("kierunek elektrotechnika", new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
//        SimpleFact f3 = SimpleFact.FactFactory.getInstance("sprzet laptop", new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
//        SimpleFact f4 = SimpleFact.FactFactory.getInstance("rok 1", new GrfIrf(new BigDecimal(0.95), new BigDecimal(0.0)));

        
        SetFact sf1 = SetFactFactory.getInstance("wydzial_rodzimy", "informatyka", new GrfIrf(new BigDecimal(1.0), new BigDecimal(1.0)),false);
        SetFact sf20 = SetFactFactory.getInstance("rok", "1 2", new GrfIrf(new BigDecimal(0.95), new BigDecimal(0.0)),true);
        SetFact sf21 = SetFactFactory.getInstance("kierunek","informatyka", new GrfIrf(new BigDecimal(0.90), new BigDecimal(0.8)),true);
        SetFact sf4 = SetFactFactory.getInstance("sprzet", "komputer_stacjonarny laptop", new GrfIrf(new BigDecimal(1.0), new BigDecimal(1.0)), true);
        
        
        
        SetRule sr1 = new SetRule(new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
        sr1.addPremises(SetPremise.Factory.getInstance("wydzial_rodzimy informatyka elektryk",false));
        sr1.addConclusion(new DefaultSetAction("kierunek informatyka", "",true));
        
        SetRule sr4 = new SetRule(new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
        sr4.addPremises(SetPremise.Factory.getInstance("kierunek informatyka",false));
        sr4.addPremises(SetPremise.Factory.getInstance("rok ! 1 2",false));
        sr4.addConclusion(new DefaultSetAction("sprzet komputer_stacjonarny laptop", "", false));
        
        
        context2.facts.add(sf1);
        context2.facts.add(sf20);
        context2.facts.add(sf21);
        context2.facts.add(sf4);
        
        context2.rules.add(sr4);
        //context2.rules.add(sr1);
        
//        SimpleRule r2 = new SimpleRule(new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
//        r2.addPremise(new Premise("wydzial rodzimy informatyka"));
//        r2.addConclusion(new DefaultAction("kierunek informatyka", ""));
//
//        SimpleRule r4 = new SimpleRule(new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
//        r4.addPremise(new Premise("kierunek elektrotechnika"));
//        r4.addPremise(new Premise("!rok 1"));
//        r4.addConclusion(new DefaultAction("sprzet zeszyt", ""));
//
//        SimpleRule r1 = new SimpleRule(new GrfIrf(new BigDecimal(0.75), new BigDecimal(0.55)));
//        r1.addPremise(new Premise("wydzial rodzimy informatyka"));
//        r1.addConclusion(new DefaultAction("sprzet laptop", ""));

//        context.facts.add(f1);
//        context.facts.add(f2);
//        context.facts.add(f3);
//        context.facts.add(f4);
//               
        
        //context.rules.add(r2);
        //context.rules.add(r4);
        //context.rules.add(r1);
        
        SetUncertainRuleEngine engine2 = new SetUncertainRuleEngine(context2);
        engine2.fireRules();
        
        //SimpleUncertainRuleEngine engine = new SimpleUncertainRuleEngine(context);
        //engine.fireRules();
    }

}
