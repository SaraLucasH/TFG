package Analizadores;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	
	HashSet<String> palabrasConectoras;
	
	
	public Resultado(){		
		this.diccionarioTextoActual=new HashMap<>();
		cd.cargaDiccionarioSF_LFTXT("DiccionarioTest002.txt");
		this.diccionarioFormaCorta=cd.getDiccionarioSF_LF();
		this.diccionarioFormaLarga=cd.getDiccionarioLF_SF();
		
		//Preposiciones
		palabrasConectoras= new HashSet<>(Arrays.asList("a", "ante", "bajo", "cabe", "con", "contra"
				, "de", "desde", "durante", "en", "entre", "hacia", "hasta", "mediante", "para", "por", "según"
				, "sin", "so", "sobre", "tras", "versus" , "vía","el","la","los","las","le","les"));
	}
	

	public void addDupla(String ac, String lf) {
		System.out.println(ac + " --- " + lf);
		if (this.diccionarioTextoActual.get(ac) == null) {			
			this.diccionarioTextoActual.put(ac, new HashSet<String>());
		}
		if(lf!=null) {
			System.out.println("Checkeo palabra");
			String lfChecked=checkLongForm(lf,ac);
			this.diccionarioTextoActual.get(ac).add(lfChecked);
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
	private String checkLongForm(String lf,String ac) {
		
		String res= lf;
		//Por si hubiese algun error
		res.replaceAll("/t"," ");
		String[]cadena= res.split(" ");
		if(cadena.length==1 || cadena.length<ac.length()){
			//Solo hay una palabra
			//System.out.println("Check long form... "+res);
			return res;
		}
		
		int indiceAcronimo=0;
		int i=cadena.length-1;
		res="";
		
		boolean tope=false;
		
		while(i>=0 && !tope){
			Character c1=Character.toLowerCase(cadena[i].charAt(0));
			Character c2=Character.toLowerCase(ac.charAt(indiceAcronimo));
			//System.out.println(c1+" equals "+c2);
			if(!this.palabrasConectoras.contains(cadena[i]) && c1.equals(c2)){
				tope=true;
			}else{
				i--;
			}
		}
		
		boolean completo=false;
		res=cadena[i]+" ";
		i++;
		while(i<cadena.length){
			res=res+" "+cadena[i];
			i++;
		}
		//System.out.println("Check long form... "+res);
		return res;
	}

	public String toString(){
		String result="Resultado del texto actual:\n";
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
