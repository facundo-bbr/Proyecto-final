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
	    private float velocidad = 80f; 
	    private int vida = 1500;      
	    private int dañoAtaque = 30;
	    protected float tiempoDesdeUltimoImpacto = 0f;
	    protected static final float COOLDOWN_DANIO = 0.3f;
	    
	    private Rectangle bounds;
	    private boolean muerto = false;
	
	  
	    private Animation<TextureRegion> animacionCaminar;
	    private Animation<TextureRegion> animacionAtaque;
	    private float tiempoEstado = 0f;
	
	   
	    private boolean atacando = false;
	    private float temporizadorAtaque = 0f;
	    private float cooldownAtaque = 3.0f; 
	    private float distanciaIdeal = 100f; 
	
	    public Jefe(Array<TextureRegion> framesCaminar, Array<TextureRegion> framesAtaque, float x, float y) {
	        this.x = x;
	        this.y = y;
	        this.bounds = new Rectangle(x, y, 96, 96); 
	
	        animacionCaminar = new Animation<TextureRegion>(0.15f, framesCaminar, Animation.PlayMode.LOOP);
	        animacionAtaque = new Animation<TextureRegion>(0.1f, framesAtaque, Animation.PlayMode.NORMAL);
	    }
	
	    public void actualizar(float deltaTime, float jugadorX, float jugadorY, Personaje jugador) {
	        tiempoEstado += deltaTime;
	        if (tiempoDesdeUltimoImpacto > 0) {
	            tiempoDesdeUltimoImpacto -= deltaTime;
	        }
	        bounds.setPosition(x, y);
	
	        if (muerto) return;
	
	        float distanciaAlJugador = Vector2.dst(x, y, jugadorX, jugadorY);
	
	        if (atacando) {
	           
	            if (animacionAtaque.isAnimationFinished(tiempoEstado)) {
	                atacando = false;
	                tiempoEstado = 0f;
	            }
	        } else {
	           
	            if (distanciaAlJugador > distanciaIdeal) {
	               
	                float angulo = (float) Math.atan2(jugadorY - y, jugadorX - x);
	                x += Math.cos(angulo) * velocidad * deltaTime;
	                y += Math.sin(angulo) * velocidad * deltaTime;
	            }
	
	         
	            float rangoParaAtacar = 160f;

	            if (distanciaAlJugador <= rangoParaAtacar) {
	               
	                temporizadorAtaque += deltaTime;
	                if (temporizadorAtaque >= cooldownAtaque) {
	                    iniciarAtaque(jugadorX, jugadorY, jugador, distanciaAlJugador);
	                    temporizadorAtaque = 0f;
	                }
	            } else {
	              
	            }
	        }
	    }
	
	    private void iniciarAtaque(float jugadorX, float jugadorY, Personaje jugador, float distanciaAlJugador) {
	        atacando = true;
	        tiempoEstado = 0f;
	
	        System.out.println("¡El Jefe prepara una Onda Expansiva!");
	
	       
	        float radioOndaExpansiva = 160f;
	        if (distanciaAlJugador <= radioOndaExpansiva) {
	            jugador.recibirDano(dañoAtaque);
	            System.out.println("¡La onda expansiva golpeó al jugador!");
	        }
	    }
	    public void recibirDano(int cantidad) {
	       
	        if (muerto) return;

	       
	        if (tiempoDesdeUltimoImpacto > 0) {
	            return; 
	        }

	        if (cantidad > 0) {
	            vida = Math.max(0, vida - cantidad);
	            
	          
	            tiempoDesdeUltimoImpacto = COOLDOWN_DANIO; 
	            
	            System.out.println("¡Jefe recibió " + cantidad + " de daño! Vida restante: " + vida);
	            
	            if (vida <= 0) {
	                muerto = true;
	                System.out.println("¡El Jefe ha sido derrotado!");
	            }
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
	          
	            float duracionTotal = animacionAtaque.getAnimationDuration();
	            float progreso = Math.min(tiempoEstado / duracionTotal, 1.0f);
	            	
	           
	            float radioMaximo = 160f;
	            float radioActual = radioMaximo * progreso;
	
	           
	            shapeRenderer.setColor(1f, 0.2f, 0.2f, 0.6f * (1.0f - progreso));
	            
	          
	            float centroX = x + (bounds.width / 2f);
	            float centroY = y + (bounds.height / 2f);
	            
	          
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