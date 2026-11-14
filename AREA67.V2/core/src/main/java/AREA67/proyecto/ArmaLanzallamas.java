package AREA67.proyecto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ArmaLanzallamas extends Arma {
    private int cantidadProyectilesPorDisparo;
    private float aperturaCono;
    private Texture texturaFuego;
    private Vector2 direccionActual; // Guarda hacia dónde apunta el lanzallamas

    // Variables para controlar la ráfaga secuencial
    private boolean enRafaga = false;
    private int proyectilesDisparadosEnRafaga = 0;
    private float tiempoEntreProyectiles = 0.06f; // Tiempo entre cada bolita de fuego
    private float temporizadorRafaga = 0f;
    private float jugadorXActual, jugadorYActual;
    private float anguloBaseRafaga; 
    private Array<Proyectil> proyectilesActuales;
    private final int proyectilesPorRafaga = 3;
    private int proyectilesTotalesLanzados = 0;
    
    public ArmaLanzallamas(Texture texturaFuego) {
        super("Lanzallamas", 40, 3f);
        this.cantidadProyectilesPorDisparo = 6; // Ráfagas de 3 proyectiles
        this.aperturaCono = 10f; // Grados de apertura del cono
        this.texturaFuego = texturaFuego;
        this.direccionActual = new Vector2(1, 0); // Por defecto a la derecha
    }

    // Método para que el personaje actualice hacia dónde apunta el lanzallamas (igual que el fusil)
    public void setDireccion(Vector2 nuevaDireccion) {
        if (nuevaDireccion.len2() > 0) {
            this.direccionActual.set(nuevaDireccion).nor();
        }
    }

    @Override
    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        // Actualizamos el temporizador general del arma de la clase padre
        tiempoRestante -= deltaTime;

        // Si el tiempo llegó a 0 y no estamos disparando una ráfaga, iniciamos una nueva
        if (tiempoRestante <= 0 && !enRafaga) {
            disparar(jugadorX, jugadorY, listaEnemigos, listaProyectiles);
            tiempoRestante = cadenciaAtaque; 
        }

        // Si hay una ráfaga en curso, gestionamos el envío secuencial de proyectiles
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
        
        // Obtenemos el ángulo base basado exactamente en la dirección actual del jugador
        this.anguloBaseRafaga = direccionActual.angleDeg();

        // Iniciamos la ráfaga
        this.enRafaga = true;
        this.proyectilesDisparadosEnRafaga = 0; // Cuántos van en esta mini-tanda de 3
        this.proyectilesTotalesLanzados = 0;    // Cuántos llevamos en total en este disparo de arma
        this.temporizadorRafaga = 0f;           // Disparar el primero inmediatamente
    }

    private void lanzarSiguienteProyectilDeRafaga() {
        // Si ya completamos la cantidad total de proyectiles que debe soltar esta arma por disparo, cerramos
        if (proyectilesTotalesLanzados >= cantidadProyectilesPorDisparo) {
            enRafaga = false; 
            return;
        }

        float velocidadFuego = 300f;

        // Calculamos el offset siempre basándonos en un bloque de 3 proyectiles (índice 0, 1 y 2)
        int indiceEnLaRafaga = proyectilesDisparadosEnRafaga % proyectilesPorRafaga;
        float offset = 0;
        
        if (proyectilesPorRafaga > 1) {
            float mitadCono = aperturaCono / 2f;
            float paso = aperturaCono / (proyectilesPorRafaga - 1);
            // Esto asegura que el índice 0 sea arriba (+mitadCono), el 1 en medio, y el 2 abajo (-mitadCono)
            offset = mitadCono - (paso * indiceEnLaRafaga);
        }

        float anguloFinal = anguloBaseRafaga + offset;

        // Definimos 'posicionObjetivo'
        Vector2 direccionCono = new Vector2(MathUtils.cosDeg(anguloFinal), MathUtils.sinDeg(anguloFinal));
        Vector2 posicionObjetivo = new Vector2(jugadorXActual, jugadorYActual).add(direccionCono.scl(1000f));

        // Creamos el proyectil
        ProyectilLlamarada fuego = new ProyectilLlamarada(
            texturaFuego, 
            jugadorXActual, 
            jugadorYActual, 
            posicionObjetivo, 
            this.dano, 
            velocidadFuego
        );
        
        proyectilesActuales.add(fuego);

        // Incrementamos contadores
        proyectilesDisparadosEnRafaga++;
        proyectilesTotalesLanzados++;

        // Si ya lanzamos los 3 de esta tanda, reiniciamos el contador interno de la ráfaga para que vuelva a empezar desde arriba
        if (proyectilesDisparadosEnRafaga >= proyectilesPorRafaga) {
            proyectilesDisparadosEnRafaga = 0; 
        }

        // Si ya alcanzamos el total configurado en el arma, detenemos la ráfaga por completo
        if (proyectilesTotalesLanzados >= cantidadProyectilesPorDisparo) {
            enRafaga = false;
        }
    }
}