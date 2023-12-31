import java.util.Scanner;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Fichas {

	private static ArrayList<String[]> tablero = new ArrayList<String[]>();
	private static ArrayList<ArrayList<String[]>> tableros = new ArrayList<ArrayList<String[]>>();
	private static Scanner in = new Scanner(System.in);

	public static void main(String[] argv) {
		int numJuegos = 0;

		try {
			numJuegos = Integer.parseInt(in.nextLine());

			if (numJuegos < 1) {
				System.exit(0);
			}

			/* espacio sobrante */
			if (in.nextLine().length() != 0) {
				System.exit(0);
			}

			for (int j = 0; j < numJuegos; j++) {
				/* Leer primera linea */
				String line = in.nextLine();
				tablero = new ArrayList<String[]>();
				/* Comprobamos el nº de columnas >= 1 <=20 */
				if (line.length() < 1 || line.length() >= 21) {
					throw new Exception();

				}

				while (!line.equals("")) {
					/* Creamos la fila */
					String[] filas = new String[line.length()];

					/* Recorrer la fila */
					for (int i = 0; i < line.length(); i++) {
						// filas.add(line.substring(i, i+1));
						/* Comrpobamos si el caracter es distinto de A, V, R */
						if (line.charAt(i) != 'A' && line.charAt(i) != 'R' && line.charAt(i) != 'V') {
							throw new Exception();

						}

						/* Comprueba que todas las columnas tengan el mismo tamano */
						if (tablero.size() > 0 && tablero.get(0).length != line.length()) {
							throw new Exception();

						}

						/* Convertimos a string con substring y anadimos a linas */
						filas[i] = line.substring(i, i + 1);
					}
					tablero.add(filas);

					if (tablero.size() > 20) {
						/* Se salio del limite posible del tablero */ throw new Exception();

					}
					if(!in.hasNextLine()) {
						break;
					}
					// filas.removeAll(filas);
					line = in.nextLine();

					/* Comrpobamos si las comlumnas son mayores a 20 */
					if (line.length() >= 21) {
						throw new Exception();
					}
				}

				tableros.add(tablero);
			}
		} catch (Exception a) {
		}
		jugar(tableros);

	}

	/*
	 * Enum que nos determina todos los movimientos posibles que podemos realizar a
	 * lo largo del tablero
	 */
	enum Movimientos {
		ARRIBA(-1, 0), ABAJO(1, 0), IZQUIERDA(0, -1), DERECHA(0, 1);

		private int fila;
		private int columna;

		private Movimientos(int fila, int columna) {
			this.fila = fila;
			this.columna = columna;
		}

		public int getFila() {
			return this.fila;
		}

		public int getColumna() {
			return this.columna;
		}
	}

	enum Colores {
		R, V, A, VACIO;
	}

	/* Metodo para comprobar que no nos salimos del tablero de juego */
	private static boolean noSalir(int fila, int columna, ArrayList<ArrayList<Ficha>> ficha) {

		// con ficha.get(0).size() mido la longitud
		// con ficha.size() mido la profundidad

		if ((fila < 0) || (columna < 0) || (fila >= ficha.size()) || (columna >= ficha.get(0).size())) {
			return false;
		}

		else {
			return true;
		}
	}

	/*
	 * Metodo para contar cuantas posiciones tenemos con fichas del mismo color
	 * contigüas
	 */
	public static int cuentaEquipo(ArrayList<ArrayList<Ficha>> tablero, int fila, int columna) {
		int movimientosPosibles = 0;
		/* Puedo moverme */
		for (Movimientos movimiento : Movimientos.values()) {
			int filaNueva = fila + movimiento.getFila();
			int columnaNueva = columna + movimiento.getColumna();
			tablero.get(fila).get(columna).setTocada(true);

			/* Compruebo si tras moverme sigo dentro del array */
			if (noSalir(filaNueva, columnaNueva, tablero)) {

				/* Comprobamos que el color sea el mismo y sumamos 1 */
				if (tablero.get(fila).get(columna).getColor()
						.equals(tablero.get(filaNueva).get(columnaNueva).getColor())) {

					/* Comprobamos que no ha pasado ya por esa posicion */
					if (!tablero.get(filaNueva).get(columnaNueva).isTocada()) {
						movimientosPosibles++;
						movimientosPosibles += cuentaEquipo(tablero, filaNueva, columnaNueva);
					}
				}
			}
		}

		return movimientosPosibles;
	}

	/*  */
	public static ArrayList<ArrayList<Ficha>> convertirStringAFicha(ArrayList<String[]> tablero) {
		ArrayList<ArrayList<Ficha>> tableros = new ArrayList<ArrayList<Ficha>>();

		for (int i = 0; i < tablero.size(); i++) {
			ArrayList<Ficha> filaFichas = new ArrayList<Ficha>();

			for (int j = 0; j < tablero.get(i).length; j++) {
				/* Creo una ficha */
				Ficha ficha = null;
				Posicion posicion = new Posicion(i, j);
				switch (tablero.get(i)[j].charAt(0)) {
				case 'A':
					ficha = new Ficha(Colores.A, posicion);
					break;
				case 'V':
					ficha = new Ficha(Colores.V, posicion);
					break;
				case 'R':
					ficha = new Ficha(Colores.R, posicion);
					break;
				}

				if (ficha == null) {
					System.exit(0);
				}

				filaFichas.add(ficha);
			}
			tableros.add(filaFichas);
		}

		return tableros;
	}

	public static void jugar(ArrayList<ArrayList<String[]>> tableros) {
		ArrayList<ArrayList<Imprimir>> solucionesFinales = new ArrayList<>();
		for (ArrayList<String[]> tablero : tableros) {
			ArrayList<ArrayList<Ficha>> tableroDeFichas = convertirStringAFicha(tablero);
			ArrayList<ArrayList<Imprimir>> soluciones = new ArrayList<>();
			ArrayList<Imprimir> solucion = new ArrayList<>();
			Fichas f = new Fichas();
			f.jugar(tableroDeFichas, soluciones, 0, solucion, 1);
			solucionesFinales.add(f.mejorSolucion(soluciones));
		}
		int x = 0;
		for (ArrayList<Imprimir> solucion : solucionesFinales) {
			System.out.println("Juego "+(x+1)+":");
			for (int i = 0; i < solucion.size(); i++) {
				
				if (i < solucion.size() - 1) {
					System.out.println(solucion.get(i).toString());

				} else {
					System.out.println(solucion.get(i).puntosFinales());

				}
			}
			x++;
			if (x < solucionesFinales.size()) {
				System.out.println();
			}
		}
	}

	private ArrayList<Imprimir> mejorSolucion(ArrayList<ArrayList<Imprimir>> soluciones) {
		ArrayList<Imprimir> mejor = soluciones.get(0);
		for (ArrayList<Imprimir> solucion : soluciones) {

			for (int i = 0; i < Math.min(mejor.size(), solucion.size()); i++) {
				if (mejor.get(mejor.size() - 1).getPuntos() < solucion.get(solucion.size() - 1).getPuntos()) {
					mejor=solucion;
					break;
				}else if (mejor.get(mejor.size() - 1).getPuntos() == solucion.get(solucion.size() - 1).getPuntos()) {
					if (mejor.get(i).getFila() >= solucion.get(i).getFila()
							|| (mejor.get(i).getFila() == solucion.get(i).getColumna()
									&& mejor.get(i).getColumna() > solucion.get(i).getColumna())) {
						mejor = solucion;
						break;
					}
				}
			}
		}
		return mejor;
	}

	/*
	 * public ArrayList<Ficha> getFichaRelevante(ArrayList<ArrayList<Ficha>>
	 * tablero, Movimientos movimientos){
	 * 
	 * Ficha fichaMasRelevanteEquipo = null; for(ArrayList<Ficha> fila : tablero){
	 * ArrayList<ArrayList<Ficha>> aux = new ArrayList<ArrayList<Ficha>> ();
	 * 
	 * for(Ficha ficha : fila){ if(noSalir(ficha.getFila(), ficha.getColumna(),
	 * tablero)){
	 * 
	 * } } } return null; }
	 */
	public ArrayList<ArrayList<Ficha>> clonar(ArrayList<ArrayList<Ficha>> tablero) {
		ArrayList<ArrayList<Ficha>> nuevo = new ArrayList<ArrayList<Ficha>>();
		int i = -1;

		for (ArrayList<Ficha> fila : tablero) {
			ArrayList<Ficha> filaC = new ArrayList<>();
			i += 1;
			for (int j = 0; j < tablero.get(0).size(); j++) {
				Posicion posicion = new Posicion(i, j);
				Ficha ficha = new Ficha(fila.get(j).getColor(), posicion);
				filaC.add(ficha);
			}
			nuevo.add(filaC);
		}

		return nuevo;
	}

	public ArrayList<Ficha> getEquipos(ArrayList<ArrayList<Ficha>> tablero) {
		ArrayList<Ficha> posiciones = new ArrayList<Ficha>();
		ArrayList<Ficha> usada = new ArrayList<Ficha>();
		for (int i = 0; i < tablero.size(); i++) {
			for (int j = 0; j < tablero.get(0).size(); j++) {
				if (tablero.get(i).get(j).getColor() != Colores.VACIO && !usada.contains(tablero.get(i).get(j))) {
					/* Obtenemos las fichas de nuestro juego */
					ArrayList<Ficha> posEquipo = new ArrayList<Ficha>();
					getEquipo(tablero, i, j, posEquipo);
					usada.addAll(posEquipo);
					/* Si esta en posEquipo */
					if (posEquipo.size() > 1) { /* Asi comprobamos que tenga más de una ficha */
						/* Seleccionar la mejor ficha de cada equipo */
						Ficha mejorFicha = posEquipo.get(0); /* Asumimos que es la primera para empezar la busqueda */

						/*
						 * Recorro las posiciones del equipo para ver si hay una mejor ficha (izq,
						 * abajo)
						 */
						for (int k = 0; k < posEquipo.size(); k++) {
							if ((mejorFicha.getFila() < posEquipo.get(k).getFila())
									|| (mejorFicha.getFila() == posEquipo.get(k).getFila()
											&& mejorFicha.getColumna() > posEquipo.get(k).getColumna())) {
								mejorFicha = posEquipo.get(k);
							}
						}
						/* Añadimos la mejor ficha */
						posiciones.add(mejorFicha);
					}
				}

			}
		}
		return posiciones;
	}

	public ArrayList<Ficha> getEquipo(ArrayList<ArrayList<Ficha>> tablero, int i, int j, ArrayList<Ficha> posEquipo) {
		posEquipo.add(tablero.get(i).get(j));

		for (Movimientos movimientos : Movimientos.values()) {
			int filaNueva = i + movimientos.getFila();
			int comlumnaNueva = j + movimientos.getColumna();
			if (noSalir(filaNueva, comlumnaNueva, tablero)) {

				/* Comprobamos el color */
				if (tablero.get(i).get(j).getColor().equals(tablero.get(filaNueva).get(comlumnaNueva).getColor())) {
					/* Validar si está en posEquipo */
					if (!posEquipo.contains(tablero.get(filaNueva).get(comlumnaNueva))) {
						getEquipo(tablero, filaNueva, comlumnaNueva, posEquipo);
					}
				}
			}
		}

		return posEquipo;
	}

	public void bajarPosiciones(ArrayList<ArrayList<Ficha>> tablero) {
		/* Recorremos por el final del tablero */
		for (int i = tablero.size() - 1; i >= 0; i--) {
			for (int j = tablero.get(0).size() - 1; j >= 0; j--) {
				/* si nos encontramos con una posicion vacía */
				if ((tablero.get(i).get(j).getColor() == Colores.VACIO)) {
					Ficha ficha = null;

					/*
					 * Recorremos para ver si tiene alguna ficha por encima de esa posicion vacia
					 * que debamos intercambiar
					 */
					for (int k = i - 1; k >= 0; k--) {
						if (tablero.get(k).get(j).getColor() != Colores.VACIO) {
							/* Añadimos el color y la posicion a la ficha */
							ficha = new Ficha(tablero.get(i).get(j).getColor(), new Posicion(i, j));

							/* Quitamos la ficha que estba arriba */
							tablero.get(i).get(k).setColor(Colores.VACIO);

							break;
						}
					}
					/* Añadimos la nueva ficha intercambiada a su nueva posicon mas abajo */
					tablero.get(i).set(j, ficha);
				}
			}
		}
	}

	public void ordenaFilas(ArrayList<ArrayList<Ficha>> tablero) {
		for (int i = tablero.size() - 1; i >= 0; i--) {
			for (int j = tablero.get(0).size() - 1; j >= 0; j--) {
				if (tablero.get(i).get(j).getColor().equals(Colores.VACIO)) {
					for (int k = i - 1; k >= 0; k--) {
						if (!tablero.get(k).get(j).getColor().equals(Colores.VACIO)) {
							tablero.get(i).get(j).setColor(tablero.get(k).get(j).getColor());
							tablero.get(i).get(j).setTocada(false);
							tablero.get(k).get(j).setColor(Colores.VACIO);
							break;
						}
					}
				}
			}
		}
	}

	public void ordenarPosiciones(ArrayList<ArrayList<Ficha>> tablero) {
		// ordenar columnas
		for (int j = 0; j < tablero.get(0).size(); j++) {
			boolean columnaVacia = true;
			for (int i = 0; i < tablero.size(); i++) {
				Ficha ficha = tablero.get(i).get(j);
				if (ficha.getColor() != Colores.VACIO) {
					columnaVacia = false;
				}
			}

			if (columnaVacia && j < tablero.get(0).size() - 1) {
				/* Buscar columna sustituta */
				for (int k = j + 1; k < tablero.get(0).size(); k++) {
					columnaVacia = true;
					for (int i = 0; i < tablero.size(); i++) {
						Ficha ficha = tablero.get(i).get(k);
						if (ficha.getColor() != Colores.VACIO) {
							columnaVacia = false;
						}
					}

					if (!columnaVacia) {
						for (int i = 0; i < tablero.size(); i++) {
							Ficha auxFicha = new Ficha(tablero.get(i).get(k).getColor(), new Posicion(i, k));
							tablero.get(i).set(j, auxFicha);
							tablero.get(i).get(k).setColor(Colores.VACIO);

						}
						break;
					}
				}
			}
		}

	}

	public int borrarFichas(ArrayList<ArrayList<Ficha>> tablero, int fila, int columna) {
		int fichasEliminadas = 0;
		Colores colores = tablero.get(fila).get(columna).getColor();
		tablero.get(fila).get(columna).setColor(Colores.VACIO);

		for (Movimientos movimiento : Movimientos.values()) {
			/* POnemos la posicion en la que estamos para no volver a caer en ella */

			/*
			 * fila += movimiento.getFila(); columna+= movimiento.getColumna();
			 */

			if (noSalir(fila + movimiento.getFila(), columna + movimiento.getColumna(), tablero)) {
				/* Comprobamos que la ficha anterior es del mismo color que la ficha actual */
				if (tablero.get(fila + movimiento.getFila()).get(columna + movimiento.getColumna()).getColor()
						.equals(colores)) {
					fichasEliminadas++;
					fichasEliminadas += borrarFichas(tablero, fila + movimiento.getFila(),
							columna + movimiento.getColumna());
				}
			}
		}

		return fichasEliminadas;
	}

	public void jugar(ArrayList<ArrayList<Ficha>> tablero, ArrayList<ArrayList<Imprimir>> soluciones, int puntos,
			ArrayList<Imprimir> texto, int movimientos) {
		ArrayList<ArrayList<Ficha>> copiaTablero = clonar(tablero);

		ArrayList<Ficha> capitanes = getEquipos(copiaTablero);

		int eliminados = 0;

		for (Ficha ficha : capitanes) {
			copiaTablero = clonar(tablero);
			eliminados = 1 + borrarFichas(copiaTablero, ficha.getFila(), ficha.getColumna());
			ordenaFilas(copiaTablero);
			ordenarPosiciones(copiaTablero);

			int auxPuntos = (int) Math.pow(eliminados - 2, 2);

			Imprimir imprimir = new Imprimir(eliminados, auxPuntos, movimientos, ficha.getColor(), ficha.getFila(),
					ficha.getColumna(), copiaTablero.size());
			ArrayList<Imprimir> cp = new ArrayList<>();
			cp.addAll(texto);
			cp.add(imprimir);

			jugar(copiaTablero, soluciones, puntos + auxPuntos, cp, movimientos + 1);
		}

		if (capitanes.size() == 0) {
			if (isEmpty(tablero)) {
				puntos += 1000;
			}
			eliminados = getFichasRestantes(tablero, 0);
			Imprimir imprimir = new Imprimir(eliminados, puntos, -1, Colores.VACIO, -1, -1, -1);
			ArrayList<Imprimir> cp = new ArrayList<>();
			cp.addAll(texto);
			cp.add(imprimir);
			soluciones.add(cp);
		}
	}

	public int getFichasRestantes(ArrayList<ArrayList<Ficha>> tablero, int restantes) {
		for (ArrayList<Ficha> fila : tablero) {
			for (Ficha ficha : fila) {
				if (ficha.getColor() != Colores.VACIO) {
					restantes++;
				}
			}
		}

		return restantes;
	}

	public boolean isEmpty(ArrayList<ArrayList<Ficha>> tablero) {
		for (ArrayList<Ficha> fila : tablero) {
			for (Ficha ficha : fila) {
				if (ficha.getColor() != (Colores.VACIO)) {
					return false;
				}
			}
		}

		return true;
	}
}