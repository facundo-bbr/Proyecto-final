package AREA67.proyecto;

import com.badlogic.gdx.utils.Array;

public abstract class Arma {
    protected String nombre;
    protected int dano;
    protected float cadenciaAtaque;
    protected float tiempoRestante;

    public Arma(String nombre, int dano, float cadenciaAtaque) {
        this.nombre = nombre;
        this.dano = dano;
        this.cadenciaAtaque = cadenciaAtaque;
        this.tiempoRestante = cadenciaAtaque;
    }

    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Array<enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        tiempoRestante -= deltaTime;

        if (tiempoRestante <= 0) {
            disparar(jugadorX, jugadorY, listaEnemigos, listaProyectiles);
            tiempoRestante = cadenciaAtaque; 
        }
    }

    // Ahora acepta cualquier proyectil que herede de Proyectil
    public abstract void disparar(float jugadorX, float jugadorY, Array<enemigos> listaEnemigos, Array<Proyectil> listaProyectiles);

    // Getters y setters...
    public String getNombre() { return nombre; }
    public int getDano() { return dano; }
    public void mejorarDano(int incremento) { this.dano += incremento; }
    public float getCadenciaAtaque() { return cadenciaAtaque; }
    public float getTiempoRestante() { return tiempoRestante; }
    public void setCadenciaAtaque(float cadenciaAtaque) { this.cadenciaAtaque = cadenciaAtaque; }
}