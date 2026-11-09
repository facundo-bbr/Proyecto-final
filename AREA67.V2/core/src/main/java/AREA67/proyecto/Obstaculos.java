package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import java.util.Random;

public class Obstaculos {
    private float x, y;
    private int vida;
    private int vidaMaxima;
    private Texture textura;
    private boolean destruido;
    private static final Random random = new Random();
    private float tiempoDesdeUltimoImpacto = 0f;
    private static final float COOLDOWN_DANIO = 0.4f;

    public Obstaculos(float x, float y, Texture textura, int vidaInicial) {
        this.x = x;
        this.y = y;
        this.textura = textura;
        this.vidaMaxima = vidaInicial;
        this.vida = vidaInicial;
        this.destruido = false;
    }

    public void actualizar(float deltaTime) {
        if (tiempoDesdeUltimoImpacto > 0) {
            tiempoDesdeUltimoImpacto -= deltaTime;
        }
    }

    // Modificamos recibirDano para respetar el cooldown
    public void recibirDano(int cantidad) {
        if (destruido) return;
        
        // Si todavía está en periodo de inmunidad, ignoramos el golpe
        if (tiempoDesdeUltimoImpacto > 0) return;

        vida -= cantidad;
        tiempoDesdeUltimoImpacto = COOLDOWN_DANIO; // Reiniciamos el tiempo de espera

        if (vida <= 0) {
            vida = 0;
            destruido = true;
        }
    }

    // Comprueba si el obstáculo ha sido destruido
    public boolean estaDestruido() {
        return destruido;
    }

    // Método que genera y suelta aleatoriamente un ObjetoSuelo al romperse
    public ObjetoSuelo soltarObjeto() {
        if (!destruido) return null;

        String[] tiposPosibles = { "GEMA_EXPERIENCIA", "CURACION", "COFRE_TESORO", "IMAN" };
        String tipoSeleccionado = tiposPosibles[random.nextInt(tiposPosibles.length)];
        
        int valorAsignado = 10;
        String varianteAsignada = "NORMAL";

        switch (tipoSeleccionado) {
            case "GEMA_EXPERIENCIA":
                // 70% de probabilidad de gema verde (baja), 30% de gema roja (alta)
                if (random.nextFloat() < 0.7f) {
                    varianteAsignada = "VERDE";
                    valorAsignado = 15; // Poca experiencia
                } else {
                    varianteAsignada = "ROJA";
                    valorAsignado = 50; // Mucha experiencia
                }
                break;
                
            case "CURACION":
                // Comida normal o comida grande
                if (random.nextFloat() < 0.6f) {
                    varianteAsignada = "POLLO";
                    valorAsignado = 20; // Cura poco
                } else {
                    varianteAsignada = "QUESO";
                    valorAsignado = 50; // Cura bastante
                }
                break;
                
            case "COFRE_TESORO":
                varianteAsignada = "UNICO";
                valorAsignado = 1;
                break;
                
            case "IMAN":
                varianteAsignada = "NORMAL";
                valorAsignado = 5; // Duración en segundos
                break;
        }

        float centroX = x + (getAncho() / 2f);
        float centroY = y + (getAlto() / 2f);

        return new ObjetoSuelo(centroX, centroY, tipoSeleccionado, valorAsignado, varianteAsignada);
    }

      

    // Rectángulo de colisiones para detectar impactos de balas
    public Rectangle getBounds() {
        return new Rectangle(x, y, getAncho(), getAlto());
    }

    public void dibujar(SpriteBatch batch) {
        if (!destruido && textura != null) {
            batch.draw(textura, x, y);
        }
    }

    // Getters y Setters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getAncho() { return textura != null ? textura.getWidth() : 32f; }
    public float getAlto() { return textura != null ? textura.getHeight() : 32f; }
}