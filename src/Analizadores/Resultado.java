package Analizadores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import Diccionarios.BARRDiccionario;
import Diccionarios.CargaDiccionario;
import Diccionarios.SEDOMDiccionario;

public class Resultado {	
	
	private class ObjetoDevuelto{
		private boolean check;
		private int index;
		
		public ObjetoDevuelto(boolean check, int index) {
			this.check=check;
			this.index=index;
		}
		
		public boolean isCheck() {
			return check;
		}
		public void setCheck(boolean check) {
			this.check = check;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}	
		
	}
	private HashMap<String, HashSet<String>> diccionarioConsultaBARR;
	private HashMap<String,HashSet<String>> diccionarioConsulta;
	CargaDiccionario cd= new CargaDiccionario();	
	HashMap<Acronimo,HashSet<FormaLarga>> diccionarioTextoActual;
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
		this.diccionarioConsulta=new SEDOMDiccionario().getDiccionario();
		this.diccionarioConsultaBARR= new BARRDiccionario().getDiccionario();
		//Preposiciones
		palabrasConectoras= new HashSet<>(Arrays.asList("a", "ante", "bajo", "cabe", "con", "contra"
				, "de", "desde", "durante", "en", "entre", "hacia", "hasta", "mediante", "para", "por", "seg√∫n"
				, "sin", "so", "sobre", "tras", "versus" , "v√≠a","el","la","los","las","le","les","and", "with"));
	}
	
	public Resultado(String nombreFicheroEntrada){	
		this.nombreFichero=nombreFicheroEntrada;
		
		this.diccionarioTextoActual=new HashMap<>();
		this.diccionarioConsulta= new HashMap<>();
		
		//Preposiciones
		palabrasConectoras= new HashSet<>(Arrays.asList("a", "ante", "bajo", "cabe", "con", "contra"
				, "de", "desde", "durante", "en", "entre", "hacia", "hasta", "mediante", "para", "por", "seg√∫n"
				, "sin", "so", "sobre", "tras", "versus" , "v√≠a","el","la","los","las","le","les",""));
	}

	public void addDupla(Acronimo acronimo, FormaLarga f) {
		String ac = acronimo.getAcronimo();
		
		
		if (ac != null && ac != " " && ac != "" && ac != "AU") {
			// System.out.println(ac + " --- " + lf);
			if (this.diccionarioTextoActual.get(acronimo) == null) {
				this.diccionarioTextoActual.put(acronimo, new HashSet<FormaLarga>());
			}
			if (f != null) {
				String lf = f.getLf();
				System.out.println("ENTRADA " + ac + " " + lf);
				if (lf != "" && lf != " "&& lf!=null) {
					System.out.println("Checkeo palabra");
					String acAux = checkDoubleAcronym(ac);
					if(acAux.contains(".")) {
						acAux=acAux.replaceAll("([.])", "");
					}
					String lfChecked;

					// 1∫Consulto dic unidades medida y sedom
					/*
					 * HashSet<String> valorSEDOM=this.diccionarioConsulta.get(ac); HashSet<String>
					 * valorBARR; if(valorSEDOM!=null) { if(valorSEDOM.size()==1) {
					 * this.diccionarioTextoActual.get(ac).add(valorSEDOM.iterator().next()); } }
					 */
					/*
					 * if(valorSEDOM== null|| valorSEDOM.size()>1) { //Busco tambien en diccionario
					 * tarea BARR2 o BARR1 valorBARR=this.diccionarioConsultaBARR.get(ac);
					 * if(valorBARR!=null) { //Desambiguacion }
					 */

					String lfCheckedStart = checkLongFromStart(lf, acAux);
					String lfCheckedEnd = checkLongFromEnd(lf, acAux);
					String lfMetodoAuxiliar1 = checkMetodoAuxiliar1(lf, acAux);
					String lfMetodoAuxiliar2 = checkMetodoAuxiliar2(lf, acAux);
					if(lfCheckedStart.length()<lfCheckedEnd.length()) {
						lfChecked = lfCheckedStart;
					}else if(lfCheckedStart.length()>lfCheckedEnd.length()) {
						lfChecked = lfCheckedEnd;
					}else {
						//Iguales
						if(lfMetodoAuxiliar1.length()< lfMetodoAuxiliar2.length()) {
							lfChecked = lfMetodoAuxiliar1;
						}else if(lfMetodoAuxiliar1.length() > lfMetodoAuxiliar2.length()) {
							lfChecked = lfMetodoAuxiliar2;
						}else if((lfMetodoAuxiliar1.length() ==  lfMetodoAuxiliar2.length())&& lfMetodoAuxiliar1.length()<lfCheckedStart.length()){
							lfChecked = lfMetodoAuxiliar1;
						}else {
							lfChecked = lfCheckedStart;
						}
					}
					
					f = new FormaLarga(lfChecked, FormaLarga.getStart(f, lfChecked), FormaLarga.getEnd(f, lfChecked));
					if (lfChecked.equalsIgnoreCase(lf)) {
						// Compruebo los nested. Si es igual es porque ha fallado la busqueda
						System.out.println("Nested");
						if (!ac.toUpperCase().equals(ac)) {
							acronimo.setNested(true);
							this.diccionarioTextoActual.put(acronimo, new HashSet<FormaLarga>());
						} else {
							// Culpa de la forma larga
							f.setNested(true);
						}
					}

					/*
					 * int minLong = Math.min(lfCheckedStart.length(),
					 * Math.min(lfCheckedEnd.length(), Math.min(lfMetodoAuxiliar1.length(),
					 * lfMetodoAuxiliar2.length()))); if (minLong == lfCheckedStart.length())
					 * lfChecked = lfCheckedStart; else if (minLong == lfCheckedEnd.length())
					 * lfChecked = lfCheckedEnd; else if (minLong == lfMetodoAuxiliar1.length())
					 * lfChecked = lfMetodoAuxiliar1; else lfChecked = lfMetodoAuxiliar2;
					 */
					System.out.println(
							"SALIDA LF " + lfChecked + " aÒadido a " + this.diccionarioTextoActual.get(acronimo));
					this.diccionarioTextoActual.get(acronimo).add(f);
				}
			}
		}
	}
	
	/*
	 * METODO AUXILIAR 1: IHQ, EEG son ejemplos. Se comprueba que exista una palabra que contenga todas
	 * siglas del acronimo, en ese orden.
	 */
	private String checkMetodoAuxiliar1(String lf, String ac) {
		//Desde el inicio del acronimo	
		boolean check=true;	
		String[] frase=lf.split(" ");
		int i=frase.length-1;
		
		String acAux=ac.toUpperCase();
		while(i>=0 && check) {
			int indiceAc=0;
			int indexSigla=-1;
			String cadenaNormalize = Normalizer.normalize(frase[i], Normalizer.Form.NFD);   
			String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "").toUpperCase();
			while(indiceAc<acAux.length()&& check) {
				if(cadenaSinAcentos.indexOf(acAux.charAt(indiceAc))>=indexSigla && cadenaSinAcentos.indexOf(acAux.charAt(indiceAc))!=-1) {
					indexSigla=cadenaSinAcentos.indexOf(acAux.charAt(indiceAc));
					if(indexSigla<cadenaSinAcentos.length()) {
						cadenaSinAcentos=cadenaSinAcentos.substring(indexSigla+1);
						indexSigla=-1;
					}
					indiceAc++;
				}else{
					check=false;					
				}
			}
			if(check) {
				check=false;
			}else {
				check=true;
				i--;
			}			
		}
		//Salgo luego o he comprobado toda la frase sin exito o he encontrado la palabra
		if(i>=0 && !this.palabrasConectoras.contains(frase[i])) {
			return frase[i];
		}
		return lf;
	}
	
	/*
	 * METODO AUXILIAR 2: Busco coincidencia con una sigla y checkeo que existe alguna palabra que comience por la siguiente,
	 * si no la encuentro entonces compruebo si la anterior palabra contiene esta segunda sigla. Asi sucesivamente
	 */
	private String checkMetodoAuxiliar2(String lf, String ac) {
		String resultado;
		
		//Desde el inicio del acronimo	
		boolean checkAc = true;
		boolean checkLf = false;
		String[] frase = lf.split(" ");
		int i= frase.length - 1;;
		Character siglaActual=' ';
		
		int pivoteInicial=-1;
		int pivoteFinal=-1;
		int indexUltimoCheck = -1;
		int indiceAc = 0;
		String acAux = ac.toUpperCase();
		while (indiceAc < ac.length() && checkAc) {
			checkLf=false;
			i = frase.length - 1;
			while (i >= 0 && !checkLf) {
				String cadenaNormalize = Normalizer.normalize(frase[i], Normalizer.Form.NFD);
				String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "").toUpperCase();
				
				siglaActual=acAux.charAt(indiceAc);
				
				if(siglaActual.equals(s) || siglaActual.equals(e)) {
					//Busco la aparicion de esa sigla o de e o s. InglÈs
					if ((cadenaSinAcentos.indexOf(s)==0 || cadenaSinAcentos.indexOf(e)==0)&& i!=indexUltimoCheck){
						//Empieza por e o s
						checkLf=true;
						if(pivoteInicial==-1 || i<pivoteInicial)
							pivoteInicial=i;
						if(pivoteFinal==-1 || i>pivoteFinal)
							pivoteFinal=i;
							
						indexUltimoCheck=i;
						
					}else {
						i--;
					}					
				}else {
					if (cadenaSinAcentos.indexOf(siglaActual)==0 && i!=indexUltimoCheck){
						checkLf=true;
						if(pivoteInicial==-1 || i<pivoteInicial)
							pivoteInicial=i;
						if(pivoteFinal==-1 || i>pivoteFinal)
							pivoteFinal=i;
						
						indexUltimoCheck=i;
					}else {
						i--;
					}
				}
			}
			if(i<0) {
				//Me he salido por no encontrarlo. Busco en la ultima
				if(indexUltimoCheck!=-1) {
					String cadenaNormalize = Normalizer.normalize(frase[indexUltimoCheck], Normalizer.Form.NFD);
					String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "").toUpperCase();
					checkLf=cadenaSinAcentos.indexOf(siglaActual)>=0;
				}else {
					return lf;
				}
				if(!checkLf) {
					return lf;
				}
			}
			indiceAc++;
		}
		
		if(pivoteInicial!=-1 && checkAc) {
			resultado=frase[pivoteInicial];
			pivoteInicial++;
			while(pivoteInicial<=pivoteFinal){
				if(i<frase.length) {
					resultado+=" "+frase[pivoteInicial];
				}
				pivoteInicial++;
			}
			return resultado;
		}
		
		return lf;
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
		
		ObjetoDevuelto respuesta= new ObjetoDevuelto(false,0);
		
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
				while(i>=0 && !respuesta.isCheck()){
					Character ac1=Character.toUpperCase(ac.charAt(indiceAcronimo));
					
					//Metodos usados para ignorar las tildes de las frases, dado que en caso de ignorarlas, el acronimo no lleva 
					//y por tanto el equals falla
					String cadenaNormalize = Normalizer.normalize(frase[i], Normalizer.Form.NFD);   
					String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
					
					if(!this.palabrasConectoras.contains(cadenaSinAcentos)&& cadenaSinAcentos!=""){
						if(cadenaSinAcentos.charAt(0)==' '){
							correccionEspacio=1;
						}else{
							correccionEspacio=0;
						}
						if(ac1.equals(s)){
							//Puede ser o igual a S o igual a E
							if(e.equals(Character.toUpperCase(cadenaSinAcentos.charAt(correccionEspacio)))||
									s.equals(Character.toUpperCase(cadenaSinAcentos.charAt(correccionEspacio))) ){
								if(i+1==frase.length){
									return lf;
								}								
								respuesta=checkRestAcFromEnd(i+1,frase,ac);
								if(!respuesta.isCheck()) {
									i-=1;
								}
							}else{
								i-=1;
							}
						}else{
							if(cadenaSinAcentos.length()>0) {
								// Si no es una s entonces simplemente comparo
								if (ac1.equals(Character.toUpperCase(cadenaSinAcentos
									.charAt(correccionEspacio)))) {
									if (i + 1 == frase.length) {
										return lf;
									}
									respuesta = checkRestAcFromEnd(i + 1, frase, ac);
									if(!respuesta.isCheck()){
										i-=1;
									}
								} else {
									i -= 1;
								}
							}else {
								//Por si es espacio
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
		if(!respuesta.isCheck()){
			return lf;
		}
		//Ahora devuelvo la frase desde i hacia delante
		resultado=frase[i];
		i++;
		while(i<=respuesta.index){
			if(i<frase.length) {
				resultado+=" "+frase[i];
			}
			i++;
		}	
		return resultado;
	}

	private ObjetoDevuelto checkRestAcFromEnd(int i, String[] frase, String ac) {
		ObjetoDevuelto respuesta= new ObjetoDevuelto(true,i);
		
		//El acronimo tiene mas de longitud 2
		int indiceAcronimo=ac.length()-2;
		boolean check=true;
		int correccionEspacio=0;
		if(i==frase.length && indiceAcronimo<0) {
			return new ObjetoDevuelto(false,i);
		}
		
		while(indiceAcronimo>=0 && check && i<frase.length){
			if (!this.palabrasConectoras.contains(frase[i])&& frase[i]!="") {
				Character aux = ac.charAt(indiceAcronimo);
				
				String cadenaNormalize = Normalizer.normalize(frase[i], Normalizer.Form.NFD);   
				String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
				
				if(cadenaSinAcentos.charAt(0)==' '){
					correccionEspacio=1;
				}else{
					correccionEspacio=0;
				}
				if (aux.equals('S')) {
					check = aux.equals(Character.toUpperCase(cadenaSinAcentos.charAt(correccionEspacio)))
							|| aux.equals(cadenaSinAcentos.charAt(correccionEspacio))
							|| this.e.equals(Character.toUpperCase(cadenaSinAcentos
									.charAt(correccionEspacio)));
				} else {
					check = aux.equals(Character.toUpperCase(cadenaSinAcentos.charAt(correccionEspacio)))
							|| aux.equals(cadenaSinAcentos.charAt(correccionEspacio));
				}
				if(check) {
					indiceAcronimo -= 1;
					i += 1;
				}				
			} else {
				i += 1;
			}
		}
		//He salido del bucle
		//Dos posibilidades: He llegado al final de la frase pero aun me queda por comparar la ultima letra del acronimo
		//Simplemente he comparado todo
		
		int index=i;
		if(indiceAcronimo==0){
			if(this.palabrasConectoras.contains(frase[i-1])) {
				index=i-2;
			}else {
				index=i-1;
			}						
			Character aux = ac.charAt(indiceAcronimo);
			check=frase[index].contains(""+Character.toUpperCase(aux))
			|| frase[index].contains(""+Character.toLowerCase(aux));			
		}
		respuesta.setCheck(check);
		respuesta.setIndex(index);
		return respuesta;
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
	 *  LCR l√≠quido  cefalorraqu√≠deo (ignorando las palabras del conjunto que tiene esta clase
	 *  como atributo)Si el acronimo tiene longitud>2 entonces busco las otras coincidencias(indice i)
	 */	
	private String checkLongFromStart(String lf,String ac) {		
		String resultado="";
		//Si el acronimo tiene longitud 2 solo con encontrar la primera letra sirve
		int indiceAcronimo=0;
		lf.replaceAll("\t", " ");
		String[] frase=lf.split(" ");
		
		ObjetoDevuelto respuesta= new ObjetoDevuelto(false,0);
		
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
				while(i>=0 && !respuesta.isCheck()){
					Character ac1=Character.toUpperCase(ac.charAt(indiceAcronimo)); 
					String cadenaNormalize = Normalizer.normalize(frase[i], Normalizer.Form.NFD);   
					String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
					if(!this.palabrasConectoras.contains(cadenaSinAcentos)&& cadenaSinAcentos!=""){
						if(ac1.equals(s)){
							//Puede ser o igual a S o igual a E
							if(e.equals(Character.toUpperCase(cadenaSinAcentos.charAt(correccionEspacio)))||
									s.equals(Character.toUpperCase(cadenaSinAcentos.charAt(correccionEspacio))) ){	
								if(i+1==frase.length){
									return lf;
								}
								respuesta=checkRestAcFromStart(i+1,frase,ac);
								if(!respuesta.isCheck()) {
									i-=1;
								}
							}else{
								i-=1;
							}
						}else{
							//Si no es una s entonces simplemente comparo
							if(cadenaSinAcentos.length()>0) {
								if(ac1.equals(Character.toUpperCase(cadenaSinAcentos.charAt(correccionEspacio)))){
									
									respuesta=checkRestAcFromStart(i+1,frase,ac);
									/*if(i+1==frase.length){
										if(indiceAcronimo+1==ac.length()-1 && (cadenaSinAcentos.contains(""+ac.charAt(indiceAcronimo+1))|| 
											cadenaSinAcentos.contains(""+Character.toLowerCase(ac.charAt(indiceAcronimo+1))))){
											//Por ejemplo TB 
											return frase[i];
										}else{
											return lf;
										}
									}*/
									if(!respuesta.isCheck()) {
										i-=1;
									}
								}else{
									i-=1;
								}
							}else {
								//Por si es un espacio
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
		while(i<=respuesta.getIndex()){
			if(i<frase.length) {
				resultado+=" "+frase[i];
			}
			i++;
		}	
		return resultado;
	}

	private ObjetoDevuelto checkRestAcFromStart(int i, String[] frase, String ac) {
		ObjetoDevuelto respuesta= new ObjetoDevuelto(true,i);
		
		// El acronimo tiene mas de longitud 2
		int indiceAcronimo = 1;
		if(i==frase.length && ac.length()>2) {
			return new ObjetoDevuelto(false,i);
		}
		boolean check = true;		
		while (indiceAcronimo < ac.length() && check && i < frase.length) {
			String cadenaNormalize = Normalizer.normalize(frase[i], Normalizer.Form.NFD);   
			String cadenaSinAcentos = cadenaNormalize.replaceAll("[^\\p{ASCII}]", "");
			if (!this.palabrasConectoras.contains(cadenaSinAcentos)&& cadenaSinAcentos!="") {
				Character aux = ac.charAt(indiceAcronimo);
				if (aux.equals('S')) {
					check = aux.equals(Character.toUpperCase(cadenaSinAcentos.charAt(0)))
							|| aux.equals(cadenaSinAcentos.charAt(0))
							|| this.e.equals(Character.toUpperCase(cadenaSinAcentos
									.charAt(0)));
				} else {
					check = aux.equals(Character.toUpperCase(cadenaSinAcentos.charAt(0)))
							|| aux.equals(cadenaSinAcentos.charAt(0));
				}
				if(check) {
					indiceAcronimo += 1;
					i += 1;
				}			
				
			} else {
				i += 1;
			}
		}
		
		//He salido del bucle
		//Dos posibilidades: He llegado al final de la frase pero aun me queda por comparar la ultima letra del acronimo
		//Simplemente he comparado todo
		int index=i;
		if(indiceAcronimo==ac.length()-1){
			if(this.palabrasConectoras.contains(frase[i-1])) {
				index=i-2;
			}else {
				index=i-1;
			}						
			Character aux = ac.charAt(indiceAcronimo);
			check=frase[index].contains(""+Character.toUpperCase(aux))
			|| frase[index].contains(""+Character.toLowerCase(aux));			
		}
		respuesta.setCheck(check);
		respuesta.setIndex(index);
		return respuesta;
	}

	/*
	 * Escritura del fichero de resultado tsv. Escritura de nombre de las columnas si el fichero no existe.
	 */
	public String toString(){
		String result="";		
		try {
			File file= new File("Testing_results_EVALUATION.tsv");	
			if(!file.exists()){
				result="#DocumentID\tMention_A_type\tMention_A_StartOffset\tMention_A_EndOffset\tMention_A\tRelation_type\t"
						+ "Mention_B_type\tMention_B_StartOffset\tMention_B_EndOffset\tMention_B\n";
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(file,true), "UTF-8"));
			
			Iterator<Entry<Acronimo,HashSet<FormaLarga>>> it=this.diccionarioTextoActual.entrySet().iterator();
			while(it.hasNext()){				
				Entry<Acronimo,HashSet<FormaLarga>> e=it.next();
				if(!e.getValue().isEmpty()){
					//File identifier
					result+=nombreFichero+"\t";
					
					//Short form
					if(e.getKey().isNested()) {
						//result+="NESTED\t"+e.getKey().getStartOffset()+"\t"+e.getKey().getEndOffset()+"\t"+e.getKey().getAcronimo()+"\tNESTED-";
						result+="NESTED\t"+e.getKey().getStartOffset()+"\t"+e.getKey().getAcronimo()+"\tNESTED-";
					}else {
						//result+="SHORT_FORM\t"+e.getKey().getStartOffset()+"\t"+e.getKey().getEndOffset()+"\t"+e.getKey().getAcronimo()+"\tSHORT-";
						result+="SHORT_FORM\t"+e.getKey().getStartOffset()+"\t"+e.getKey().getAcronimo()+"\tSHORT-";
					}
					
					Iterator<FormaLarga> indice=e.getValue().iterator();
					FormaLarga aux;
					
					//Long form
					if(indice.hasNext()) {					
						aux=indice.next();
						if(aux.isNested()) {
							result+="NESTED\tNESTED\t";	
						}else {
							result+="LONG\tLONG_FORM\t";	
						}
						result+=aux.getStartOffset()+"\t"+aux.getEndOffset()+"\t"+aux.getLf();
					}
					while(indice.hasNext()) {
						aux=indice.next();
						result+=aux.getStartOffset()+"\t"+aux.getEndOffset()+"\t"+aux.getLf();
					}			
					result+="\n";
				}
			}			
			out.write(result);
			out.flush();
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return result;		
	}	
		
	public static void main(String[] args){
		Resultado r=new Resultado();
		Character ch='B';	
		/*r.addDupla(new Acronimo(0,15,"P.A.AF"), new FormaLarga("Se realiza punciÛn aspiraciÛn por aguja fina ",93,98));
		r.toString();*/		
		//System.out.println(r.checkMetodoAuxiliar1("y la biomicroscopia", "BMC"));
		/*System.out.println(String.format("\\u%04x", (int) ch));*/
		//System.out.println(r.checkMetodoAuxiliar1("un coloboma de retina y nervio Ûptico","NO"));
		//System.out.println(r.checkLongFromEnd("una tomograf√≠a de coherencia √≥ptica ","OCT"));
	}
}
