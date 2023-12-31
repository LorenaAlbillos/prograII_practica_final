public class Imprimir {

    private int numFichasEliminadas;
    private int puntos;
    private int fila, columna;
    private int movimiento;
    private Fichas.Colores color;
    private int size;


    public Imprimir(int fichasEliminadas, int puntos, int movimiento, Fichas.Colores colores, int fila, int columna, int size){
        this.numFichasEliminadas = fichasEliminadas;
        this.puntos = puntos;
        this.movimiento = movimiento;
        this.color = colores;
        this.fila = fila;
        this.columna = columna;
        this.size=size;
    }

    public int getNumFichasEliminadas(){ return this.numFichasEliminadas; }

    public int getPuntos(){ return this.puntos; }

    public int getMovimiento(){ return this.movimiento; }

    public Fichas.Colores getColor(){ return this.color; }

    public int getFila(){ return this.fila; }

    public int getColumna(){ return this.columna; }

    public String toString(){
        StringBuilder cadena = new StringBuilder();

        cadena.append("Movimiento " + this.movimiento + " en (" + (this.size-this.fila) + ", " + (this.columna+1) + "): ");
        cadena.append("eliminó " + this.numFichasEliminadas + " fichas de color " + this.color);
        cadena.append(" y obtuvo ");

        if(this.puntos != 1){
            cadena.append(this.puntos + " puntos.");
        } else{
            cadena.append(this.puntos + " punto.");
        }
        return cadena.toString();
    }

	public String puntosFinales() {
		// TODO Auto-generated method stub
		StringBuilder cadena = new StringBuilder("Puntuación final: "+this.puntos+"");
		cadena.append(", quedando "+this.numFichasEliminadas+" ficha");
		if(this.numFichasEliminadas!= 1) {
			cadena.append("s");
		}
		cadena.append(".");
		
		return cadena.toString();
	}
}
