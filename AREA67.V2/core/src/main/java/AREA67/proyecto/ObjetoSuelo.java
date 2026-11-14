package AREA67.proyecto;

import com.badlogic.gdx.math.Vector2;

public class ObjetoSuelo {
    private Vector2 posicion;
    private boolean recogido;
    private String tipo; 
    private int valor;   
    private String variante;

    public ObjetoSuelo(float x, float y, String tipo, int valor,String variante) {
        this.posicion = new Vector2(x, y);
        this.recogido = false;
        this.tipo = tipo;
        this.valor = valor;
        this.variante = variante;
    }

    public boolean estaRecogido() { return recogido; }
    public void marcarComoRecogido() { this.recogido = true; }
    public String getTipo() { return tipo; }
    public int getValor() { return valor; }
    public String getVariante() { return variante; }
    public float getX() { return posicion.x; }
    public float getY() { return posicion.y; }
    public void setPosicion(float x, float y) { this.posicion.set(x, y); }


}
