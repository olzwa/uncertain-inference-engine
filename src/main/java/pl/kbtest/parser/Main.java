package pl.kbtest.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public class Main {

	public static List<Fact> facts = new ArrayList<Fact>();
	public static List<Rule> rules = new ArrayList<Rule>();
	
	public void start(){
		String input;
		Scanner s = new Scanner(System.in);
		System.out.println("Welcome to clipsy. Application is ready to use.");
		System.out.println("--------------------");
		do {
			input = s.nextLine();
			if (input.equals("exit")) {
				break;
			} else if (input.equals("help")) {
				System.out.println("--------------------");
				System.out.println("--------HELP--------");
				System.out.println("--------------------");
				System.out.println("exit -> exit application");
				System.out.println("help -> display help");
				System.out.println("file -> load file");
				System.out.println("facts -> list all facts");
				System.out.println("rules -> list all rules");
				System.out.println("clear -> clear memory");
				System.out.println("--------------------");
				System.out.println("FIRE A RULE -> type it name with parameters: rule(p1,p2)");
				System.out.println("FIRE A FACT -> type fact name: fact_name, fact_name(?, fact_known_arg)");
				System.out.println("--------------------");
			} else if (input.equals("file")) {
				System.out.println("Filepath:");
				String filepath ="c:/ak/t.clipsy";//s.nextLine();
				Parser dl = new Parser();
				try {
					dl.load(filepath);
					facts.addAll(dl.getFacts());
					rules.addAll(dl.getRules());
					System.out.println("File loaded.");
				} catch(IOException e) {
					System.out.println("File not found.");					
				} catch(ActionNotExistsException e) {
					System.out.println("Input is incorrect.");
				}
			} else if (input.equals("clear_facts")){
				
			} else if (input.equals("facts")){
				for(Fact f : facts) {
					System.out.println(f.toString());
				}
			} else if (input.equals("rules")){
				for(Rule r : rules) {
					System.out.println(r.toString());
				}
			} else if (input.equals("clear")){
				facts.clear();
				rules.clear();
			} else {
				// add rules, facts execute and so on...
				// facts
				boolean found = false;
				int num_of_found = 0;
				for(Fact fact : facts) {
					if (input.trim().equals(fact.name.trim())) {
						out(fact.toString());
						found = true;
						num_of_found++;
						continue;
					}
					
					if (input.contains(" ")){
						String name = input.substring(0, input.indexOf(" "));
						if (name.trim().equals(fact.name.trim())) {
							String attr[] = input.substring(input.indexOf("[") + 1, input.indexOf("]")).split(":");
							if (fact.hasAttribute(attr[0]) && fact.getAttributeValue(attr[0]).equals(attr[1].trim())) {
								out(fact.toString());
								found = true;
								num_of_found++;
								continue; 	 
							}
						}
					}
				}
				if (found) {
					out(((float)(num_of_found*100)/facts.size()) + "% of facts are match.");
					continue;
				}
				
				// rules
				for(Rule rule : rules) {
					if (input.trim().equals(rule.name.trim())) {
						rule.act();
						found = true;
						break;
					}
				}
				if (found) continue;
				
				// not found
				System.out.println("Fact, rule or command not exists.");
			}
		}		
		while(true);
	}
		
	public List<Fact> getFacts() {
		return facts;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main().start();
	}
	
	public static void d(String name){
		System.out.print(name);System.exit(0);
	}

	public static void out(String s) {
		System.out.println(s);
	}
	
	public static List<Fact> findMatchingFacts(Map<String, String> attrs) 
			throws NoMatchException {
		List<Fact> matchingFacts = new ArrayList<Fact>();
		
		for (Fact fact : facts) {
			boolean fact_matches = true;
			for (Entry<String, String> attr : attrs.entrySet()) {
				if (fact.hasAttribute(attr.getKey())) {
					// if matches everything
					if (attr.getValue().equals("?")) continue;
					
					// if matches variable
					if (attr.getValue().startsWith("?")) {
						fact.variables.put(attr.getValue(), fact.getAttributeValue(attr.getKey()));
						continue;
					}
					
					// if matches concrete thing
					String value = fact.getAttributeValue(attr.getKey());
					if (value.equals(attr.getValue())) continue;
				}
				
				fact_matches = false;
				break;
			}
			if (fact_matches) matchingFacts.add(fact);
		}
		
		if (matchingFacts.isEmpty()) throw new NoMatchException();
		return matchingFacts;
	}
}
