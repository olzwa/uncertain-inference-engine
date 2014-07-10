package pl.kbtest.parser;
import java.util.Map.Entry;
import pl.kbtest.Fact;

public class DefFactAction extends Action {

	Fact fact;
	
	public DefFactAction(Fact f) {
		fact = f;
	}
	
	@Override
	public void act() {
		// set variables...
		Fact tmp = null;
//		for(Fact ff : foundFacts) {
//			tmp = this.fact.clone();
//			for(Entry<String, String> attr : tmp.attributes.entrySet()) {
//				for(Entry<String, String> var : ff.variables.entrySet()) {
//					if (var.getKey().equals(attr.getValue()))
//						attr.setValue(var.getValue());
//				}
//			}
//			Main.facts.add(tmp);
//		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("def_fact ").append("(").append(fact.toString()).append(")");
		return sb.toString();
	}

}
