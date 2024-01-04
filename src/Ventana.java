import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class Ventana extends JFrame implements ActionListener {
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

	private ArrayList<ArrayList<Ficha>> copia;
	private boolean estoyJugando;

	private ArrayList<Imprimir> jugadaActual;

	// Variables globales de guardar como
	private String ruta;
	private File file;

	/* Variables globales para el juego */
	int puntos = 0;
	int numMovimientos = 1;

	/* Variables globales de rehacer y deshacer */
	private int posicion = 0;
	private ArrayList<ArrayList<ArrayList<Ficha>>> historial;
	private String rutaJuegos;
	private JMenuItem guardarSolucion;

	public Ventana(boolean juego) {
		this.setTitle("Juego de las fichas -- Lorena Albillos");
		this.panel1 = new JPanel();
		this.panel2 = new JPanel();

		this.setSize(400, 400);

		this.add(panel1);
		this.add(panel2);

		/* definimos las areas en el frame */
		this.setLayout(new BorderLayout());
		this.add(panel1, BorderLayout.NORTH);
		this.add(panel1, BorderLayout.WEST);
		this.add(panel2, BorderLayout.CENTER);

		this.barra = new JMenuBar();
		this.setJMenuBar(barra);
		// this.panel1.add(this.barra);

		// Inicializar los JMenu que iran en la barra
		this.archivo = new JMenu("Archivo");
		this.juegos = new JMenu("Juego");
		this.acciones = new JMenu("Acciones");
		this.extra = new JMenu("Extra");

		// A�adimos los JMenu a la barra
		this.barra.add(this.archivo);
		this.barra.add(this.juegos);
		this.barra.add(this.acciones);
		this.barra.add(this.extra);

		// Inicializamos los JMenuItems de cada JMenu
		this.crear = new JMenuItem("Crear");
		this.guardar = new JMenuItem("Guardar");
		this.guardarComo = new JMenuItem("Guardar como");
		this.cargar = new JMenuItem("Cargar");

		this.jugar = new JMenuItem("Jugar");
		this.solucionOptima = new JMenuItem("Soluci�n �ptima");
		this.guardarSolucion = new JMenuItem("Guardar Solucion");

		this.rehacer = new JMenuItem("Rehacer");
		this.deshacer = new JMenuItem("Deshacer");

		this.informacion = new JMenuItem("Informaci�n");

		// Ana�idos los JMenuItems a los JMenu
		this.archivo.add(this.crear);
		this.archivo.add(this.guardar);
		this.archivo.add(this.guardarComo);
		this.archivo.add(this.cargar);

		this.juegos.add(this.jugar);
		this.juegos.add(this.solucionOptima);
		this.juegos.add(this.guardarSolucion);

		this.acciones.add(this.rehacer);
		this.acciones.add(this.deshacer);

		this.extra.add(this.informacion);

		// Creamos los actionListener
		this.crear.addActionListener(this);
		this.guardar.addActionListener(this);
		this.guardarComo.addActionListener(this);
		this.cargar.addActionListener(this);
		this.jugar.addActionListener(this);
		this.solucionOptima.addActionListener(this);
		this.deshacer.addActionListener(this);
		this.rehacer.addActionListener(this);
		this.informacion.addActionListener(this);
		this.guardarSolucion.addActionListener(this);

		tablero = new ArrayList<ArrayList<Ficha>>();

		this.estoyJugando = juego;
		if (this.estoyJugando) {
			this.jugadaActual = new ArrayList<>();
		}

		/* Para rehacer y deshacer */
		this.historial = new ArrayList<ArrayList<ArrayList<Ficha>>>();
		this.posicion = 0;
	}

	public static void main(String[] args) {
		Ventana ventana = new Ventana(true);
		// ventana.setSize(400,400);
		ventana.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* JOptionPane -> mensajes que salen en ventanas emergentes */

		if (this.crear == e.getSource()) {
			// Pedir numero de filas
			int filas = 0;
			try {
				String f = JOptionPane.showInputDialog("Introduce el n�mero de filas que tendr� el juego");
				filas = Integer.parseInt(f);

				if (filas <= 0) {
					JOptionPane.showMessageDialog(this, "El n�mero de filas tiene que ser mayor a 0");
					return;
				}

				if (filas > 20) {
					JOptionPane.showMessageDialog(this, "El n�mero de filas tiene que ser menor a 20");
					return;
				}

			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this, "No se admite texto, solo n�meros enteros");
				return;
			}

			// Pedir numero de columnas
			int columnas = 0;
			try {
				String c = JOptionPane.showInputDialog("Introduce el n�mero de columnas que tendr� el juego");
				columnas = Integer.parseInt(c);

				if (columnas <= 0) {
					JOptionPane.showMessageDialog(this, "El n�mero de columnas tiene que ser mayor a 0");
					return;
				}

				if (columnas > 20) {
					JOptionPane.showMessageDialog(this, "El n�mero de columnas tiene que ser menor a 20");
					return;
				}
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this, "No se admita texto, solo n�meros enteros");
				return;
			}

			// Llamo a crear tablero
			crearTablero(filas, columnas);
		}

		if (this.guardar == e.getSource()) {
			guardar();
		}

		if (this.guardarComo == e.getSource()) {
			guardarComo();
		}

		if (this.cargar == e.getSource()) {
			cargar();
		}
		if ((guardarSolucion == e.getSource())) {
			guardarSolucion();
		}
		if (this.jugar == e.getSource()) {
			Ventana ventana = new Ventana(true);
			ventana.setTablero(this.tablero);
			ventana.cargarPanelJuego();
		}

		if (this.solucionOptima == e.getSource()) {
			solucionOptima();
		}

		if (this.rehacer == e.getSource()) {
			rehacer();
		}

		if (this.deshacer == e.getSource()) {
			deshacer();
		}

		if (this.informacion == e.getSource()) {
			crearTextoInformativo();

		}
	}

	private void crearTextoInformativo() {
		StringBuffer cadena = new StringBuffer();

		cadena.append("Informaci�n acerca del juego.\n");
		cadena.append(
				"El men� principal cuenta con una barra con cuatro opciones. En ellas hay diferentes opciones.\n");
		cadena.append("Archivo cuenta con las opciones:\n");
		cadena.append(
				"   �Crear: Crea un nuevo juego. Para ello pedir� pasar el n�mero de filas y columnas que tendr� el tablero de juego.\n");
		cadena.append(
				"           NO contempla la entrada por String, tanto la fila como la columna tienen que ser n�meros enteros.\n");
		cadena.append("           NO se crear� la matriz si las filas o columnas son mayores a 20 o menores a 0.\n");
		cadena.append(
				"           Una vez creado el tablero, solo se admitir�n fichas de colores A (azul), V (verde) y R (rojo).\n");
		cadena.append("           cualquier otro tipo de colores no se a�adir�n al tablero.\n");
		cadena.append(
				"   �Guardar: Guarda el tablero una vez creado. Si no hay un tablero creado saltar� un error indicando que no se puede\n");
		cadena.append(
				"             guardar el tablero. Si el tablero NO est� guardado en un archivo con extensi�n .txt crear� dicho archivo\n");
		cadena.append(
				"             con extensi�n .txt al cual el usuario podr� nombrarlo y guardarlo a su conveniencia.\n");
		cadena.append(
				"   �Guardar como: Similar a la opci�n guardar solo que si escoges guardar el nuevo tablero en un archivo ya existente\n");
		cadena.append(
				"                  dejar� elejir al usuario si desea sobreescribir el archivo o a�adirlo como uno nuevo.\n");
		cadena.append(
				"   �Cargar: Cargar� un tablero ya guardado previamente.\n        El juego NO contempla cargar ning�n fichero que no tenga\n");
		cadena.append(
				"            de extensi�n .txt y en el cual diferentes caracteres a parte de los colores de las fichas A, R, y V.\n");
		cadena.append("Juego cuenta con las opciones:\n");
		cadena.append(
				"   �Jugar: Permite al usuario poder jugar con su astucia e inteligencia, simplemente clicando en la parte del tablero\n");
		cadena.append("           correspondiente.\n");
		cadena.append(
				"   �Soluci�n �ptima: Permite que el propio juego encuentre la soluci�n m�s �ptima al tablero correspondiente.\n");
		cadena.append("Acciones cuenta con las opciones:\n");
		cadena.append("   �Rehacer: Permite al usuario porder rehacer un movimiento.\n");
		cadena.append("   �Deshacer: Permite al usuario poder revertir una acci�n realizada.\n");
		cadena.append(
				"Extra cuenta con la opci�n:\n   �Informaci�n: Permite al usuario poder acceder a estes instrucciones del juego\n");
		cadena.append("              para su mayor entendimiento del funcionamiento del juego.");

		JTextArea texto = new JTextArea(cadena.toString());

		// texto.setContentType("text/html");
		texto.setText(cadena.toString());
		texto.setEditable(false);

		// Crea un JDialog personalizado
		JDialog dialog = new JDialog(this, "Informaci�n", true);
		dialog.setLayout(new BorderLayout());

		// Agrega el JTextPane al centro del JDialog
		dialog.add(new JScrollPane(texto), BorderLayout.CENTER);

		// Agrega un bot�n de cerrar al sur del JDialog
		JButton cerrarButton = new JButton("Cerrar");
		cerrarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		dialog.add(cerrarButton, BorderLayout.SOUTH);

		// Configura el JDialog
		dialog.setSize(new Dimension(700, 500));
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);

	}

	private void crearTablero(int filas, int columnas) {
		this.ruta = null;
		this.file = null;

		// vaciar el panel
		this.panel2.removeAll();

		// actualizo tablero
		this.setVisible(true);

		tablero = new ArrayList<ArrayList<Ficha>>();

		for (int i = 0; i < filas; i++) {
			ArrayList<Ficha> fila = new ArrayList<>();
			for (int j = 0; j < columnas; j++) {
				fila.add(new Ficha(Fichas.Colores.VACIO, new Posicion(i, j)));
			}
			tablero.add(fila);
		}

		GridLayout grid = new GridLayout(filas, columnas);
		this.panel2.setLayout(grid);

		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				JTextField text = new JTextField();

				addFocus(text, i, j);

				this.panel2.add(text);
			}
		}

		// Para actualizar el panel
		this.setVisible(true);
		return;
	}

	public void addFocus(JTextField text, int fila, int columna) {
		// subclase no puedo referenciar a this
		text.addFocusListener(new FocusAdapter() {
			private String texto;

			public void focusGained(FocusEvent event) {
				this.texto = text.getText();
			}

			public void focusLost(FocusEvent event) {
				// Recoger el texto del texto con el metodo getText

				String txt = text.getText();

				if (txt.length() == 0) {
					text.setText("");
				}

				// Comprobar si el texto es R, V, A -> se sustituye el color de la ficha
				switch (txt) {
				case "A":
					tablero.get(fila).get(columna).setColor(Fichas.Colores.A);
					break;
				case "V":
					tablero.get(fila).get(columna).setColor(Fichas.Colores.V);
					break;
				case "R":
					tablero.get(fila).get(columna).setColor(Fichas.Colores.R);
					break;

				// Si no hay nada o hay algo que no sea uno de los 3 colores se sustituye por
				// VACIO
				default:
					if (txt.length() == 0)
						break;
					JOptionPane.showMessageDialog(getParent(), "Error " + txt + " no es un caracter v�lido.");
					tablero.get(fila).get(columna).setColor(Fichas.Colores.VACIO);
					text.setText(this.texto);
					break;
				}
				if (!text.getText().equals(this.texto)) {
					posicion++;

					/* Clonamos el tablero */
					Fichas aux = new Fichas();
					ArrayList<ArrayList<Ficha>> copiaTablero = aux.clonar(tablero);

					if (this.texto.equals("")) {
						copiaTablero.get(fila).get(columna).setColor(Fichas.Colores.VACIO);
					} else {
						switch (this.texto) {
						case "A":
							copiaTablero.get(fila).get(columna).setColor(Fichas.Colores.A);
							break;
						case "V":
							copiaTablero.get(fila).get(columna).setColor(Fichas.Colores.V);
							break;
						case "R":
							copiaTablero.get(fila).get(columna).setColor(Fichas.Colores.R);
							break;

						default:
							break;
						}

					}
					historial.add(copiaTablero);
				}

				// Llamar al metodo imprimir tablero
				imprimirTablero();
			}
		});
	}

	private void imprimirTablero() {
		for (ArrayList<Ficha> filas : tablero) {
			for (Ficha ficha : filas) {
				System.out.print(ficha.getColor() + " ");
			}
			System.out.println();
		}
	}

	private boolean comprobarTableroLleno() {
		if (tablero.size() == 0) {
			JOptionPane option = new JOptionPane();
			option.showMessageDialog(this, "No se puedo guardar. El tablero est� vac�o");
			return false;
		}

		for (ArrayList<Ficha> filas : tablero) {
			for (Ficha ficha : filas) {
				if (ficha.getColor().equals(Fichas.Colores.VACIO)) {
					return false;
				}
			}
		}
		return true;
	}

	private void guardar() {

		/* Compruebo si el tablero est� lleno */
		if (!comprobarTableroLleno()) {
			JOptionPane.showMessageDialog(this, "No se pudo guardar la partida. Tablero no lleno", null,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		/*
		 * Compruebo si file y ruta est�n vacios -> si es asi llamo a guardar como
		 */
		if ((this.file == null)) {
			guardarComo();
		}
		/*
		 * Creo mi hijo de escritura FileWriter que recibe ruta o file Comprobar
		 * excepciones
		 */
		try {
			this.ruta = this.file.getAbsolutePath();
			FileWriter file = new FileWriter(this.ruta);

			/* Creo el boli PrintWriter al que le pasamos el file */
			PrintWriter escribir = new PrintWriter(file);

			/*
			 * Recorro con 2 for el tablero y con el metodo print o println -> metodo de
			 * PrintWriter a�adir el contenido
			 */
			for (ArrayList<Ficha> filas : tablero) {
				for (Ficha ficha : filas) {
					escribir.print(ficha.getColor());
				}
				escribir.println();
			}

			/* Guardo el archivo con file.close */
			escribir.close();
			file.close();

			/* A�adir mensaje de que se ha guardado el file */
			JOptionPane.showMessageDialog(this, "Se ha guardado el juego");

			/* Cerrar try catch con mensaje de error */
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "No se pudo guardar la partida", null, JOptionPane.ERROR_MESSAGE);
		}
	}

	private void guardarComo() {

		JFileChooser archivo = new JFileChooser();
		/*
		 * Seleccionar el archivo con ShowSaveDialog(null) -> devuelve 0 si se ha
		 * pulsado en Aceptar -> -1 si se ha cancelado
		 */
		int status = archivo.showSaveDialog(null);

		/*
		 * Si se aceptase seleccionar el file que se va a guardar con getSelectedFile()
		 */
		if (status == 0) {
			file = archivo.getSelectedFile();
		} else {
			return;
		}

		/*
		 * Se guarda la ruta en el archivo en la variable global con el m�todo
		 * getAbsolutePath()
		 */
		if (estoyJugando) {
			this.rutaJuegos = file.getAbsolutePath();
			if (!rutaJuegos.endsWith(".txt")) {
				rutaJuegos = rutaJuegos + ".txt";
			}
		} else {
			this.ruta = file.getAbsolutePath();
			if (!ruta.endsWith(".txt")) {
				ruta = ruta + ".txt";
			}

			/* Comprobamos que tenga una extensi�n .txt -> sino se lo a�adimos */

			File aux = new File(this.ruta);

			/*
			 * Opcional comprobamos que el archivo existe con el metodo exists() -> si
			 * existe preguntar si quiere sobreescribirlo
			 */
			if (aux.exists()) {
				int respuesta = JOptionPane.showConfirmDialog(this, "�Desea sobreescribir el archivo?");

				if (respuesta != 0) {
					return;
				}
			}

			/* Llamamos a guardar */
			guardar();
		}
	}

	public void guardarSolucion() {
		if (!estoyJugando) {
			JOptionPane.showMessageDialog(this, "No se puede guardar la soluci�n sin estar jugando.");
			return;
		}
		guardarComo();

		try {
			FileWriter hoja = new FileWriter(this.rutaJuegos);
			PrintWriter boli = new PrintWriter(hoja);

			/* Recorrer el arrayList */
			int i =0;
			for (Imprimir ficha : jugadaActual) {
				if(i < jugadaActual.size()-1) {
					boli.println(ficha.toString());
				}else {
					boli.println(ficha.puntosFinales());
				}
			}

			hoja.close();
			boli.close();
			JOptionPane.showMessageDialog(this, "Soluci�n guardada.");
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(this, "Error al guardar la soluci�n.");
		}
	}

	private void cargar() {
		JFileChooser archivo = new JFileChooser();

		// con el metodo showOpenDialog(null) escogemos el archivo que queramos leer
		int status = archivo.showOpenDialog(null);

		/*
		 * Comprobamos que se haya presionado en confirmar y seleccionamos nuestro
		 * archivo
		 */
		if (status == 0) {
			this.file = archivo.getSelectedFile();
		} else {
			return;
		}

		try {
			this.ruta = this.file.getAbsolutePath();

			/* Comprobar que el archivo sea correcto */
			File aux = new File(this.ruta);
			if (!aux.exists()) {
				JOptionPane.showMessageDialog(this, "No existe el fichero como para poder cargarlo.", null,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!this.ruta.endsWith(".txt")) {
				JOptionPane.showMessageDialog(this, "Este tipo de archivo no se puede cargar..", null,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			/* Creamos una hoja de lectura pasandole el archivo obtenido */
			FileReader lectura = new FileReader(this.file);

			/* Crear bufferedReader al que le pasaremos la hoja */
			BufferedReader buffer = new BufferedReader(lectura);

			/* Crear una lista para guardar las lineas leidas del archivo */
			ArrayList<String> lista = new ArrayList<String>();

			/* Leeremos todas las lineas con el metodo readLine y a�adirlas a la lista */
			String linea = buffer.readLine();
			while (linea != null && !linea.isEmpty()) {
				lista.add(linea);
				linea = buffer.readLine();
			}

			/* Comprobar que el numero de lineas sea >0 */
			if (lista.size() <= 0) {
				return;
			}

			/* Comprobar que el numero de lineas sea <= 20 */
			if (lista.size() > 20) {
				return;
			}

			for (int i = 0; i < lista.size(); i++) {
				/* Comprobar que los caracteres de una lina sean > 0 && <= 20 */
				if (lista.get(i).length() <= 0 || lista.get(i).length() > 20) {
					JOptionPane.showMessageDialog(this, "Error al cargar el archivo 1.", null,
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				/* Comprobar que hay la misma cantidad de caracteres por fila */
				if (i != 0 && lista.get(i).length() != lista.get(i - 1).length()) {
					JOptionPane.showMessageDialog(this, "Error al cargar el archivo 2.", null,
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				for (int j = 0; j < lista.get(i).length(); j++) {
					/*
					 * Recorrer todas las lineas y analizar si contienen R, V, A -> si alguna no lo
					 * contiene se saca error y salimos
					 */
					if (lista.get(i).charAt(j) != 'R' && lista.get(i).charAt(j) != 'A'
							&& lista.get(i).charAt(j) != 'V') {
						JOptionPane.showMessageDialog(this, "Error al cargar el archivo 3.", null,
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}
			guardarTablero(lista);
			cargarTablero();

		} catch (Exception exception) {
			JOptionPane.showMessageDialog(this, "Error al cargar el archivo 4.", null, JOptionPane.ERROR_MESSAGE);
			return;
		}

	}

	private void guardarTablero(ArrayList<String> lista) {
		this.tablero = new ArrayList<>();
		for (int i = 0; i < lista.size(); i++) {
			ArrayList<Ficha> fila = new ArrayList<Ficha>();
			for (int j = 0; j < lista.get(i).length(); j++) {
				Ficha ficha;
				switch (lista.get(i).charAt(j)) {
				case 'A':
					ficha = new Ficha(Fichas.Colores.A, new Posicion(i, j));
					fila.add(ficha);
					break;
				case 'R':
					ficha = new Ficha(Fichas.Colores.R, new Posicion(i, j));
					fila.add(ficha);
					break;
				case 'V':
					ficha = new Ficha(Fichas.Colores.V, new Posicion(i, j));
					fila.add(ficha);
					break;
				}
			}
			this.tablero.add(fila);
		}
	}

	private void cargarTablero() {
		/* Elimino todo del panel2 */
		this.panel2.removeAll();

		// actualizo tablero
		this.setVisible(true);

		/* Definir nuevo gridLayout con las nuevas filas y columnas */
		GridLayout grid = new GridLayout(this.tablero.size(), this.tablero.get(0).size());
		this.panel2.setLayout(grid);

		/* Para cada elemento, mostramos en el JTextField su contenido */
		for (int i = 0; i < this.tablero.size(); i++) {
			for (int j = 0; j < this.tablero.get(0).size(); j++) {
				JTextField text = new JTextField();

				if (!this.tablero.get(i).get(j).getColor().equals(Fichas.Colores.VACIO)) {
					text.setText(this.tablero.get(i).get(j).getColor() + "");
				}
				/* A�adir el addFocus */
				addFocus(text, i, j);
				this.panel2.add(text);
			}
		}

		/* Recargar la lista */
		this.setVisible(true);
	}

	public void setJuego(boolean respuesta) {
		this.estoyJugando = respuesta;
	}

	public void setTablero(ArrayList<ArrayList<Ficha>> nuevoTablero) {
		this.tablero = nuevoTablero;
		/* Guardar una copia del tablero */
		this.copia = new ArrayList<ArrayList<Ficha>>();
		for (int i = 0; i < this.tablero.size(); i++) {
			ArrayList<Ficha> filas = new ArrayList<Ficha>();
			for (int j = 0; j < this.tablero.get(0).size(); j++) {
				Ficha ficha = new Ficha(this.tablero.get(i).get(j).getColor(), new Posicion(i, j));
				filas.add(ficha);
			}
			this.copia.add(filas);

		}
	}

	public void cargarPanelJuego() {
		/* Remove all panel2 */
		this.panel2.removeAll();

		// actualizo tablero
		this.setVisible(true);

		/* Crear gridLayout con tama�o del tablero */
		GridLayout grid = new GridLayout(this.tablero.size(), this.tablero.get(0).size());

		/* Asignar el grid al this */
		this.panel2.setLayout(grid);

		/* Para cada elemento de tablero */
		for (int i = 0; i < tablero.size(); i++) {
			for (int j = 0; j < tablero.get(0).size(); j++) {
				/* Creamos una instancia de BotonJugar */
				Ficha ficha = new Ficha(this.tablero.get(i).get(j).getColor(), new Posicion(i, j));
				BotonJuego boton = new BotonJuego(ficha);

				/* A�adir instancia al panel */
				this.panel2.add(boton);
			}
		}

		/* Recargar vista */
		this.setVisible(true);
	}

	private void comprobarFinJuegos() {
		Fichas nuevaFicha = new Fichas();
		ArrayList<Ficha> equipos = new ArrayList<Ficha>();
		equipos = nuevaFicha.getEquipos(this.tablero);

		if (equipos.size() == 0) {
			/* Hemos finalizado el juego */
			int fichasRestantes = nuevaFicha.getFichasRestantes(this.tablero, 0);

			if (fichasRestantes == 0) {
				puntos += 1000;
			}

			StringBuilder frase = new StringBuilder();
			frase.append("Has encontrado una soluci�n. Quedan " + fichasRestantes + " ficha");

			if (fichasRestantes != 1) {
				frase.append("s");
			}

			frase.append(" Has obtenido " + this.puntos + " punto");
			Imprimir imprimir = new Imprimir(fichasRestantes, puntos, -1, Fichas.Colores.VACIO, -1, -1, -1);
			this.jugadaActual.add(imprimir);
			if (this.puntos != 1) {
				frase.append("s");
			}

			frase.append(".");

			JOptionPane.showMessageDialog(this, frase.toString());
		}
	}

	private void rehacer() {
		this.posicion++;

		/* Si posicion ha llegado al tope del tama�o del historial, se sale del juego */
		if (this.posicion == this.historial.size()) {
			this.posicion--;
			JOptionPane.showMessageDialog(this, "Error. No se puede rehacer m�s.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Fichas aux = new Fichas();
		ArrayList<ArrayList<Ficha>> otraCopia = aux.clonar(tablero);
		otraCopia = historial.get(this.posicion);

		/* Por si falla */
		this.tablero = otraCopia;

		if (this.estoyJugando) {
			/* Cargar botones */
			this.cargarPanelJuego();
		} else {
			/* Cargar el JTextField */
			this.cargarTablero();
		}
	}

	private void deshacer() {
		if (this.estoyJugando) {
			Fichas aux = new Fichas();
			ArrayList<ArrayList<Ficha>> otraCopia = aux.clonar(tablero);
			this.historial.add(otraCopia);
		}

		this.posicion--;
		if (posicion == -1) {
			this.posicion++;
			JOptionPane.showMessageDialog(this, "Error. No se puede deshacer m�s.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Fichas aux = new Fichas();
		ArrayList<ArrayList<Ficha>> otraCopia = aux.clonar(tablero);
		otraCopia = historial.get(this.posicion);

		/* Por si falla */
		this.tablero = otraCopia;

		if (this.estoyJugando) {
			/* Cargar botones */
			this.cargarPanelJuego();
		} else {
			/* Cargar el JTextField */
			this.cargarTablero();
		}
	}

	public void solucionOptima() {
		this.setTablero(this.tablero);
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			protected String doInBackground() throws Exception {
				String salida = Fichas.jugar(copia);
				JOptionPane.showMessageDialog(null, salida);
				return salida;
			}
		};
		worker.execute();
	}

	enum Colores {
		R, V, A, VACIO;
	}

	private class BotonJuego extends JButton implements ActionListener {

		private Ficha ficha;
		private Fichas.Colores color;

		public BotonJuego(Ficha ficha) {
			this.ficha = ficha;
			switch (ficha.getColor()) {
			case A:
				this.setBackground(Color.BLUE);
				this.color = Fichas.Colores.A;
				break;
			case R:
				this.setBackground(Color.RED);
				this.color = Fichas.Colores.R;
				break;
			case V:
				this.setBackground(Color.GREEN);
				this.color = Fichas.Colores.V;
				break;
			default:
			}

			this.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// JOptionPane.showMessageDialog(this, "He pulsado un bot�n");

			if (this.ficha.getColor().toString().equals("VACIO")) {
				JOptionPane.showMessageDialog(this, "Error. No se puede eliminar el vacio.", null,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			Fichas aux = new Fichas();
			ArrayList<ArrayList<Ficha>> clon = new ArrayList<ArrayList<Ficha>>();
			ArrayList<ArrayList<Ficha>> otroClon = aux.clonar(tablero);

			/* Llamar al metodo getEquipo */
			Fichas nuevaFicha = new Fichas();
			clon = nuevaFicha.clonar(tablero);
			Ficha capitan = nuevaFicha.mejorFicha(clon, this.ficha.getFila(), this.ficha.getColumna());

			if (capitan == null) {
				JOptionPane.showMessageDialog(this, "Error. No se pueden eliminar fichas sin emparejar.", null,
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			historial.add(otroClon);
			posicion++;

			int fichasEliminadas = nuevaFicha.borrarFichas(tablero, this.ficha.getFila(), this.ficha.getColumna()) + 1;

			int puntuacion = (int) Math.pow(fichasEliminadas - 2, 2);
			nuevaFicha.ordenaFilas(tablero);
			nuevaFicha.ordenarPosiciones(tablero);
			StringBuilder frase = new StringBuilder();
			frase.append("Elimin� " + fichasEliminadas + " fichas de color ");
			frase.append(capitan.getColor() + " y obtuvo ");
			Imprimir imprimir = new Imprimir(fichasEliminadas, puntuacion, numMovimientos++, ficha.getColor(),
					ficha.getFila(), ficha.getColumna(), tablero.size());
			jugadaActual.add(imprimir);
			if (puntuacion != 1) {
				frase.append(puntuacion + " puntos.");
			} else {
				frase.append(puntuacion + " punto.");
			}
			puntos += puntuacion;
			JOptionPane.showMessageDialog(this, frase.toString());

			comprobarFinJuegos();
			cargarPanelJuego();
		}

	}
}