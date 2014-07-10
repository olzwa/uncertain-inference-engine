package pl.kbtest.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Fact2  {
	
	public String name;
	public Map<String, String> attributes = new HashMap<String, String>();
	public Map<String, String> variables = new HashMap<String, String>();
	public double grf, irf;
	
	private Fact2(){}
	
	public static Fact2 factory(String name){
		Fact2 f = new Fact2();
		f.name = name.trim();
		return f;
	}
	
	public static Fact2 build(String name, Map<String, String> attrs, double grf, double irf){
		Fact2 f = Fact2.factory(name);
		f.attributes = attrs;
		f.grf = grf;
		f.irf = irf;
		return f;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(name).append(" || Attributes: ");
		for(Entry<String, String> e : attributes.entrySet()) {
			sb.append(e.getKey()).append(":").append(e.getValue()).append(";");
		}
		
		sb.append(" {").append(grf).append(";").append(irf).append("}");
		
		return sb.toString();
	}
	
	public boolean hasAttribute(String name){
		return attributes.containsKey(name);
	}
	
	public String getAttributeValue(String attr) {
		return attributes.get(attr);
	}
	
	public Fact2 clone(){
		Fact2 f = new Fact2();
		f.name = name;
		f.attributes.putAll(this.attributes);
		f.variables.putAll(this.variables);
		return f;
	}
}
