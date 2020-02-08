package Diccionarios;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class SEDOMDiccionario {
	//Un unico diccionario para evitar repeticiones en la lectura de los archivos
	HashMap<String,HashSet<String>> diccionario;
	
	public SEDOMDiccionario() {
		this.diccionario= new HashMap<>();
		this.load();
	}
	
	/*
	 * Carga el contenido del fichero txt, en caso de estar vacio manda un -1
	 */
	private int load() {
		
		FileReader fr = null;
		BufferedReader br = null;
		
		try {					
			fr = new FileReader("Herramientas/AllAcronyms.txt");
			br = new BufferedReader(fr);			
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
					String[] valores= cadena[1].split("-");
					for(String s:valores) {
							value.add(s);
					}					
				}
			}
			//Lectura de fichero unidades de medida
			fr = new FileReader("Herramientas/mesuresAcronyms.csv");
			br = new BufferedReader(fr);			
					
			while ((linea = br.readLine()) != null){
				String[]cadena=linea.split(";");
				HashSet<String> value;
				//Filtramos lineas basura
				if(cadena.length>1) {
					value=this.diccionario.get(cadena[0]);
					if(value==null) {
						value= new HashSet<String>();
						this.diccionario.put(cadena[0],value);
					}
					value.add(cadena[1]);					
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}finally {			
			try {
				if (null != fr) {
					fr.close();					
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
		return 0;
	}
	public static void main(String[]args) {
		SEDOMDiccionario s= new SEDOMDiccionario();	
	}

	public HashMap<String, HashSet<String>> getDiccionario() {
		return this.diccionario;
	}
}
