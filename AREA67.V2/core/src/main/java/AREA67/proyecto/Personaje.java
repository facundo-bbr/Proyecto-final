	package AREA67.proyecto;
	
	import com.badlogic.gdx.Gdx;
	import com.badlogic.gdx.Input;
	import com.badlogic.gdx.graphics.Color;
	import com.badlogic.gdx.graphics.Texture;
	import com.badlogic.gdx.graphics.g2d.Animation;
	import com.badlogic.gdx.graphics.g2d.SpriteBatch;
	import com.badlogic.gdx.graphics.g2d.TextureRegion;
	import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
	import com.badlogic.gdx.math.Vector2;
	import com.badlogic.gdx.utils.Array;
	
	public class Personaje {
	    
	    private Vector2 posicion;
	    public static float velocidadMovimiento = 200f;
	    private boolean mirandoDerecha;
	    private boolean seEstaMoviendo;
	
	    private Texture sheetSoldado;
	    private Animation<TextureRegion> animacionCaminar;
	    private TextureRegion frameEstatico;
	    private TextureRegion frameActual;
	    private float stateTime;
	    
	    private Vector2 direccionMirada = new Vector2(1, 0); // Por defecto mira a la derecha
	
	    GestorVida salud = new GestorVida(100);
	    GestorExperiencia exp = new GestorExperiencia();
	    Recolector recolector = new Recolector(70f);
	    Arma[] arsenal = new Arma[5];
	    private float multiplicadorDañoRecibido = 1.0f;
	    private int contadorReducionDano = 0;
	    
	    
	    public Personaje() {
	        this(400, 300, null);
	    }
	
	    public Personaje(float xInicial, float yInicial, Texture texturaBala) {
	        this.posicion = new Vector2(xInicial, yInicial);
	        this.mirandoDerecha = true;
	        this.seEstaMoviendo = false;
	        this.stateTime = 0f;
	
	       
	
	        sheetSoldado = new Texture(Gdx.files.internal("donpollo_derecha.png"));
	        int anchoSoldado = sheetSoldado.getWidth() / 3;
	        TextureRegion[][] matrizCortes = TextureRegion.split(sheetSoldado, anchoSoldado, sheetSoldado.getHeight());
	
	        TextureRegion[] framesCaminar = new TextureRegion[3];
	        for (int i = 0; i < 3; i++) {
	            framesCaminar[i] = matrizCortes[0][i];
	        }
	
	        frameEstatico = framesCaminar[0];
	        animacionCaminar = new Animation<TextureRegion>(0.15f, framesCaminar);
	        frameActual = frameEstatico;
	
	        if (texturaBala != null) {
	            this.arsenal[0] = new Fusil(texturaBala);
	        }
	    }
	    
	    public void actualizar(float deltaTime, Array<Enemigos> listaEnemigos, Array<Proyectil> listaProyectiles,Array<Jefe> listaJefes) {
	        procesarEntrada(deltaTime);
	
	        salud.actualizar(deltaTime);
	        recolector.actualizar(deltaTime);
	
	        if (seEstaMoviendo) {
	            stateTime += deltaTime;
	            frameActual = animacionCaminar.getKeyFrame(stateTime, true);
	        } else {
	            stateTime = 0f;
	            frameActual = frameEstatico;
	        }
	
	        if (mirandoDerecha && frameActual.isFlipX()) {
	            frameActual.flip(true, false);
	        } else if (!mirandoDerecha && !frameActual.isFlipX()) {
	            frameActual.flip(true, false);
	        }
	
	        float centroX = getCentroX();
	        float centroY = getCentroY();
	        float ancho = frameActual.getRegionWidth();
	        float alto = frameActual.getRegionHeight();
	        for (Arma arma : arsenal) {
	            if (arma != null) {
	                if (arma instanceof Fusil) {
	                    ((Fusil) arma).setDireccion(direccionMirada);
	                } else if (arma instanceof ArmaLanzallamas) {
	                    ((ArmaLanzallamas) arma).setDireccion(direccionMirada);
	                }
	                
	                if (arma instanceof ArmaCuchillo) {
	                    ((ArmaCuchillo) arma).actualizarConJefes(deltaTime,posicion.x, posicion.y, ancho, alto, listaEnemigos, listaJefes, listaProyectiles);
	                } else if (arma instanceof Molotov) {
	                    ((Molotov) arma).actualizarConJefes(deltaTime, posicion.x, posicion.y, ancho, alto, listaEnemigos, listaJefes, listaProyectiles);
	                } else {
	                    arma.actualizar(deltaTime, centroX, centroY, listaEnemigos, listaProyectiles);
	                }
	            }
	    }
	    }
	    public void curarAlMaximo() {
	        if (salud != null) {
	            salud.curar(99999);
	        }
	    }
	    
	    public int getContadorReducionDano() {
	        return contadorReducionDano;
	    }
	    
	    public void incrementarContadorReducionDano() {
	        this.contadorReducionDano++;
	    }
	    
	    public void aplicarReducionDano(float porcentaje) {
	        this.multiplicadorDañoRecibido -= porcentaje;
	        if (this.multiplicadorDañoRecibido < 0.7f) {
	            this.multiplicadorDañoRecibido = 0.7f; 
	        }
	    }
	    
	    private void procesarEntrada(float deltaTime) {
	        float speed = velocidadMovimiento * deltaTime;
	        seEstaMoviendo = false;
	
	        if (Gdx.input.isKeyPressed(Input.Keys.W)) { posicion.y += speed; seEstaMoviendo = true; direccionMirada.set(0, 1); }
	        if (Gdx.input.isKeyPressed(Input.Keys.S)) { posicion.y -= speed; seEstaMoviendo = true; direccionMirada.set(0, -1); }
	        if (Gdx.input.isKeyPressed(Input.Keys.A)) { posicion.x -= speed; seEstaMoviendo = true; mirandoDerecha = false; direccionMirada.set(-1, 0); }
	        if (Gdx.input.isKeyPressed(Input.Keys.D)) { posicion.x += speed; seEstaMoviendo = true; mirandoDerecha = true; direccionMirada.set(1, 0); }
	    }
	
	    public void dibujar(SpriteBatch batch) {
	        if (frameActual != null) {
	            batch.draw(frameActual, posicion.x, posicion.y);
	        }
	    }
	
	    public void dibujarUI(ShapeRenderer shapeRenderer) {
	        float anchoMaximo = 50f;
	        float offsetY = getAlto() + 10f;
	        float altoBarra = 6f;
	        float barraX = getCentroX() - (anchoMaximo / 2f);
	        float barraY = posicion.y + offsetY;
	        float separacion = 14f;
	
	        float expActual = exp.getXpActual();
	        float expMax = exp.getXpRequeridaActual();
	        float porcentajeExp = Math.min(1f, expActual / expMax);
	
	        shapeRenderer.setColor(Color.DARK_GRAY);
	        shapeRenderer.rect(barraX, barraY + separacion, anchoMaximo, altoBarra);
	
	        shapeRenderer.setColor(Color.YELLOW);
	        shapeRenderer.rect(barraX, barraY + separacion, anchoMaximo * porcentajeExp, altoBarra);
	
	        float porcentajeVida = (float) salud.getVidaActual() / salud.getVidaMaxima();
	        float anchoActual = anchoMaximo * porcentajeVida;
	
	        shapeRenderer.setColor(Color.RED);
	        shapeRenderer.rect(barraX, barraY, anchoMaximo, altoBarra);
	
	        if (anchoActual > 0) {
	            shapeRenderer.setColor(Color.GREEN);
	            shapeRenderer.rect(barraX, barraY, anchoActual, altoBarra);
	        }
	    }
	
	    public void recibirDano(int danoBase) {
	        int dañoFinal = (int) (danoBase * multiplicadorDañoRecibido);
	        
	        if (dañoFinal < 1 && danoBase > 0) {
	            dañoFinal = 1; 
	        }

	        if (salud != null) {
	            salud.aplicardano(dañoFinal);
	        } else {
	        }
	    }
	
	    public boolean estaMuerto() {
	        return salud.getVidaActual() <= 0;
	    }
	
	    public float getX() { return posicion.x; }
	    public float getY() { return posicion.y; }
	    public void setX(float x) { this.posicion.x = x; }
	    public void setY(float y) { this.posicion.y = y; }
	
	    public float getAncho() { return frameActual != null ? frameActual.getRegionWidth() : 0; }
	    public float getAlto() { return frameActual != null ? frameActual.getRegionHeight() : 0; }
	
	    public float getCentroX() { return posicion.x + (getAncho() / 2f); }
	    public float getCentroY() { return posicion.y + (getAlto() / 2f); }
	
	    public float getVelocidadMovimiento() { return velocidadMovimiento; }
	    public GestorVida getSalud() { return salud; }
	    public GestorVida getGestorVida() { return salud; }
	    public GestorExperiencia getGestorExperiencia() { return exp; }
	    public Recolector getRecolector() { return recolector; }
	    public Arma[] getArsenal() { return arsenal; }
	
	    public void dispose() {
	        if (sheetSoldado != null) {
	            sheetSoldado.dispose();
	        }
	    }
	}
