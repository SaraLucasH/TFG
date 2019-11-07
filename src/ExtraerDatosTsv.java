import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;


public class ExtraerDatosTsv {
	
	private int numShort,numLong;
	private int cadShort,cadLong;
	
	public ExtraerDatosTsv(){
		
	}
	public void transformarATxtDiccionario(String path){
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		
		FileWriter ficheroEscritura=null;
		PrintWriter pwEscritura = null;

		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File(path);
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			
			//Escribimos al final de lo ya exitente
			//ficheroEscritura = new FileWriter("DiccionarioTest001.txt",true);
			
			ficheroEscritura = new FileWriter("DiccionarioTest001.txt",true);
			pwEscritura= new PrintWriter(ficheroEscritura);

			// Lectura del fichero
			String linea;
			String shortForm="";
			String longForm="";
			
			calculaNumColumnaTSV(br.readLine());
			boolean escriboLineaActual=true;
			while ((linea = br.readLine()) != null && shortForm!=null && longForm!=null){
				String[]cadena=linea.split("\t");
				if(cadena[numShort].equalsIgnoreCase("short") 
						&& cadena[numLong].equalsIgnoreCase("long")){
					shortForm=cadena[cadShort];
					longForm=cadena[cadLong];
				}else if(cadena[numLong].equalsIgnoreCase("short") 
						&& cadena[numShort].equalsIgnoreCase("long")){
					shortForm=cadena[cadLong];
					longForm=cadena[cadShort];
				}else{
					//Si no escribiria de nuevo lo anterior
					escriboLineaActual=false;
				}
				if(escriboLineaActual){
					//Para una correcta escritura con acentos, ñ etc
					byte[] sb = shortForm.getBytes("ISO-8859-1");
					shortForm= new String(sb,"UTF-8");
					byte[]lb=longForm.getBytes("ISO-8859-1");
					longForm= new String(lb,"UTF-8");
					
					pwEscritura.println(shortForm+"\t"+longForm);
				}
				escriboLineaActual=true;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			try {
				if (null != fr) {
					fr.close();
					ficheroEscritura.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	private void calculaNumColumnaTSV(String readLine) {
		String[]cadena=readLine.split("\t");
		for(int i=0;i<cadena.length;i++){
			if(cadena[i].toUpperCase().replaceAll("_"," ").equals("ANNOTATION A TYPE")){
				numShort=i;
			}	
			if(cadena[i].toUpperCase().replaceAll("_"," ").equals("ANNOTATION A") 
					||cadena[i].toUpperCase().replaceAll("_"," ").equals("ANNOTATION A TEXT") ){
				cadShort=i;
			}
			if(cadena[i].toUpperCase().replaceAll("_"," ").equals("ANNOTATION B TYPE")){
				numLong=i;
			}
			if(cadena[i].toUpperCase().replaceAll("_"," ").equals("ANNOTATION B")
					||cadena[i].toUpperCase().replaceAll("_"," ").equals("ANNOTATION B TEXT")){
				cadLong=i;
			}			
		}		
	}	
}	

