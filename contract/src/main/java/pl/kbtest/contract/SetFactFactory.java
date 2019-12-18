/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.contract;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static pl.kbtest.contract.Config.GLOBAL_SPLIT_REGEX;

/**
 * @author Kamil
 */
public class SetFactFactory {

    private SetFactFactory() {
    }

    public static SetFact getInstance(final String head, final String body, final GrfIrf grfIrf,
                                      final BigDecimal wParamter, final boolean axiom, boolean conj) {
        boolean negate = false;
        String[] b = body.split(GLOBAL_SPLIT_REGEX);
        if (b.length > 1) {
            if (b[1].trim().equals("!")) {
                negate = true;
            }
        }
        Set<String> set = new HashSet<>(Arrays.asList(b));
        return new DefaultSetFact(head, set, grfIrf, wParamter, axiom, negate, conj);
    }

    public static SetFact getInstance(final String head, final String body, final GrfIrf grfIrf, boolean conj) {
        boolean negate = false;
        String[] b = body.split(GLOBAL_SPLIT_REGEX);
        if (b.length > 1) {
            if (b[1].trim().equals("!")) {
                negate = true;
            }
        }
        Set<String> set = new HashSet<>(Arrays.asList(body.split(GLOBAL_SPLIT_REGEX)));
        return new DefaultSetFact(head, set, grfIrf, BigDecimal.ONE, false, negate, conj);
    }

    public static SetFact getAxiomInstance(final String head, final String body, final GrfIrf grfIrf, boolean conj) {
        boolean negate = false;
        String[] b = body.split(GLOBAL_SPLIT_REGEX);
        if (b.length > 1) {
            if (b[1].trim().equals("!")) {
                negate = true;
            }
        }
        Set<String> set = new HashSet(Arrays.asList(body.split(GLOBAL_SPLIT_REGEX)));
        return new DefaultSetFact(head, set, grfIrf, BigDecimal.ONE, true, negate, conj);
    }

    public static SetFact getInstance(final String head, final Set<String> bodySet, final GrfIrf grfIrf, final BigDecimal w, boolean negate, boolean conj) {

        return new DefaultSetFact(head, bodySet, grfIrf, w, false, negate, conj);
    }

    @Deprecated
    public static SetFact getInstance(final String fact, final GrfIrf grfIrf, boolean conj) {
        boolean negate = false;
        String[] splitHead = fact.split("=");

        if (splitHead.length < 2) {
            System.out.println(Arrays.toString(splitHead));
            System.exit(0);
        }

        String[] parts = splitHead[1].split(GLOBAL_SPLIT_REGEX);
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        if (parts.length > 1) {
            if (parts[1].trim().equals("!")) {
                negate = true;
            }
        }
        return new DefaultSetFact(splitHead[0], new HashSet<>(Arrays.asList(parts)), grfIrf, BigDecimal.ONE, false, negate, conj);
    }

    public static SetFact getInstance(final String fact, final GrfIrf grfIrf) {
        boolean negate = false;

        boolean isConjunction = true;
        String split = Config.FACT_CONJUNCTION;

        String[] splitHead = fact.split(Config.FACT_ATTRIBUTION);

        if (splitHead.length < 2) {
            System.exit(0);
        }
        if (splitHead[1].contains(Config.FACT_DISJUNCTION)) {
            isConjunction = false;
            split = Config.FACT_DISJUNCTION;
        } else if (splitHead[1].contains(Config.FACT_DISJUNCTION) && splitHead[1].contains(Config.FACT_CONJUNCTION)) {
            throw new IllegalArgumentException();
        }
        String[] parts = splitHead[1].split(Pattern.quote(split));
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        if (parts.length > 1) {
            if (parts[1].trim().equals("!")) {
                negate = true;
            }
        }
        return new DefaultSetFact(splitHead[0], new HashSet<>(Arrays.asList(parts)), grfIrf, BigDecimal.ONE, false, negate, isConjunction);
    }


}
