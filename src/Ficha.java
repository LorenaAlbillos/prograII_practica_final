/* Clase particular de la ficha */

public class Ficha {
    enum Colores{
        R, V, A, VACIO;
    }

    private Fichas.Colores color;
    private Posicion posicion;
    private boolean tocada;

    public Ficha(Fichas.Colores a, Posicion posicion){
        this.color = a;
        this.posicion = posicion;
        this.tocada = false;
    }

    public int getPosicion(){ return this.posicion.getPosicion(); }

    public Fichas.Colores getColor() { return this.color; }

    public int getFila() { return posicion.getFila(); }

    public int getColumna() { return posicion.getColumna();}

    public void setTocada(boolean si){
        if(si) { this.tocada = true; } 
        else { this.tocada = false; }
    }

    public void setColor(Fichas.Colores color){
        this.color = color;
    }

    public boolean isTocada(){ return this.tocada; }

    public boolean equals(Object o){
        if(o instanceof Ficha){
            Ficha ficha = (Ficha) o;
            return ficha.getFila() == this.getFila() && ficha.getColumna() == this.getColumna();
        }

        else{ return false; }
    }
}
