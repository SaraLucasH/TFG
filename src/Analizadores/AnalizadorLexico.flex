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

%eofval{
 
%eofval}

%{
	StringBuffer aux = new StringBuffer();
     	int auxLinea = 0;
     	int auxColumna = 0;
	String varAux="";
	String posibleLF="";
	String acronimo="";

%}

Frase=([0-9]*|([A-ZÑÁÉÍÓÚ][a-zñáéíóú]+))(' '([a-zñáéíóú]*|[0-9]*))*('\.')?
Acronimo= [A-Z]{1,5}| [A-Z]+[a-z]{1} |[a-z]{1,4}"-"[A-Z]{2,4}| (([a-z]|[A-Z])"\."){1,5} | [0-9]"-"[A-Z]{2,4} | [a-z]"\." |[A-Z]{1,4}[0-9]{1,3} |([A-Z]"\.")*[A-Z]
%xstate estado1,estado2,estado3


%%	
	{Frase} {posibleLF=yytext();
				yybegin(estado1);
				return new Symbol(sym.formaLarga,yyline +1, yycolumn +1,posibleLF);}	
	 {Acronimo} {return new Symbol(sym.acronimo,yyline +1, yycolumn +1,yytext());}
	 '/'|\t| \n |\r| \r\n | ' '| '\.'|','|'%' {acronimo=""; posibleLF="";}	
	 . {System.err.println("Error lexico: caracter no reconocido <" + yytext() + "> en la linea " + (yyline+1) 
	+ " y columna " + (yycolumn +1));}

<estado1> "(" {yybegin(estado2);
		return new Symbol(sym.parentesisAbierto,yyline +1, yycolumn +1,yytext());}

<estado2> {Acronimo} {acronimo=yytext();
			yybegin(estado3);
			return new Symbol(sym.acronimo,yyline +1, yycolumn +1,acronimo);}
<estado3> ")" {yybegin(YYINITIAL);
		return new Symbol(sym.parentesisCerrado,yyline +1, yycolumn +1,yytext());}	



		