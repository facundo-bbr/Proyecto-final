	package AREA67.proyecto;
	
	import com.badlogic.gdx.graphics.g2d.Animation;
	import com.badlogic.gdx.graphics.g2d.SpriteBatch;
	import com.badlogic.gdx.graphics.g2d.TextureRegion;
	import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
	import com.badlogic.gdx.math.Rectangle;
	import com.badlogic.gdx.math.Vector2;
	import com.badlogic.gdx.utils.Array;
	
	public class Jefe {
	    private float x, y;
	    private float velocidad = 80f; // Un poco más lento pero imponente
	    private int vida = 300;       // Mucha más vida que un enemigo normal
	    private int dañoAtaque = 25;
	    
	    private Rectangle bounds;
	    private boolean muerto = false;
	
	    // Animaciones
	    private Animation<TextureRegion> animacionCaminar;
	    private Animation<TextureRegion> animacionAtaque;
	    private float tiempoEstado = 0f;
	
	    // Control de estados de IA del Jefe
	    private boolean atacando = false;
	    private float temporizadorAtaque = 0f;
	    private float cooldownAtaque = 3.0f; // Ataca cada 3 segundos
	    private float distanciaIdeal = 100f; // Se acerca pero se detiene antes de tocar al jugador
	
	    public Jefe(Array<TextureRegion> framesCaminar, Array<TextureRegion> framesAtaque, float x, float y) {
	        this.x = x;
	        this.y = y;
	        this.bounds = new Rectangle(x, y, 64, 64); // Ajusta el tamaño según tus sprites
	
	        animacionCaminar = new Animation<TextureRegion>(0.15f, framesCaminar, Animation.PlayMode.LOOP);
	        animacionAtaque = new Animation<TextureRegion>(0.1f, framesAtaque, Animation.PlayMode.NORMAL);
	    }
	
	    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Personaje jugador) {
	        tiempoEstado += deltaTime;
	        bounds.setPosition(x, y);
	
	        if (muerto) return;
	
	        float distanciaAlJugador = Vector2.dst(x, y, jugadorX, jugadorY);
	
	        if (atacando) {
	            // Si está ejecutando la animación de ataque, esperamos a que termine
	            if (animacionAtaque.isAnimationFinished(tiempoEstado)) {
	                atacando = false;
	                tiempoEstado = 0f;
	            }
	        } else {
	            // Lógica de movimiento: Se acerca solo si está más lejos que su distancia ideal
	            if (distanciaAlJugador > distanciaIdeal) {
	                // Moverse hacia el jugador
	                float angulo = (float) Math.atan2(jugadorY - y, jugadorX - x);
	                x += Math.cos(angulo) * velocidad * deltaTime;
	                y += Math.sin(angulo) * velocidad * deltaTime;
	            }
	
	            // Control del cooldown para lanzar la onda expansiva
	         // Definimos un radio de activación para el ataque (ej: 180 píxeles)
	            float rangoParaAtacar = 160f;

	            if (distanciaAlJugador <= rangoParaAtacar) {
	                // Solo acumulamos tiempo para atacar si el jugador está cerca
	                temporizadorAtaque += deltaTime;
	                if (temporizadorAtaque >= cooldownAtaque) {
	                    iniciarAtaque(jugadorX, jugadorY, jugador, distanciaAlJugador);
	                    temporizadorAtaque = 0f;
	                }
	            } else {
	                // Opcional: si el jugador se aleja mientras cargaba el ataque, 
	                // podemos congelar o mantener el temporizador para que no se reinicie injustamente, 
	                // o simplemente dejarlo quieto. Aquí lo dejamos estático hasta que vuelva a acercarse.
	            }
	        }
	    }
	
	    private void iniciarAtaque(float jugadorX, float jugadorY, Personaje jugador, float distanciaAlJugador) {
	        atacando = true;
	        tiempoEstado = 0f; // Reinicia el tiempo para que la animación de ataque comience desde el frame 1
	
	        System.out.println("¡El Jefe prepara una Onda Expansiva!");
	
	        // Comprobamos si el jugador está dentro del radio de la onda expansiva (ej. 160 píxeles alrededor del jefe)
	        float radioOndaExpansiva = 160f;
	        if (distanciaAlJugador <= radioOndaExpansiva) {
	            jugador.recibirDano(dañoAtaque);
	            System.out.println("¡La onda expansiva golpeó al jugador!");
	        }
	    }
	
	    public void recibirDano(int cantidad) {
	        vida -= cantidad;
	        if (vida <= 0) {
	            muerto = true;
	        }
	    }
	
	    public void dibujar(SpriteBatch batch) {
	        TextureRegion frameActual;
	
	        if (atacando) {
	            frameActual = animacionAtaque.getKeyFrame(tiempoEstado, false);
	        } else {
	            frameActual = animacionCaminar.getKeyFrame(tiempoEstado, true);
	        }
	
	        batch.draw(frameActual, x, y, bounds.width, bounds.height);
	    }
	    
	    public void dibujarOndaExpansiva(ShapeRenderer shapeRenderer) {
	        if (atacando) {
	            // Calculamos el progreso de la animación de ataque (de 0.0 a 1.0)
	            float duracionTotal = animacionAtaque.getAnimationDuration();
	            float progreso = Math.min(tiempoEstado / duracionTotal, 1.0f);
	            	
	            // El radio crece desde 0 hasta el radio máximo de la onda (ej. 160 píxeles)
	            float radioMaximo = 160f;
	            float radioActual = radioMaximo * progreso;
	
	            // Dibujamos un círculo de advertencia/onda (puedes ajustar el color RGBA)
	            // Rojo translúcido que se va desvaneciendo conforme se expande
	            shapeRenderer.setColor(1f, 0.2f, 0.2f, 0.6f * (1.0f - progreso));
	            
	            // Obtenemos el centro exacto del jefe
	            float centroX = x + (bounds.width / 2f);
	            float centroY = y + (bounds.height / 2f);
	            
	            // Dibujamos el círculo en modo línea o relleno (aquí usamos círculos concéntricos o línea gruesa)
	            shapeRenderer.circle(centroX, centroY, radioActual);
	        }
	    }
	
	    public boolean estaMuerto() {
	        return muerto;
	    }
	
	    public Rectangle getBounds() {
	        return bounds;
	    }
	
	    public float getX() { return x; }
	    public float getY() { return y; }
	}