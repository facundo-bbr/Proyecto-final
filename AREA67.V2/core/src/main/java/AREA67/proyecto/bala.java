package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class bala {
    private Vector2 posicion;
    private Vector2 velocidad; // Guardará el vector de desplazamiento por segundo
    private Texture texturaBala;
    private int dano;
    private float tiempoDeVida; 
    private boolean activa;
    private float anguloRotacion;
    
    public bala(Texture textura, float origenX, float origenY, Vector2 posicionObjetivo, int danoArma, float velocidadBala) {
        this.texturaBala = textura;
        this.posicion = new Vector2(origenX, origenY);
        this.dano = danoArma;
        this.tiempoDeVida = 3.0f; // 3 segundos de vida máxima
        this.activa = true;

        // --- CÁLCULO CORRECTO DE LA DIRECCIÓN HACIA EL ENEMIGO ---
        // 1. Creamos un vector que va desde el origen hasta el objetivo
        Vector2 direccion = new Vector2(posicionObjetivo).sub(posicion);
        
        // 2. Normalizamos la dirección (para que mida exactamente 1 de longitud) y la escalamos por la velocidad
        if (direccion.len() > 0) {
        	this.anguloRotacion = direccion.angleDeg();
            direccion.nor().scl(velocidadBala);
        }
        
        // 3. Guardamos esto como la velocidad constante con la que se moverá la bala
        this.velocidad = direccion;
    }

    public void actualizar(float deltaTime) {
        if (!activa) return;

        // Mover la bala sumando la velocidad calculada por el deltaTime
        posicion.add(velocidad.x * deltaTime, velocidad.y * deltaTime);

        // Reducir tiempo de vida
        tiempoDeVida -= deltaTime;
        if (tiempoDeVida <= 0) {
            activa = false;
        }
    }

    public void dibujar(SpriteBatch batch) {
        if (!activa) return;

        float ancho = texturaBala.getWidth();
        float alto = texturaBala.getHeight();

        // Dibujamos la bala rotada desde su centro exacto
        batch.draw(texturaBala, 
                   posicion.x - (ancho / 2f), posicion.y - (alto / 2f), // Posición X e Y (centrada)
                   ancho / 2f, alto / 2f,                             // Origen/Pivote (el centro de la bala)
                   ancho, alto,                                       // Ancho y Alto
                   1f, 1f,                                            // Escala X e Y
                   anguloRotacion,                                    // <--- Ángulo de inclinación calculado
                   0, 0,                                              // Coordenadas de la textura rect (srcX, srcY)
                   (int) ancho, (int) alto,                           // Tamaños src
                   false, false                                       // Flip X e Y
        );
    }

    public Rectangle getBounds() {
        return new Rectangle(posicion.x - 8, posicion.y - 8, 16, 16);
    }

    public int getDano() {
        return dano;
    }

    public boolean estaActiva() {
        return activa;
    }
    
}