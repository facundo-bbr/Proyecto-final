package AREA67.proyecto;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public abstract class Proyectil {
    protected float x, y;
    protected boolean activo;
    protected int dano;

    public Proyectil(float x, float y, int dano) {
        this.x = x;
        this.y = y;
        this.dano = dano;
        this.activo = true;
    }

    public abstract void actualizar(float deltaTime, Array<Enemigos> listaEnemigos);
    public abstract void dibujar(SpriteBatch batch);
    public abstract Rectangle getBounds();

    public boolean isActivo() {
        return activo;
    }

    public int getDano() {
        return dano;
    }
    
    public void destruir() {
        this.activo = false; // Desactiva el proyectil para que el bucle principal lo elimine de la lista
    }
}