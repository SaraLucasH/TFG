package Window;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import Analizadores.AnalizadorLexico;
import Analizadores.parser;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.regex.Matcher;

import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;



public class Analizador extends JFrame {

	private JPanel contentPane;
	private JTextField rutaEntrada;
	private JTextField rutaSalida;
	private JComboBox comboSeleccion;
	private WordVectors wvect;
	private TextArea textEntrada;
	private TextArea textSalida;
	private JButton buscarEntrada;
	private JButton buscarSalida;

	
	private static WordVectors loadEmbeddings() {
		return WordVectorSerializer.loadStaticModel(new File(".\\Herramientas\\Embeddings_2019-01-01\\Embeddings"
    			+ "\\Embeddings_ES\\Scielo\\50\\W2V_scielo_w10_c5_50_15epoch.txt"));
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Analizador frame = new Analizador();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Analizador() {
		this.wvect=loadEmbeddings();
		setTitle("Desambiguacion de acronimos en el dominio medico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 458, 641);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Introduce ruta/s fichero/s de entrada (ej: ruta1;ruta2...):");
		lblNewLabel.setBounds(10, 62, 287, 23);
		contentPane.add(lblNewLabel);
		
		rutaEntrada = new JTextField();
		rutaEntrada.setToolTipText("Ruta de los ficheros a desambiguar. En caso de querer introducir varios ficheros, separar las rutas por ; (ejemplo:ruta1;ruta2)");
		rutaEntrada.setBounds(10, 88, 276, 20);
		contentPane.add(rutaEntrada);
		rutaEntrada.setColumns(10);
		
		JLabel lblIntroduceRutaDe = new JLabel("Introduce ruta de salida:");
		lblIntroduceRutaDe.setBounds(10, 110, 287, 23);
		contentPane.add(lblIntroduceRutaDe);
		
		rutaSalida = new JTextField();
		rutaSalida.setToolTipText("Ruta de salida para el fichero resultado");
		rutaSalida.setBounds(10, 133, 276, 20);
		contentPane.add(rutaSalida);
		rutaSalida.setColumns(10);		
		
		TextArea textoEntrada = new TextArea();
		textoEntrada.setBounds(10, 193, 414, 160);
		contentPane.add(textoEntrada);
		this.textEntrada=textoEntrada;
		
		this.textSalida = new TextArea();
		textSalida.setBounds(10, 388, 414, 160);
		contentPane.add(textSalida);
		
		JLabel lblNewLabel_1 = new JLabel("Introduce texto de entrada:");
		lblNewLabel_1.setBounds(10, 164, 205, 23);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblSalidaGenerada = new JLabel("Salida generada:");
		lblSalidaGenerada.setBounds(10, 359, 205, 23);
		contentPane.add(lblSalidaGenerada);
		
		this.buscarEntrada= new JButton("Buscar");
		buscarEntrada.setToolTipText("Buscar en el explorador de archivos archivos de entrada");
		buscarEntrada.setBounds(296, 87, 89, 23);
		contentPane.add(buscarEntrada);
		
		this.buscarSalida = new JButton("Selecciona carpeta");
		buscarSalida.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Selecciona una carpeta destino");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				int result = chooser.showOpenDialog(new JFrame());
				if (result == JFileChooser.APPROVE_OPTION) {										
					rutaSalida.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		buscarSalida.setToolTipText("Buscar en el explorador de archivos una ruta de salida");
		buscarSalida.setBounds(296, 132, 128, 23);
		contentPane.add(buscarSalida);
		
		JButton desambiguarButton = new JButton("Desambiguar");
		desambiguarButton.addMouseListener(new MouseAdapter()  {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Compruebo los campos de ruta
				String charset = "UTF-8";				
				AnalizadorLexico lexico = null;	
				if(comboSeleccion.getSelectedItem().equals("Ficheros")) {					
					try {
						String rutaEntradaArray[]=rutaEntrada.getText().split(";");
											
						File dirSalida = new File(rutaSalida.getText());
						
						if(dirSalida.isDirectory() && dirSalida.exists()) {
							String rutaS=rutaSalida.getText();
							for (int i = 0; i < rutaEntradaArray.length; i++) {			
								try {							
									File file = new File(rutaEntradaArray[i]);
									String[] archivo=rutaEntradaArray[i].split(Matcher.quoteReplacement(System.getProperty("file.separator")));
									
									String id=archivo[archivo.length-1].split("\\.")[0];
									Reader br = new InputStreamReader(new FileInputStream(file), charset);
									BufferedReader buffer = new BufferedReader(br);
									lexico = new AnalizadorLexico(br);				
									parser sintactico = new parser(lexico,id,rutaS, wvect);
									sintactico.parse();
									br.close();
									buffer.close();
									
								}catch(NullPointerException ex) {
									//Debe introducir ruta salida
									JOptionPane.showMessageDialog(new JFrame(),"Debe introducir una ruta de salida.");
									
								}catch (UnsupportedEncodingException unsupportedEncoding) {									
									JOptionPane.showMessageDialog(new JFrame(), unsupportedEncoding.getMessage());
								} catch (java.io.FileNotFoundException fileNotFound) {									
									JOptionPane.showMessageDialog(new JFrame(), "Archivo \"" + rutaEntradaArray[i] + "\" no encontrado.");
								} catch (java.io.IOException ioExcep) {
									JOptionPane.showMessageDialog(new JFrame(), "Error durante la lectura del" + " archivo \"" + rutaEntradaArray[i] + "\".");
									
								} catch (Exception exception) {
									JOptionPane.showMessageDialog(new JFrame(), exception.getMessage());
								}
							}
							JOptionPane.showMessageDialog(new JFrame(), "Archivo de salida (SalidaDesambiguacion.tsv) generado en "+rutaSalida.getText());
						}else {
							JOptionPane.showMessageDialog(new JFrame(), "Debe introducir un destino valido");
							
						}
					}catch(NullPointerException ex) {
						JOptionPane.showMessageDialog(new JFrame(), "Debe introducir una ruta de entrada.");
					}
				}else {
					//No hay ruta de entrada cojo el texto del text area				
					String cadenaEntrada=textEntrada.getText();
					if(!cadenaEntrada.isEmpty()) {	
						try {
							InputStream is = new ByteArrayInputStream( cadenaEntrada.getBytes( charset ) );
							Reader br = new InputStreamReader(is);
							lexico = new AnalizadorLexico(br);				
							parser sintactico = new parser(lexico,"","", wvect);
							sintactico.parse();
							textSalida.setText(sintactico.getResultado());
							br.close();
						} catch (Exception exception) {
							System.out.println("Excepcion:");
							exception.printStackTrace();
						}
					}else {
						//Lanzo ventana modal
						JOptionPane.showMessageDialog(new JFrame(),"Debe introducir texto en el area de entrada para desambiguar.");
					}
				}				
			}
		});
		desambiguarButton.setBounds(169, 554, 106, 23);
		contentPane.add(desambiguarButton);
		
		JButton borrarButton = new JButton("Borrar");
		borrarButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rutaEntrada.setText("");
				rutaSalida.setText("");
				textEntrada.setText("");
				textSalida.setText("");
			}
		});
		borrarButton.setBounds(335, 11, 89, 23);
		contentPane.add(borrarButton);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setToolTipText("Escoge entre introducir texto por teclado o mediante fichero de entrada");
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedItem().equals("Ficheros")) {
					textEntrada.setEnabled(false);
					textSalida.setEnabled(false);
					rutaEntrada.setEnabled(true);
					rutaSalida.setEnabled(true);
					buscarEntrada.setEnabled(true);
					buscarSalida.setEnabled(true);
				}else {
					textEntrada.setEnabled(true);
					textSalida.setEnabled(true);
					rutaEntrada.setEnabled(false);
					rutaSalida.setEnabled(false);
					buscarEntrada.setEnabled(false);
					buscarSalida.setEnabled(false);
				}
			}
		});
		comboBox.setBounds(10, 29, 205, 22);
		comboBox.addItem("Texto en pantalla");
		comboBox.addItem("Ficheros");
		
		
		this.comboSeleccion=comboBox;
		if(comboBox.getSelectedItem().equals("Ficheros")) {
			textEntrada.setEnabled(false);
			textSalida.setEnabled(false);
		}else {
			rutaEntrada.setEnabled(false);
			rutaSalida.setEnabled(false);
		}
		
		contentPane.add(comboBox);		
		
		buscarEntrada.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setMultiSelectionEnabled(true);
				int result = jFileChooser.showOpenDialog(new JFrame());
				if (result == JFileChooser.APPROVE_OPTION) {
					String cadenaFicheros="";
					File[] files=jFileChooser.getSelectedFiles();
					if(files.length>0) {
						cadenaFicheros=files[0].getAbsolutePath();
						for(int i=1;i<files.length;i++) {
							cadenaFicheros+=";"+files[i];
						}
					}
					rutaEntrada.setText(cadenaFicheros);
				}				
			}
		});
		
		
		JLabel lblNewLabel_2 = new JLabel("Selecciona una opci\u00F3n:");
		lblNewLabel_2.setBounds(10, 11, 193, 14);
		contentPane.add(lblNewLabel_2);
		
				
	}
}
