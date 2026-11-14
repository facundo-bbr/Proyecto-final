package AREA67.proyecto;

import com.badlogic.gdx.utils.Array;

public abstract class Arma {
    protected String nombre;
    protected int dano;
    protected float cadenciaAtaque;
    protected float tiempoRestante;
    private int contadorMejorasArma = 0;

    public Arma(String nombre, int dano, float cadenciaAtaque) {
        this.nombre = nombre;
        this.dano = dano;
        this.cadenciaAtaque = cadenciaAtaque;
        this.tiempoRestante = cadenciaAtaque;
    }

    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
        tiempoRestante -= deltaTime;

        if (tiempoRestante <= 0) {
            disparar(jugadorX, jugadorY, listaEnemigos, listaProyectiles);
            tiempoRestante = cadenciaAtaque; 
        }
    }

    public abstract void disparar(float jugadorX, float jugadorY, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles);

    public String getNombre() { return nombre; }
    public int getDano() { return dano; }
    public void mejorarDano(int incremento) { this.dano += incremento; }
    public float getCadenciaAtaque() { return cadenciaAtaque; }
    public float getTiempoRestante() { return tiempoRestante; }
    public void setCadenciaAtaque(float cadenciaAtaque) { this.cadenciaAtaque = cadenciaAtaque; }
    public void aplicarMejoraProgresiva() {
        contadorMejorasArma++;
        
        if (contadorMejorasArma <= 3) {
            this.dano += 15;
            System.out.println("¡Arma mejorada! Daño aumentado a: " + this.dano);
        } else if (contadorMejorasArma <= 5) {
            this.cadenciaAtaque -= 0.15f;
            if (this.cadenciaAtaque < 0.1f) {
                this.cadenciaAtaque = 0.1f;
            }
            System.out.println("¡Arma mejorada! Cooldown reducido a: " + this.cadenciaAtaque);
        } else {
            this.dano += 5;
            System.out.println("¡Arma mejorada al máximo nivel! Daño extra: " + this.dano);
        }
    }
}