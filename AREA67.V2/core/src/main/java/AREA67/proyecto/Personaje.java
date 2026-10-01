package AREA67.proyecto;

public class Personaje {
	static float velocidadMovimiento = 5f;

	GestorVida salud = new GestorVida(100); 
    GestorExperiencia exp = new GestorExperiencia();
    Recolector recolector = new Recolector(12f);
    Arma[] arsenal = new Arma[6];
   
	public void recibirDano(int cantidad) {
        salud.aplicardano(cantidad);
        
        
    }
	public float getVelocidadMovimiento() {
        return velocidadMovimiento;
    }
}
