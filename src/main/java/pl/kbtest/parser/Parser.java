package pl.kbtest.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.kbtest.Fact;
import pl.kbtest.GrfIrf;

public class Parser {
	List<Fact> facts = new ArrayList<Fact>();
	List<Rule2> rules = new ArrayList<Rule2>();
	
	String filepath;
	
	public List<Fact> getFacts() {
		return facts;
	}
	public List<Rule2> getRules() {
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
					//TODO replace with my own fact factory
					//this.facts.add(Fact.build(name, attrs, grf, irf));
                                        GrfIrf grfIrf = new GrfIrf(new BigDecimal(grf),new BigDecimal(irf));
                                        Fact.FactFactory.getInstance(name, grfIrf);
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
                                                
                                                
                                                                                                
						//factsList.add(Fact2.build(factName, Parser.parseAttributes(factAttrs), grf, irf));
					}
					
					String actions = tmp[1];
					String actionsArr[] = actions.split(";");
					for(String s : actionsArr){
						conclusionsList.add(Action.build(s));
					}
					//TODO replace with my own rule factory
					//this.rules.add(Rule.build(name, factsList, conclusionsList, grf, irf));
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
