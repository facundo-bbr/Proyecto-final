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

    
    private boolean activoCiclo = true; 
    private float tiempoActivo = 3.0f;    
    private float cooldown = 5.0f;        
    private float temporizadorCiclo = 0f; 

    public BolaPinchos(Texture texturaBola) {
        super("Bola de Pinchos", 120, 0f); 
        this.texturaBola = texturaBola;
        this.angulo = 0f;
    }

    @Override
    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        
        temporizadorCiclo += deltaTime;

        if (activoCiclo) {
           
            angulo -= velocidadGiro * deltaTime;
            if (angulo < 0) angulo += 360f;

            float rad = (float) Math.toRadians(angulo);
            xActual = jugadorX + (float) Math.cos(rad) * radioOrbita;
            yActual = jugadorY + (float) Math.sin(rad) * radioOrbita;

           
            if (listaEnemigos != null) {
                Rectangle boundsBola = getBounds();
                for (int i = 0; i < listaEnemigos.size; i++) {
                    Enemigos enemigo = listaEnemigos.get(i);
                    if (boundsBola.overlaps(enemigo.getBounds())) {
                        enemigo.recibirDano(dano);
                    }
                }
            }

           
            if (temporizadorCiclo >= tiempoActivo) {
                activoCiclo = false;
                temporizadorCiclo = 0f; 
            }

        } else {


            if (temporizadorCiclo >= cooldown) {
                activoCiclo = true;
                temporizadorCiclo = 0f; 
            }
        }
    }

    public void dibujar(SpriteBatch batch) {

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
    public void disparar(float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        
    }
}