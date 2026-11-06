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
	    // Posición y física
	    private Vector2 posicion;
	    public static float velocidadMovimiento = 200f;
	    private boolean mirandoDerecha;
	    private boolean seEstaMoviendo;
	
	    // Texturas y Animación
	    private Texture sheetSoldado;
	    private Animation<TextureRegion> animacionCaminar;
	    private TextureRegion frameEstatico;
	    private TextureRegion frameActual;
	    private float stateTime;
	    
	    private Vector2 direccionMirada = new Vector2(1, 0); // Por defecto mira a la derecha
	
	    // Sistemas del Jugador
	    GestorVida salud = new GestorVida(100);
	    GestorExperiencia exp = new GestorExperiencia();
	    Recolector recolector = new Recolector(70f);
	    Arma[] arsenal = new Arma[6];
	
	    // Constructor predeterminado (compatibilidad)
	    public Personaje() {
	        this(400, 300, null);
	    }
	
	    // Constructor principal con posición inicial y textura para el arma inicial
	    public Personaje(float xInicial, float yInicial, Texture texturaBala) {
	        this.posicion = new Vector2(xInicial, yInicial);
	        this.mirandoDerecha = true;
	        this.seEstaMoviendo = false;
	        this.stateTime = 0f;
	
	        // Carga y procesado de la textura del jugador
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
	
	        // Equipar arma inicial si se pasa la textura de la bala
	        if (texturaBala != null) {
	            this.arsenal[0] = new Fusil(texturaBala);
	        }
	    }
	
	    public void actualizar(float deltaTime, Array<enemigos> listaEnemigos, Array<Proyectil> listaProyectiles) {
	        // 1. Procesar movimiento por teclado
	        procesarEntrada(deltaTime);
	
	        // 2. Actualizar estados del jugador y sistemas internos
	        salud.actualizar(deltaTime);
	        recolector.actualizar(deltaTime);
	
	        // 3. Actualizar animación
	        if (seEstaMoviendo) {
	            stateTime += deltaTime;
	            frameActual = animacionCaminar.getKeyFrame(stateTime, true);
	        } else {
	            stateTime = 0f;
	            frameActual = frameEstatico;
	        }
	
	        // Girar el sprite según hacia dónde mira
	        if (mirandoDerecha && frameActual.isFlipX()) {
	            frameActual.flip(true, false);
	        } else if (!mirandoDerecha && !frameActual.isFlipX()) {
	            frameActual.flip(true, false);
	        }
	
	        // 4. Actualizar armas del arsenal
	     // 4. Actualizar armas del arsenal
	        float centroX = getCentroX();
	        float centroY = getCentroY();
	        for (Arma arma : arsenal) {
	            if (arma != null) {
	                // Si el arma es un Fusil, le actualizamos su dirección antes de que actualice su reloj
	                if (arma instanceof Fusil) {
	                    ((Fusil) arma).setDireccion(direccionMirada);
	                }
	                // Todas las armas (incluido el fusil y el cuchillo) usan el actualizar normal con cadencia
	                arma.actualizar(deltaTime, centroX, centroY, listaEnemigos, listaProyectiles);
	            }
	        }
	    }
	    public void curarAlMaximo() {
	        if (salud != null) {
	            salud.curar(99999);
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
	
	        // --- BARRA DE EXPERIENCIA (AMARILLA) ---
	        float expActual = exp.getXpActual();
	        float expMax = exp.getXpRequeridaActual();
	        float porcentajeExp = Math.min(1f, expActual / expMax);
	
	        shapeRenderer.setColor(Color.DARK_GRAY);
	        shapeRenderer.rect(barraX, barraY + separacion, anchoMaximo, altoBarra);
	
	        shapeRenderer.setColor(Color.YELLOW);
	        shapeRenderer.rect(barraX, barraY + separacion, anchoMaximo * porcentajeExp, altoBarra);
	
	        // --- BARRA DE VIDA (ROJA / VERDE) ---
	        float porcentajeVida = (float) salud.getVidaActual() / salud.getVidaMaxima();
	        float anchoActual = anchoMaximo * porcentajeVida;
	
	        shapeRenderer.setColor(Color.RED);
	        shapeRenderer.rect(barraX, barraY, anchoMaximo, altoBarra);
	
	        if (anchoActual > 0) {
	            shapeRenderer.setColor(Color.GREEN);
	            shapeRenderer.rect(barraX, barraY, anchoActual, altoBarra);
	        }
	    }
	
	    public void recibirDano(int cantidad) {
	        salud.aplicardano(cantidad);
	    }
	
	    public boolean estaMuerto() {
	        return salud.getVidaActual() <= 0;
	    }
	
	    // Getters y Setters de Posición y Dimensiones
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
