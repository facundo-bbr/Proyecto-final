package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ArmaCuchillo extends Arma {
    private Texture texturaCuchillo;
    private float velocidadCuchillo = 350f;

    public ArmaCuchillo(Texture texturaCuchillo) {
        super("Cuchillo Teledirigido", 100, 1.5f); // 15 de daño base, 1.5 segundos de cadencia inicial
        this.texturaCuchillo = texturaCuchillo;
    }

    // Método especializado que respeta el temporizador del padre y evalúa jefes + enemigos
    public void actualizarConJefes(float deltaTime, float jugadorX, float jugadorY, float jugadorAncho, float jugadorAlto, Array<Enemigos> listaEnemigos, Array<Jefe> listaJefes, Array<Proyectil> listaProyectiles) {
        // 1. Usamos la lógica de tiempo exacta heredada de la clase padre Arma
        tiempoRestante -= deltaTime;

        if (tiempoRestante <= 0) {
            // 2. Ejecutamos el disparo buscando al objetivo más cercano (jefe o enemigo)
            dispararConJefesLogica(jugadorX, jugadorY, jugadorAncho, jugadorAlto, listaEnemigos, listaJefes, listaProyectiles);
            
            // 3. Reiniciamos el temporizador usando la cadencia (que se adapta si el arma sube de nivel)
            tiempoRestante = cadenciaAtaque; 
        }
    }

    // Lógica interna para buscar al blanco más cercano (priorizando jefes si están en rango o son los más próximos)
    private void dispararConJefesLogica(float jugadorX, float jugadorY, float jugadorAncho, float jugadorAlto, Array<Enemigos> listaEnemigos, Array<Jefe> listaJefes, Array<Proyectil> listaProyectiles) {
        boolean hayEnemigos = (listaEnemigos != null && listaEnemigos.size > 0);
        boolean hayJefes = (listaJefes != null && listaJefes.size > 0);

        if (!hayEnemigos && !hayJefes) return; // Si no hay absolutamente nada, no gasta el disparo

        Object objetivoMasCercano = null;
        float menorDistanciaSq = Float.MAX_VALUE;
        Vector2 posJugador = new Vector2(jugadorX, jugadorY);

        // 1. Buscar entre los Jefes
        if (hayJefes) {
            for (int i = 0; i < listaJefes.size; i++) {
                Jefe jefe = listaJefes.get(i);
                if (jefe != null && !jefe.estaMuerto()) {
                    float centroJefeX = jefe.getX() + (jefe.getBounds().width / 2f);
                    float centroJefeY = jefe.getY() + (jefe.getBounds().height / 2f);
                    
                    float distanciaSq = posJugador.dst2(centroJefeX, centroJefeY);
                    if (distanciaSq < menorDistanciaSq) {
                        menorDistanciaSq = distanciaSq;
                        objetivoMasCercano = jefe;
                    }
                }
            }
        }

        // 2. Buscar entre los Enemigos comunes
        if (hayEnemigos) {
            for (int i = 0; i < listaEnemigos.size; i++) {
                Enemigos e = listaEnemigos.get(i);
                if (e != null) {
                    float centroEnemigoX = e.getX() + (e.getBounds().width / 2f);
                    float centroEnemigoY = e.getY() + (e.getBounds().height / 2f);
                    
                    float distanciaSq = posJugador.dst2(centroEnemigoX, centroEnemigoY);
                    if (distanciaSq < menorDistanciaSq) {
                        menorDistanciaSq = distanciaSq;
                        objetivoMasCercano = e;
                    }
                }
            }
        }

        // 3. Si encuentra un objetivo, crea el cuchillo y lo lanza al centro de su pecho
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

            Cuchillo nuevoCuchillo = new Cuchillo(texturaCuchillo, origenX, origenY, objetivoMasCercano, dano, velocidadCuchillo);
            listaProyectiles.add(nuevoCuchillo);
        }
    }

    // Método obligatorio heredado de la clase abstracta Arma (puente de compatibilidad)
    @Override
    public void disparar(float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        // Toda la lógica principal de búsqueda y jefes pasa por actualizarConJefes
    }
}