package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Molotov extends Arma {
    private Texture texturaBotella;

    public Molotov(Texture texturaBotella) {
        super("Molotov", 30, 4.0f); // 30 de daño base, 4.0 segundos de cadencia definidos en el padre
        this.texturaBotella = texturaBotella;
    }

    // Método especializado que actualiza el cooldown del padre y evalúa enemigos + jefes
    public void actualizarConJefes(float deltaTime, float jugadorX, float jugadorY, float jugadorAncho, float jugadorAlto, Array<Enemigos> listaEnemigos, Array<Jefe> listaJefes, Array<Proyectil> listaProyectiles) {
        // 1. Usamos la lógica de tiempo exacta de la clase padre Arma
        tiempoRestante -= deltaTime;

        if (tiempoRestante <= 0) {
            // 2. Ejecutamos el disparo buscando al más cercano (jefe o enemigo)
            dispararConJefesLogica(jugadorX, jugadorY, jugadorAncho, jugadorAlto, listaEnemigos, listaJefes, listaProyectiles);
            
            // 3. Reiniciamos el temporizador usando la cadencia (que puede modificarse si se mejora el arma)
            tiempoRestante = cadenciaAtaque; 
        }
    }

    // Lógica interna para buscar al objetivo más cercano (priorizando jefe o enemigo)
    private void dispararConJefesLogica(float jugadorX, float jugadorY, float jugadorAncho, float jugadorAlto, Array<Enemigos> listaEnemigos, Array<Jefe> listaJefes, Array<Proyectil> listaProyectiles) {
        boolean hayEnemigos = (listaEnemigos != null && listaEnemigos.size > 0);
        boolean hayJefes = (listaJefes != null && listaJefes.size > 0);

        if (!hayEnemigos && !hayJefes) return; // Si no hay nada, no gasta el disparo

        Object objetivoMasCercano = null;
        float menorDistancia = Float.MAX_VALUE;
        Vector2 posJugador = new Vector2(jugadorX, jugadorY);

        // Buscar entre los Jefes
        if (hayJefes) {
            for (Jefe jefe : listaJefes) {
                if (jefe != null && !jefe.estaMuerto()) {
                    float centroJefeX = jefe.getX() + (jefe.getBounds().width / 2f);
                    float centroJefeY = jefe.getY() + (jefe.getBounds().height / 2f);
                    
                    float distancia = posJugador.dst(centroJefeX, centroJefeY);
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        objetivoMasCercano = jefe;
                    }
                }
            }
        }

        // Buscar entre los Enemigos comunes
        if (hayEnemigos) {
            for (Enemigos e : listaEnemigos) {
                if (e != null) {
                    float centroEnemigoX = e.getX() + (e.getBounds().width / 2f);
                    float centroEnemigoY = e.getY() + (e.getBounds().height / 2f);
                    
                    float distancia = posJugador.dst(centroEnemigoX, centroEnemigoY);
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        objetivoMasCercano = e;
                    }
                }
            }
        }

        // Si encuentra un objetivo, lanza la botella al centro de su pecho
        if (objetivoMasCercano != null) {
            float objetivoX = 0;
            float objetivoY = 0;

            if (objetivoMasCercano instanceof Jefe) {
                Jefe j = (Jefe) objetivoMasCercano;
                objetivoX = j.getX() + (j.getBounds().width / 2f);
                objetivoY = j.getY() + (j.getBounds().height / 2f);
            } else if (objetivoMasCercano instanceof Enemigos) {
                Enemigos e = (Enemigos) objetivoMasCercano;
                objetivoX = e.getX() + (e.getBounds().width / 2f);
                objetivoY = e.getY() + (e.getBounds().height / 2f);
            }

            // Origen desde el centro (pecho) del jugador
            float origenX = jugadorX + (jugadorAncho / 2f);
            float origenY = jugadorY + (jugadorAlto / 2f);

            ProyectilMolotov molotov = new ProyectilMolotov(origenX, origenY, objetivoX, objetivoY, dano, texturaBotella);
            listaProyectiles.add(molotov);
        }
    }

    // Método obligatorio heredado de la clase abstracta Arma (por si se usa en un bucle genérico sin jefes)
    @Override
    public void disparar(float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        // Método puente estándar (puedes dejarlo vacío si toda la lógica pasa por actualizarConJefes)
    }
}