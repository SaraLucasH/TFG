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
			/*System.out.println("Inserta nombre de archivo\n"
					+ "( Usage : java Analizador <inputfile> )");*/
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
						if(cadena.length>1 && cadena[1].equalsIgnoreCase("es")) {
							System.out.println(linea.split("\t")[0]);
							lexico = new AnalizadorLexico(
									new java.io.StringReader(
											linea.split("\t")[2]));
							parser sintactico = new parser(lexico);
							sintactico.parse();
						}
					}				
					
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
