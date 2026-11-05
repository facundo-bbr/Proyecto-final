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
		if (!esInvulnerable) {
            salud -= danoinflijido;
            esInvulnerable = true;              // Activamos la inmunidad
            tiempoInvulnerable = 1.0f; // Reiniciamos el temporizador
            
            if (salud < 0) salud = 0;
        }
    }
		
	 public void curar (int curacion)
		 {
		 salud += curacion;
	     if (salud > saludMax) {
	         salud = saludMax;
		 }}
	public void aumentarVidaMaxima(int cantidadAumento) {
        saludMax += cantidadAumento;
        salud += cantidadAumento; 
    }
	
	public void actualizar(float deltaTime) {
        if (esInvulnerable) {
            tiempoInvulnerable -= deltaTime; // ¡Esto es lo que hace que el tiempo corra!
            
            if (tiempoInvulnerable <= 0) {
                esInvulnerable = false; // Se acaba la inmunidad, vuelve a poder recibir daño
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
