package Analizadores;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;










import Diccionarios.CargaDiccionario;

public class Resultado {	
	HashMap<String,HashSet<String>> diccionarioFormaCorta;
	HashMap<String,String> diccionarioFormaLarga;
	CargaDiccionario cd= new CargaDiccionario();	
	HashMap<String,HashSet<String>> diccionarioTextoActual;
	HashSet<String> palabrasConectoras;	
	
	String nombreFichero="";
	
	Character e='E';
	Character s='S';
	
	/*
	 * Para la escritura del fichero de resultado
	 */	
	FileWriter fichero = null;
    PrintWriter pw = null;
	
	public Resultado(){		
		this.diccionarioTextoActual=new HashMap<>();
		cd.cargaDiccionarioSF_LFTXT("DiccionarioTest002.txt");
		this.diccionarioFormaCorta=cd.getDiccionarioSF_LF();
		this.diccionarioFormaLarga=cd.getDiccionarioLF_SF();
		
		//Preposiciones
		palabrasConectoras= new HashSet<>(Arrays.asList("a", "ante", "bajo", "cabe", "con", "contra"
				, "de", "desde", "durante", "en", "entre", "hacia", "hasta", "mediante", "para", "por", "según"
				, "sin", "so", "sobre", "tras", "versus" , "vía","el","la","los","las","le","les"));
	}
	
	public Resultado(String nombreFicheroEntrada){	
		this.nombreFichero=nombreFicheroEntrada;
		
		this.diccionarioTextoActual=new HashMap<>();
		cd.cargaDiccionarioSF_LFTXT("DiccionarioTest002.txt");
		this.diccionarioFormaCorta=cd.getDiccionarioSF_LF();
		this.diccionarioFormaLarga=cd.getDiccionarioLF_SF();
		
		//Preposiciones
		palabrasConectoras= new HashSet<>(Arrays.asList("a", "ante", "bajo", "cabe", "con", "contra"
				, "de", "desde", "durante", "en", "entre", "hacia", "hasta", "mediante", "para", "por", "según"
				, "sin", "so", "sobre", "tras", "versus" , "vía","el","la","los","las","le","les",""));
	}

	public void addDupla(String ac, String lf) {
		if (ac != null && ac != " " && ac!="" && ac!="AU") {
			//System.out.println(ac + " --- " + lf);
			if (this.diccionarioTextoActual.get(ac) == null) {
				this.diccionarioTextoActual.put(ac, new HashSet<String>());
			}
			if (lf != null && lf!="" && lf!=" ") {
				
				System.out.println("Checkeo palabra");
				String acAux=checkDoubleAcronym(ac);
				String lfChecked;
				//HashSet<String> consulta=consultaDiccionario(ac);
				
				//Empiezo por la primera forma
				String lfCheckedStart = checkLongFromStart(lf, acAux);			
				String lfCheckedEnd = checkLongFromEnd(lf, acAux);
				if(lfCheckedStart.length()>lfCheckedEnd.length()){
					lfChecked=lfCheckedEnd;
				}else{
					lfChecked=lfCheckedStart;
				}
				
				this.diccionarioTextoActual.get(ac).add(lfChecked);
			}
			if (lf != null && (ac == null || ac == "")) {
				String acAux = this.diccionarioFormaLarga.get(lf);
				if (acAux != null) {
					System.out.println("*********Accedo a dic lf*********");
					HashSet<String> longForm = new HashSet<>();
					longForm.add(lf);
					this.diccionarioTextoActual.put(acAux, longForm);
				}
			}
		}
	}
	
	private String checkDoubleAcronym(String ac) {
		if(ac.length()==4){
			Character aux0=ac.charAt(0);
			Character aux2=ac.charAt(2);
			if(aux0.equals(ac.charAt(1)) && aux2.equals(ac.charAt(3))){
				return ""+aux0+aux2;
			}
		}
		return ac;
	}

	/*
	 *  _ _ _ _ X Busco desde el final las siglas en la frase obtenida del lexico
	 *  PCR Reaccion en cadena de la polimerasa (ignorando las palabras del conjunto que tiene esta clase
	 *  como atributo)
	 */
	private String checkLongFromEnd(String lf, String ac) {
		String resultado="";
		//Si el acronimo tiene longitud 2 solo con encontrar la primera letra sirve
		int indiceAcronimo=ac.length()-1;
		lf.replaceAll("\t", " ");
		String[] frase=lf.split(" ");
		
		boolean tope=false;
		int i=frase.length-1;
		int correccionEspacio=0;
		
		if(frase.length!=1){		
			// ----------- Acronimo long 2 -----------------
			/*if (ac.length() == 2) {
				// No consideramos acronimos de longitud 1
				while (i >= 0 && !tope) {					
						Character ac1 = Character.toUpperCase(ac
								.charAt(indiceAcronimo));
						if (!this.palabrasConectoras.contains(frase[i]) && frase[i]!="") {
							if(frase[i].charAt(0)==' '){
								correccionEspacio=1;
							}else{
								correccionEspacio=0;
							}
							if (ac1.equals(s)) {
								// Puede ser o igual a S o igual a E
								if (e.equals(Character.toUpperCase(frase[i]
										.charAt(correccionEspacio)))
										|| s.equals(Character
												.toUpperCase(frase[i].charAt(correccionEspacio)))) {
									tope = true;
								} else {
									i -= 1;
								}
							} else {
								// Si no es una s entonces simplemente comparo								
								if (ac1.equals(Character.toUpperCase(frase[i]
										.charAt(correccionEspacio)))) {
									tope = true;
								} else {
									i -= 1;
								}
							}
						} else {
							i -= 1;
						}
					
				}
			//----------- Acronimo > long 2 -----------------
			}else{*/
				while(i>=0 && !tope){
					Character ac1=Character.toUpperCase(ac.charAt(indiceAcronimo)); 
					if(!this.palabrasConectoras.contains(frase[i])&& frase[i]!=""){
						if(frase[i].charAt(0)==' '){
							correccionEspacio=1;
						}else{
							correccionEspacio=0;
						}
						if(ac1.equals(s)){
							//Puede ser o igual a S o igual a E
							if(e.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio)))||
									s.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio))) ){
								if(i+1==frase.length){
									return lf;
								}								
								tope=checkRestAcFromEnd(i+1,frase,ac);
								if(!tope){
									i-=1;
								}
							}else{
								i-=1;
							}
						}else{
							// Si no es una s entonces simplemente comparo
							if (ac1.equals(Character.toUpperCase(frase[i]
									.charAt(correccionEspacio)))) {
								if (i + 1 == frase.length) {
									return lf;
								}
								tope = checkRestAcFromEnd(i + 1, frase, ac);
								if(!tope){
									i-=1;
								}
							} else {
								i -= 1;
							}
						}
					}else{
						i-=1;
					}
				}
			//}
		}else{
			return lf;
		}
		if(!tope){
			return lf;
		}
		//Ahora devuelvo la frase desde i hacia delante
		resultado=frase[i];
		i++;
		while(i<frase.length){
			resultado+=" "+frase[i];
			i++;
		}	
		return resultado;
	}

	private boolean checkRestAcFromEnd(int i, String[] frase, String ac) {
		//El acronimo tiene mas de longitud 2
		int indiceAcronimo=ac.length()-2;
		boolean check=true;
		int correccionEspacio=0;
		
		while(indiceAcronimo>=0 && check && i<frase.length){
			if (!this.palabrasConectoras.contains(frase[i])&& frase[i]!="") {
				Character aux = ac.charAt(indiceAcronimo);
				if(frase[i].charAt(0)==' '){
					correccionEspacio=1;
				}else{
					correccionEspacio=0;
				}
				if (aux.equals('S')) {
					check = aux.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio)))
							|| aux.equals(frase[i].charAt(correccionEspacio))
							|| this.e.equals(Character.toUpperCase(frase[i]
									.charAt(correccionEspacio)));
				} else {
					check = aux.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio)))
							|| aux.equals(frase[i].charAt(correccionEspacio));
				}
				indiceAcronimo -= 1;
				i += 1;
			} else {
				i += 1;
			}
		}
		//He salido del bucle
		//Dos posibilidades: He llegado al final de la frase pero aun me queda por comparar la ultima letra del acronimo
		//Simplemente he comparado todo
		
		if(indiceAcronimo==0){
			Character aux = ac.charAt(indiceAcronimo);
			check=frase[i-1].contains(""+Character.toUpperCase(aux))
			|| frase[i-1].contains(""+Character.toLowerCase(aux));			
		}
		return check;
	}

	//Metodo Soto, al menos no funciona con los primeros casos
	public static String findBestLongForm( String longForm,String shortForm) {
		int sIndex;
		int lIndex;
		char currChar;

		sIndex = shortForm.length() - 1;
		lIndex = longForm.length() - 1;
		for (; sIndex >= 0; sIndex--) {
			currChar = Character.toLowerCase(shortForm.charAt(sIndex));
			if (!Character.isLetterOrDigit(currChar))
				continue;
			while (((lIndex >= 0) && (CharacterUtil.clean(Character.toLowerCase(longForm.charAt(lIndex))) != currChar))
					|| ((sIndex == 0) && (lIndex > 0) && (Character.isLetterOrDigit(longForm.charAt(lIndex - 1)))))
			//while (((lIndex >= 0) && (Character.toLowerCase(longForm.charAt(lIndex)) != currChar))
				//	|| ((sIndex == 0) && (lIndex > 0) && (Character.isLetterOrDigit(longForm.charAt(lIndex - 1)))))
				lIndex--;
			if (lIndex < 0)
				return null;
			lIndex--;
		}
		lIndex = longForm.lastIndexOf(" ", lIndex) + 1;
		return longForm.substring(lIndex);
	}
	
	/*
	 *   X _ _ _ _  Busco desde el inicio las siglas en la frase obtenida del lexico
	 *  LCR líquido  cefalorraquídeo (ignorando las palabras del conjunto que tiene esta clase
	 *  como atributo)Si el acronimo tiene longitud>2 entonces busco las otras coincidencias(indice i)
	 */	
	private String checkLongFromStart(String lf,String ac) {		
		String resultado="";
		//Si el acronimo tiene longitud 2 solo con encontrar la primera letra sirve
		int indiceAcronimo=0;
		lf.replaceAll("\t", " ");
		String[] frase=lf.split(" ");
		
		boolean tope=false;
		int i=frase.length-1;
		int correccionEspacio=0;
		
		if(frase.length!=1){		
			//----------- Acronimo long 2 -----------------
			/*if(ac.length()==2){
				//No consideramos acronimos de longitud 1				
				while(i>=0 && !tope){
					Character ac1=Character.toUpperCase(ac.charAt(indiceAcronimo)); 
					if(!this.palabrasConectoras.contains(frase[i])&& frase[i]!=""){
						if(frase[i].charAt(0)==' '){
							correccionEspacio=1;
						}else{
							correccionEspacio=0;
						}
						if(ac1.equals(s)){
							//Puede ser o igual a S o igual a E
							if(e.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio)))||
									s.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio))) ){								
								tope=true;
							}else{
								i-=1;
							}
						}else{
							//Si no es una s entonces simplemente comparo
							if(ac1.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio)))){
								tope=true;
							}else{
								i-=1;
							}
						}
					}else{
						i-=1;
					}
				}
			//----------- Acronimo > long 2 -----------------
			}else{*/
				while(i>=0 && !tope){
					Character ac1=Character.toUpperCase(ac.charAt(indiceAcronimo)); 
					if(!this.palabrasConectoras.contains(frase[i])&& frase[i]!=""){
						if(ac1.equals(s)){
							//Puede ser o igual a S o igual a E
							if(e.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio)))||
									s.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio))) ){	
								if(i+1==frase.length){
									return lf;
								}
								tope=checkRestAcFromStart(i+1,frase,ac);
							}else{
								i-=1;
							}
						}else{
							//Si no es una s entonces simplemente comparo
							if(ac1.equals(Character.toUpperCase(frase[i].charAt(correccionEspacio)))){
								if(i+1==frase.length){
									return lf;
								}
								tope=checkRestAcFromStart(i+1,frase,ac);
							}else{
								i-=1;
							}
						}
					}else{
						i-=1;
					}
				}
			//}
		}else{
			return lf;
		}
		
		//Ahora devuelvo la frase desde i hacia delante
		if(i<0){
			return lf;
		}
		resultado=frase[i];
		i++;
		while(i<frase.length){
			resultado+=" "+frase[i];
			i++;
		}	
		return resultado;
	}

	private boolean checkRestAcFromStart(int i, String[] frase, String ac) {
		// El acronimo tiene mas de longitud 2
		int indiceAcronimo = 1;
		boolean check = true;		
		while (indiceAcronimo < ac.length() && check && i < frase.length) {
			if (!this.palabrasConectoras.contains(frase[i])&& frase[i]!="") {
				Character aux = ac.charAt(indiceAcronimo);
				if (aux.equals('S')) {
					check = aux.equals(Character.toUpperCase(frase[i].charAt(0)))
							|| aux.equals(frase[i].charAt(0))
							|| this.e.equals(Character.toUpperCase(frase[i]
									.charAt(0)));
				} else {
					check = aux.equals(Character.toUpperCase(frase[i].charAt(0)))
							|| aux.equals(frase[i].charAt(0));
				}
				indiceAcronimo += 1;
				i += 1;
			} else {
				i += 1;
			}
		}
		
		//He salido del bucle
		//Dos posibilidades: He llegado al final de la frase pero aun me queda por comparar la ultima letra del acronimo
		//Simplemente he comparado todo
		
		if(indiceAcronimo==ac.length()-1){
			Character aux = ac.charAt(indiceAcronimo);
			check=frase[i-1].contains(""+Character.toUpperCase(aux))
			|| frase[i-1].contains(""+Character.toLowerCase(aux));			
		}
		return check;
	}

	/*
	 * Escritura del fichero de resultado tsv. Escritura de nombre de las columnas si el fichero no existe.
	 */
	public String toString(){
		String result="";
		try {
			File file= new File("Training_set_results.tsv");
			if(!file.exists()){
				result="#DocumentID\tMention_A_type\tMention_A_StartOffset\tMention_A_EndOffsetMention_A\tRelation_type\t"
						+ "Mention_B_type\tMention_B_StartOffset\tMention_B_EndOffset\tMention_B\n";
			}
			fichero= new FileWriter(file,true);
			pw = new PrintWriter(fichero);	
			Iterator<Entry<String,HashSet<String>>> it=this.diccionarioTextoActual.entrySet().iterator();
			while(it.hasNext()){
				
				Entry<String,HashSet<String>> e=it.next();
				if(!e.getValue().isEmpty()){
					result+=nombreFichero+"\t";
					//Acronimo 
					result+="SHORT_FORM\t"+e.getKey()+"\tSHORT-LONG\tLONG_FORM\t";	
					Iterator<String> indice=e.getValue().iterator();				
					if(indice.hasNext()) {					
						result+=indice.next();
					}
					while(indice.hasNext()) {
						result+=" // "+indice.next()+" // ";
					}				
					result+="\n";
				}
			}
			pw.print(result);
			fichero.close();
			pw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return result;		
	}	
	
	private HashSet<String> consultaDiccionario(String acronimo) {	
		if(this.diccionarioFormaCorta==null){			
			cd.cargaDiccionarioSF_LFTXT("DiccionarioTest002");
			this.diccionarioFormaCorta=cd.getDiccionarioSF_LF();			
		}
		if(this.diccionarioFormaLarga==null){
			cd.cargaDiccionarioLF_SFTXT("DiccionarioTest002");
			this.diccionarioFormaLarga=cd.getDiccionarioLF_SF();	
		}
		System.out.println("Acronimo identificado: "+acronimo);
		HashSet<String> respuesta=this.diccionarioFormaCorta.get(acronimo);
		if(respuesta==null){			
			respuesta=new HashSet<String>();
			respuesta.add("No hay formas largas en el diccionario para este acronimo");
			
		}	
		return respuesta;
	}
	public static void main(String[] args){
		Resultado r=new Resultado();
		//System.out.println(r.checkLongFromStart("hgh dhdg dfgdhd líquido cefalorraquídeo","LCR"));
		System.out.println(r.checkLongFromEnd("y la reacción en cadena de la polimerasa","PCR"));
		//System.out.println(r.checkLongFromEnd("El  paciente fue diagnosticado de paraparesia espástica tropical","TSP"));
	}
}
