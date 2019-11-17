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
FormaLarga={Frase}"("

Acronimo= [A-Z]+| [A-Z]+[a-z]{1} |[a-z]{1,4}"-"[A-Z]{2,4}| (([a-z]|[A-Z])"\."){1,5} | [0-9]"-"[A-Z]{2,4} | [a-z]"\." |[A-Z]{1,4}[0-9]{1,3} |([A-Z]"\.")*[A-Z]


%xstate estado1,estado2,estado3,estado4
%%		
	<YYINITIAL> {FormaLarga} {posibleLF=yytext();
					yybegin(estado1);}	
	<YYINITIAL> \t| \n |\r| \r\n | ' '| '\.'|',' {acronimo=""; posibleLF="";}
	<YYINITIAL> {Acronimo} {return new Symbol(sym.acronimo,yyline +1, yycolumn +1,yytext());}
	<YYINITIAL> . {;}
	
<estado1> {Acronimo} {acronimo=yytext();
			yybegin(estado2);
			return new Symbol(sym.acronimo,yyline +1, yycolumn +1,varAux);
						
			}
<estado1> {Frase} {yybegin(YYINITIAL);}
<estado1> . {yybegin(YYINITIAL);}


<estado2> ")" {yybegin(YYINITIAL);
		return new Symbol(sym.formaLarga,yyline +1, yycolumn +1,posibleLF);}	
<estado2> [^)] {yybegin(YYINITIAL);}


		