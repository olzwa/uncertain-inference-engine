package pl.kbtest.interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import pl.kbtest.action.DefaultSetAction;
import pl.kbtest.action.SetAction;
import pl.kbtest.contract.Context;
import pl.kbtest.contract.GrfIrf;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetFactFactory;
import pl.kbtest.contract.SetPremise;
import pl.kbtest.contract.SetRule;
import pl.kbtest.interpreter.grammar.UncertainBaseListener;
import pl.kbtest.interpreter.grammar.UncertainLexer;
import pl.kbtest.interpreter.grammar.UncertainParser;
import pl.kbtest.interpreter.grammar.UncertainParser.AssertFactContext;
import pl.kbtest.interpreter.grammar.UncertainParser.CompileContext;
import pl.kbtest.interpreter.grammar.UncertainParser.DefineRuleContext;
import pl.kbtest.interpreter.grammar.UncertainParser.EffectContext;
import pl.kbtest.interpreter.grammar.UncertainParser.EffectsContext;
import pl.kbtest.interpreter.grammar.UncertainParser.FactContext;
import pl.kbtest.interpreter.grammar.UncertainParser.ValueContext;

public class Interpreter extends UncertainBaseListener {

	private Deque<SetFact> facts = new LinkedList<SetFact>();
	private Deque<SetRule> rules = new LinkedList<SetRule>();

	private Interpreter() {
	}

	public static Context interpret(File sourceFilePath) throws Throwable {
		Interpreter interpreter = new Interpreter();
		UncertainLexer lexer = new UncertainLexer(new ANTLRInputStream(new FileInputStream(sourceFilePath.getAbsolutePath())));
		UncertainParser parser = new UncertainParser(new CommonTokenStream(lexer));
		CompileContext context = parser.compile();
		ParseTreeWalker.DEFAULT.walk(interpreter, context);
		return new Context(interpreter.facts, interpreter.rules);
	}

	@Override
	public void enterAssertFact(AssertFactContext ctx) {
		ParserRuleContext parent = ctx.getParent();
		if (parent instanceof EffectsContext || parent instanceof EffectContext) {
			return;
		}
		String factText = ctx.fact().getText();

		BigDecimal grf = BigDecimal.valueOf(Double.parseDouble(ctx.fact().grfirf().PROBABILITY(1).getText()));
		BigDecimal irf = BigDecimal.valueOf(Double.parseDouble(ctx.fact().grfirf().PROBABILITY(0).getText()));
		factText = ctx.fact().LITERAL().getText();
		for (ValueContext literal : ctx.fact().value()) {
			factText += " " + literal.getText();
		}
		SetFact setFact = SetFactFactory.getInstance(factText, new GrfIrf(grf, irf), ctx.fact().LOGIC_OR() == null);
		facts.add(setFact);
	}

	@Override
	public void enterDefineRule(DefineRuleContext ctx) {
		String ruleName = ctx.LITERAL().getText();
		List<SetPremise> premises = new ArrayList<SetPremise>();
		List<SetAction> conlusions = new ArrayList<SetAction>();

		BigDecimal grf = BigDecimal.valueOf(Double.parseDouble(ctx.grfirf().PROBABILITY(1).getText()));
		BigDecimal irf = BigDecimal.valueOf(Double.parseDouble(ctx.grfirf().PROBABILITY(0).getText()));
		for (FactContext premise : ctx.fact()) {
			String factText = premise.LITERAL().getText();
			for (ValueContext literal : premise.value()) {
				factText += " " + literal.getText();
			}
			BigDecimal premiseGrf = BigDecimal.valueOf(Double.parseDouble(premise.grfirf().PROBABILITY(1).getText()));
			BigDecimal premiseIrf = BigDecimal.valueOf(Double.parseDouble(premise.grfirf().PROBABILITY(0).getText()));
			SetFact premiseFact = SetFactFactory.getInstance(factText, new GrfIrf(premiseGrf, premiseIrf), premise.LOGIC_OR() == null);
			SetPremise setPremise = new SetPremise(premiseFact.getHead(), premiseFact.getSet(), premiseFact.isNegated(), premiseFact.isConjunction());
			setPremise.setGrfIrf(premiseFact.getGrfIrf());
			premises.add(setPremise);

		}
		for (EffectContext action : ctx.effects().effect()) {
			FactContext fact;
			if (action.fact() != null) {
				fact = action.fact();
			} else {
				fact = action.assertFact().fact();
			}
			String factText = fact.LITERAL().getText();
			for (ValueContext literal : fact.value()) {
				factText += " " + literal.getText();
			}
			SetAction setAction = new DefaultSetAction(factText, "output", fact.LOGIC_OR() == null);
			conlusions.add(setAction);
		}

		SetRule rule = new SetRule(premises, conlusions, new GrfIrf(grf, irf));
		rules.add(rule);
	}
}
