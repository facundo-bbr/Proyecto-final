package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Fusil extends Arma {
    private Texture texturaBala; 
    private float velocidadBala = 400f; 
    private Vector2 direccionActual; 

    public Fusil(Texture texturaBala) {
        super("Fusil de Asalto", 50, 0.8f); 
        this.texturaBala = texturaBala;
        this.direccionActual = new Vector2(1, 0); 
    }

   
    public void setDireccion(Vector2 nuevaDireccion) {
        if (nuevaDireccion.len2() > 0) {
            this.direccionActual.set(nuevaDireccion).nor();
        }
    }

    @Override
    public void disparar(float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
       
        Vector2 posicionObjetivo = new Vector2(jugadorX, jugadorY).add(new Vector2(direccionActual).scl(1000f));

       
        listaProyectiles.add(new bala(texturaBala, jugadorX, jugadorY, posicionObjetivo, dano, velocidadBala));
    }
}