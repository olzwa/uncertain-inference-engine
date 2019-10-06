/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest;

import org.apache.commons.cli.*;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.nio.file.Files;
import java.util.stream.Collectors;

public class UncertainRules4 {

  static final String LOAD_RULES = "load rules";
  static final String SHOW_RULES = "show rules";

  static final String ADD_FACT = "add fact";
  static final String LOAD_FACTS = "load facts";
  static final String LOAD_AXIOM_FACTS = "load axiom facts";
  static final String SHOW_FACTS = "show facts";

  static final String FIRE_RULES = "fire rules";
  public static final String HELP = "help";

  public static void main(String[] args) throws Exception {
    Context context = new Context();
    UncertainRuleEngine engine = new UncertainRuleEngine(context);

    Set delimiters = new HashSet<>(Arrays.asList("="));
    String conjunctionToken = "AND";
    String disjunctionToken = "OR";

    Options options = new Options();
    CommandLineParser parser = new BasicParser();

    Option loadRulesOption = OptionBuilder
        .hasArg()
        .withDescription("load rules from file")
        .withArgName("filename")
        .create(getAsProgramArg(LOAD_RULES));

    Option loadFactsOption = OptionBuilder
        .hasArg()
        .withDescription("load facts from file")
        .withArgName("filename")
        .create(getAsProgramArg(LOAD_FACTS));

    Option loadAxiomFactsOption = OptionBuilder
        .hasArg()
        .withDescription("load facts from file as axioms")
        .withArgName("filename")
        .create(getAsProgramArg(LOAD_AXIOM_FACTS));

    Option fireRulesOption = new Option(getAsProgramArg(FIRE_RULES), "run inference process");
    Option helpOption = new Option(HELP, "print this message");

    options.addOption(loadRulesOption);
    options.addOption(loadFactsOption);
    options.addOption(loadAxiomFactsOption);
    options.addOption(fireRulesOption);
    options.addOption(helpOption);

    CommandLine line = parser.parse(options, args);
    if (line.hasOption(getAsProgramArg(LOAD_RULES))) {
      String fileName = line.getOptionValue(getAsProgramArg(LOAD_RULES));
      File ruleFile = new File(fileName);
      Deque<SetRule> loadedRules = loadJsonRulesAction(ruleFile);
      context.addRules(loadedRules);
    }
    if (line.hasOption(getAsProgramArg(LOAD_FACTS))) {
      String fileName = line.getOptionValue(getAsProgramArg(LOAD_FACTS));
      File factFile = new File(fileName);
      Deque<SetFact> loadedFacts = loadJsonFactsAction(factFile, false);
      context.addFacts(loadedFacts);
    }
    if (line.hasOption(getAsProgramArg(LOAD_AXIOM_FACTS))) {
      String fileName = line.getOptionValue(getAsProgramArg(LOAD_AXIOM_FACTS));
      File factFile = new File(fileName);
      Deque<SetFact> loadedFacts = loadJsonFactsAction(factFile, true);
			context.addFacts(loadedFacts);
    }

    if (line.hasOption(getAsProgramArg(FIRE_RULES))) {
      engine.fireRules();
    }

    if (line.hasOption(HELP)) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("java -jar uncertain-inference.jar", options);
      System.exit(0);
    }

    printRulesReport(context);
    printFactsReport(context);

    Scanner scanner = new Scanner(System.in);
    String command;
    do {
      command = scanner.nextLine();
      if (command.startsWith(LOAD_RULES)) {
        String[] split = command.split(LOAD_RULES);
        File ruleFile = new File(split[1].trim());
        Deque<SetRule> loadedRules = loadJsonRulesAction(ruleFile);
        System.out.println("Loaded " + loadedRules.size() + " rules");
        context.addRules(loadedRules);
      }
      if (command.equals(SHOW_RULES)) {
        printRules(context);
      }
      if (command.equals(SHOW_FACTS)) {
        printFacts(context);
      }
      if (command.startsWith(LOAD_FACTS)) {
        String[] split = command.split(LOAD_FACTS);
        File factsFile = new File(split[1].trim());
        Deque<SetFact> loadedFacts = loadJsonFactsAction(factsFile, false);
        System.out.println("Loaded " + loadedFacts.size() + " facts");
				context.addFacts(loadedFacts);
      }
      if (command.startsWith(LOAD_AXIOM_FACTS)) {
        String[] split = command.split(LOAD_AXIOM_FACTS);
        File factsFile = new File(split[1].trim());
        Deque<SetFact> loadedFacts = loadJsonFactsAction(factsFile, true);
        System.out.println("Loaded " + loadedFacts.size() + " facts as axioms");
				context.addFacts(loadedFacts);
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
				context.addFact(fact);
        System.out.println("Added: " + fact);
      }
      if (command.equals(FIRE_RULES)) {
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

  private static void printFacts(Context context) {
    List<SetFact> facts = context.getFacts();
    printFactsReport(context);
    facts.forEach(System.out::println);
  }

  private static void printFactsReport(Context context) {
    System.out.println("Facts: " + context.getFacts().size());
  }

  private static void printRules(Context context) {
    Collection<SetRule> rules = context.getRules();
    printRulesReport(context);
    rules.forEach(System.out::println);
  }

  private static void printRulesReport(Context context) {
    System.out.println("Rules: " + context.getRules().size());
  }

  private static Deque<SetFact> loadJsonFactsAction(File factsFile, boolean asAxiom) throws IOException {
    Deque<SetFact> result = new ArrayDeque<>();
    String content = new String(Files.readAllBytes(Paths.get(factsFile.getAbsolutePath())));
    JSONArray factsArray = new JSONArray(content);
    for (Object object : factsArray) {
      JSONObject jsonFact = (JSONObject) object;
      String premiseHead = jsonFact.getString("head");
      String factBody = jsonFact.getJSONArray("set")
          .toList()
          .stream()
          .map(Object::toString)
          .collect(Collectors.joining(","));
      JSONObject jsonGrfIrf = jsonFact.getJSONObject("GrfIrf");
      BigDecimal irf = jsonGrfIrf.getBigDecimal("irf");
      BigDecimal grf = jsonGrfIrf.getBigDecimal("grf");
      GrfIrf grfIrf = new GrfIrf(grf, irf);
      boolean conjunction = jsonFact.getBoolean("conjunction");
      if (asAxiom) {
        result.add(SetFactFactory.getAxiomInstance(premiseHead, factBody, grfIrf, conjunction));
      } else {
        result.add(SetFactFactory.getInstance(premiseHead, factBody, grfIrf, conjunction));
      }
    }
    return result;
  }

  private static Deque<SetRule> loadJsonRulesAction(File ruleFile) throws IOException {
    Deque<SetRule> result = new ArrayDeque<>();
    String content = new String(Files.readAllBytes(Paths.get(ruleFile.getAbsolutePath())));
    // Convert JSON string to JSONObject
    JSONArray rulesArray = new JSONArray(content);
    for (Object object : rulesArray) {
      JSONObject jsonRule = (JSONObject) object;
      //premise
      List<SetPremise> premisesList = new ArrayList<>();
      JSONArray premises = jsonRule.getJSONArray("premises");
      for (Object o : premises) {
        JSONObject jsonPremise = (JSONObject) o;
        String premiseHead = jsonPremise.getString("head");
        Set<String> premiseSet = jsonPremise.getJSONArray("set")
            .toList()
            .stream()
            .map(Object::toString)
            .collect(Collectors.toSet());
        boolean conjunction = jsonPremise.getBoolean("conjunction");
        SetPremise setPremise = new SetPremise(premiseHead, premiseSet, false, conjunction);
        premisesList.add(setPremise);
      }
      //grfIrf
      JSONObject jsonGrfIrf = jsonRule.getJSONObject("GrfIrf");
      BigDecimal irf = jsonGrfIrf.getBigDecimal("irf");
      BigDecimal grf = jsonGrfIrf.getBigDecimal("grf");
      GrfIrf grfIrf = new GrfIrf(grf, irf);
      //conclusion
      JSONObject jsonConclusion = jsonRule.getJSONObject("conclusion");
      String conclusionHead = jsonConclusion.getString("head");
      String conclusion = jsonConclusion.getJSONArray("set")
          .toList()
          .stream()
          .map(Object::toString)
          .collect(Collectors.joining(","));
      boolean isConjunction = jsonConclusion.getBoolean("conjunction");
      SetAction setAction = new DefaultSetAction(conclusionHead, conclusion, isConjunction);
      result.add(new SetRule(premisesList, Collections.singletonList(setAction), grfIrf));
    }
    return result;
  }

  private static String getAsProgramArg(String command) {
    return command.replaceAll("\\s+", "");
  }

}
