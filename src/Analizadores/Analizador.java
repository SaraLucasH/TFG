package Analizadores;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
*/
public class Analizador {
	
	public static void main(String argv[]) {
		if (argv.length == 0) {
			FileReader file;
			try {
				file=new java.io.FileReader("ArchivoPrueba1.txt");
				AnalizadorLexico lexico = new AnalizadorLexico(file);
				parser sintactico = new parser(lexico);
				sintactico.parse();
				file.close();
			} catch (java.io.FileNotFoundException e) {
				System.out.println("Archivo \"" + "prueba1" + "\" no encontrado.");
			} catch (java.io.IOException e) {
				System.out.println("Error durante la lectura del" + " archivo \"" +  "prueba1" + "\".");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Excepcion:");
				e.printStackTrace();
			}
		} else {
			for (int i = 0; i < argv.length; i++) {
				AnalizadorLexico lexico = null;
				try {
					//Pasando como parametro archivo docs de la tarea BARR2
					FileReader file= new FileReader(argv[i]);
					BufferedReader br = new BufferedReader(file);
					String linea;
					while ((linea = br.readLine()) != null) {
						String[] cadena=linea.split("\t");
						
						//Es posible que haya algun error en el texto proporcionado, y que contenga alguna tabulacion. Por ello,
						//para su correcta separacion, se inspecciona
						if(cadena.length>3){
							int longitudInnecesaria=cadena.length-1;
							String aux=cadena[longitudInnecesaria];
							longitudInnecesaria--;
							while(longitudInnecesaria>2){
								aux=cadena[longitudInnecesaria]+" "+aux;
							}
							cadena[2]+=aux;
						}
						
						//Solo textos en castellano
						if(cadena.length>1 && cadena[1].equalsIgnoreCase("es")) {
							//Identificador del texto
							System.out.println(cadena[0]);
							cadena[2].replaceAll("\t", " ");
							
							//Si no las tildes no las saca de forma correcta.
							String cad=new String(cadena[2].getBytes("ISO_8859_1"),"UTF-8");
							
							lexico = new AnalizadorLexico(
									new java.io.StringReader(cad
											));
							parser sintactico = new parser(lexico);
			
							sintactico.parse();
						}
					}
					br.close();
					file.close();
				} catch (java.io.FileNotFoundException e) {
					System.out.println("Archivo \"" + argv[i]
							+ "\" no encontrado.");
				} catch (java.io.IOException e) {
					System.out.println("Error durante la lectura del"
							+ " archivo \"" + argv[i] + "\".");
					e.printStackTrace();
				} catch (Exception e) {
					System.out.println("Excepcion:");
					e.printStackTrace();
				}
			}
		}
	}
}
