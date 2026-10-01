package AREA67.proyecto;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2; // NUEVO: Para calcular colisiones

public class Principal extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    public AssetManager manager;
    private OrthographicCamera camera;
    private float pjX;
    private float pjY;
    private float tiempoJuego;
    private BitmapFont fuenteSurvival;

    // VARIABLES JUGADOR
    private Texture sheetSoldado; 
    private Animation<TextureRegion> animacionCaminar;
    private TextureRegion frameEstatico;
    private float stateTime; 
    private GestorVida vidaJugador;
    private boolean mirandoDerecha;


    // VARIABLES ENEMIGOS 
    private Array<enemigos> listaEnemigos;
    private Texture sheetEnemigo; 
    private TextureRegion[] framesEnemigo;
    private float temporizadorSpawneo; 

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        image = new Texture("mapa.jpg");
        manager = new AssetManager();
        camera = new OrthographicCamera();
        tiempoJuego = 0f;
        fuenteSurvival = new BitmapFont();
        fuenteSurvival.getData().setScale(1.5f);
        camera.setToOrtho(false, 800, 600);
        pjX = 400;
        pjY = 300;
        mirandoDerecha = true;

        vidaJugador = new GestorVida(100);

        // 1. Carga segura del Soldado
        sheetSoldado = new Texture(Gdx.files.internal("donpollo_derecha.png"));
        int anchoSoldado = sheetSoldado.getWidth() / 3;
        TextureRegion[][] matrizCortes = TextureRegion.split(sheetSoldado, anchoSoldado, sheetSoldado.getHeight());

        TextureRegion[] framesCaminar = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            framesCaminar[i] = matrizCortes[0][i];
        }

        frameEstatico = framesCaminar[0];
        animacionCaminar = new Animation<TextureRegion>(0.15f, framesCaminar);
        stateTime = 0f;

        // 2. Carga segura del Zombi (Automática según lo que mida la imagen)
        sheetEnemigo = new Texture(Gdx.files.internal("zombieverdeizq.png")); 
        TextureRegion[][] matrizCortesEnemigo = TextureRegion.split(sheetEnemigo, 60, 60);
        
        // Calculamos cuántos cuadros de 60x60 tiene la imagen para no pasarnos del límite
        int cantFramesZombi = sheetEnemigo.getWidth() / 60;
        framesEnemigo = new TextureRegion[cantFramesZombi];
        for(int i = 0; i < cantFramesZombi; i++){
            framesEnemigo[i] = matrizCortesEnemigo[0][i];
        }

        // 3. Sistema
        listaEnemigos = new Array<>(); 
        temporizadorSpawneo = 0f;
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float speed = 160f * deltaTime;
        
        vidaJugador.actualizar(deltaTime);
        tiempoJuego += deltaTime;	
        // LÓGICA DE MOVIMIENTO JUGADOR
        boolean seEstaMoviendo = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) { pjY += speed; seEstaMoviendo = true; }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) { pjY -= speed; seEstaMoviendo = true; }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) { pjX -= speed; seEstaMoviendo = true; mirandoDerecha = false; }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) { pjX += speed; seEstaMoviendo = true; mirandoDerecha = true; }

        if (seEstaMoviendo) {
            stateTime += deltaTime;
        } else {
            stateTime = 0f; 
        }

        TextureRegion frameActual;
        if (seEstaMoviendo) {
            frameActual = animacionCaminar.getKeyFrame(stateTime, true);
        } else {
            frameActual = frameEstatico;
        }

        if (mirandoDerecha && frameActual.isFlipX()) {
            frameActual.flip(true, false);
        } else if (!mirandoDerecha && !frameActual.isFlipX()) {
            frameActual.flip(true, false);
        }

        // LÓGICA ENEMIGOS Y COLISIONES
        temporizadorSpawneo += deltaTime;
        
        float centroPjX = pjX + (frameActual.getRegionWidth() / 2f);
        float centroPjY = pjY + (frameActual.getRegionHeight() / 2f);
        
        float objetivoX = centroPjX - (framesEnemigo[0].getRegionWidth() / 2f);
        float objetivoY = centroPjY - (framesEnemigo[0].getRegionHeight() / 2f);

        if (temporizadorSpawneo > 1.5f) {
            listaEnemigos.add(new enemigos(framesEnemigo, centroPjX, centroPjY));
            temporizadorSpawneo = 0f; 
        }

        for (enemigos enemigo : listaEnemigos) {
            enemigo.perseguir(objetivoX, objetivoY, deltaTime);
            
            // NUEVO: Comprobar colisión. Si el zombi está a menos de 45 píxeles de tu centro, te daña.
            float centroZombiX = enemigo.getX() + (framesEnemigo[0].getRegionWidth() / 2f);
            float centroZombiY = enemigo.getY() + (framesEnemigo[0].getRegionHeight() / 2f);
            
            float distancia = Vector2.dst(centroPjX, centroPjY, centroZombiX, centroZombiY);
            
            if (distancia < 45f) {
                // Llama a tu clase GestorVida. Los I-Frames evitarán que te mate de un solo golpe.
                vidaJugador.aplicardano(10); 
            }
        }

        // CÁMARA
        pjX = MathUtils.clamp(pjX, 0, image.getWidth() - frameActual.getRegionWidth());
        pjY = MathUtils.clamp(pjY, 0, image.getHeight() - frameActual.getRegionHeight());

        camera.position.x = pjX + (frameActual.getRegionWidth() / 2f);
        camera.position.y = pjY + (frameActual.getRegionHeight() / 2f);

        float halfCameraWidth = camera.viewportWidth / 2f;
        float halfCameraHeight = camera.viewportHeight / 2f;
        
        camera.position.x = MathUtils.clamp(camera.position.x, halfCameraWidth, image.getWidth() - halfCameraWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, halfCameraHeight, image.getHeight() - halfCameraHeight);

        camera.update();

        // DIBUJO DE TEXTURAS (Batch)
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        batch.draw(image, 0, 0); 
        
        for (enemigos enemigo : listaEnemigos) {
            enemigo.dibujar(batch);
        }
     // Convertimos el tiempo total en formato de minutos y segundos
        int minutos = (int) (tiempoJuego / 60);
        int segundos = (int) (tiempoJuego % 60);
        String textoTiempo = String.format("TIEMPO: %02d:%02d", minutos, segundos);

        // Posición fija en la esquina superior izquierda respecto a la cámara
        float textoX = camera.position.x - 380f; 
        float textoY = camera.position.y + 270f; 

        // BORDE NEGRO GRUESO (Simulado duplicando el texto en cruz y diagonales)
        fuenteSurvival.setColor(Color.BLACK);
        fuenteSurvival.draw(batch, textoTiempo, textoX - 2, textoY);
        fuenteSurvival.draw(batch, textoTiempo, textoX + 2, textoY);
        fuenteSurvival.draw(batch, textoTiempo, textoX, textoY - 2);
        fuenteSurvival.draw(batch, textoTiempo, textoX, textoY + 2);
        fuenteSurvival.draw(batch, textoTiempo, textoX - 2, textoY - 2);
        fuenteSurvival.draw(batch, textoTiempo, textoX + 2, textoY + 2);

        // TEXTO PRINCIPAL EN COLOR AMARILLO/BLANCO ARCADE (Ideal para estilo retro/survival)
        fuenteSurvival.setColor(Color.YELLOW); // O puedes usar Color.WHITE según prefieras
        fuenteSurvival.draw(batch, textoTiempo, textoX, textoY);

        batch.draw(frameActual, pjX, pjY); 
        
        batch.end(); 

        // DIBUJO DE BARRA DE VIDA (ShapeRenderer)
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float porcentajeVida = (float) vidaJugador.getVidaActual() / vidaJugador.getVidaMaxima();
        float anchoMaximo = 50f; 
        float altoBarra = 6f;    
        float offsetY = frameActual.getRegionHeight() + 10f; 
        float anchoActual = anchoMaximo * porcentajeVida;

        float barraX = pjX + (frameActual.getRegionWidth() / 2f) - (anchoMaximo / 2f);
        float barraY = pjY + offsetY;

        // Si la vida es mayor a 0, dibuja la vida. Si no, solo el rojo de fondo
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barraX, barraY, anchoMaximo, altoBarra);

        if (anchoActual > 0) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(barraX, barraY, anchoActual, altoBarra);
        }

        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        image.dispose();
        sheetSoldado.dispose();
        sheetEnemigo.dispose();
        fuenteSurvival.dispose(); // NUEVO: Limpia la fuente de la memoria
    }
}