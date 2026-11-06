package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ArmaCuchillo extends Arma {
    private Texture texturaCuchillo;
    private float velocidadCuchillo = 350f;

    public ArmaCuchillo(Texture texturaCuchillo) {
        super("Cuchillo Teledirigido", 100, 2f);
        this.texturaCuchillo = texturaCuchillo; // <--- Asegúrate de guardar esta textura
    }

    @Override
    public void disparar(float jugadorX, float jugadorY, Array<enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        if (listaEnemigos == null || listaEnemigos.size == 0) return;

        enemigos enemigoMasCercano = null;
        float menorDistanciaSq = Float.MAX_VALUE;
        Vector2 posJugador = new Vector2(jugadorX, jugadorY);

        for (int i = 0; i < listaEnemigos.size; i++) {
            enemigos e = listaEnemigos.get(i);
            float distanciaSq = posJugador.dst2(e.getX(), e.getY());

            if (distanciaSq < menorDistanciaSq) {
                menorDistanciaSq = distanciaSq;
                enemigoMasCercano = e;
            }
        }

        if (enemigoMasCercano != null) {
            // AQUÍ ESTABA EL PROBLEMA: Debes pasarle 'texturaCuchillo' como primer parámetro
            Cuchillo nuevoCuchillo = new Cuchillo(texturaCuchillo, jugadorX, jugadorY, enemigoMasCercano, dano, velocidadCuchillo);
            listaProyectiles.add(nuevoCuchillo);
        }
    }
}