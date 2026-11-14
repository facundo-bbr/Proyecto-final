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
    

    private Object objetivo;

    public Cuchillo(Texture textura, float origenX, float origenY, Object objetivo, int danoArma, float velocidadCuchillo) {
        super(origenX, origenY, danoArma);
        this.texturaCuchillo = textura;
        this.objetivo = objetivo;
        this.velocidadMovimiento = velocidadCuchillo;
        this.velocidad = new Vector2();
        this.tiempoDeVida = 4.0f; 
    }


    public void actualizarConJefes(float deltaTime, Array<Enemigos> listaEnemigos, Array<Jefe> listaJefes) {
        if (!activo) return;

      
        boolean objetivoValido = false;
        float objX = 0;
        float objY = 0;

        if (objetivo instanceof Jefe) {
            Jefe j = (Jefe) objetivo;
            if (j != null && !j.estaMuerto() && (listaJefes == null || listaJefes.contains(j, true))) {
                objetivoValido = true;
              
                objX = j.getX() + (j.getBounds().width / 2f);
                objY = j.getY() + (j.getBounds().height / 2f);
            }
        } else if (objetivo instanceof Enemigos) {
            Enemigos e = (Enemigos) objetivo;
            if (e != null && listaEnemigos != null && listaEnemigos.contains(e, true)) {
                objetivoValido = true;
                

                if (e.getBounds() != null) {
                    objX = e.getBounds().x + (e.getBounds().width / 2f);
                    objY = e.getBounds().y + (e.getBounds().height / 2f);
                } else {

                    objX = e.getX(); 
                    objY = e.getY() + 30f; 
                }
            }
        }

        if (!objetivoValido) {
            activo = false;
            return;
        }

        Vector2 posObjetivo = new Vector2(objX, objY);
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
    }


    @Override
    public void actualizar(float deltaTime, Array<Enemigos> listaEnemigos) {
        actualizarConJefes(deltaTime, listaEnemigos, null);
    }

    @Override
    public void dibujar(com.badlogic.gdx.graphics.g2d.SpriteBatch batch) {
        if (!activo) return;

        float ancho = texturaCuchillo.getWidth();
        float alto = texturaCuchillo.getHeight();

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