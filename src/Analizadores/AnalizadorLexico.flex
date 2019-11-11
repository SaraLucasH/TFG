package Analizadores;
import java.util.LinkedList;
import java_cup.runtime.*;


%%
%public
%class AnalizadorLexico
%unicode
%line
%column
%cup 
%full

%{
	StringBuffer aux = new StringBuffer();
     	int auxLinea = 0;
     	int auxColumna = 0;
%}

Acronimo= [A-Z]+ 
Letra=[a-z]{1,4}"-"[A-Z]{2,4}| (([a-z]|[A-Z])"\."){1,5} | [0-9]"-"[A-Z]{2,4} | [a-z]"\." |[A-Z]{1,4}[0-9]{1,3} |([A-Z]"\.")*[A-Z]

%%	
	<YYINITIAL> "("	{return new Symbol(sym.parentesisAbierto,yyline+1,yycolumn+1,yytext());}
	<YYINITIAL> ")"	{return new Symbol(sym.parentesisCerrado,yyline+1,yycolumn+1,yytext());}
	<YYINITIAL> \t| \n |\r| \r\n | ' '| '\.'|',' {;}
	<YYINITIAL> {Acronimo} {return new Symbol(sym.acronimo,yyline +1, yycolumn +1,yytext());}	
	<YYINITIAL> ([A-Z] |[a-z])+	{;}
	<YYINITIAL> . {;}
	

	