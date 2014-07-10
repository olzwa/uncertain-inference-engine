package pl.kbtest.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Rule2 extends Action {

	public String name;
	public double grf, irf;
	
	public List<Fact2> facts = new ArrayList<Fact2>();
	public List<Action> conclusions = new ArrayList<Action>();
	
	private Rule2(){}
	
	public static Rule2 factory(String name){
		Rule2 f = new Rule2();
		f.name = name.trim();
		return f;
	}
	
	public static Rule2 build(String name, List<Fact2> facts, List<Action> conclusions, double grf, double irf)
			throws ActionNotExistsException{
		Rule2 r = Rule2.factory(name);
		r.facts = facts;
		r.conclusions = conclusions;
		r.grf = grf;
		r.irf = irf;
		return r;
	}
        
        @Override
        public void act(){
            
        }

//	@Override
//	public void act() {
//		// check if facts are matching
//		boolean facts_are_fullfilled = true;
//		List<Fact2> foundFacts = new ArrayList<Fact2>();
//		for (Fact2 f : facts) {
//			try {
//				foundFacts.addAll(Main.findMatchingFacts(f.attributes));
//			} catch (NoMatchException e) {
//				facts_are_fullfilled = false;
//			}
//		}
//		
//		if (facts_are_fullfilled)
//			for(Action a : conclusions) {
//				a.setFoundFacts(foundFacts);
//				a.act();
//			}
//	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(this.name).append(" || ").append("Facts: ");
		for(Fact2 f : facts) 
			sb.append('(').append(f.toString()).append(")");
		sb.append('\n').append("     Actions: ");
		for(Action a : conclusions)
			sb.append("{").append(a.toString()).append("}");

		sb.append(" {").append(grf).append(";").append(irf).append("}");
		
		return sb.toString();
	}
}
