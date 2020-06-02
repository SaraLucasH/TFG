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


%{
	StringBuffer aux = new StringBuffer();
     	int auxLinea = 0;
     	int auxColumna = 0;
	String varAux="";
	String posibleLF="";
	Acronimo acronimo;
	
	//FormaLarga f=new FormaLarga();
	
	int offset=0;
	//FormaLargaWithAc acWLfacWLf=new FormaLargaWithAc ();
	
%}
Minuscula=[[\u00F1\u00E1\u00E9\u00ED\u00F3\u00FA\u00F6\u00FC]||[a-z]] 
Mayuscula=[[A-Z]||[\u00D1\u00C1\u00C9\u00CD\u00D3\u00DA]]
FinFrase=[\u002E\u003B\u002C\u003A\u002D\n]

Frase=([0-9]+[\u0025]?|
	([0-9]+|[0-9]+[\u002C][0-9]+)[\u002D\u002E]([0-9]+|[0-9]+[\u002C\u002E][0-9]+)|
	(
		([A]|[aeou]|"El"|"La"|"Los"|"Las"|"En"|"Al"|"No"|"Se"|"Ni"|"Le"|"en"|"al"|"el"|"la"|"lo"|"le"|"de"|"no"|"se"|"ni"|"le"|"y"|"un"|"Un"|"si"|"Si"|"su"|"Su"|"Es")|
		[\u002D]{Mayuscula}{Minuscula}+[\u003A\u002D]?|
		{Mayuscula}{Minuscula}{Minuscula}+[\u003A\u002D]?|
		[I]+([\u002D][I]+)?|
		{Minuscula}{Minuscula}{Minuscula}+	 
	)
       )
	(" "
	(	[aeou]| ("en"|"al"|"el"|"la"|"lo"|"le"|"de"|"no"|"se"|"ni"|"le"|"y"|"un"|"si"|"su"|"es") |
		{Minuscula}{Minuscula}{Minuscula}+|
		[\u002D]?{Minuscula}{Minuscula}{Minuscula}+[\u003A\u002D]?|		
 		{Minuscula}{Minuscula}+([\u003A\u002D]{Minuscula}{Minuscula}+)+|
		{Mayuscula}{Minuscula}{Minuscula}+|
		[0-9]+[\u0025]?|
		[I]+([\u002D][I]+)?|
		[0-9]+[u002C][0-9]+|
		([0-9]+|[0-9]+[\u002C][0-9]+)[\u002D\u002E]([0-9]+|[0-9]+[\u002C\u002E][0-9]+)		
		)
	)*

Acronimo= {Mayuscula}{2,7}|
			"VN"|
			[m]({Minuscula}|{Minuscula}{3})|
			[m]{Minuscula}?{Mayuscula}{Minuscula}|
			[[A-Z]--[I]]|
			[A-Z]+[a-z]{1} |
			[a-z]{1,3}[\u002E] |
			([\u00c2\u00b5]|[\u00b5\u03BC\u0540])|
			{Mayuscula}{1,2}" "[0-9]|
			{Mayuscula}{Minuscula}(" "?([AEGM]|"AR"))?|			
			{Mayuscula}?{Minuscula}[\u002D]("I")+|
			{Mayuscula}[\u002E]({Mayuscula}[\u002E])+|
			[[[A-Z]||[\u00D1\u00C1\u00C9\u00CD\u00D3\u00DA]]--[L]][\u002E]|
			{Minuscula}[\u002E](({Minuscula}[\u002E])+|" ")|
			[[[A-Z]||[\u00D1\u00C1\u00C9\u00CD\u00D3\u00DA]]--[UL]][\u002F][[[A-Z]||[\u00D1\u00C1\u00C9\u00CD\u00D3\u00DA]]--[L]]|
			[D]{Minuscula}[\u002F][D]{Minuscula}|
			([\u00c2\u00b5]|[\u00b5\u03BC\u0540]){Mayuscula}+|
			([\u00c2\u00b5]|[\u00b5\u03BC\u0540]){Minuscula}+({Mayuscula}+)?|
			{Mayuscula}{Minuscula}{Mayuscula}|	
			{Mayuscula}+{Minuscula}|
			{Minuscula}{Minuscula}{5}{Minuscula}*(" ")?{Mayuscula}((" ")?{Minuscula}+)?|
			{Mayuscula}{Minuscula}([0-9]+{Minuscula}|[0-9]+|[\u002E])? |
			{Minuscula}{Mayuscula}{Minuscula}|
			{Minuscula}{1,4}({Mayuscula}{1,3}|{Mayuscula}{1,3}[0-9]|[0-9])?|
			{Mayuscula}{Minuscula}+[\u002D]([0-9]+[\u002D]|{Mayuscula})?{Minuscula}+|
			{Mayuscula}{1,4}[\u002D]({Mayuscula}{1,4}[0-9]+|{Mayuscula}{1,2}|{Mayuscula}{4}|[0-9]+|{Minuscula}+)|
			{Mayuscula}{4}[\u002D]{Mayuscula}{3}|
			{Mayuscula}{3}[\u002D]{Mayuscula}{4}|
			{Mayuscula}{1,2}[0-9]+((([\u002E]|" ")[0-9]+)|{Mayuscula}{1,3})?|
			{Mayuscula}{3}[0-9]|
			{Mayuscula}{Minuscula}{1,2}{Mayuscula}{3}|
			[0-9][\u002D][A-Z]{2,4} 
					 
%state estado1,estado2,estado3,estado4


%%
	<YYINITIAL> [\u03b1] {//Alpha
		offset=offset+yytext().length();}
	<YYINITIAL> [\u00B1] {//mas menos
		offset=offset+yytext().length();}
	<YYINITIAL> [\u03b3] {//gamma
		offset=offset+yytext().length();}
	<YYINITIAL> [\u00E5] {//a con anillo encima 
		offset=offset+yytext().length();}	
	<YYINITIAL> [\u2122] {//tm
		offset=offset+yytext().length();}
	<YYINITIAL> [\u2014] {//guion largo 
		offset=offset+yytext().length();}	
	<YYINITIAL> [\u2022] {//Bala
		offset=offset+yytext().length();}
	<YYINITIAL> [\u00E4] {//a minuscula dieresis
		offset=offset+yytext().length();}	
	<YYINITIAL> [\u03B2] {//Beta
		offset=offset+yytext().length();}
	<YYINITIAL> [\u00A0] {//Espacio de no separacion
		offset=offset+yytext().length();}
	<YYINITIAL> ")" {//Si detecta frases explicatorias entre parentesis
				offset=offset+yytext().length();}
	<YYINITIAL> "("|"[" {//Si hay acronimos en una frase no detectara el parentesis, pues al principio estaba en el estado 1
				yypushback(yytext().length());
				yybegin(estado1);}
	<YYINITIAL> " "|[\u0020] {offset=offset+yytext().length();}	
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
	<YYINITIAL> {Frase}({FinFrase}|" ") {posibleLF=yytext();				
				offset=offset+posibleLF.length();
				yybegin(estado1);				
				return new Symbol(sym.frase,posibleLF);}
	<YYINITIAL> {Acronimo} {
				offset=offset+yytext().length();
				acronimo= new Acronimo(offset-yytext().length(),offset,yytext());
				yybegin(estado4);}
	<YYINITIAL> "¿"|"?" {offset=offset+yytext().length();}
	<YYINITIAL> [\u002C] {//Coma
				offset=offset+yytext().length();}
	<YYINITIAL> [\u002E] {//Punto
				offset=offset+yytext().length();}
	<YYINITIAL> [0-9] {//NUMERO
				offset=offset+yytext().length();}
	<YYINITIAL> " "|[\u0020] {offset=offset+yytext().length();}				
	<YYINITIAL> [\u002D] {//barra -
				offset=offset+yytext().length();}
	<YYINITIAL> {FinFrase} {offset=offset+yytext().length();}
	<YYINITIAL> [\u0025]  {//porcentaje
				offset=offset+yytext().length();}
	<YYINITIAL>	[\u005c][\u0072][\u005c][\u006e] | [\u005c][\u006e] {offset=offset+yytext().length();}
	<YYINITIAL> [\t]| [\n] | [\r]| "\r\n" {offset=offset+yytext().length(); posibleLF="";}	
	<YYINITIAL> . {offset=offset+yytext().length();System.err.println("Error lexico: caracter no reconocido <" + yytext() + "> en la linea " + (yyline+1) 
	+ " y columna " + (yycolumn +1));}
		

<estado1> "("|"[" {offset=offset+yytext().length();yybegin(estado2);
		}
<estado1> [\u002D] {offset=offset+yytext().length();}
<estado1> [^(\u002D] {String b=yytext();
			if(b!=null){
				yypushback(b.length());
			}
			yybegin(YYINITIAL);}
			
<estado2> {Frase}({FinFrase}{Frase})* {posibleLF=yytext(); offset=offset+posibleLF.length();return new Symbol(sym.frase,posibleLF);}
<estado2> {Acronimo} {
			offset=offset+yytext().length();
			acronimo= new Acronimo(offset-yytext().length(),offset,yytext());			
			yybegin(estado3);
			}

<estado2> [^] {	if(yytext()!=null){
			yypushback(yytext().length());
			}
			yybegin(YYINITIAL);}

<estado3> ")"|"]" {offset=offset+yytext().length();
				yybegin(YYINITIAL);		
				//Cuidado si le paso el objeto en el sintactico lo usa como puntero y solo se guarda la ultima ocurrencia. Por ello new Object
				return new Symbol(sym.acWithContext,yyline +1, yycolumn +1,new AcWithContext(new Acronimo(acronimo.getStartOffset(),acronimo.getEndOffset(),acronimo.getAcronimo()),posibleLF));
			}	
<estado3> [^)] {if(yytext()!=null){
			yypushback(yytext().length());
			}
			yybegin(YYINITIAL);}
			
<estado4> ({FinFrase}|" "|[\u002f]|")") {
			offset=offset+yytext().length();
			yybegin(YYINITIAL);
			return new Symbol(sym.acWithContext,yyline +1, yycolumn +1,new AcWithContext(new Acronimo(acronimo.getStartOffset(),acronimo.getEndOffset(),acronimo.getAcronimo()),posibleLF));}
<estado4> [^] {	if(yytext()!=null){
					yypushback(yytext().length());
				}
				yybegin(YYINITIAL);}
			
<<EOF>> {offset=offset+yytext().length(); return new Symbol(sym.EOF);}
