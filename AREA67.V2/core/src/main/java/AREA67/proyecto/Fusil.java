package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Fusil extends Arma {
    private float rangoVision;
    private Texture texturaBala; // ¡Importante! Necesitamos la textura de la bala aquí
    private float velocidadBala = 400f; // Velocidad de la bala en píxeles por segundo

    // NUEVO CONSTRUCTOR: Ahora recibe la textura de la bala
    public Fusil(Texture texturaBala) {
        super("Fusil de Asalto", 50, 0.8f);
        this.rangoVision = 450f;
        this.texturaBala = texturaBala;
    }

    @Override
    public void disparar(float jugadorX, float jugadorY, Array<enemigos> listaEnemigos, Array<bala> listaBalas) {
        if (listaEnemigos == null || listaEnemigos.size == 0) {
            return;
        }

        enemigos enemigoMasCercano = null;
        float menorDistancia = Float.MAX_VALUE;

        // 1. Encontrar al enemigo más cercano en rango
        for (enemigos enemigo : listaEnemigos) {
            float distancia = Vector2.dst(jugadorX, jugadorY, enemigo.getX(), enemigo.getY());
            if (distancia < menorDistancia && distancia <= rangoVision) {
                menorDistancia = distancia;
                enemigoMasCercano = enemigo;
            }
        }

        // 2. Si hay un enemigo cercano, creamos la bala apuntando HACIA ÉL
        if (enemigoMasCercano != null) {
            // Obtenemos el centro del enemigo (o su posición exacta)
            // Ojo: si enemigo.getX() y getY() devuelven la esquina inferior izquierda, 
            // sumarle la mitad del ancho/alto hace que apunte al centro exacto del zombi:
            float enemigoCentroX = enemigoMasCercano.getX() + (enemigoMasCercano.getBounds().width / 2f);
            float enemigoCentroY = enemigoMasCercano.getY() + (enemigoMasCercano.getBounds().height / 2f);

            // Creamos el vector dirección con las coordenadas del centro del enemigo
            Vector2 direccion = new Vector2(enemigoCentroX, enemigoCentroY);
            
            // Creamos la bala pasándole el origen del jugador y la dirección calculada
            listaBalas.add(new bala(texturaBala, jugadorX, jugadorY, direccion, dano, velocidadBala));
        }
    }

	}