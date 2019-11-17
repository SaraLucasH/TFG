package Analizadores;

import java.io.File;
import java.util.ArrayList;
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
	
	
	public Resultado(){		
		this.diccionarioTextoActual=new HashMap<>();
		cd.cargaDiccionarioSF_LFTXT("DiccionarioTest002.txt");
		this.diccionarioFormaCorta=cd.getDiccionarioSF_LF();
		this.diccionarioFormaLarga=cd.getDiccionarioLF_SF();
	}
	

	public void addDupla(String ac, String lf) {
		System.out.println(ac + " --- " + lf);
		if (this.diccionarioTextoActual.get(ac) == null) {
			HashSet<String> longForm = new HashSet<>();
			this.diccionarioTextoActual.put(ac, longForm);
		}
		if(lf!=null && (ac==null || ac=="")){
			String acAux=this.diccionarioFormaLarga.get(lf);
			if(acAux!=null){
				System.out.println("*********Accedo a dic lf*********");
				HashSet<String> longForm = new HashSet<>();
				longForm.add(lf);
				this.diccionarioTextoActual.put(acAux,longForm);				
			}				
		}
	}
	public String toString(){
		String result="Resultado del texto actual:\n ";
		Iterator<Entry<String,HashSet<String>>> it=this.diccionarioTextoActual.entrySet().iterator();
		while(it.hasNext()){
			Entry<String,HashSet<String>> e=it.next();
			result+=e.getKey()+"\n";
			for(String s:e.getValue()){
				result+=s+" // ";
			}
			result+="\n";
			consultaDiccionario(e.getKey());
		}
		return result;
	}	
	
	private void consultaDiccionario(String acronimo) {		
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
			System.out.println("No hay formas largas en el diccionario para este acronimo");
			
		}else {		
			Iterator<String> it=respuesta.iterator();
			int i=1;
			while(it.hasNext()){
				System.out.println("Forma larga "+i+": "+it.next());
				i++;
			}
		}		
	}
}
