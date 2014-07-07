package pl.kbtest.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
	List<Fact> facts = new ArrayList<Fact>();
	List<Rule> rules = new ArrayList<Rule>();
	
	String filepath;
	
	public List<Fact> getFacts() {
		return facts;
	}
	public List<Rule> getRules() {
		return rules;
	}
	
	public void load(String filepath) throws ActionNotExistsException, IOException {
		this.filepath = filepath;
		
		File fin = new File(filepath);
		
		FileInputStream fis = new FileInputStream(fin);
		 
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		StringBuilder statementBuilder = new StringBuilder();
		String type = null, name = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			line = line.trim();
			
			statementBuilder.append(line);
			
			if (line.length() < 1) continue;
			
			if (line.substring(line.length() - 1).equals("]")) {
				line = statementBuilder.toString();
				type = line.substring(0, line.indexOf(' '));
				line = line.substring(line.indexOf(' ') + 1);
				name = line.substring(0, line.indexOf('{'));
				line = line.substring(line.indexOf('{'));
				double grf = Double.parseDouble(line.substring(line.indexOf("{") + 1, line.indexOf(";")));
				double irf = Double.parseDouble(line.substring(line.indexOf(";") + 1, line.indexOf("}")));
				line = line.substring(line.indexOf("}") + 1);
				
				if (type.equals("def_fact")) {

					Map<String, String> attrs = Parser.parseAttributes(line);
					
					this.facts.add(Fact.build(name, attrs, grf, irf));
				} else if (type.equals("def_rule")) {
					String definition = line.substring(0, line.length() - 1);
					String tmp[] = definition.split("=>");
					List<Fact> factsList = new ArrayList<Fact>();
					List<Action> conclusionsList = new ArrayList<Action>();
					
					String facts = tmp[0];
					String factsArr[] = facts.split(";");
					for(String s : factsArr) {
						String factName = s.substring(0, s.indexOf('[')).trim();
						String factAttrs = s.substring(s.indexOf('[') + 1, s.indexOf(']')).trim();

						factsList.add(Fact.build(factName, Parser.parseAttributes(factAttrs), grf, irf));
					}
					
					String actions = tmp[1];
					String actionsArr[] = actions.split(";");
					for(String s : actionsArr){
						conclusionsList.add(Action.build(s));
					}
					
					this.rules.add(Rule.build(name, factsList, conclusionsList, grf, irf));
				}
				
				statementBuilder = new StringBuilder();
			}
		}
	 
		br.close();
	}
	
	public static Map<String, String> parseAttributes(String line) {
		if (line.indexOf(0) == '[')
			line = line.substring(1);
		if (line.indexOf(']') > 0)
			line = line.substring(0, line.length() - 1);

		String[] arr = line.split(";");
		String[] pair;
		Map<String, String> attrs = new HashMap<String, String>();
		for(String s : arr) {
			s = s.trim();
			pair = s.split(":");
			attrs.put(pair[0], pair[1]);
		}
		return attrs;
	}
}
