package AREA67.proyecto;

import com.badlogic.gdx.utils.Array;

public abstract class Arma {
    protected String nombre;
    protected int dano;
    protected float cadenciaAtaque; // Tiempo en segundos entre cada disparo automático
    protected float tiempoRestante;  // Temporizador interno

    public Arma(String nombre, int dano, float cadenciaAtaque) {
        this.nombre = nombre;
        this.dano = dano;
        this.cadenciaAtaque = cadenciaAtaque;
        this.tiempoRestante = cadenciaAtaque;
    }

    // El bucle principal actualiza el reloj de todas las armas constantemente
    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Array<enemigos> listaEnemigos) {
        tiempoRestante -= deltaTime;

        // El ataque se ejecuta de forma automática al agotarse el tiempo, 
        // sin importar si hay o no enemigos en el rango.
        if (tiempoRestante <= 0) {
            disparar(jugadorX, jugadorY, listaEnemigos);
            tiempoRestante = cadenciaAtaque; // Reinicia el ciclo automático
        }
    }

    // Método abstracto: Cada variedad de arma definirá su propia lógica de ataque aquí
    public abstract void disparar(float jugadorX, float jugadorY, Array<enemigos> listaEnemigos);

    // --- GETTERS Y MEJORAS ---
    public String getNombre() {
        return nombre;
    }

    public int getDano() {
        return dano;
    }

    public void mejorarDano(int incremento) {
        this.dano += incremento;
    }
}