package pl.kbtest.parser;

import java.util.List;

public abstract class Action {
	
	List<Fact> foundFacts = null;
	
	public abstract void act();
	
	public static Action build(String input) throws ActionNotExistsException{
		String type = input.trim();
		if (type.contains(" "))
			type = input.substring(0, input.indexOf(' ')).trim();
		if (type.equals("printline")) {
			return new PrintAction(input.substring(input.indexOf('"') + 1, input.lastIndexOf('"')));
		} else if (type.equals("def_fact")) {
			input = input.substring(input.indexOf(' ' )+1);
			String name = input.substring(0, input.indexOf('[')).trim();
			String attrs = input.substring(input.indexOf('[')+1, input.indexOf(']')).trim();
			return new DefFactAction(Fact.build(name, Parser.parseAttributes(attrs), 0, 0));
		} else {
//			List<Rule> rules = Main.rules;
//			for(Rule rule : rules) {
//				if (rule.name.equals(type)) {
//					return new FireRuleAction(type);
//				}
//			}
			if (type.startsWith("*")) return new FireRuleAction(type.substring(1));
		}
		
		throw new ActionNotExistsException("Action " + type + " is not available.");
	}

	public void setFoundFacts(List<Fact> foundFacts) {
		this.foundFacts = foundFacts;
	}
}
