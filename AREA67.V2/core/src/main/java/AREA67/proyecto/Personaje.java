package AREA67.proyecto;

public class Personaje {
	int velocidadMovimiento = 5;

	GestorVida salud = new GestorVida(100); 
    GestorExperiencia exp = new GestorExperiencia();
    Recolector recolector = new Recolector();
    Arma[] arsenal = new Arma[6];
   
	public void recibirDano(int cantidad) {
        GestorVida.aplicardano(cantidad);
        
        
    }
}
