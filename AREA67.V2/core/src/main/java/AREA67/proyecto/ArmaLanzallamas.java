package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ArmaLanzallamas extends Arma {
    private int cantidadProyectilesPorDisparo;
    private float aperturaCono;
    private Texture texturaFuego;
    private Vector2 direccionActual; 
    private boolean enRafaga = false;
    private int proyectilesDisparadosEnRafaga = 0;
    private float tiempoEntreProyectiles = 0.06f; 
    private float temporizadorRafaga = 0f;
    private float jugadorXActual, jugadorYActual;
    private float anguloBaseRafaga; 
    private Array<Proyectil> proyectilesActuales;
    private final int proyectilesPorRafaga = 3;
    private int proyectilesTotalesLanzados = 0;
    
    public ArmaLanzallamas(Texture texturaFuego) {
        super("Lanzallamas", 40, 3f);
        this.cantidadProyectilesPorDisparo = 6; 
        this.aperturaCono = 10f; 
        this.texturaFuego = texturaFuego;
        this.direccionActual = new Vector2(1, 0); 
    }

    public void setDireccion(Vector2 nuevaDireccion) {
        if (nuevaDireccion.len2() > 0) {
            this.direccionActual.set(nuevaDireccion).nor();
        }
    }

    @Override
    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        tiempoRestante -= deltaTime;

        if (tiempoRestante <= 0 && !enRafaga) {
            disparar(jugadorX, jugadorY, listaEnemigos, listaProyectiles);
            tiempoRestante = cadenciaAtaque; 
        }

        if (enRafaga) {
            temporizadorRafaga -= deltaTime;
            if (temporizadorRafaga <= 0) {
                lanzarSiguienteProyectilDeRafaga();
                temporizadorRafaga = tiempoEntreProyectiles;
            }
        }
    }

    @Override
    public void disparar(float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        this.jugadorXActual = jugadorX;
        this.jugadorYActual = jugadorY;
        this.proyectilesActuales = listaProyectiles;
        
        this.anguloBaseRafaga = direccionActual.angleDeg();

        this.enRafaga = true;
        this.proyectilesDisparadosEnRafaga = 0; 
        this.proyectilesTotalesLanzados = 0;    
        this.temporizadorRafaga = 0f;           
    }

    private void lanzarSiguienteProyectilDeRafaga() {
        if (proyectilesTotalesLanzados >= cantidadProyectilesPorDisparo) {
            enRafaga = false; 
            return;
        }

        float velocidadFuego = 300f;

        int indiceEnLaRafaga = proyectilesDisparadosEnRafaga % proyectilesPorRafaga;
        float offset = 0;
        
        if (proyectilesPorRafaga > 1) {
            float mitadCono = aperturaCono / 2f;
            float paso = aperturaCono / (proyectilesPorRafaga - 1);
            offset = mitadCono - (paso * indiceEnLaRafaga);
        }

        float anguloFinal = anguloBaseRafaga + offset;

        Vector2 direccionCono = new Vector2(MathUtils.cosDeg(anguloFinal), MathUtils.sinDeg(anguloFinal));
        Vector2 posicionObjetivo = new Vector2(jugadorXActual, jugadorYActual).add(direccionCono.scl(1000f));

        
        ProyectilLlamarada fuego = new ProyectilLlamarada(
            texturaFuego, 
            jugadorXActual, 
            jugadorYActual, 
            posicionObjetivo, 
            this.dano, 
            velocidadFuego
        );
        
        proyectilesActuales.add(fuego);

        
        proyectilesDisparadosEnRafaga++;
        proyectilesTotalesLanzados++;

        
        if (proyectilesDisparadosEnRafaga >= proyectilesPorRafaga) {
            proyectilesDisparadosEnRafaga = 0; 
        }

        
        if (proyectilesTotalesLanzados >= cantidadProyectilesPorDisparo) {
            enRafaga = false;
        }
    }
}