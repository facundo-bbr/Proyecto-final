package AREA67.proyecto;

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
    
    private float duracionFuego = 3.0f; 
    private float radioFuego = 70f;   
    private Texture texturaBotella;
    private Rectangle bounds;

    public ProyectilMolotov(float origenX, float origenY, float destinoX, float destinoY, int dano, Texture texturaBotella) {
        super(origenX, origenY, dano);
        this.x = origenX; 
        this.y = origenY;
        this.texturaBotella = texturaBotella;
        this.destinoX = destinoX;
        this.destinoY = destinoY;
        
        this.bounds = new Rectangle(x, y, 24, 24);

        Vector2 direccion = new Vector2(destinoX - x, destinoY - y);
        float distanciaTotal = direccion.len();
        
        if (distanciaTotal == 0) {
            this.velocidad = new Vector2(0, 0);
            this.haImpactado = true; 
        } else {
            direccion.nor();
            float velocidadViaje = 400f; 
            this.velocidad = new Vector2(direccion.x * velocidadViaje, direccion.y * velocidadViaje);
        }
    }
    

    public void actualizarConJefes(float deltaTime, Array<Enemigos> listaEnemigos, Array<Jefe> listaJefes) {
        if (!activo) return;

        if (!haImpactado) {
            x += velocidad.x * deltaTime;
            y += velocidad.y * deltaTime;
            bounds.setPosition(x, y);

            if (Vector2.dst(x, y, destinoX, destinoY) < 20f) {
                haImpactado = true;
                x = destinoX;
                y = destinoY;
                
                bounds.set(x - radioFuego, y - radioFuego, radioFuego * 2, radioFuego * 2);
                System.out.println("¡IMPACTO LOGRADO! Fuego activo en el suelo.");
            }
        } else {
            duracionFuego -= deltaTime;
            if (duracionFuego <= 0) {
                activo = false; 
            } else {
                
                if (listaEnemigos != null) {
                    for (Enemigos e : listaEnemigos) {
                        if (e != null && !e.estaMuerto()) {
                            float centroEnemigoX = e.getX() + (e.getBounds().width / 2f);
                            float centroEnemigoY = e.getY() + (e.getBounds().height / 2f);
                            
                            if (Vector2.dst(x, y, centroEnemigoX, centroEnemigoY) <= radioFuego) {
                                e.recibirDano(dano); 
                            }
                        }
                    }
                }

                if (listaJefes != null) {
                    for (Jefe jefe : listaJefes) {
                        if (jefe != null && !jefe.estaMuerto()) {
                        	
                            float centroJefeX = jefe.getX() + (jefe.getBounds().width / 2f);
                            float centroJefeY = jefe.getY() + (jefe.getBounds().height / 2f);
                            
                            if (Vector2.dst(x, y, centroJefeX, centroJefeY) <= radioFuego) {
                                jefe.recibirDano(dano); 
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public void actualizar(float deltaTime, Array<Enemigos> listaEnemigos) {
        actualizarConJefes(deltaTime, listaEnemigos, null);
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        if (!activo) return;

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
            shapeRenderer.setColor(1f, 0.3f, 0.0f, 0.15f);
            shapeRenderer.circle(x, y, radioFuego);
        }
    }
}