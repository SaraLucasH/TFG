package Analizadores;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import Diccionarios.CargaDiccionario;

public class Resultado {	
	String significado;
	File archivoDiccionario=new File("DiccionarioTest001.txt");
	HashMap<String,HashSet<String>> diccionario;
	
	String posibleFormaLarga;
	
	public Resultado(String acronimo){
		if(acronimo!=null) {
			consultaDiccionario(acronimo);
		}else {
			//Lambda
			nadaDetectado();
		}
	}
	private void nadaDetectado() {
		System.out.println("No se han encontrado acronimos");
		
	}
	public Resultado(String ac,String lf) {
		this.posibleFormaLarga=lf;
		consultaDiccionario(ac);
	}
	
	private void consultaDiccionario(String acronimo) {
		if(this.diccionario==null){
			CargaDiccionario cd= new CargaDiccionario();
			cd.cargaDiccionario(this.archivoDiccionario);
			this.diccionario=cd.getDiccionario();
		}
		System.out.println("Acronimo identificado: "+acronimo);
		HashSet<String> respuesta=this.diccionario.get(acronimo);
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
		if(posibleFormaLarga!=null) {
			System.out.println("Forma larga detectada en el texto: "+posibleFormaLarga);
		}
	}
}
