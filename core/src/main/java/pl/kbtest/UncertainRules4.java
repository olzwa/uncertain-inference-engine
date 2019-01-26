/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.kbtest;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import pl.kbtest.app.UncertainRulesApp;
import pl.kbtest.contract.Context;
import pl.kbtest.contract.SetFact;
import pl.kbtest.contract.SetRule;
import pl.kbtest.utils.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import static pl.kbtest.app.Commands.DB_SERVICE_ADDRESS;
import static pl.kbtest.app.Commands.FIRE_RULES;
import static pl.kbtest.app.Commands.HELP;
import static pl.kbtest.app.Commands.LOAD_FACTS;
import static pl.kbtest.app.Commands.LOAD_RULES;
import static pl.kbtest.app.Commands.SERVICE_MODE;

public class UncertainRules4 {

	public static void main(String[] args) throws Exception {
		Deque<SetRule> rules = new ConcurrentLinkedDeque<>();
		Deque<SetFact> facts = new ConcurrentLinkedDeque<>();

		Context context = new Context(facts, rules);
		UncertainRuleEngine engine = new UncertainRuleEngine(context);

		Set delimiters = new HashSet<>(Arrays.asList("="));
		String conjunctionToken = "AND";
		String disjunctionToken = "OR";

		Options options = new Options();
		CommandLineParser parser = new BasicParser();

		Option runAsServiceOption = OptionBuilder
				.withDescription("run application as service [default=localhost:8080")
				.create(getAsProgramArg(SERVICE_MODE));

		Option setDbServiceAddress = OptionBuilder
				.hasArg()
				.withDescription("set address of db-service")
				.withArgName("address")
				.create(getAsProgramArg(DB_SERVICE_ADDRESS));

		Option loadRulesOption = OptionBuilder
				.hasArg()
				.withDescription("load rules from file")
				.withArgName("filename")
				.create(getAsProgramArg(LOAD_RULES));

		Option loadFactsOption = OptionBuilder
				.hasArg()
				.withDescription("load facts from file")
				.withArgName("filename")
				.create(getAsProgramArg(LOAD_FACTS));

		Option fireRulesOption = new Option(getAsProgramArg(FIRE_RULES), "run inference process");
		Option helpOption = new Option(HELP, "print this message");

		options.addOption(runAsServiceOption);
		options.addOption(setDbServiceAddress);
		options.addOption(loadRulesOption);
		options.addOption(loadFactsOption);
		options.addOption(fireRulesOption);
		options.addOption(helpOption);

		CommandLine line = parser.parse(options, args);
		if (line.hasOption(getAsProgramArg(LOAD_RULES))) {
			String fileName = line.getOptionValue(getAsProgramArg(LOAD_RULES));
			File ruleFile = new File(fileName);
			Deque<SetRule> loadedRules = Utils.loadJsonRulesAction(ruleFile);
			rules.addAll(loadedRules);
		}
		if (line.hasOption(getAsProgramArg(LOAD_FACTS))) {
			String fileName = line.getOptionValue(getAsProgramArg(LOAD_FACTS));
			File factFile = new File(fileName);
			Deque<SetFact> loadedFacts = Utils.loadJsonFactsAction(factFile);
			facts.addAll(loadedFacts);
		}

		if (line.hasOption(getAsProgramArg(FIRE_RULES))) {
			engine.fireRules();
		}

		if (line.hasOption(HELP)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar uncertain-inference.jar", options);
			System.exit(0);
		}

		UncertainRulesApp app = new UncertainRulesApp(rules, facts);

		if (line.hasOption(SERVICE_MODE)) {
			String address = "http://" + line.getOptionValue(getAsProgramArg(DB_SERVICE_ADDRESS), "localhost:8080");
			Utils.log("using db-service address: " + address);
			app.serviceMode(address);
		}
		else app.cliMode();
	}

	private static String getAsProgramArg(String command) {
		return command.replaceAll("\\s+", "");
	}

}
