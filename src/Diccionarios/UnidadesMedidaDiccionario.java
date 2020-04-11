package Diccionarios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;

public class UnidadesMedidaDiccionario {
	HashMap<String,HashSet<String>> diccionario;
	String charset="UTF-8";
	
	public UnidadesMedidaDiccionario() {
		this.diccionario= new HashMap<>();
		this.load();
	}

	public HashMap<String, HashSet<String>> getDiccionario() {
		return this.diccionario;
	}
	
	private void load() {
		File fr = null;
		Reader reader;
		BufferedReader br = null;
		
		try {	
			String linea;
			//Lectura de fichero unidades de medida
			fr = new File("./Herramientas/Diccionarios/mesuresAcronyms.csv");
			reader = new InputStreamReader (new FileInputStream(fr), charset);
			br = new BufferedReader(reader);			
				
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
		}finally {			
			try {
				if (null != br) {
					br.close();					
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}