package AREA67.proyecto;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ProyectilMolotov extends Proyectil {
    private Vector2 velocidad;
    private float destinoX, destinoY;
    private boolean haImpactado = false;
    
    // Propiedades del fuego en el suelo
    private float duracionFuego = 3.0f; // El fuego dura 3 segundos en el suelo
    private float radioFuego = 70f;   // Radio de daño de la zona de fuego
    private Texture texturaBotella;
    private Rectangle bounds;

    public ProyectilMolotov(float origenX, float origenY, float destinoX, float destinoY, int dano, Texture texturaBotella) {
        super(origenX, origenY, dano);
        this.x = origenX; // Aseguramos las coordenadas iniciales del padre
        this.y = origenY;
        this.texturaBotella = texturaBotella;
        this.destinoX = destinoX;
        this.destinoY = destinoY;
        
        this.bounds = new Rectangle(x, y, 24, 24);

        // Vector dirección hacia el destino
        Vector2 direccion = new Vector2(destinoX - x, destinoY - y);
        float distanciaTotal = direccion.len();
        
        if (distanciaTotal == 0) {
            this.velocidad = new Vector2(0, 0);
            this.haImpactado = true; // Si nació en el mismo lugar, impacta de inmediato
        } else {
            direccion.nor();
            float velocidadViaje = 400f; // Velocidad de vuelo de la botella
            this.velocidad = new Vector2(direccion.x * velocidadViaje, direccion.y * velocidadViaje);
        }
    }

    @Override

    public void actualizar(float deltaTime, Array<enemigos> listaEnemigos) {
        if (!activo) return;

        if (!haImpactado) {
            // Mover la botella en vuelo
            x += velocidad.x * deltaTime;
            y += velocidad.y * deltaTime;
            bounds.setPosition(x, y);

            // Comprobar si llegó al destino
            if (Vector2.dst(x, y, destinoX, destinoY) < 20f) {
                haImpactado = true;
                x = destinoX;
                y = destinoY;
                
                // ¡IMPORTANTE! Ampliamos el bounds al tamaño exacto del área de fuego (círculo de 90 de radio = 180 de diámetro)
                bounds.set(x - radioFuego, y - radioFuego, radioFuego * 2, radioFuego * 2);
                System.out.println("¡IMPACTO LOGRADO! Fuego activo en el suelo.");
            }
        } else {
            // El fuego quema durante 3 segundos en el suelo
            duracionFuego -= deltaTime;
            if (duracionFuego <= 0) {
                activo = false; // Desactiva el proyectil para limpiarlo
            } else {
                // Hacer daño continuo a los enemigos que se encuentren dentro del radio de fuego
                for (enemigos e : listaEnemigos) {
                    if (e != null && !e.estaMuerto()) {
                        float centroEnemigoX = e.getX() + (e.getBounds().width / 2f);
                        float centroEnemigoY = e.getY() + (e.getBounds().height / 2f);
                        
                        // Si la distancia entre el centro del fuego y el centro del enemigo es menor o igual al radio
                        if (Vector2.dst(x, y, centroEnemigoX, centroEnemigoY) <= radioFuego) {
                            e.recibirDano(dano); // Aplica el daño configurado del arma por frame o puedes ajustarlo
                        }
                    }
                }
            }
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activo) return;

        // Dibuja la botella solo mientras vuela
        if (!haImpactado && texturaBotella != null) {
            batch.draw(texturaBotella, x, y, bounds.width, bounds.height);
        }
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    public void dibujarFuegoVisual(ShapeRenderer shapeRenderer) {
        if (activo && haImpactado) {
            // Color Naranja/Fuego con transparencia (Rojo=1f, Verde=0.4f, Azul=0f, Alfa=0.45f)
        	shapeRenderer.setColor(1f, 0.3f, 0.0f, 0.15f);
            shapeRenderer.circle(x, y, radioFuego);
        }
    }
}