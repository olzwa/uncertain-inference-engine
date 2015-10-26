/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.kbtest.rule_induction;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Kamil
 */
public class Powerset {


    protected static <T> Set<T> copyWithout(Set<T> s, T e) {

        Set<T> result = new HashSet<T>(s);

        result.remove(e);

        return result;

    }


    protected static <T> Set<T> copyWith(Set<T> s, T e) {

        Set<T> result = new HashSet<T>(s);

        result.add(e);

        return result;

    }


    public static <T> Set<Set<T>> powerset(Set<T> s) {

        Set<Set<T>> result = new HashSet<Set<T>>();

        if(s.isEmpty()) {

            Set<T> empty = Collections.emptySet();

            result.add(empty);

        } else {

            for (T e : s) {

                Set<T> t = copyWithout(s, e);

                Set<Set<T>> ps = powerset(t);

                result.addAll(ps);

                for (Set<T> ts : ps) {

                    result.add(copyWith(ts, e));

                }

            }

        }

        return result;

    }


    public static void main(String[] args) {

        final List<Column> columns = new LinkedList<>();
        columns.add(new Column("kolumna1", true));
        columns.add(new Column("kolumna2"));
        columns.add(new Column("kolumna3"));
        
        Set<Column> columnsSet = new HashSet<Column>(columns);
        
//        Set<String> s = new HashSet<String>();
//
//        s.add("apple");
//
//        s.add("banana");
//
//        s.add("orange");

        System.out.println(powerset(columnsSet));

    }

}
