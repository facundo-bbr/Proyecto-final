package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Cuchillo extends Proyectil {
    private Texture texturaCuchillo;
    private Vector2 velocidad;
    private float velocidadMovimiento;
    private float anguloRotacion;
    private float tiempoDeVida;
    private enemigos objetivo;

    // Modificamos el constructor para que reciba la textura del PNG
    public Cuchillo(Texture textura, float origenX, float origenY, enemigos objetivo, int danoArma, float velocidadCuchillo) {
        super(origenX, origenY, danoArma);
        this.texturaCuchillo = textura; // Guardamos la textura
        this.objetivo = objetivo;
        this.velocidadMovimiento = velocidadCuchillo;
        this.velocidad = new Vector2();
        this.tiempoDeVida = 4.0f; 
    }

    @Override
    public void actualizar(float deltaTime, Array<enemigos> listaEnemigos) {
        if (!activo) return;

        if (objetivo == null || !listaEnemigos.contains(objetivo, true)) {
            activo = false;
            return;
        }

        Vector2 posObjetivo = new Vector2(objetivo.getX(), objetivo.getY());
        Vector2 direccion = posObjetivo.sub(x, y);

        if (direccion.len() > 0) {
            anguloRotacion = direccion.angleDeg();
            velocidad = direccion.nor().scl(velocidadMovimiento);
        }

        x += velocidad.x * deltaTime;
        y += velocidad.y * deltaTime;

        tiempoDeVida -= deltaTime;
        if (tiempoDeVida <= 0) {
            activo = false;
        }
        
        // ELIMINAMOS el "if (getBounds().overlaps(objetivo.getBounds()))" de aquí.
        // Ahora dejamos que la clase Principal se encargue de detectar el choque y aplicar el daño.
    }

    @Override
    public void dibujar(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        if (!activo) return;

        float ancho = texturaCuchillo.getWidth();
        float alto = texturaCuchillo.getHeight();

        // Dibuja el PNG del cuchillo rotando hacia el enemigo
        batch.draw(texturaCuchillo, 
                x - (ancho / 2f), y - (alto / 2f), 
                ancho / 2f, alto / 2f,                      
                ancho, alto,                                 
                1f, 1f,                                     
                anguloRotacion,                              
                0, 0,                                      
                (int) ancho, (int) alto,                     
                false, false                                 
        );
    }

    @Override
    public com.badlogic.gdx.math.Rectangle getBounds() {
        return new com.badlogic.gdx.math.Rectangle(x - 8, y - 8, 16, 16);
    }
}