package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Molotov extends Arma {
    private Texture texturaBotella;

    public Molotov(Texture texturaBotella) {
        super("Molotov", 30, 4.0f); // 40 de daño base, dispara cada 4 segundos
        this.texturaBotella = texturaBotella;
    }

    @Override
    public void disparar(float jugadorX, float jugadorY, Array<enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        if (listaEnemigos.size == 0) return; // Si no hay enemigos, no gasta el molotov

        // 1. Buscar al enemigo más cercano
        enemigos enemigoMasCercano = null;
        float menorDistancia = Float.MAX_VALUE;

        for (enemigos e : listaEnemigos) {
            float centroEnemigoX = e.getX() + (e.getBounds().width / 2f);
            float centroEnemigoY = e.getY() + (e.getBounds().height / 2f);
            
            float distancia = Vector2.dst(jugadorX, jugadorY, centroEnemigoX, centroEnemigoY);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                enemigoMasCercano = e;
            }
        }

        // 2. Si encuentra un enemigo, lanza la botella hacia su posición actual
        if (enemigoMasCercano != null) {
            float objetivoX = enemigoMasCercano.getX() + (enemigoMasCercano.getBounds().width / 2f);
            float objetivoY = enemigoMasCercano.getY() + (enemigoMasCercano.getBounds().height / 2f);

            // Crear el proyectil y añadirlo a la lista general del juego
            ProyectilMolotov molotov = new ProyectilMolotov(jugadorX, jugadorY, objetivoX, objetivoY, dano, texturaBotella);
            listaProyectiles.add(molotov);
        }
    }
}