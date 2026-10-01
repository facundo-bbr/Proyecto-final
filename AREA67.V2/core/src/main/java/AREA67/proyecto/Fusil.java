package AREA67.proyecto;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;

public class Fusil extends Arma {
    private float rangoVision; // Distancia máxima a la que el fusil detecta enemigos

    public Fusil() {
        // Asignamos: Nombre, Daño base (18), y Cadencia rápida (0.4 segundos entre disparos)
        super("Fusil de Asalto", 18, 0.4f);
        this.rangoVision = 450f; 
    }

    @Override
    public void disparar(float jugadorX, float jugadorY, Array<enemigos> listaEnemigos) {
        if (listaEnemigos == null || listaEnemigos.size == 0) {
            return;
        }

        enemigos enemigoMasCercano = null;
        float menorDistancia = Float.MAX_VALUE;

        // 1. Recorremos la lista para encontrar al enemigo más próximo
        for (enemigos enemigo : listaEnemigos) {
            float distancia = Vector2.dst(jugadorX, jugadorY, enemigo.getX(), enemigo.getY());

            // Verificamos si es el más cercano y si está dentro del campo de visión del arma
            if (distancia < menorDistancia && distancia <= rangoVision) {
                menorDistancia = distancia;
                enemigoMasCercano = enemigo;
            }
        }

        // 2. Si encontró un objetivo válido, ejecuta el disparo automático hacia él
        if (enemigoMasCercano != null) {
            System.out.println(nombre + " disparó automáticamente al enemigo más cercano (Distancia: " + menorDistancia + ")");
            
            // Si en tu clase 'enemigos' agregas un método para recibir daño, lo harías así:
            // enemigoMasCercano.recibirDano(dano);
            
        } else {
            // El arma igual se dispara sola por el temporizador, pero al aire si no hay nadie cerca
            System.out.println(nombre + " realizó un disparo automático al vacío (sin objetivos en rango).");
        }
    }
}