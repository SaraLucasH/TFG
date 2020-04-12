package Analizadores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import Diccionarios.BARRDiccionario;
import Diccionarios.SEDOMDiccionario;
import Diccionarios.UnidadesMedidaDiccionario;
import WordMoversDistance.WordMovers;
import WordMoversDistance.WordMovers.Builder;

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
	
	
	//Mapas para consulta
	private HashMap<Acronimo,HashSet<FormaLarga>> diccionarioTextoActual;
	//Subtarea 2
	private HashMap<String, HashSet<String>> diccionarioConsultaBARR;
	private HashMap<String, HashSet<String>> diccionarioConsultaUnidadesMedida;
	private HashMap<String,HashSet<String>> diccionarioConsultaSEDOM;
	private HashMap<Acronimo,String> diccionarioDesambiguacion;	
	private HashSet<String> stopWords;	
	private HashSet<String> signosPuntuacion;
	
	//Desambiguacion
	Builder b;
	WordVectors v;
	WordMovers wmd;
	
	//Del analizador sintactico
	ArrayList<String> contexto;
	HashMap<Acronimo,HashSet<String>> almacen;
	
	//Identificador fichero resultado
	String nombreFichero="";
	
	//Comparar caracteres
	Character e='E';
	Character s='S';
	
	//Metodo encontrar candidato en desambiguacion
	String candidato;
	double min;
	HashSet<String> valorUnidadesMedida;
	HashSet<String> valorBARR;
	HashSet<String> valorSEDOM;
	
	//Escritura del fichero de resultado	 
	FileWriter fichero = null;
    PrintWriter pw = null;
	
    private void init() {
    	this.diccionarioTextoActual=new HashMap<>();	
		this.diccionarioDesambiguacion=new HashMap<>();
		this.diccionarioConsultaSEDOM=new SEDOMDiccionario().getDiccionario();
		this.diccionarioConsultaBARR= new BARRDiccionario().getDiccionario();
		this.diccionarioConsultaUnidadesMedida=new UnidadesMedidaDiccionario().getDiccionario();
		this.almacen=new HashMap<>();
		this.contexto=new ArrayList<>();
		//Palabras de Parada
		stopWords= this.loadStopWords();	
		b.stopwords(this.stopWords);
		this.wmd=new WordMovers(b);
		
		signosPuntuacion= new HashSet<>(Arrays.asList(",", ";","."));
	}
    
	public Resultado(){	
		init();
	}
	
	public Resultado(String nombreFicheroEntrada,WordVectors wvect){
		this.b=WordMovers.Builder();
		this.v=wvect;
		this.b.wordVectors=wvect;
		this.nombreFichero=nombreFicheroEntrada;
		init();
	}

	public void load() {		
		for(Entry<Acronimo,HashSet<String>> e:this.almacen.entrySet()) {
			for(String s:e.getValue()) {
				this.addDuplaDesambiguacion(e.getKey(), s);				
			}
		}
		toOutputFileSecondTask();
	}
	
	public ArrayList<String> getContexto(){
		return this.contexto;
	}
	
	public void addAcWithContext(AcWithContext a) {
		HashSet<String> value=this.almacen.get(a.getAc());
		if(value==null) {
			value= new HashSet<String>();
			this.almacen.put(a.getAc(), value);
		}
		value.add(a.getFraseInmediata());
	}
	
	private void encuentraCandidato(HashSet<String> conjunto,String newContexto) {
		String newSentence;
		int index=0;
		double minAux;
		
		for (String i : conjunto) {									
			newSentence = this.deleteStopWords(i);									
			if (newSentence != null) {
				boolean existsVectorNewSentence=true;
				String[] cadenaNewSentence= newSentence.split(" ");										
				index=0;
				if(cadenaNewSentence.length==1 && cadenaNewSentence[0].equals("")) {
					existsVectorNewSentence=false;
				}
				while (index<cadenaNewSentence.length && existsVectorNewSentence) {
					if(cadenaNewSentence[index]!="" && !v.hasWord(cadenaNewSentence[index])) {
						existsVectorNewSentence=false;
					}
					index++;
				}
				if(existsVectorNewSentence) {
					minAux = wmd.distance(newContexto, newSentence);
					if (minAux < this.min) {
						this.min = minAux;
						this.candidato = i;
					}
				}										
			}
		}
	}
	
	public void addDuplaDesambiguacion(Acronimo ac, String contextoAc) {
		if (this.diccionarioDesambiguacion.get(ac) == null) {
			// 1ºConsulto dicc unidades medida y sedom
			this.valorUnidadesMedida=this.diccionarioConsultaUnidadesMedida.get(ac.getAcronimo());	
			this.valorSEDOM = this.diccionarioConsultaSEDOM.get(ac.getAcronimo());
			
			if (valorUnidadesMedida!=null && valorUnidadesMedida.size() == 1) {
				this.diccionarioDesambiguacion.put(ac, valorUnidadesMedida.iterator().next());
			}else if (valorSEDOM!=null && valorSEDOM.size() == 1) {
				this.diccionarioDesambiguacion.put(ac, valorSEDOM.iterator().next());
			}else {				 
				this.valorBARR = this.diccionarioConsultaBARR.get(ac.getAcronimo());
				
				if (valorBARR != null || valorSEDOM != null || valorUnidadesMedida!=null) {
					boolean encontrado = false;
					
					this.candidato=null;
					this.min = Double.MAX_VALUE;
					
					int inicio = this.contexto.indexOf(contextoAc);
					int pivote = this.contexto.indexOf(contextoAc);
					while (!encontrado && !(pivote > this.contexto.size() - 1)) {
						// Desambiguacion
						String newContexto = deleteStopWords(contextoAc);						
						boolean existsVectorNewContexto=true;
						String[] cadenaNewContexto=newContexto.split(" ");
						int index=0;
						
						if(cadenaNewContexto.length==1 && cadenaNewContexto[0].equals("")) {
							existsVectorNewContexto=false;
						}
						while (index<cadenaNewContexto.length && existsVectorNewContexto) {
							if(!cadenaNewContexto[index].equals("") && !v.hasWord(cadenaNewContexto[index])) {
								existsVectorNewContexto=false;
							}
							index++;
						}
						
						if (existsVectorNewContexto) {
							if(this.valorUnidadesMedida!=null) {
								encuentraCandidato(this.valorUnidadesMedida,newContexto);
							}							
							if (this.valorSEDOM != null) {
								encuentraCandidato(this.valorSEDOM,newContexto);
							}
							if (this.valorBARR != null) {
								encuentraCandidato(this.valorBARR,newContexto);
							}
						}
						if (min < 7) {
							encontrado = true;
						} else {
							if (pivote > 0 && pivote <= inicio) {
								// Continuo hacia atras
								contextoAc = this.contexto.get(--pivote);
							} else {
								// Continuo hacia delante
								if (pivote < inicio) {
									pivote = inicio;
								}
								if (pivote >= inicio && pivote < this.contexto.size() - 2) {
									contextoAc = this.contexto.get(++pivote);
								} else {
									pivote++;
								}
							}
						}

					}
					if (encontrado || min < 14) {
						this.diccionarioDesambiguacion.put(ac, candidato);
					}
				}
			}
		}
	}
	
	private String deleteStopWords(String contexto) {
		String[] sentence= contexto.split(" ");
		String newContexto="";
		for(String s:sentence) {
			if(!this.stopWords.contains(s.toLowerCase())) {
				if(newContexto=="") {
					newContexto=s;
				}else {
					newContexto+=" "+s;
				}
			}
		}
		return newContexto;
	}

	
	private HashSet<String> loadStopWords() {
		HashSet<String> list=new HashSet<String>();
    	File file= new File("C:\\Users\\saral\\Documents\\tfg\\Evaluation\\stopwords-es.txt");	
    	String charset = "UTF-8";
		Reader br;
		try {
			br = new InputStreamReader (new FileInputStream(file),charset);
			BufferedReader buffer= new BufferedReader(br);
			String linea;
			
			while((linea=buffer.readLine())!=null) {
				list.add(linea);
			}
			
			buffer.close();
			br.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return list;
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
					System.out.println(
							"SALIDA LF " + lfChecked + " añadido a " + this.diccionarioTextoActual.get(acronimo));
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
		if(i>=0 && !this.stopWords.contains(frase[i])) {
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
					//Busco la aparicion de esa sigla o de e o s. Inglés
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
					
					if(!this.stopWords.contains(cadenaSinAcentos)&& cadenaSinAcentos!=""){
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
			if (!this.stopWords.contains(frase[i])&& frase[i]!="") {
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
			if(this.stopWords.contains(frase[i-1])) {
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
	 *  LCR lÃ­quido  cefalorraquÃ­deo (ignorando las palabras del conjunto que tiene esta clase
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
					if(!this.stopWords.contains(cadenaSinAcentos)&& cadenaSinAcentos!=""){
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
			if (!this.stopWords.contains(cadenaSinAcentos)&& cadenaSinAcentos!="") {
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
			if(this.stopWords.contains(frase[i-1])) {
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

	public void setContexto(ArrayList<String> c) {
		this.contexto=c;
	}
	
	/*
	 * PRIMERA TAREA:Escritura del fichero de resultado tsv. Escritura de nombre de las columnas si el fichero no existe.
	 */
	public String toOutputFileFirstTask(){
		String result="";		
		try {
			File file= new File(".\\Herramientas\\Archivos salida\\Sample_SecondTask_Results_EVALUATION.tsv");	
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
	
	/*
	 * SEGUNDA TAREA:Escritura del fichero de resultado tsv. Escritura de nombre de las columnas si el fichero no existe.
	 */
	public String toOutputFileSecondTask(){
		String result="";		
		try {
			File file= new File(".\\Herramientas\\Archivos_salida\\Training_SecondTask_Results_EVALUATION.tsv");	
			if(!file.exists()){
				result="#DocumentID\tStartOffset\tEndOffset\tAbbreviation\tDefinition\tDefinition_lemmatized\n";
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(file,true), "UTF-8"));
			
			Iterator<Entry<Acronimo, String>> it=this.diccionarioDesambiguacion.entrySet().iterator();
			while(it.hasNext()){				
				Entry<Acronimo, String> e=it.next();
				if(e.getValue()!=null || !e.getValue().isEmpty() ){
					//File identifier
					result+=nombreFichero+"\t"+e.getKey().getStartOffset()+"\t"+e.getKey().getEndOffset()+
							"\t"+e.getKey().getAcronimo()+"\t"+e.getValue()+"\t"+e.getValue();
								
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
		r.addDuplaDesambiguacion(new Acronimo(0,15,"TSP"),"El paciente fue diagnosticado de paraparesia paraparesia espástica tropical");
		r.toString();		
		//System.out.println(r.checkMetodoAuxiliar1("y la biomicroscopia", "BMC"));
		/*System.out.println(String.format("\\u%04x", (int) ch));*/
		//System.out.println(r.checkMetodoAuxiliar1("un coloboma de retina y nervio óptico","NO"));
		//System.out.println(r.checkLongFromEnd("una tomografÃ­a de coherencia Ã³ptica ","OCT"));
	}
}
