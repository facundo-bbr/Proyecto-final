package AREA67.proyecto;

import com.badlogic.gdx.math.Vector2;

public class Recolector {
	private float radio;
	private float multiplicador;
	private float velocidadAtraccion = 400f;
	private boolean imanActivo;
	private float tiempoIman;
	private float iman = radio*4;


    public Recolector(float imanNormal) {
        this.radio = imanNormal;
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

    public void actualizarObjeto(ObjetoSuelo objeto, float deltaTime, float jugadorX, float jugadorY) {
        if (objeto.estaRecogido()) return;

        // Distancia entre el objeto y el jugador
        float distancia = Vector2.dst(objeto.getX(), objeto.getY(), jugadorX, jugadorY);
        // Si el objeto entra en el radio del imán, se desplaza hacia el jugador
        if (distancia <= radio) {
            // Calculamos la dirección hacia el jugador
            float dirX = jugadorX - objeto.getX();
            float dirY = jugadorY - objeto.getY();

            // Normalizamos el vector y movemos el objeto paso a paso con la velocidad
            Vector2 direccion = new Vector2(dirX, dirY).nor();

            float nuevaX = objeto.getX() + (direccion.x * velocidadAtraccion * deltaTime);
            float nuevaY = objeto.getY() + (direccion.y * velocidadAtraccion * deltaTime);

            // Actualizamos la posición del objeto en el suelo
            objeto.setPosicion(nuevaX, nuevaY);
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

	public float getRangoIman() {
        return radio;
    }

	public void mejorarRango(float incremento) {
        this.radio += incremento;
    }

}
