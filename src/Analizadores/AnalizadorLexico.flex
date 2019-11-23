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
 return new Symbol(sym.EOF, null); 
%eofval}

%{
	StringBuffer aux = new StringBuffer();
     	int auxLinea = 0;
     	int auxColumna = 0;
	String varAux="";
	String posibleLF="";
	String acronimo="";

%}
Minuscula=[ñáéíóú]|[a-z]|[´]
Mayuscula=[A-ZÑÁÉÍÓÚ]

Frase=([0-9]*|({Mayuscula}{Minuscula}+))(" "(({Mayuscula}{Minuscula}*|{Minuscula}*)|[0-9]*))*('\.')?
Acronimo= [A-Z]{1,5}| [A-Z]+[a-z]{1} |[a-z]{1,4}"-"[A-Z]{2,4}| (([a-z]|[A-Z])"\."){1,5} | [0-9]"-"[A-Z]{2,4} | [a-z]"\." |[A-Z]{1,4}[0-9]{1,3} |([A-Z]"\.")*[A-Z]
%state estado1,estado2,estado3


%%	
	<YYINITIAL> " " {System.out.println("space");}
	<YYINITIAL> {Frase} {posibleLF=yytext();
				yybegin(estado1);
				System.out.println("lf");
				return new Symbol(sym.formaLarga,yyline +1, yycolumn +1,posibleLF);}	
	 <YYINITIAL> {Acronimo} {return new Symbol(sym.acronimo,yyline +1, yycolumn +1,yytext());}
	 <YYINITIAL> '/'|\t| \n |\r| \r\n |'\.'|','|'%' {acronimo=""; posibleLF="";}	
	 <YYINITIAL> . {System.err.println("Error lexico: caracter no reconocido <" + yytext() + "> en la linea " + (yyline+1) 
	+ " y columna " + (yycolumn +1));}
		

<estado1> "(" {yybegin(estado2);
		System.out.println("pa");
		return new Symbol(sym.parentesisAbierto,yyline +1, yycolumn +1,yytext());}
<estado1> " " {;}

<estado2> {Acronimo} {acronimo=yytext();
			yybegin(estado3);
			System.out.println("ac");
			return new Symbol(sym.acronimo,yyline +1, yycolumn +1,acronimo);}


<estado3> ")" {yybegin(YYINITIAL);
		System.out.println("pc");
		return new Symbol(sym.parentesisCerrado,yyline +1, yycolumn +1,yytext());}	


	