package AREA67.proyecto;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class Recolector {
    private float radio;
    private float multiplicador;
    private float velocidadAtraccion = 400f;
    private boolean imanActivo;
    private float tiempoIman;

    public Recolector(float imanNormal) {
        this.radio = imanNormal;
        this.multiplicador = 1.0f;
        this.imanActivo = false;
        this.tiempoIman = 0f;
    }

    public float getRadioTotal() {
        if (imanActivo) {
            return radio * 4f; // Calculado dinámicamente según el radio actual
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
        
        // CORREGIDO: Usar getRadioTotal() para que tome en cuenta el imán o las mejoras
        if (distancia <= getRadioTotal()) {
            float dirX = jugadorX - objeto.getX();
            float dirY = jugadorY - objeto.getY();

            Vector2 direccion = new Vector2(dirX, dirY).nor();

            float nuevaX = objeto.getX() + (direccion.x * velocidadAtraccion * deltaTime);
            float nuevaY = objeto.getY() + (direccion.y * velocidadAtraccion * deltaTime);

            objeto.setPosicion(nuevaX, nuevaY);
        }
    }

    // CAMBIADO A PUBLIC para poder llamarlo desde tu bucle principal al chocar con objetos
    public void recogerObjeto(ObjetoSuelo objeto, GestorExperiencia exp, GestorVida salud, Texture texturaCuchillo,Texture texturaPincho,Personaje jugador,Texture texturaBolafuego) {
        if (objeto.estaRecogido()) return;
        
        objeto.marcarComoRecogido();

        switch (objeto.getTipo()) {
            case "GEMA_EXPERIENCIA":
                exp.sumarExperiencia(objeto.getValor());
                break;
            case "CURACION":
                salud.curar(objeto.getValor());
                break;
            case "COFRE_TESORO":
            	Arma[] arsenal = jugador.getArsenal();
                boolean equipado = false;
                int tipoArmaRandom = (int) (Math.random() * 2); 

                for (int i = 0; i < arsenal.length; i++) {
                    if (arsenal[i] == null) {
                        if (tipoArmaRandom == 0) {
                            arsenal[i] = new ArmaCuchillo(texturaCuchillo);
                            System.out.println("¡Cuchillo obtenido del cofre!");
                        } else if (tipoArmaRandom == 1) {
                            arsenal[i] = new BolaPinchos(texturaPincho);
                            System.out.println("¡Bola de pinchos obtenida del cofre!");
                        }else {
                        	 arsenal[i] = new ArmaLanzallamas(texturaBolafuego);
                             System.out.println("¡Bola de pinchos obtenida del cofre!");
                        }
                        equipado = true;
                        break;
                    }
                }
                if (!equipado) System.out.println("¡Arsenal lleno!");
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