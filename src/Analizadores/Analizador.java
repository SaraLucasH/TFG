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

/**
*/
public class Analizador {
	
	
	public static void main(String argv[]) {
		String charset = "UTF-8";
		if (argv.length == 0) {
			File file;
			try {
				file=new File("ArchivoPrueba1.txt");				
				/*BufferedReader br = new BufferedReader( 
				      new InputStreamReader (new FileInputStream(file), charset));*/
				Reader br = new InputStreamReader (new FileInputStream(file), charset);
				String linea;
				String cadenaCompleta="";
				
				/*
				 * Lee linea a linea
				 */
				/*while ((linea = br.readLine()) != null) {
					// Si no las tildes no las saca de forma correcta.
					 cadenaCompleta+= linea;
				}	*/		
				System.out.println(br);
				AnalizadorLexico lexico= new AnalizadorLexico(br);				
				parser sintactico = new parser(lexico,"");
				sintactico.parse();
				br.close();
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
					/*File file= new File("C:/Users/sara.lucas.hernandez/Documents/Tfg/TestingSet/txt/"+argv[i]);					
					Reader br = new InputStreamReader (new FileInputStream(file), charset);
					
					String linea;
					String cadenaCompleta="";*/
					
					/*
					 * Lee linea a linea
					 */
					/*while ((linea = br.readLine()) != null) {
						// Si no las tildes no las saca de forma correcta.
						 cadenaCompleta+= linea;
					}*/
					
					//lexico = new AnalizadorLexico(new java.io.StringReader(cadenaCompleta));
					/*lexico= new AnalizadorLexico(br);
					System.out.println(argv[i]);
					parser sintactico = new parser(lexico,argv[i]);

					sintactico.parse();
					br.close();*/
					File file= new File(argv[i]);					
					Reader br = new InputStreamReader (new FileInputStream(file), charset);
					BufferedReader buffer= new BufferedReader(br);
					
					String linea;
					String cadenaCompleta="";
					buffer.readLine();
					while((linea = buffer.readLine()) != null) {
						String[] cad=linea.split("\t");
						System.out.println(cad[0]);
						if(cad[1].toUpperCase()=="ES") {
							File archivo= new File(cad[0]);	
							Writer out = new BufferedWriter(new OutputStreamWriter(
								    new FileOutputStream(archivo,true), "UTF-8"));
							out.write(cad[2]);
							out.close();
						}
					}
					
					
					br.close();
					buffer.close();
				}catch (UnsupportedEncodingException e){
		            System.out.println(e.getMessage());		        	
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
