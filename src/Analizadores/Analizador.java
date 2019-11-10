package Analizadores;
import java.io.IOException;
import java.io.PrintWriter;

/**
*/
public class Analizador {
	public static void main(String argv[]) {
		if (argv.length == 0) {
			System.out.println("Inserta nombre de archivo\n"
					+ "( Usage : java Analizador <inputfile> )");
		} else {
			for (int i = 0; i < argv.length; i++) {
				AnalizadorLexico lexico = null;
				try {
					lexico = new AnalizadorLexico(new java.io.FileReader(
							argv[i]));
					parser sintactico = new parser(lexico);
					sintactico.parse();
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
