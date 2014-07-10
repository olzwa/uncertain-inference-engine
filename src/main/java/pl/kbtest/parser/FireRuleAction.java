package pl.kbtest.parser;
import java.util.List;

public class FireRuleAction extends Action{

	String name;
	
	public FireRuleAction(String name) {
		this.name = name;
	}
	
	@Override
	public void act() {
		List<Rule2> rules = Main.rules;
		for(Rule2 r : rules) {
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
