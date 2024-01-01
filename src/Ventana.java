import java.awt.BorderLayout; 
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Ventana extends JFrame implements ActionListener{
	private JMenuBar barra;
	private JLabel titulo;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	
	private JMenu archivo;
	private JMenuItem crear;
	private JMenuItem guardar;
	private JMenuItem guardarComo;
	private JMenuItem cargar;
	
	private JMenu juegos;
	private JMenuItem jugar;
	private JMenuItem solucionOptima;
	
	private JMenu acciones;
	private JMenuItem rehacer;
	private JMenuItem deshacer;
	
	private JMenu extra;
	private JMenuItem informacion;
	
	private ArrayList<ArrayList<Ficha>> tablero;
	
	//Variables globales de guardar como
	private String ruta;
	private File file;
	
	
	public Ventana() {
		this.setTitle("Juego de las fichas -- Lorena Albillos");
		this.panel1 = new JPanel();
		this.panel2 = new JPanel();
		
		this.add(panel1);
		this.add(panel2);
		
		/*definimos las areas en el frame*/
		this.setLayout(new BorderLayout());
		this.add(panel1, BorderLayout.NORTH);
		this.add(panel1, BorderLayout.WEST);
		this.add(panel2, BorderLayout.CENTER);
		
		this.barra = new JMenuBar();
		this.setJMenuBar(barra);
		//this.panel1.add(this.barra);
		
		//Inicializar los JMenu que iran en la barra
		this.archivo = new JMenu("Archivo");
		this.juegos = new JMenu("Juego");
		this.acciones = new JMenu("Acciones");
		this.extra = new JMenu("Extra");
		
		//A�adimos los JMenu a la barra
		this.barra.add(this.archivo);
		this.barra.add(this.juegos);
		this.barra.add(this.acciones);
		this.barra.add(this.extra);
		
		//Inicializamos los JMenuItems de cada JMenu
		this.crear = new JMenuItem("Crear");
		this.guardar = new JMenuItem("Guardar");
		this.guardarComo = new JMenuItem("Guardar como");
		this.cargar = new JMenuItem("Cargar");
		
		this.jugar = new JMenuItem("Jugar");
		this.solucionOptima = new JMenuItem("Soluci�n �ptima");
		
		this.rehacer = new JMenuItem("Rehacer");
		this.deshacer = new JMenuItem("Deshacer");
		
		this.informacion = new JMenuItem("Informaci�n");
		
		//Ana�idos los JMenuItems a los JMenu
		this.archivo.add(this.crear);
		this.archivo.add(this.guardar);
		this.archivo.add(this.guardarComo);
		this.archivo.add(this.cargar);
		
		this.juegos.add(this.jugar);
		this.juegos.add(solucionOptima);
		
		this.acciones.add(this.rehacer);
		this.acciones.add(this.deshacer);
		
		this.extra.add(this.informacion);
		
		//Creamos los actionListener
		this.crear.addActionListener(this);
		this.guardar.addActionListener(this);
		this.guardarComo.addActionListener(this);
		this.cargar.addActionListener(this);
		
		tablero = new ArrayList<ArrayList<Ficha>>();
		
	}
	
	public static void main(String[] args) {
		Ventana ventana = new Ventana();
		ventana.setSize(400,400);
		ventana.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*JOptionPane -> mensajes que salen en ventanas emergentes*/
		JOptionPane option = new JOptionPane();
		
		if(this.crear == e.getSource()) {
			//Pedir numero de filas
			int filas = 0;
			try {
				String f = option.showInputDialog("Introduce el n�mero de filas que tendr� el juego");
				filas = Integer.parseInt(f);

				if(filas <= 0) { option.showMessageDialog(this, "El n�mero de filas tiene que ser mayor a 0");return;}
			
				if(filas > 20) {option.showMessageDialog(this, "El n�mero de filas tiene que ser menor a 20");return;}
			
			} catch(Exception exception) {option.showMessageDialog(this, "No se admite texto, solo n�meros enteros");return;}
				
			//Pedir numero de columnas
			int columnas = 0;
			try {
				String c = option.showInputDialog("Introduce el n�mero de columnas que tendr� el juego");
				columnas = Integer.parseInt(c);
				
				if(columnas <= 0) { option.showMessageDialog(this, "El n�mero de columnas tiene que ser mayor a 0");return;}
				
				if(columnas > 20) { option.showMessageDialog(this, "El n�mero de columnas tiene que ser menor a 20");return;}
			} catch(Exception exception) {option.showMessageDialog(this, "No se admita texto, solo n�meros enteros");return;}
		
			//Llamo a crear tablero
			crearTablero(filas, columnas);
		}
		
		if(this.guardar == e.getSource()) {
			guardar();
		}
		
		if(this.guardarComo == e.getSource()) {
			guardarComo();
		}
		
		if(this.cargar == e.getSource()) {
			cargar();
		}
	}
	
	private void crearTablero(int filas, int columnas) {
		//vaciar el panel
		this.panel2.removeAll();
		//actualizo tablero
		this.setVisible(true);
		
		tablero = new ArrayList<ArrayList<Ficha>>();
		
		for(int i = 0; i < filas; i++) {
			ArrayList<Ficha> fila = new ArrayList<>();
			for(int j = 0; j < columnas; j++) {
				fila.add(new Ficha(Fichas.Colores.VACIO, new Posicion(i, j)));
			}
			tablero.add(fila);
		}
		
		GridLayout grid = new GridLayout(filas, columnas);
		this.panel2.setLayout(grid);
		
		for(int i = 0; i < filas; i++) {
			for(int j = 0; j < columnas; j++) {
				JTextField text = new JTextField();
				
				addFocus(text, i, j);
				
				this.panel2.add(text);
			}
		}
		
		//Para actualizar el panel
		this.setVisible(true);
		return;
	}
	
	public void addFocus(JTextField text, int fila, int columna) {
		//subclase no puedo referenciar a this
		text.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent event) {
				
			}
			
			public void focusLost(FocusEvent event) {
				//Recoger el texto del texto con el metodo getText
				String txt = text.getText();
				
				if(txt.length() == 0) {
					
					text.setText("");
				}
				
				//Comprobar si el texto es R, V, A -> se sustituye el color de la ficha
				switch(txt) {
					case "A": tablero.get(fila).get(columna).setColor(Fichas.Colores.A);
						break;
					case "V": tablero.get(fila).get(columna).setColor(Fichas.Colores.V);
						break;
					case "R": tablero.get(fila).get(columna).setColor(Fichas.Colores.R);
						break;
						
						//Si no hay nada o hay algo que no sea uno de los 3 colores se sustituye por VACIO
					default: 
							 //JOptionPane.showMessageDialog(getParent(), "Error en la posicion ( " + fila + "," + columna + " )");
							 tablero.get(fila).get(columna).setColor(Fichas.Colores.VACIO);
							 text.setText("");
						break;
				}
				
				//Llamar al metodo imprimir tablero
				imprimirTablero();
			}
		});
	}
	
	private void imprimirTablero() {
		for(ArrayList<Ficha> filas: tablero) {
			for(Ficha ficha: filas) {
				System.out.print(ficha.getColor() + " ");
			}
			System.out.println();
		}
	}
	
	private boolean comprobarTableroLleno() {
		if(tablero.size() == 0) { 
			JOptionPane option = new JOptionPane();
			option.showMessageDialog(this, "No se puedo guardar. El tablero est� vac�o");
			return false; 
		}
		
		for(ArrayList<Ficha> filas: tablero) {
			for(Ficha ficha: filas) {
				if(ficha.getColor().equals(Fichas.Colores.VACIO)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void guardar() {
		
		/*Compruebo si el tablero est� lleno*/
		if(!comprobarTableroLleno()) { 
			JOptionPane.showMessageDialog(this, "No se pudo guardar la partida. Tablero no lleno", null, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		/*Compruebo si file y ruta est�n vacios
		 * -> si es asi llamo a guardar como*/
		if((this.file == null)) {
			guardarComo();
		}
		/*Creo mi hijo de escritura FileWriter que recibe ruta o file
		 *  Comprobar excepciones*/
		try {
			FileWriter file = new FileWriter(this.ruta);
			
		/*Creo el boli PrintWriter al que le pasamos el file*/
			PrintWriter escribir = new PrintWriter(file);
		
		/*Recorro con  2 for el tablero y con el metodo print o println -> metodo de PrintWriter
		 * a�adir el contenido*/
			for(ArrayList<Ficha> filas: tablero) {
				for(Ficha ficha: filas) {
					escribir.print(ficha.getColor());
				}
				escribir.println();
			}
			
		/*Guardo el archivo con file.close*/
			escribir.close();
			file.close();
			
		/*A�adir mensaje de que se ha guardado el file*/
			JOptionPane.showMessageDialog(this, "Se ha guardado el juego");
			
		/*Cerrar try catch con mensaje de error */
		} catch(Exception e) {
			JOptionPane.showMessageDialog(this, "No se pudo guardar la partida", null, JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void guardarComo() {
		
		if(comprobarTableroLleno()){
			JFileChooser archivo = new JFileChooser();
			/*Seleccionar el archivo con ShowSaveDialog(null)
			 * -> devuelve 0 si se ha pulsado en Aceptar
			 * -> -1 si se ha cancelado*/
			int status = archivo.showSaveDialog(null);
			
			/*Si se aceptase seleccionar el file que se va a guardar
			 * con getSelectedFile()*/
			if(status == 0) {
				file = archivo.getSelectedFile();
			} else {
				return;
			}
			
			/*Se guarda la ruta en el archivo en la variable global con
			 * el m�todo getAbsolutePath()*/
			this.ruta = file.getAbsolutePath();
			
			/*Comprobamos que tenga una extensi�n .txt -> sino se lo a�adimos*/
			if(!ruta.endsWith(".txt")) {
				ruta = ruta + ".txt";
			}
			
			File aux = new File(this.ruta);
			
			/*Opcional comprobamos que el archivo existe con el metodo exists()
			 * -> si existe preguntar si quiere sobreescribirlo*/
			if(aux.exists()) {
				int respuesta =  JOptionPane.showConfirmDialog(this, "�Desea sobreescribir el archivo?");
				
				if(respuesta != 0) { return;}
			}
			
			/*Llamamos a guardar*/
			guardar();
			
		} else {
			//Mostrar mensaje de error
			JOptionPane.showMessageDialog(this, "No se pudo guardar", null, JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	private void cargar() {
		JFileChooser archivo = new JFileChooser();
		
		//con el metodo showOpenDialog(null) escogemos el archivo que queramos leer
		int status = archivo.showOpenDialog(null);
		
		/*Comprobamos que se haya presionado en confirmar
		 * y seleccionamos nuestro archivo*/	
		if(status == 0) {
			this.file = archivo.getSelectedFile();
		} else {
			return;
		}
		
		try {
			this.ruta = this.file.getAbsolutePath();
			
			/*Comprobar que el archivo sea correcto*/
			File aux = new File(this.ruta);
			if(!aux.exists()) {
				JOptionPane.showMessageDialog(this, "No existe el fichero como para poder cargarlo.", null, JOptionPane.ERROR_MESSAGE);
				return;
			}
				
			if(!this.ruta.endsWith(".txt")) {
				JOptionPane.showMessageDialog(this, "Este tipo de archivo no se puede cargar..", null, JOptionPane.ERROR_MESSAGE);
				return;	
			}
			
			/*Creamos una hoja de lectura pasandole el archivo obtenido*/
			FileReader lectura = new FileReader(this.file);
			
			/*Crear bufferedReader al que le pasaremos la hoja*/
			BufferedReader buffer = new BufferedReader(lectura);
			
			/*Crear una lista para guardar las lineas leidas del archivo*/
			ArrayList<String> lista = new ArrayList<String>();
			
			/*Leeremos todas las lineas con el metodo readLine y a�adirlas a la lista*/
			String linea = buffer.readLine();
			while(linea != null) {
				lista.add(linea);
				linea = buffer.readLine();
			}
			
			/*Comprobar que el numero de lineas sea >0*/
			if(lista.size() <= 0) {return;}
			
			/*Comprobar que el numero de lineas sea <= 20*/
			if(lista.size() > 20) {return;}
			
			for(int i = 0; i < lista.size(); i++) {
				/*Comprobar que los caracteres de una lina sean > 0 && <= 20*/
				if(lista.get(i).length()  <= 0 || lista.get(i).length() > 20) {
					JOptionPane.showMessageDialog(this, "Error al cargar el archivo 1.", null, JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				/*Comprobar que hay la misma cantidad de caracteres por fila*/
				if(i != 0 && lista.get(i).length() != lista.get(i - 1).length()) {
					JOptionPane.showMessageDialog(this, "Error al cargar el archivo 2.", null, JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				for(int j = 0; j < lista.get(i).length(); j++) {
					/*Recorrer todas las lineas y analizar si contienen R, V, A
					 * -> si alguna no lo contiene se saca error y salimos*/
					if(lista.get(i).charAt(j) != 'R'  && lista.get(i).charAt(j) != 'A' && lista.get(i).charAt(j) != 'V'){
						JOptionPane.showMessageDialog(this, "Error al cargar el archivo 3.", null, JOptionPane.ERROR_MESSAGE);
						return;
					}	
				}
			}
			guardarTablero(lista);
			cargarTablero();
			
		} catch(Exception exception) {
			JOptionPane.showMessageDialog(this, "Error al cargar el archivo 4.", null, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
	}
	
	private void guardarTablero(ArrayList<String> lista) {
		this.tablero= new ArrayList<>();
		for(int i = 0; i< lista.size(); i++) {
			ArrayList<Ficha> fila = new ArrayList<Ficha>();
			for(int j = 0; j < lista.get(i).length(); j++) {
				Ficha ficha;
				switch(lista.get(i).charAt(j)) {
					case 'A': ficha = new Ficha(Fichas.Colores.A, new Posicion(i, j));
							  fila.add(ficha);
						break;
					case 'R': ficha = new Ficha(Fichas.Colores.R, new Posicion(i, j));
							  fila.add(ficha);
						break;
					case 'V': ficha = new Ficha(Fichas.Colores.V, new Posicion(i, j));
							  fila.add(ficha);
						break;
				}
			}
			this.tablero.add(fila);
		}
	}
	
	private void cargarTablero() {
		/*Elimino todo del panel2*/
		this.panel2.removeAll();
		
		//actualizo tablero
		this.setVisible(true);
		
		/*Definir nuevo gridLayout con las nuevas filas y columnas*/
		GridLayout grid = new GridLayout(this.tablero.size(), this.tablero.get(0).size());
		this.panel2.setLayout(grid);
		
		/*Para cada elemento, mostramos en el JTextField su contenido*/
		for(int i = 0; i < this.tablero.size(); i++) {
			for(int j = 0; j < this.tablero.get(0).size(); j++) {
				JTextField text = new JTextField();
				
				if(!this.tablero.get(i).get(j).getColor().equals(Colores.VACIO)) {
					text.setText(this.tablero.get(i).get(j).getColor() + " ");
				}
				/*A�adir el addFocus*/
				addFocus(text, i, j);
				this.panel2.add(text);
			}
		}
		
		
		/*Recargar la lista*/
		this.setVisible(true);
	}
	
	enum Colores{
        R, V, A, VACIO;
    }
}

private class BotonJuego extends JButton implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
