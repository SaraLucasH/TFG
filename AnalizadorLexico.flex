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

Acronimo= "("[A-Z]+")" |[a-z]{1,4}"-"[A-Z]{2,4}| (([a-z]|[A-Z])"\."){1,5} | [0-9]"-"[A-Z]{2,4} | [a-z]"\." |[A-Z]{1,4}[0-9]{1,3} |([A-Z]"\.")*[A-Z] | [A-Z]+ 

%%	
	{Acronimo} {return new Symbol(sym.acronimo,yyline +1, yycolumn +1,yytext());}	
	([A-Z] |[a-z])+	{;}
	"\t"| "\n" |"\r"| "\r\n" | " "| "\."|"," {;}
	. {;}
	

	