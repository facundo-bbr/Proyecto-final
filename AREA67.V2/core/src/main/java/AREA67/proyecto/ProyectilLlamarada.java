package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ProyectilLlamarada extends Proyectil {
    private Vector2 velocidad;
    private Texture texturaFuego;
    private float tiempoDeVida;
    private float escalaActual;
    private float escalaMaxima;

    public ProyectilLlamarada(Texture textura, float origenX, float origenY, Vector2 posicionObjetivo, int danoArma, float velocidadFuego) {
        super(origenX, origenY, danoArma);
        this.texturaFuego = textura;
        this.tiempoDeVida = 1f; // Duración antes de disiparse
        this.escalaActual = 0.4f;  // Empieza pequeño
        this.escalaMaxima = 3.0f;  // Termina grande

        Vector2 posicionVector = new Vector2(origenX, origenY);
        Vector2 direccion = new Vector2(posicionObjetivo).sub(posicionVector);
        
        if (direccion.len() > 0) {
            direccion.nor().scl(velocidadFuego);
        }
        this.velocidad = direccion;
    }

    @Override
    public void actualizar(float deltaTime, Array<enemigos> listaEnemigos) {
        if (!activo) return;

        // Movimiento idéntico al de la clase bala
        x += velocidad.x * deltaTime;
        y += velocidad.y * deltaTime;

        // Reducir tiempo de vida
        tiempoDeVida -= deltaTime;
        
        // A mayor tiempo transcurrido (o distancia recorrida), mayor escala visual
        float progreso = 1.0f - (tiempoDeVida / 1.5f); // Va de 0 a 1
        escalaActual = 0.4f + (progreso * (escalaMaxima - 0.4f));

        if (tiempoDeVida <= 0) {
            activo = false;
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activo) return;

        float anchoBase = texturaFuego.getWidth();
        float altoBase = texturaFuego.getHeight();
        
        float anchoEscalado = anchoBase * escalaActual;
        float altoEscalado = altoBase * escalaActual;

        // Dibujamos la textura escalada de forma fluida
        batch.draw(texturaFuego, 
                x - (anchoEscalado / 2f), y - (altoEscalado / 2f), 
                anchoEscalado / 2f, altoEscalado / 2f,                     
                anchoEscalado, altoEscalado,                         
                1f, 1f,                                     
                0f,                                         
                0, 0,                                       
                (int) anchoBase, (int) altoBase,                     
                false, false                                
        );
    }

    @Override
    public Rectangle getBounds() {
        // El rectángulo de colisión crece dinámicamente con la escala del proyectil
        float size = 16 * escalaActual;
        return new Rectangle(x - (size / 2f), y - (size / 2f), size, size);
    }
}