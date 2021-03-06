package Diccionarios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class SEDOMDiccionario {
	private String charset="UTF8";
	//Un unico diccionario para evitar repeticiones en la lectura de los archivos.
	//Emplea tanto el diccionario de unidades de medida como el SEDOM
	
	HashMap<String,HashSet<String>> diccionario;
	
	public SEDOMDiccionario() {
		this.diccionario= new HashMap<String, HashSet<String>>();
		this.load();
	}
	
	/*
	 * Carga el contenido del fichero txt, en caso de estar vacio manda un -1
	 */
	private void load() {
		InputStreamReader reader;
		BufferedReader br = null;
		
		try {
			reader = new InputStreamReader (getClass().getResourceAsStream("/Herramientas/Diccionarios/prueba.txt"), charset);
			br = new BufferedReader(reader);			
			String linea;			
			while ((linea = br.readLine()) != null){
				String[]cadena=linea.split(":");
				HashSet<String> value;
				//Filtramos lineas basura
				if(cadena.length>1) {
					value=this.diccionario.get(cadena[0]);
					if(value==null) {
						value= new HashSet<String>();
						this.diccionario.put(cadena[0],value);
					}
					String[] valores= cadena[1].split("//");
					for(String s:valores) {
							value.add(s);
					}					
				}
			}			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {			
			try {
				if (null != br) {
					br.close();					
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		/*for(Entry<String, HashSet<String>> e:this.diccionario.entrySet()) {
			System.out.print(e.getKey()+":");
			for(String s: e.getValue()) {
				System.out.print(s+" // ");
			}
			System.out.println();
		}*/
		
	}

	public HashSet<String> get(String key){
		return this.diccionario.get(key);
	}

	public void put(String key,HashSet<String>value) {
		this.diccionario.put(key, value);
	}
}
