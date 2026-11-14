	package AREA67.proyecto;
	
	import com.badlogic.gdx.graphics.Texture;
	import com.badlogic.gdx.graphics.g2d.SpriteBatch;
	import com.badlogic.gdx.math.Rectangle;
	import com.badlogic.gdx.math.Vector2;
	import com.badlogic.gdx.utils.Array;
	
	public class bala extends Proyectil {
	    private Vector2 velocidad; 
	    private Texture texturaBala;
	    private float tiempoDeVida; 
	    private float anguloRotacion;
	    
	    public bala(Texture textura, float origenX, float origenY, Vector2 posicionObjetivo, int danoArma, float velocidadBala) {
	        super(origenX, origenY, danoArma);
	        this.texturaBala = textura;
	        this.tiempoDeVida = 3.0f; 
	
	        Vector2 posicionVector = new Vector2(origenX, origenY);
	        Vector2 direccion = new Vector2(posicionObjetivo).sub(posicionVector);
	        
	        if (direccion.len() > 0) {
	            this.anguloRotacion = direccion.angleDeg();
	            direccion.nor().scl(velocidadBala);
	        }
	        this.velocidad = direccion;
	    }
	
	    @Override
	    public void actualizar(float deltaTime, Array<Enemigos> listaEnemigos) {
	        if (!activo) return;
	
	        x += velocidad.x * deltaTime;
	        y += velocidad.y * deltaTime;
	
	        tiempoDeVida -= deltaTime;
	        if (tiempoDeVida <= 0) {
	            activo = false;
	        }
	    }
	
	    @Override
	    public void dibujar(SpriteBatch batch) {
	        if (!activo) return;
	
	        float ancho = texturaBala.getWidth();
	        float alto = texturaBala.getHeight();
	
	        batch.draw(texturaBala, 
	                x - (ancho / 2f), y - (alto / 2f), 
	                ancho / 2f, alto / 2f,                      
	                ancho, alto,                                 
	                1f, 1f,                                     
	                anguloRotacion,                              
	                0, 0,                                      
	                (int) ancho, (int) alto,                     
	                false, false                                 
	        );
	    }
	
	    @Override
	    public Rectangle getBounds() {
	        return new Rectangle(x - 8, y - 8, 16, 16);
	    }
	}