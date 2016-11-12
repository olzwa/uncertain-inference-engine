/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest;

import org.apache.commons.cli.*;
import pl.kbtest.contract.Context;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetRule;
import pl.kbtest.rule.induction.input.GsonSetRuleReader;
import pl.kbtest.rule.induction.SetFactReader;
import pl.kbtest.rule.induction.SetRuleReader;

public class UncertainRules4 {

    static final String LOAD_RULES = "load rules";
    static final String SHOW_RULES = "show rules";

    static final String ADD_FACT = "add fact";
    static final String LOAD_FACTS = "load facts";
    static final String SHOW_FACTS = "show facts";

    static final String FIRE = "fire rules";

    public static void main(String[] args) throws Exception {
        Deque<SetRule> rules = new ConcurrentLinkedDeque<>();
        Deque<SetFact> facts = new ConcurrentLinkedDeque<>();

        Context context = new Context(facts, rules);
        UncertainRuleEngine engine = new UncertainRuleEngine(context);

        Set delimiters = new HashSet<>(Arrays.asList("="));
        String conjunctionToken = "AND";
        String disjunctionToken = "OR";

        Options options = new Options();
        CommandLineParser parser = new BasicParser();

        Option loadRulesOption = OptionBuilder.hasArg().create(getAsProgramArg(LOAD_RULES));
        Option loadFactsOption = OptionBuilder.hasArg().create(getAsProgramArg(LOAD_FACTS));

        options.addOption(loadRulesOption);
        options.addOption(loadFactsOption);

        CommandLine line = parser.parse(options, args);
        if (line.hasOption(getAsProgramArg(LOAD_RULES))) {
            String fileName = line.getOptionValue(getAsProgramArg(LOAD_RULES));
            File ruleFile = new File(fileName);
            loadJsonRulesAction(rules,ruleFile);
        }
        if (line.hasOption(getAsProgramArg(LOAD_FACTS))) {
            String fileName = line.getOptionValue(getAsProgramArg(LOAD_FACTS));
            File factFile = new File(fileName);
            loadFactsAction(facts, delimiters, conjunctionToken, disjunctionToken, factFile);
        }

        Scanner scanner = new Scanner(System.in);
        String command;
        do {
            command = scanner.nextLine();
            if (command.startsWith(LOAD_RULES)) {
                String[] split = command.split(LOAD_RULES);
                File ruleFile = new File(split[1].trim());
                loadJsonRulesAction(rules,ruleFile);
            }
            if (command.equals(SHOW_RULES)) {
                rules.forEach(System.out::println);
            }
            if (command.equals(SHOW_FACTS)) {
                facts.forEach(System.out::println);
            }
            if (command.startsWith(LOAD_FACTS)) {
                String[] split = command.split(LOAD_FACTS);
                File factsFile = new File(split[1].trim());
                loadFactsAction(facts, delimiters, conjunctionToken, disjunctionToken, factsFile);
            }
            if (command.startsWith(ADD_FACT)) {
                String[] splitCommand = command.split(ADD_FACT);
                String factBody = splitCommand[1].trim();
                String[] splitFactBody = factBody.split(" ");
                Pattern grfIrfPattern = Pattern.compile("\\{([0-9]+),([0-9]+)\\}");
                Matcher m = grfIrfPattern.matcher(splitFactBody[1]);
                BigDecimal grf = null;
                BigDecimal irf = null;
                if (m.find()) {
                    grf = BigDecimal.valueOf(Integer.parseInt(m.group(1)));
                    irf = BigDecimal.valueOf(Integer.parseInt(m.group(2)));
                }
                SetFact fact = SetFactFactory.getInstance(splitFactBody[0], new GrfIrf(grf, irf));
                facts.add(fact);
                System.out.println("Added: " + fact);
            }
            if (command.equals(FIRE)) {
                engine.fireRules();
            }
        } while (!command.equals("exit"));

/*        SetFact sf1 = SetFactFactory.getInstance("wydzial_rodzimy", "informatyka", new GrfIrf(new BigDecimal(1.0), new BigDecimal(1.0)),false);
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

        facts.add(sf1);
        facts.add(sf20);
        facts.add(sf21);
        facts.add(sf4);
        rules.add(sr4);*/


        //engine2.fireRules();
    }

    private static void loadFactsAction(Deque<SetFact> facts, Set delimiters, String conjunctionToken, String disjunctionToken, File factsFile) {
        SetFactReader srr = new SetFactReader(factsFile, delimiters, conjunctionToken, disjunctionToken);
        List<SetFact> setFacts = srr.readFacts();
        facts.addAll(setFacts);
        System.out.println("Added " + setFacts.size() + " facts");
    }

    private static void loadRulesAction(Deque<SetRule> rules, Set delimiters, String conjunctionToken, String disjunctionToken, File ruleFile) {
        SetRuleReader srr = new SetRuleReader(ruleFile, delimiters, conjunctionToken, disjunctionToken, ",");
        List<SetRule> setRules = srr.readRules();
        rules.addAll(setRules);
        System.out.println("Added " + rules.size() + " rules");
    }

    private static void loadJsonRulesAction(Deque<SetRule> rules, File ruleFile){
        GsonSetRuleReader grr = new GsonSetRuleReader(ruleFile);
        List<SetRule> setRules = grr.readRules();
        rules.addAll(setRules);
        System.out.println("Added " + rules.size() + " rules");
    }

    private static String getAsProgramArg(String command) {
        return command.replaceAll("\\s+", "");
    }

}
