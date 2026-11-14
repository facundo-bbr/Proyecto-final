package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Molotov extends Arma {
    private Texture texturaBotella;

    public Molotov(Texture texturaBotella) {
        super("Molotov", 30, 4.0f); 
        this.texturaBotella = texturaBotella;
    }

    public void actualizarConJefes(float deltaTime, float jugadorX, float jugadorY, float jugadorAncho, float jugadorAlto, Array<Enemigos> listaEnemigos, Array<Jefe> listaJefes, Array<Proyectil> listaProyectiles) {
        tiempoRestante -= deltaTime;

        if (tiempoRestante <= 0) {
            dispararConJefesLogica(jugadorX, jugadorY, jugadorAncho, jugadorAlto, listaEnemigos, listaJefes, listaProyectiles);
            
            tiempoRestante = cadenciaAtaque; 
        }
    }

    private void dispararConJefesLogica(float jugadorX, float jugadorY, float jugadorAncho, float jugadorAlto, Array<Enemigos> listaEnemigos, Array<Jefe> listaJefes, Array<Proyectil> listaProyectiles) {
        boolean hayEnemigos = (listaEnemigos != null && listaEnemigos.size > 0);
        boolean hayJefes = (listaJefes != null && listaJefes.size > 0);

        if (!hayEnemigos && !hayJefes) return; 

        Object objetivoMasCercano = null;
        float menorDistancia = Float.MAX_VALUE;
        Vector2 posJugador = new Vector2(jugadorX, jugadorY);

        if (hayJefes) {
            for (Jefe jefe : listaJefes) {
                if (jefe != null && !jefe.estaMuerto()) {
                    float centroJefeX = jefe.getX() + (jefe.getBounds().width / 2f);
                    float centroJefeY = jefe.getY() + (jefe.getBounds().height / 2f);
                    
                    float distancia = posJugador.dst(centroJefeX, centroJefeY);
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        objetivoMasCercano = jefe;
                    }
                }
            }
        }

        if (hayEnemigos) {
            for (Enemigos e : listaEnemigos) {
                if (e != null) {
                    float centroEnemigoX = e.getX() + (e.getBounds().width / 2f);
                    float centroEnemigoY = e.getY() + (e.getBounds().height / 2f);
                    
                    float distancia = posJugador.dst(centroEnemigoX, centroEnemigoY);
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        objetivoMasCercano = e;
                    }
                }
            }
        }

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

            float origenX = jugadorX + (jugadorAncho / 2f);
            float origenY = jugadorY + (jugadorAlto / 2f);

            ProyectilMolotov molotov = new ProyectilMolotov(origenX, origenY, objetivoX, objetivoY, dano, texturaBotella);
            listaProyectiles.add(molotov);
        }
    }

    @Override
    public void disparar(float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
    }
}