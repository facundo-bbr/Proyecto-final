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
    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Array<enemigos> listaEnemigos, Array<bala> listaBalas) {
        tiempoRestante -= deltaTime;

        if (tiempoRestante <= 0) {
            disparar(jugadorX, jugadorY, listaEnemigos, listaBalas); // Le pasamos la lista de balas aquí también
            tiempoRestante = cadenciaAtaque; 
        }
    }

 // En Arma.java (debe verse exactamente así, sin llaves):
    public abstract void disparar(float jugadorX, float jugadorY, Array<enemigos> listaEnemigos, Array<bala> listaBalas); // --- GETTERS Y MEJORAS ---
    public String getNombre() {
        return nombre;
    }

    public int getDano() {
        return dano;
    }

    public void mejorarDano(int incremento) {
        this.dano += incremento;
    }
    public float getCadenciaAtaque() {
        return cadenciaAtaque;
    }

    public float getTiempoRestante() {
        return tiempoRestante;
    }

    public void setCadenciaAtaque(float cadenciaAtaque) {
        this.cadenciaAtaque = cadenciaAtaque;
    }
}