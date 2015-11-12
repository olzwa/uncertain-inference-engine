package pl.kbtest.interpreter;

import java.io.File;

import pl.kbtest.action.SetAction;
import pl.kbtest.contract.Context;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;

public class Run {

	public static void main(String args[]) throws Throwable {
		Context ctx = Interpreter.interpret(new File(args[0]));
		System.out.println("CTX.facts");
		for (SetFact fact : ctx.getFacts()) {
			System.out.println(fact.toString());
		}

		System.out.println("CTX.rules");
		for (SetRule rule : ctx.getRules()) {
			System.out.println("{");
			for (SetPremise premise : rule.getPremises()) {
				System.out.println(premise);
			}
			System.out.println("=>");
			for (SetAction action : rule.getConclusions()) {
				System.out.println(action);
			}
			System.out.println("}");
		}
	}

}
