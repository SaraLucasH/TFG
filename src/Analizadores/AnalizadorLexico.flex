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
	String varAux="";
	String posibleLF="";
	String acronimo="";
%}
Frase=[A-Z]?[a-z]*(" "[a-z]*)*"\."?

Acronimo= [A-Z]+| [A-Z]+[a-z]{1} |[a-z]{1,4}"-"[A-Z]{2,4}| (([a-z]|[A-Z])"\."){1,5} | [0-9]"-"[A-Z]{2,4} | [a-z]"\." |[A-Z]{1,4}[0-9]{1,3} |([A-Z]"\.")*[A-Z]

%xstate estado1,estado2,estado3,estado4
%%		
	<YYINITIAL> "("	{yybegin(estado1);}	
	<YYINITIAL> \t| \n |\r| \r\n | ' '| '\.'|',' {acronimo=""; posibleLF="";}
	<YYINITIAL> {Acronimo} {acronimo=yytext();
				yybegin(estado4);}
	<YYINITIAL> {Frase} | ")" {;}
	<YYINITIAL> . {acronimo=""; posibleLF="";}
	
<estado1> {Acronimo} {varAux=yytext();
			yybegin(estado2);}
<estado1> {Frase} {if(acronimo!=""){
			posibleLF=yytext();
			yybegin(estado3);
			}else{ yybegin(YYINITIAL);
			}}
<estado1> . {yybegin(YYINITIAL);}


<estado2> ")" {yybegin(YYINITIAL);
		return new Symbol(sym.acronimo,yyline +1, yycolumn +1,varAux);}	
<estado2> . {yybegin(YYINITIAL);}


<estado3> ")" {	yybegin(YYINITIAL);
		return new Symbol(sym.formaLarga,yyline +1, yycolumn +1,posibleLF);			
		}


<estado4> "(" {yybegin(estado1);
		return new Symbol(sym.acronimo,yyline,yycolumn,acronimo);}
<estado4> [^)] {	yybegin(YYINITIAL);
		return new Symbol(sym.acronimo,yyline,yycolumn,acronimo);}
		