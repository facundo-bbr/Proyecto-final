package AREA67.proyecto;

import com.badlogic.gdx.math.Vector2;

public class ObjetoSuelo {
    private Vector2 posicion;
    private boolean recogido;
    private String tipo; // Ej: "GEMA_EXPERIENCIA", "POLLO_CURACION", "COFRE_TESORO", "IMAN"
    private int valor;   // Cantidad de experiencia, vida a curar o segundos de duración

    public ObjetoSuelo(float x, float y, String tipo, int valor) {
        this.posicion = new Vector2(x, y);
        this.recogido = false;
        this.tipo = tipo;
        this.valor = valor;
    }

    // Métodos requeridos por la clase Recolector
    public boolean estaRecogido() {
        return recogido;
    }

    public void marcarComoRecogido() {
        this.recogido = true;
    }

    public String getTipo() {
        return tipo;
    }

    public int getValor() {
        return valor;
    }

    public float getX() {
        return posicion.x;
    }

    public float getY() {
        return posicion.y;
    }
}