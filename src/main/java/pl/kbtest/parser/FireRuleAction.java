package pl.kbtest.parser;
import java.util.List;

public class FireRuleAction extends Action{

	String name;
	
	public FireRuleAction(String name) {
		this.name = name;
	}
	
	@Override
	public void act() {
		List<Rule> rules = Main.rules;
		for(Rule r : rules) {
			if (r.name.equals(this.name)) {
				r.act();
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		return sb.toString();
	}

}
