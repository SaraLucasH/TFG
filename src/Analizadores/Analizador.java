package Analizadores;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

/**
*/
public class Analizador {
	
	private static WordVectors loadEmbeddings() {
		return WordVectorSerializer.loadStaticModel(new File("C:\\Users\\saral\\Documents\\tfg\\Embeddings_2019-01-01\\Embeddings"
    			+ "\\Embeddings_ES\\Scielo\\50\\W2V_scielo_w10_c5_50_15epoch.txt"));
	}
	
	public static void main(String argv[]) {
		String charset = "UTF-8";
		WordVectors wvect=loadEmbeddings();
		for (int i = 0; i < argv.length; i++) {
			AnalizadorLexico lexico = null;
			try {
				File file = new File("C:\\Users\\saral\\Documents\\tfg\\Evaluation\\training\\txt\\" + argv[i] + ".txt");
				System.out.println(argv[i]);
				Reader br = new InputStreamReader(new FileInputStream(file), charset);
				BufferedReader buffer = new BufferedReader(br);
				lexico = new AnalizadorLexico(br);				
				parser sintactico = new parser(lexico, argv[i], wvect);
				sintactico.parse();
				br.close();
				buffer.close();
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.getMessage());
			} catch (java.io.FileNotFoundException e) {
				System.out.println("Archivo \"" + argv[i] + "\" no encontrado.");
			} catch (java.io.IOException e) {
				System.out.println("Error durante la lectura del" + " archivo \"" + argv[i] + "\".");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Excepcion:");
				e.printStackTrace();
			}
		}
	}

}
