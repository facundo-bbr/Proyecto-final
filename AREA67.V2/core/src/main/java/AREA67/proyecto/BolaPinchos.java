package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class BolaPinchos extends Arma {
    private Texture texturaBola;
    private float angulo; 
    private float radioOrbita = 75f; 
    private float velocidadGiro = 180f; 
    private float xActual, yActual; 

    // --- NUEVAS VARIABLES DE CONTROL DE TIEMPO ---
    private boolean activoCiclo = true; // Si está girando actualmente
    private float tiempoActivo = 3.0f;    // Cuánto tiempo dura girando (segundos)
    private float cooldown = 5.0f;        // Tiempo que pasa oculta antes de volver a salir
    private float temporizadorCiclo = 0f; // Reloj interno del ciclo

    public BolaPinchos(Texture texturaBola) {
        super("Bola de Pinchos", 120, 0f); 
        this.texturaBola = texturaBola;
        this.angulo = 0f;
    }

    @Override
    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Array<enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        // Actualizamos el temporizador del ciclo
        temporizadorCiclo += deltaTime;

        if (activoCiclo) {
            // --- FASE 1: GIRANDO Y DAÑANDO ---
            angulo -= velocidadGiro * deltaTime;
            if (angulo < 0) angulo += 360f;

            float rad = (float) Math.toRadians(angulo);
            xActual = jugadorX + (float) Math.cos(rad) * radioOrbita;
            yActual = jugadorY + (float) Math.sin(rad) * radioOrbita;

            // Comprobar colisiones mientras está activa
            if (listaEnemigos != null) {
                Rectangle boundsBola = getBounds();
                for (int i = 0; i < listaEnemigos.size; i++) {
                    enemigos enemigo = listaEnemigos.get(i);
                    if (boundsBola.overlaps(enemigo.getBounds())) {
                        enemigo.recibirDano(dano);
                    }
                }
            }

            // Si se acabó el tiempo activo, pasa a modo cooldown (desaparece)
            if (temporizadorCiclo >= tiempoActivo) {
                activoCiclo = false;
                temporizadorCiclo = 0f; // Reiniciamos reloj para el cooldown
            }

        } else {
            // --- FASE 2: EN COOLDOWN (DESAPARECIDA) ---
            // Aquí la bola no se dibuja ni hace daño

            // Si se acabó el tiempo de espera, vuelve a aparecer
            if (temporizadorCiclo >= cooldown) {
                activoCiclo = true;
                temporizadorCiclo = 0f; // Reiniciamos reloj para el tiempo activo
            }
        }
    }

    public void dibujar(SpriteBatch batch) {
        // Solo se dibuja si el ciclo está activo
        if (activoCiclo && texturaBola != null) {
            float ancho = texturaBola.getWidth();
            float alto = texturaBola.getHeight();
            batch.draw(texturaBola, xActual - (ancho / 2f), yActual - (alto / 2f));
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(xActual - 16, yActual - 16, 32, 32);
    }

    @Override
    public void disparar(float jugadorX, float jugadorY, Array<enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        // No aplica para armas de órbita
    }
}