package Diccionarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class BARRDiccionario {
	HashMap<String,HashSet<String>> diccionario;
	String charset="UTF8";
	
	public BARRDiccionario() {
		this.diccionario= new HashMap<>();
		this.loadFromTSV();
	}
	
	private void loadFromTSV() {
		File file=null;
		Reader br;
		BufferedReader buffer=null;
		try {			
			file= new File("./Herramientas/Diccionarios/DiccionarioBARR2.tsv");
			if(!file.exists() || file.length()==0) {
				//Ya carga el diccionario
				this.updateTSV();
			}else {
				br = new InputStreamReader (new FileInputStream(file), charset);
				buffer= new BufferedReader(br);
			
				String linea;
				while((linea=buffer.readLine())!=null) {
					String[]row=linea.split("\t");
					HashSet<String> value=this.diccionario.get(row[0].toUpperCase());
					if(value==null) {
						value = new HashSet<String>();
						this.diccionario.put(row[0].toUpperCase(), value);
					}
					for(int i=1;i<row.length;i++) {
						value.add(row[i]);
					}
				}				
			}			
		} catch(Exception e) {
			e.printStackTrace();			
		}finally {			
			try {
				if (null != buffer) {
					buffer.close();					
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}		
	}
	
	private int updateTSV(){
		String charset = "UTF-8";		
		try {					
			File fr = new File(".\\Herramientas\\Archivos salida\\Training_results_EVALUATION.tsv");
			Reader br = new InputStreamReader (new FileInputStream(fr), charset);
			BufferedReader buffer= new BufferedReader(br);
			
			File file= new File("./Herramientas/Diccionarios/DiccionarioBARR2.tsv");	
			Writer out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(file,true), "UTF-8"));
			
			String linea;
			buffer.readLine();
			while ((linea = buffer.readLine()) != null){
				String[]cadena=linea.split("\t");
				HashSet<String> value;
				//Filtramos lineas basura
				if(cadena.length>1 && !cadena[4].contains("NESTED")) {
					value=this.diccionario.get(cadena[3].toUpperCase());
					if(value==null) {
						value= new HashSet<String>();
						this.diccionario.put(cadena[3].toUpperCase(),value);
					}
					value.add(cadena[8]);					
				}
			}
			
			fr = new File(".\\Herramientas\\Archivos salida\\Testing_results_EVALUATION.tsv");
			br = new InputStreamReader (new FileInputStream(fr), charset);
			buffer= new BufferedReader(br);
			
			buffer.readLine();
			while ((linea = buffer.readLine()) != null){
				String[]cadena=linea.split("\t");
				HashSet<String> value;
				//Filtramos lineas basura
				if(cadena.length>1 && !cadena[4].contains("NESTED")) {
					value=this.diccionario.get(cadena[3].toUpperCase());
					if(value==null) {
						value= new HashSet<String>();
						this.diccionario.put(cadena[3].toUpperCase(),value);
					}
					value.add(cadena[8]);					
				}
			}
			
			fr = new File(".\\Herramientas\\Archivos salida\\Development_results_EVALUATION.tsv");
			br = new InputStreamReader (new FileInputStream(fr), charset);
			buffer= new BufferedReader(br);
			
			buffer.readLine();
			while ((linea = buffer.readLine()) != null){
				String[]cadena=linea.split("\t");
				HashSet<String> value;
				//Filtramos lineas basura
				if(cadena.length>1 && !cadena[4].toUpperCase().contains("NESTED")) {
					value=this.diccionario.get(cadena[3].toUpperCase());
					if(value==null) {
						value= new HashSet<String>();
						this.diccionario.put(cadena[3].toUpperCase(),value);
					}
					value.add(cadena[8]);					
				}
			}
			
			fr = new File(".\\Herramientas\\Archivos salida\\Sample_results_EVALUATION.tsv");
			br = new InputStreamReader (new FileInputStream(fr), charset);
			buffer= new BufferedReader(br);
			
			buffer.readLine();
			while ((linea = buffer.readLine()) != null){
				String[]cadena=linea.split("\t");
				HashSet<String> value;
				//Filtramos lineas basura
				if(cadena.length>1 && !cadena[4].contains("NESTED")) {
					value=this.diccionario.get(cadena[3].toUpperCase());
					if(value==null) {
						value= new HashSet<String>();
						this.diccionario.put(cadena[3].toUpperCase(),value);
					}
					value.add(cadena[8]);					
				}
			}
			
			String result="";
			for(Entry<String,HashSet<String>> e:this.diccionario.entrySet()) {
				result+=e.getKey()+"\t";
				Iterator<String> it=e.getValue().iterator();
				result+=it.next();
				while(it.hasNext()) {
					result+="\t"+it.next();
				}
				result+="\n";
			}
			out.write(result);
			out.close();
			buffer.close();
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	public static void main(String[]args) {
		BARRDiccionario s= new BARRDiccionario();	
	}
	
	public HashMap<String, HashSet<String>> getDiccionario() {
		return this.diccionario;
	}
	
}
