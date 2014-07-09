/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import pl.kbtest.action.Action;
import pl.kbtest.action.DefaultAction;
import pl.kbtest.parser.ActionNotExistsException;
import static pl.kbtest.parser.Main.facts;
import static pl.kbtest.parser.Main.rules;
import pl.kbtest.parser.Parser;

/**
 *
 * @author Kamil
 */
public class UncertainRules4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Context context = new Context();

        String input = "";
        Scanner s = new Scanner(System.in);
        System.out.println("Welcome.");
        System.out.println("--------------------");

        do {
            input = s.nextLine();
            if (input.equals("file")) {
                input = s.nextLine();
                System.out.println("Filepath:");
                String filepath = input;//s.nextLine();
                Parser dl = new Parser();
                try {
                    dl.load(filepath);
                    facts.addAll(dl.getFacts());
                    rules.addAll(dl.getRules());
                    System.out.println("File loaded.");
                } catch (IOException e) {
                    System.out.println("File not found.");
                } catch (ActionNotExistsException e) {
                    System.out.println("Input is incorrect.");
                }
            }
            if (input.equals("exit")) {
                break;
            }
        } while (true);

        Fact f1 = Fact.FactFactory.getInstance("wydzial rodzimy informatyka", new GrfIrf(new BigDecimal(1.0), new BigDecimal(1.0)));
        Fact f2 = Fact.FactFactory.getInstance("kierunek elektrotechnika", new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
        Fact f3 = Fact.FactFactory.getInstance("sprzet laptop", new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
        Fact f4 = Fact.FactFactory.getInstance("rok 1", new GrfIrf(new BigDecimal(0.95), new BigDecimal(0.0)));

        Rule r2 = new Rule(new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
        r2.addPremise(new Premise("wydzial rodzimy informatyka"));
        r2.addConclusion(new DefaultAction("kierunek informatyka", ""));

        Rule r4 = new Rule(new GrfIrf(new BigDecimal(0.9), new BigDecimal(0.8)));
        r4.addPremise(new Premise("kierunek elektrotechnika"));
        r4.addPremise(new Premise("!rok 1"));
        r4.addConclusion(new DefaultAction("sprzet zeszyt", ""));

        Rule r1 = new Rule(new GrfIrf(new BigDecimal(0.75), new BigDecimal(0.55)));
        r1.addPremise(new Premise("wydzial rodzimy informatyka"));
        r1.addConclusion(new DefaultAction("sprzet laptop", ""));

        context.facts.add(f1);
        context.facts.add(f2);
        context.facts.add(f3);
        context.facts.add(f4);

        context.rules.add(r2);
        context.rules.add(r4);
        context.rules.add(r1);

        UncertainRuleEngine engine = new UncertainRuleEngine(context);
        engine.fireRules();
    }

}
