package Diccionarios;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;


public class CargaDiccionario {
	HashMap<String,HashSet<String>> diccionario;

	public CargaDiccionario(){
		diccionario= new HashMap<>();		
	}
	
	public HashMap<String,HashSet<String>> cargaDiccionario(File archivoTxt){		
		FileReader fr = null;
		BufferedReader br = null;
		
		try {					
			fr = new FileReader(archivoTxt);
			br = new BufferedReader(fr);
			
			String linea;
			
			while ((linea = br.readLine()) != null){
				String[]cadena=linea.split("\t");
				HashSet<String> value;
				if(diccionario.keySet().contains(cadena[0])){
					value=diccionario.get(cadena[0]);
					if(value==null){
						value= new HashSet<String>();
					}
					value.add(cadena[1]);					
				}else{
					value=new HashSet<>();
					value.add(cadena[1]);
					diccionario.put(cadena[0], value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			try {
				if (null != fr) {
					fr.close();					
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return diccionario;
	}

	public HashMap<String, HashSet<String>> getDiccionario() {
		return diccionario;
	}
	
	
}
