package pl.kbtest.parser;

import java.util.Map.Entry;

public class PrintAction extends Action {

	String s;
	
	public PrintAction(String string) {
		s = string;
	}

	@Override
	public void act() {
		String tmp = "";
		for(Fact2 ff : foundFacts) {
			for(Entry<String, String> var : ff.variables.entrySet()) {
				tmp = s.replace(var.getKey(), var.getValue());				
			}
			Main.out(tmp);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("printline \"").append(s).append("\"");
		return sb.toString();
	}
}
