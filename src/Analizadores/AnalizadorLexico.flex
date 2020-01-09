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
	FormaLargaWithAc acWLf=new FormaLargaWithAc ();
	
%}
Minuscula=[\u00F1\u00E1\u00E9\u00ED\u00F3\u00FA]|[a-z]
Mayuscula=[A-Z]|[\u00D1\u00C1\u00C9\u00CD\u00D3\u00DA]

Frase=([0-9]*|[0-9]+[\u002D][0-9]+|({Mayuscula}{Minuscula}+[\u003A]? | {Minuscula}+[\u003A]?))(" "(("A "|{Mayuscula}{Minuscula}+[\u003A]?|{Minuscula}*[\u003A]?)|[0-9]*))*((\u002E)|(\u003B))?
Acronimo= [A-Z]{1,5}| [A-Z]+[a-z]{1} |[a-z]{1,4}"-"[A-Z]{2,4}| (([a-z]|[A-Z])"\."){1,5} | [0-9]"-"[A-Z]{2,4} | [a-z]"\." |[A-Z]{1,4}[0-9]{1,3} |([A-Z]"\.")*[A-Z]
%state estado1,estado2,estado3


%%	
	<YYINITIAL> ")" {//Si detecta frases explicatorias entre parentesis
				;}
	<YYINITIAL> "(" {//Si hay acronimos en una frase no detectara el parentesis, pues al principio estaba en el estado 1
				yypushback(yytext().length());
				yybegin(estado1);}				
	<YYINITIAL> [\u002D] {//barra -
				;}
	<YYINITIAL> [\u003C] {//Simbolo menor que
				;}
	<YYINITIAL> [\u0025] {//Porcentaje
				;}
	<YYINITIAL> [\u0022] {//Comilla "
				;}
	<YYINITIAL> " " {System.out.println("space");}
	<YYINITIAL> {Acronimo} {return new Symbol(sym.acronimo,yyline +1, yycolumn +1,yytext());}
	<YYINITIAL> {Frase} {posibleLF=yytext();
				yybegin(estado1);
				acWLf.clean();
				acWLf.setFormaLarga(posibleLF);
				System.out.println("lf");}	
	 
	 <YYINITIAL> [\u002C] {;}
	 <YYINITIAL> \t| \n |\r| \r\n |',' {acronimo=""; posibleLF="";}	
	<YYINITIAL> [\u002F] {//Barra / 
			;}
	 <YYINITIAL> . {System.err.println("Error lexico: caracter no reconocido <" + yytext() + "> en la linea " + (yyline+1) 
	+ " y columna " + (yycolumn +1));}
		

<estado1> "(" {yybegin(estado2);
		System.out.println("pa");}

<estado1> [^(] {String b=yytext();
			if(b!=null){
				yypushback(b.length());
				acWLf.clean();
			}
			yybegin(YYINITIAL);}

<estado2> {Acronimo} {acronimo=yytext();
			acWLf.setAcronimo(acronimo);
			yybegin(estado3);
			System.out.println("ac");}
<estado2> {Frase}")" {yybegin(YYINITIAL); acWLf.clean();}
<estado2> {Frase}" )" {yybegin(YYINITIAL); acWLf.clean();}
<estado2> [^] {	if(yytext()!=null){
			yypushback(yytext().length());
			acWLf.clean();}
			yybegin(YYINITIAL);}

<estado3> ")" {yybegin(YYINITIAL);
		System.out.println("pc");
		//Cuidado si le paso el objeto en el sintactico lo usa como puntero y solo se guarda la ultima ocurrencia. Por ello new Object
		return new Symbol(sym.acWithLf,yyline+1,yycolumn+1,new FormaLargaWithAc(acWLf.getAcronimo(),acWLf.getFormaLarga()));
		
		}	
<estado3> [^)] {if(yytext()!=null){
			yypushback(yytext().length());
			acWLf.clean();}
			yybegin(YYINITIAL);}

	