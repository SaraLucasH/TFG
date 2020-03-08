 package Analizadores;
import java.util.LinkedList;
import java_cup.runtime.*;


%%

%public
%class AnalizadorLexico

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
	Acronimo acronimo=new Acronimo();
	int offset=0;
	
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
Acronimo= [A-Z]{1,5}| [A-Z]+[a-z]{1} |
{Mayuscula}{Minuscula}|{Minuscula}{1,3}|{Mayuscula}{1,4}[\u002D]{Mayuscula}{1,4}|
[a-z]{1,4}[\u002D][A-Z]{2,4}| (([a-z]|[A-Z])"\."){1,5} | [0-9][\u002D][A-Z]{2,4} | [a-z]"\." |[A-Z]{1,4}[0-9]{1,3}|([A-Z][0-9])+ |([A-Z]"\.")*[A-Z]
%state estado1,estado2,estado3


%%	
	<YYINITIAL> [\u03b1] {//Alpha
		System.out.println("Alpha");offset=offset+yytext().length();}
	<YYINITIAL> [\u03b3] {//gamma
		System.out.println("Gamma");offset=offset+yytext().length();}
	<YYINITIAL> [\u2122] {//tm
		offset=offset+yytext().length();}
	<YYINITIAL> [\u2022] {//Bala
		offset=offset+yytext().length();}
	<YYINITIAL> [\u00A0] {//Espacio de no separacion
		offset=offset+yytext().length();}
	<YYINITIAL> ")" {//Si detecta frases explicatorias entre parentesis
				offset=offset+yytext().length();}
	<YYINITIAL> "(" {//Si hay acronimos en una frase no detectara el parentesis, pues al principio estaba en el estado 1
				yypushback(yytext().length());
				
				yybegin(estado1);}
	<YYINITIAL> " " {System.out.println("space");
				offset=offset+yytext().length();}
	<YYINITIAL> [\u0020] {System.out.println("space");
				offset=offset+yytext().length();}
	<YYINITIAL> [\u002F] {//Barra / 
				offset=offset+yytext().length();}
	<YYINITIAL> [\u007B] {//Corchete derecho { 
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00BD] {//fraccion 1 medio 
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00D7] {//Aspa multiplicacion
				offset=offset+yytext().length();}
	<YYINITIAL> [\u007D] {//Corchete izq } 
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00BB] {//comillas latinas cierre
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00B5\u03BC] {//Simbolo micro
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00AB] {//comillas latinas apertura
				offset=offset+yytext().length();}
	<YYINITIAL> [\u0027] {//comilla simple
				offset=offset+yytext().length();}
	<YYINITIAL> [\u002A] {//asterisco
				offset=offset+yytext().length();}
	<YYINITIAL> [\u005B\u005D] {//Corchetes 
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00DF] {//minúscula S aguda 
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00B7] {//Punto centrado
				offset=offset+yytext().length();}
	<YYINITIAL> [\u003C] {//Simbolo menor que
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00AA] {//a sufijo
				offset=offset+yytext().length();}
	<YYINITIAL> [\u003E] {//Simbolo mayor que
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00AE] {//Simbolo registro
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00B0] {//Simbolo grados
				offset=offset+yytext().length();}
	<YYINITIAL> [\u00BA] {//Simbolo ordinal
				offset=offset+yytext().length();}
	<YYINITIAL> [\u003D] {//Simbolo igual
				offset=offset+yytext().length();}
	<YYINITIAL> [\u0026] {//Ampersan 
				offset=offset+yytext().length();}
	<YYINITIAL> [\u0022] {//Comilla "
				offset=offset+yytext().length();}
	<YYINITIAL> [\u002B] {//Sumatorio	
				offset=offset+yytext().length();}
	<YYINITIAL> {Frase} {posibleLF=yytext();
				offset=offset+yytext().length();
				yybegin(estado1);
				acWLf.clean();
				acWLf.setFormaLarga(posibleLF);
				System.out.println("lf");}
	<YYINITIAL> {Acronimo} {System.out.println("Acronimo");
				offset=offset+yytext().length();
				return new Symbol(sym.acronimo,yyline +1, yycolumn +1,new Acronimo(offset-yytext().length(),offset,yytext()));}
	
				
	<YYINITIAL> [\u002D] {//barra -
				System.out.println("Barra -");
				offset=offset+yytext().length();}
	
	<YYINITIAL> [\u003A]  {//Simbolo dos puntos
				offset=offset+yytext().length();}
	<YYINITIAL> [\u0025] {//Porcentaje
				offset=offset+yytext().length();}
	<YYINITIAL> [\u002C] {//Coma
				offset=offset+yytext().length();}
	<YYINITIAL> \u03b1 {//Alpha
		System.out.println("Alpha");
		offset=offset+yytext().length();}
	<YYINITIAL> \t| \n |\r| \r\n {acronimo=new Acronimo(); offset=offset+yytext().length(); posibleLF="";}	
	<YYINITIAL> [\u002F] {//Barra / 
			offset=offset+yytext().length();}
	 <YYINITIAL> . {System.err.println("Error lexico: caracter no reconocido <" + yytext() + "> en la linea " + (yyline+1) 
	+ " y columna " + (yycolumn +1));
	offset=offset+yytext().length();}
		

<estado1> "(" {offset=offset+yytext().length();yybegin(estado2);
		System.out.println("pa");}
<estado1> [\u002D] {offset=offset+yytext().length();}
<estado1> [^(\u002D] {String b=yytext();
			if(b!=null){
				yypushback(b.length());
				acWLf.clean();
			}
			yybegin(YYINITIAL);}

<estado2> {Acronimo} {System.out.println("Acronimo");
			acronimo= new Acronimo(offset,offset+yytext().length(),yytext());
			offset=offset+yytext().length();
			acWLf.setAcronimo(acronimo);
			yybegin(estado3);
			System.out.println("ac");}
<estado2> {Frase}")" {offset=offset+yytext().length(); yybegin(YYINITIAL); acWLf.clean();}
<estado2> {Frase}" )" {offset=offset+yytext().length();yybegin(YYINITIAL); acWLf.clean();}
<estado2> [^] {	if(yytext()!=null){
			yypushback(yytext().length());
			acWLf.clean();}
			yybegin(YYINITIAL);}

<estado3> ")" {offset=offset+yytext().length();yybegin(YYINITIAL);
		System.out.println("pc");
		//Cuidado si le paso el objeto en el sintactico lo usa como puntero y solo se guarda la ultima ocurrencia. Por ello new Object
		return new Symbol(sym.acWithLf,yyline+1,yycolumn+1,new FormaLargaWithAc(acWLf.getAcronimo(),acWLf.getFormaLarga()));
		
		}	
<estado3> [^)] {if(yytext()!=null){
			yypushback(yytext().length());
			acWLf.clean();}
			yybegin(YYINITIAL);}
