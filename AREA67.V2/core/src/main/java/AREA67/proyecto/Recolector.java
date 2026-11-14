package AREA67.proyecto;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;



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
            return radio * 4f; 
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

        float distancia = Vector2.dst(objeto.getX(), objeto.getY(), jugadorX, jugadorY);
        
        if (distancia <= getRadioTotal()) {
            float dirX = jugadorX - objeto.getX();
            float dirY = jugadorY - objeto.getY();

            Vector2 direccion = new Vector2(dirX, dirY).nor();

            float nuevaX = objeto.getX() + (direccion.x * velocidadAtraccion * deltaTime);
            float nuevaY = objeto.getY() + (direccion.y * velocidadAtraccion * deltaTime);

            objeto.setPosicion(nuevaX, nuevaY);
        }
    }

    public void recogerObjeto(ObjetoSuelo objeto, GestorExperiencia exp, GestorVida salud, Texture texturaCuchillo,Texture texturaPincho,Personaje jugador,Texture texturaBolafuego,Texture texturaBotella) {
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
            	int tipoArmaRandom = (int) (Math.random() * 5); 

            	boolean armaEncontrada = false;

            	if (tipoArmaRandom == 0) {
            	    for (Arma arma : arsenal) {
            	        if (arma instanceof ArmaCuchillo) {
            	            ((ArmaCuchillo) arma).aplicarMejoraProgresiva();
            	            System.out.println("¡Cuchillo mejorado desde el cofre!");
            	            armaEncontrada = true;
            	            break;
            	        }
            	    }
            	} else if (tipoArmaRandom == 1) {
            	    for (Arma arma : arsenal) {
            	        if (arma instanceof BolaPinchos) {
            	            ((BolaPinchos) arma).aplicarMejoraProgresiva();
            	            System.out.println("¡Bola de pinchos mejorada desde el cofre!");
            	            armaEncontrada = true;
            	            break;
            	        }
            	    }
            	} else if (tipoArmaRandom == 2) {
            	    for (Arma arma : arsenal) {
            	        if (arma instanceof ArmaLanzallamas) {
            	            ((ArmaLanzallamas) arma).aplicarMejoraProgresiva();
            	            System.out.println("¡Lanzallamas mejorado desde el cofre!");
            	            armaEncontrada = true;
            	            break;
            	        }
            	    }
            	} else if (tipoArmaRandom == 2) {
            	    for (Arma arma : arsenal) {
            	        if (arma instanceof ArmaLanzallamas) {
            	            ((ArmaLanzallamas) arma).aplicarMejoraProgresiva();
            	            System.out.println("¡Lanzallamas mejorado desde el cofre!");
            	            armaEncontrada = true;
            	            break;
            	        }
            	    }
            	} else if (tipoArmaRandom == 3) {
            	    for (Arma arma : arsenal) {
            	        if (arma instanceof Molotov) {
            	            ((Molotov) arma).aplicarMejoraProgresiva();
            	            System.out.println("¡Molotov mejorado desde el cofre!");
            	            armaEncontrada = true;
            	            break;
            	        }
            	    }
            	} else { 
            	    for (Arma arma : arsenal) {
            	        if (arma instanceof Fusil) {
            	            ((Fusil) arma).aplicarMejoraProgresiva();
            	            System.out.println("¡Rifle mejorado desde el cofre!");
            	            armaEncontrada = true;
            	            break;
            	        }
            	    }
            	}

            	if (!armaEncontrada) {
            	    for (int i = 0; i < arsenal.length; i++) {
            	        if (arsenal[i] == null) {
            	            if (tipoArmaRandom == 0) {
            	                arsenal[i] = new ArmaCuchillo(texturaCuchillo);
            	                System.out.println("¡Cuchillo obtenido del cofre!");
            	            } else if (tipoArmaRandom == 1) {
            	                arsenal[i] = new BolaPinchos(texturaPincho);
            	                System.out.println("¡Bola de pinchos obtenida del cofre!");
            	            } else if (tipoArmaRandom == 2) {
            	                arsenal[i] = new ArmaLanzallamas(texturaBolafuego);
            	                System.out.println("¡Lanzallamas obtenido del cofre!");
            	            } else {
            	                arsenal[i] = new Molotov(texturaBotella);
            	                System.out.println("¡Molotov obtenido del cofre!");
            	            }
            	            break;
            	        }
            	    }
            	}
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