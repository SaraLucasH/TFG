package Analizadores;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import Diccionarios.CargaDiccionario;

public class Resultado {	
	String significado;
	File archivoDiccionario=new File("C:/Users/saral/OneDrive/Documentos/GitHub/TFG/DiccionarioTest001.txt");
	HashMap<String,HashSet<String>> diccionario;
	
	public Resultado(String acronimo){		
		consultaDiccionario(acronimo);
	}
	
	private void consultaDiccionario(String acronimo) {
		if(this.diccionario==null){
			CargaDiccionario cd= new CargaDiccionario();
			cd.cargaDiccionario(this.archivoDiccionario);
			this.diccionario=cd.getDiccionario();
		}
		HashSet<String> respuesta=this.diccionario.get(acronimo);
		if(respuesta==null){
			System.out.println("No hay formas largas");
		}
		System.out.println("Acronimo identificado: "+acronimo);
		
		Iterator<String> it=respuesta.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		
	}

}
