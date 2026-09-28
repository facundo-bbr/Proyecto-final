package AREA67.proyecto;

public class GestorVida {
	private int saludMax;
	private int salud;
	private boolean esInvulnerable;
	private float tiempoInvulnerable;
	
	public GestorVida(int saludMax) {
		this.saludMax=saludMax;
		this.salud=saludMax;
		this.esInvulnerable=false;
		this.tiempoInvulnerable=0f;

	}
	
	public void aplicardano(int danoinflijido) {
		if(esInvulnerable==true || danoinflijido < 0)
		{
			return ;
		}
		salud -=danoinflijido;
		
		if (salud <= 0) {
            salud = 0;
        } else {
            activarInvulnerabilidad(0.5f);
        }
		
		
	}
	public void aumentarVidaMaxima(int cantidadAumento) {
        saludMax += cantidadAumento;
        salud += cantidadAumento; 
    }
	
	public void actualizar(float deltaTime) {
        if (esInvulnerable) {
            tiempoInvulnerable -= deltaTime;
            if (tiempoInvulnerable <= 0) {
                esInvulnerable = false;
            }
        }
    }
	private void activarInvulnerabilidad(float tiempo) {
        this.esInvulnerable = true;
        this.tiempoInvulnerable = tiempo;
    }

	public int getVidaActual() {
        return salud;
    }

    public int getVidaMaxima() {
        return saludMax;
    }
}
