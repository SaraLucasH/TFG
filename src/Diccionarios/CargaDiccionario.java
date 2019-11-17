package Diccionarios;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class CargaDiccionario {
	HashMap<String,HashSet<String>> diccionarioSF_LF;
	HashMap<String,String> diccionarioLF_SF;

	public CargaDiccionario(){
		diccionarioLF_SF= new HashMap<>();	
		diccionarioSF_LF= new HashMap<>();	
	}
	
	public HashMap<String,HashSet<String>> cargaDiccionarioTSV(File archivoTxt){	
	
		FileReader fr = null;
		BufferedReader br = null;
		
		try {					
			fr = new FileReader(archivoTxt);
			br = new BufferedReader(fr);
			
			String linea;
			
			while ((linea = br.readLine()) != null){
				String[]cadena=linea.split("\t");
				HashSet<String> value;
				if(diccionarioSF_LF.keySet().contains(cadena[0])){
					value=diccionarioSF_LF.get(cadena[0]);
					if(value==null){
						value= new HashSet<String>();
					}
					value.add(cadena[1]);					
				}else{
					value=new HashSet<>();
					value.add(cadena[1]);
					diccionarioSF_LF.put(cadena[0], value);
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
		return diccionarioSF_LF;
	}
	
	public HashMap<String,HashSet<String>> cargaDiccionarioSF_LFTXT(String path){		
		FileReader fr = null;
		BufferedReader br = null;
		
		try {					
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			
			String linea;
			
			while ((linea = br.readLine()) != null) {
				String[] cadena = linea.split(":");
				HashSet<String> value;
				value = diccionarioSF_LF.get(cadena[0]);
				if (value == null) {
					value = new HashSet<String>();
					diccionarioSF_LF.put(cadena[0], value);
				}
				String[] valueList = cadena[1].split("//");
				for (String s : valueList) {
					value.add(s);
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
		return diccionarioSF_LF;
		
	}
	
	public HashMap<String,String> cargaDiccionarioLF_SFTXT(String path){		
		FileReader fr = null;
		BufferedReader br = null;
		
		try {					
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			
			String linea;
			
			while ((linea = br.readLine()) != null) {
				String[] cadena = linea.split(":");
				String[] valueList = cadena[1].split("//");
				for(String v: valueList){
					String a =diccionarioLF_SF.get(v);
					if(a==null){
						diccionarioLF_SF.put(v, cadena[0]);
					}
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
		return diccionarioLF_SF;
		
	}
	
	public void escribeDiccionarioTXT(){
		FileWriter ficheroEscritura=null;
		PrintWriter pwEscritura = null;
		String var;
		try {
			ficheroEscritura = new FileWriter("DiccionarioTest002.txt",true);
			pwEscritura= new PrintWriter(ficheroEscritura);
			
			for(String s:this.diccionarioSF_LF.keySet()){
				byte[] sb = s.getBytes("ISO-8859-1");
				var= new String(sb,"UTF-8");
				pwEscritura.print(var+":");
				int i=0;
				Iterator<String> it=this.diccionarioSF_LF.get(s).iterator();
				while(i<this.diccionarioSF_LF.get(s).size()-1){
					sb = it.next().getBytes("ISO-8859-1");
					var= new String(sb,"UTF-8");
					pwEscritura.print(var+"//");
					i++;
				}
				sb = it.next().getBytes("ISO-8859-1");
				var= new String(sb,"UTF-8");
				pwEscritura.println(var);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			try {
				if (null != ficheroEscritura) {
					ficheroEscritura.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	
	public HashMap<String, HashSet<String>> getDiccionarioSF_LF() {
		return diccionarioSF_LF;
	}

	

	public HashMap<String, String> getDiccionarioLF_SF() {
		return diccionarioLF_SF;
	}
	

}
