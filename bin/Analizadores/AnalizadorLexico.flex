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
Minuscula=[\u00F1\u00E1\u00E9\u00ED\u00F3\u00FA\u00F6\u00FC]|[a-z]
Mayuscula=[A-Z]|[\u00D1\u00C1\u00C9\u00CD\u00D3\u00DA]
FinFrase=[\u002E\u003B]

Frase=([0-9]*[\u0025]?|
	([0-9]+|[0-9]+[\u002C][0-9]+)[\u002D\u002E]([0-9]+|[0-9]+[\u002C\u002E][0-9]+)|
	(
		[\u002D]{Mayuscula}{Minuscula}+[\u003A\u002D]?|
		{Mayuscula}{Minuscula}+[\u003A\u002D]?
		[\u002D]?{Mayuscula}{Minuscula}+[\u003A\u002D]{Minuscula}+([\u003A\u002D]{Minuscula}+)*|
		{Minuscula}+[\u003A\u002D]?|
		[\u00B5\u039C]{Minuscula}[\u002F]{Minuscula}+
	 )
       )
	(" "
	(
		("A "|
		[\u002D]?{Mayuscula}{Minuscula}+[\u003A\u002D]?|
		{Minuscula}*[\u003A\u002D]?)|
 		{Minuscula}+([\u003A\u002D]{Minuscula}+)*|
		[0-9]*[\u0025]?|
		([0-9]+|[0-9]+[\u002C][0-9]+)[\u002D\u002E]([0-9]+|[0-9]+[\u002C\u002E][0-9]+)|
		[\u00B5\u039C]{Minuscula}[\u002F]{Minuscula}+)
	)*
{FinFrase}?
Acronimo= [A-Z]{1,5}| [A-Z]+[a-z]{1} |[a-z]{1,4}[\u002D][A-Z]{2,4}| (([a-z]|[A-Z])"\."){1,5} | [0-9][\u002D][A-Z]{2,4} | [a-z]"\." |[A-Z]{1,4}[0-9]{1,3} |([A-Z]"\.")*[A-Z]
%state estado1,estado2,estado3


%%	
	<YYINITIAL> ")" {//Si detecta frases explicatorias entre parentesis
				;}
	<YYINITIAL> "(" {//Si hay acronimos en una frase no detectara el parentesis, pues al principio estaba en el estado 1
				yypushback(yytext().length());
				yybegin(estado1);}
	<YYINITIAL> " " {System.out.println("space");}
	<YYINITIAL> [\u0020] {System.out.println("space");}
	<YYINITIAL> [\u002F] {//Barra / 
			;}				
	<YYINITIAL> [\u00BB] {//comillas latinas cierre
				;}
	<YYINITIAL> [\u00B5] {//Simbolo micro
				;}
	<YYINITIAL> [\u00AB] {//comillas latinas apertura
				;}
	<YYINITIAL> [\u0027] {//comilla simple
				;}
	<YYINITIAL> [\u002A] {//asterisco
				;}
	<YYINITIAL> [\u005B\u005D] {//Corchetes 
				;}
	<YYINITIAL> [\u00DF] {//minúscula S aguda 
				;}
	<YYINITIAL> [\u00B7] {//Punto centrado
				;}
	<YYINITIAL> [\u003C] {//Simbolo menor que
				;}
	<YYINITIAL> [\u00AA] {//a sufijo
				;}
	<YYINITIAL> [\u003E] {//Simbolo mayor que
				;}
	<YYINITIAL> [\u00AE] {//Simbolo registro
				;}
	<YYINITIAL> [\u00B0] {//Simbolo grados
				;}
	<YYINITIAL> [\u00BA] {//Simbolo ordinal
				;}
	<YYINITIAL> [\u003D] {//Simbolo igual
				;}
	<YYINITIAL> [\u0026] {//Ampersan 
				;}
	<YYINITIAL> [\u0022] {//Comilla "
				;}
	<YYINITIAL> [\u002B] {//Sumatorio	
				;}
	<YYINITIAL> [\u002D] {//barra -
				System.out.println("Barra -");
				;}
	<YYINITIAL> {Acronimo} {return new Symbol(sym.acronimo,yyline +1, yycolumn +1,yytext());}
	<YYINITIAL> {Frase} {posibleLF=yytext();
				yybegin(estado1);
				acWLf.clean();
				acWLf.setFormaLarga(posibleLF);
				System.out.println("lf");}
	<YYINITIAL> [\u003A]  {//Simbolo dos puntos
				;}
	<YYINITIAL> [\u0025] {//Porcentaje
				;}
	<YYINITIAL> [\u002C] {//Coma
				;}
	<YYINITIAL> \t| \n |\r| \r\n {acronimo=""; posibleLF="";}	
	<YYINITIAL> [\u002F] {//Barra / 
			;}
	 <YYINITIAL> . {System.err.println("Error lexico: caracter no reconocido <" + yytext() + "> en la linea " + (yyline+1) 
	+ " y columna " + (yycolumn +1));}
		

<estado1> "(" {yybegin(estado2);
		System.out.println("pa");}
<estado1> [\u002D] {;}
<estado1> [^(\u002D] {String b=yytext();
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
