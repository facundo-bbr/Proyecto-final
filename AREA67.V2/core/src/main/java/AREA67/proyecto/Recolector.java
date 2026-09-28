package AREA67.proyecto;

public class Recolector {
	private float radio;
	private float multiplicador;
	
	private boolean imanActivo;
	private float tiempoIman;
	private float iman = radio*4;
	
	
	public Recolector(float radioBaseInicial) {
        this.radio = radioBaseInicial;
        this.multiplicador = 1.0f;
        this.imanActivo=false;
        this.tiempoIman=0f;
    }
	
	
	public float getRadioTotal() {
		if (imanActivo) {
            return iman; 
        }
        return radio * multiplicador;
    }
	
	public void actualizar(float deltaTime) {
        if (imanActivo) {
            tiempoIman -= deltaTime; 
            
            if (tiempoIman <= 0) {
                imanActivo = false;
                System.out.println("Efecto de Imán terminado.");
            }
        }
    }
	
	public void buscarObjetosCercanos(ObjetoSuelo[] objetosEnPantalla, float xJugador, float yJugador, GestorExperiencia exp, GestorVida salud) {
        if (objetosEnPantalla == null) {
            return;
        }

        for (int i = 0; i < objetosEnPantalla.length; i++) {
            ObjetoSuelo objeto = objetosEnPantalla[i];
            
            if (objeto == null || objeto.estaRecogido()) {
                continue; 
            }

            double distancia = Math.sqrt(Math.pow(objeto.getX() - xJugador, 2) + Math.pow(objeto.getY() - yJugador, 2));

            if (distancia <= getRadioTotal()) {
                recogerObjeto(objeto, exp, salud);
            }
        }
    }
	
	private void recogerObjeto(ObjetoSuelo objeto, GestorExperiencia exp, GestorVida salud) {
        objeto.marcarComoRecogido();

        switch (objeto.getTipo()) {
            case "GEMA_EXPERIENCIA":
                exp.sumarExperiencia(objeto.getValor());
                break;
            case "POLLO_CURACION":
                salud.curar(objeto.getValor());
                break;
            case "COFRE_TESORO":
                System.out.println("Abriendo interfaz de cofre...");
                break;
            case "IMAN":
            	activarIman(objeto.getValor());
            	break;
            default:
                System.out.println("Objeto desconocido.");
                break;
        }
    }
	
	private void activarIman(float duracionEnSegundos) {
        this.imanActivo = true;
        
        this.tiempoIman += duracionEnSegundos; 
    }
	
}
