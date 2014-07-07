package pl.kbtest.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Fact {
	
	public String name;
	public Map<String, String> attributes = new HashMap<String, String>();
	public Map<String, String> variables = new HashMap<String, String>();
	public double grf, irf;
	
	private Fact(){}
	
	public static Fact factory(String name){
		Fact f = new Fact();
		f.name = name.trim();
		return f;
	}
	
	public static Fact build(String name, Map<String, String> attrs, double grf, double irf){
		Fact f = Fact.factory(name);
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
	
	public Fact clone(){
		Fact f = new Fact();
		f.name = name;
		f.attributes.putAll(this.attributes);
		f.variables.putAll(this.variables);
		return f;
	}
}
