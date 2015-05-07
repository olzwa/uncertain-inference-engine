grammar Uncertain;
options {
	language = Java;
}

@header {
package pl.kbtest.parser.grammar;
}

@members {
	
 //private java.util.Map<?, ?> temporary = new java.util.HashMap<?,?>();
 public java.util.List<String> facts = new java.util.ArrayList<String>();	
 
}

compile: (defineRule | assertFact)*
	;
		   
COMMENT : WS?'//' ~[\r\n]* WS? -> skip;

PROBABILITY: '1'|('0'([\.][0-9]+)?) ;

NUMBER: [0-9]+([\.][0-9]+)?;
  
VARIABLE: '?' LITERAL;
  
LITERAL: [a-z][a-zA-Z0-9]+; 
STRING: ["] ([a-zA-Z0-9\s]|WS)* ["];
LOGIC_OR: '+';

assertFact : WS? '(assert' WS fact WS? ')' WS?;
  
 
effects: '=>' WS (effect WS?)+ WS?;
effect: (fact|assertFact);

defineRule : '(rule' WS LITERAL WS (fact WS)+ effects')'; 
 
fact: WS? LOGIC_OR? '(' WS? LITERAL WS (value WS)+ WS? '{' WS? PROBABILITY WS? ';' WS? PROBABILITY WS? '}' WS? ')' WS?; 
value: (LITERAL|fact|STRING|NUMBER|VARIABLE);

   

ID : [a-z]+ ;             // match lower-case identifiers

WS : [ \t\r\n]+ ; // skip spaces, tabs, newlines


 