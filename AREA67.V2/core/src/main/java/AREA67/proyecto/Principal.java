package AREA67.proyecto;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class Principal extends ApplicationAdapter {
    
	private boolean juegoIniciado = false;
    private boolean enMenuPrincipal = false;
    private BitmapFont fuenteMenu;
	
	private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    public AssetManager manager;
    private OrthographicCamera camera;

    private float tiempoJuego;
    private BitmapFont fuenteSurvival;
    private BitmapFont fuenteTexto;

    private Texture texturaGemaVerde;
    private Texture texturaBala;
    private Texture texturaCuchillo;
    private Texture texturaPincho;
    private Texture texturaPollo;
    private Texture texturaIman;
    private Texture texturaCofre;
    private Texture texturaQueso;
    private Texture texturaGemaRoja;


    // Entidad Jugador
    private Personaje jugador;
    
    //Menu de subir de nivel
    private MenuMejora menuMejora;
    
    // Listas del juego
    private Array<Proyectil> listaProyectiles = new Array<>();
    private Array<enemigos> listaEnemigos = new Array<>();
    private Array<ObjetoSuelo> listaObjetosSuelo = new Array<>();
    private Array<Obstaculos> listaObstaculos = new Array<>();

    // Recursos Enemigo
    private Texture sheetEnemigo;
    private TextureRegion[] framesEnemigo;
    private float temporizadorSpawneo;
    
    private Texture texturaObstaculo;

    private boolean yaMurio = false;
    

    
    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        image = new Texture("mapa.jpg");
        manager = new AssetManager();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        tiempoJuego = 0f;
        fuenteSurvival = new BitmapFont();
        fuenteSurvival.getData().setScale(1.5f);
        fuenteTexto = new BitmapFont();

        texturaGemaVerde = new Texture("xpverde.png");
        texturaBala = new Texture("bala.png");
        texturaCuchillo=new Texture ("Cuchillo.png");
        texturaPincho=new Texture ("Pincho.png");
        texturaPollo=new Texture ("Pollo.png");
        texturaObstaculo= new Texture ("Barril.png");
        texturaIman = new Texture ("Iman.png");
        texturaCofre= new Texture ("Cofre.png");
        texturaQueso= new Texture ("Queso.png");
        texturaGemaRoja=new Texture ("xprojo.png");

        // Instancia del Personaje
        jugador = new Personaje(400, 300, texturaBala);

        // Carga de Sprites del Zombi
        sheetEnemigo = new Texture(Gdx.files.internal("zombieverdeizq.png"));
        TextureRegion[][] matrizCortesEnemigo = TextureRegion.split(sheetEnemigo, 60, 60);
        int cantFramesZombi = sheetEnemigo.getWidth() / 60;
        framesEnemigo = new TextureRegion[cantFramesZombi];
        for (int i = 0; i < cantFramesZombi; i++) {
            framesEnemigo[i] = matrizCortesEnemigo[0][i];
        }

        temporizadorSpawneo = 0f;
        
        fuenteMenu = new BitmapFont();
        fuenteMenu.getData().setScale(2.0f);
        
        menuMejora = new MenuMejora(texturaCuchillo,texturaPincho);
        
        float anchoMapa = image.getWidth();
        float altoMapa = image.getHeight();
        
        int cantidadObstaculos=7;
        
        for (int i = 0; i < cantidadObstaculos; i++) {
            float posX = com.badlogic.gdx.math.MathUtils.random(50, anchoMapa - 100);
            float posY = com.badlogic.gdx.math.MathUtils.random(50, altoMapa - 100);
            
            // Crear el obstáculo con vida inicial (ej: 50) y añadirlo a la lista
            listaObstaculos.add(new Obstaculos(posX, posY, texturaObstaculo, 250));
        }
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Texture texturaAElegir = texturaGemaVerde;
        if (!juegoIniciado) {
            ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
            
            // IMPORTANTE: Sincronizamos la cámara para que el ancho sea exactamente 800x600
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            
            // Dibuja el mapa de fondo estático
            batch.draw(image, 0, 0);

            com.badlogic.gdx.graphics.g2d.GlyphLayout layout = new com.badlogic.gdx.graphics.g2d.GlyphLayout();

            if (!enMenuPrincipal) {
                // PANTALLA 1: "Press to Play"
                fuenteMenu.setColor(Color.RED);
                fuenteMenu.getData().setScale(2.0f);
                String textoTitulo = "VAMPIRE SURVIVORS - JAVA";
                layout.setText(fuenteMenu, textoTitulo);
                float xTitulo = (camera.viewportWidth - layout.width) / 2f; // Centrado exacto usando la cámara
                fuenteMenu.draw(batch, textoTitulo, xTitulo, 380);

                fuenteMenu.setColor(Color.WHITE);
                fuenteMenu.getData().setScale(1.2f);
                String textoPress = "PRESS ANY KEY OR CLICK TO START";
                layout.setText(fuenteMenu, textoPress);
                float xPress = (camera.viewportWidth - layout.width) / 2f; // Centrado exacto
                fuenteMenu.draw(batch, textoPress, xPress, 300);

                // Detectar clic o tecla para pasar al menú de botones
                if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ANY_KEY)) {
                    enMenuPrincipal = true;
                }
            } else {
                // PANTALLA 2: Botones PLAY y CONFIGURACIÓN
                fuenteMenu.setColor(Color.YELLOW);
                fuenteMenu.getData().setScale(2.0f); // Escala grande para el título
                String textoMenu = "MENU PRINCIPAL";
                layout.setText(fuenteMenu, textoMenu);
                float xMenu = (camera.viewportWidth - layout.width) / 2f;
                fuenteMenu.draw(batch, textoMenu, xMenu, 420);

                // Cambiamos a escala 1.5f para los botones y medimos bien su centrado
                fuenteMenu.getData().setScale(1.5f);
                fuenteMenu.setColor(Color.WHITE);

                String textoPlay = "> PLAY <";
                layout.setText(fuenteMenu, textoPlay);
                float xPlay = (camera.viewportWidth - layout.width) / 2f;
                fuenteMenu.draw(batch, textoPlay, xPlay, 320);

                String textoConfig = "> CONFIGURACION <";
                layout.setText(fuenteMenu, textoConfig);
                float xConfig = (camera.viewportWidth - layout.width) / 2f;
                fuenteMenu.draw(batch, textoConfig, xConfig, 240);

                // 1. Control por Teclado
                if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)) {
                    juegoIniciado = true; // ¡Arranca el juego!
                } else if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUM_2)) {
                    System.out.println("Configuración abierta");
                }

                // 2. Control por Clic del Mouse mapeado con la cámara
                if (Gdx.input.justTouched()) {
                    com.badlogic.gdx.math.Vector3 touchPos = new com.badlogic.gdx.math.Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                    camera.unproject(touchPos);

                    float mouseX = touchPos.x;
                    float mouseY = touchPos.y;

                    // Rango horizontal centrado (de 200 a 600) y vertical exacto para cada opción
                    if (mouseX >= 200 && mouseX <= 600) {
                        // Botón PLAY (Y: 280 a 330)
                        if (mouseY >= 280 && mouseY <= 330) {
                            juegoIniciado = true;
                        }
                        // Botón CONFIGURACIÓN (Y: 200 a 250)
                        else if (mouseY >= 200 && mouseY <= 250) {
                            System.out.println("Configuración abierta");
                        }
                    }
                }
            }

            batch.end();
            return; 
        }
        


        if (jugador.estaMuerto()) {
            if (!yaMurio) {
                System.out.println("¡Game Over!");
                yaMurio = true;
            }
            return;
        }

        float centroPjX = jugador.getCentroX();
        float centroPjY = jugador.getCentroY();
     // 1. COMPROBAR SI HAY NIVELES PENDIENTES PARA ABRIR EL MENÚ
        if (jugador.getGestorExperiencia().tieneNivelesPendientes() && !menuMejora.isActivo()) {
            menuMejora.abrir();
        }

         centroPjX = jugador.getCentroX();
         centroPjY = jugador.getCentroY();

        // 2. CONTROLAR EL FLUJO: Menú Activo vs Lógica del Juego
        if (menuMejora.isActivo()) {
            // El menú está abierto: congelamos TODO el juego y solo manejamos el menú
            menuMejora.manejarEntrada(jugador);
        } else {
            // --- TODO ESTE BLOQUE SE CONGELA CUANDO EL MENÚ ESTÁ ABIERTO ---
            tiempoJuego += deltaTime; 

            // Actualizar movimiento, armas y cooldowns del jugador
            jugador.actualizar(deltaTime, listaEnemigos, listaProyectiles);


            // Spawneo de enemigos
            temporizadorSpawneo += deltaTime;
            if (temporizadorSpawneo > 1.5f) {
                listaEnemigos.add(new enemigos(framesEnemigo, centroPjX, centroPjY));
                temporizadorSpawneo = 0f;
            }

         // Colisiones: Proyectiles vs Enemigos y Obstáculos
            for (int i = listaProyectiles.size - 1; i >= 0; i--) {
                Proyectil p = listaProyectiles.get(i);
                p.actualizar(deltaTime, listaEnemigos);

                if (!p.isActivo()) {
                    listaProyectiles.removeIndex(i);
                    continue;
                }

                boolean impacto = false;

                // 1. Colisión contra Enemigos
                for (int j = listaEnemigos.size - 1; j >= 0; j--) {
                    enemigos enemigo = listaEnemigos.get(j);
                    if (p.getBounds().overlaps(enemigo.getBounds())) {
                        enemigo.recibirDano(p.getDano());
                        impacto = true;
                        p.destruir();
                        break;
                    }
                }

                // 2. Colisión contra Obstáculos (Solo si el proyectil sigue activo)
                if (p.isActivo()) {
                    for (int k = listaObstaculos.size - 1; k >= 0; k--) {
                        Obstaculos obstaculo = listaObstaculos.get(k);
                        
                        if (p.getBounds().overlaps(obstaculo.getBounds())) {
                            obstaculo.recibirDano(p.getDano());
                            impacto = true;
                            p.destruir(); // El proyectil desaparece al impactar

                            if (obstaculo.estaDestruido()) {
                                // Suelta el objeto al suelo
                                ObjetoSuelo loot = obstaculo.soltarObjeto(); // Ajustado a tu método de la clase Obstaculos
                                if (loot != null) {
                                    listaObjetosSuelo.add(loot);
                                }
                                listaObstaculos.removeIndex(k); // Elimina el obstáculo roto
                            }
                            break;
                        }
                    }
                }

                // Si el proyectil impactó contra algo o se desactivó, lo eliminamos de la lista
                if (impacto || !p.isActivo()) {
                    listaProyectiles.removeIndex(i);
                }
            }
            
           
            
            // Colisiones: Persecución y Ataque del Enemigo al Jugador
            float objetivoX = centroPjX - (framesEnemigo[0].getRegionWidth() / 2f);
            float objetivoY = centroPjY - (framesEnemigo[0].getRegionHeight() / 2f);

            for (int i = listaEnemigos.size - 1; i >= 0; i--) {
                enemigos enemigo = listaEnemigos.get(i);
                enemigo.perseguir(objetivoX, objetivoY, deltaTime); // Se mueven solo aquí

                if (enemigo.estaMuerto()) {
                    float centroZombiX = enemigo.getX() + (framesEnemigo[0].getRegionWidth() / 2f);
                    float centroZombiY = enemigo.getY() + (framesEnemigo[0].getRegionHeight() / 2f);
                    float gemaX = centroZombiX - (texturaGemaVerde.getWidth() / 2f);
                    float gemaY = centroZombiY - (texturaGemaVerde.getHeight() / 2f);

                    // --- DECIDIR QUÉ SUELTA EL ENEMIGO AL MORIR ---
                    // Por ejemplo, 80% de probabilidad de gema verde (baja) y 20% de gema roja (alta)
                    String varianteGema = "VERDE";
                    int valorExperiencia = 10;
                    
                    if (Math.random() < 0.20) { // 20% de probabilidad
                        varianteGema = "ROJA";
                        valorExperiencia = 35;  // Da más experiencia
                    }

                    // Creamos el objeto con su tipo, valor y variante específica
                    listaObjetosSuelo.add(new ObjetoSuelo(gemaX, gemaY, "GEMA_EXPERIENCIA", valorExperiencia, varianteGema));
                    
                    listaEnemigos.removeIndex(i);
                    continue;
                }

                float centroZombiX = enemigo.getX() + (framesEnemigo[0].getRegionWidth() / 2f);
                float centroZombiY = enemigo.getY() + (framesEnemigo[0].getRegionHeight() / 2f);
                if (Vector2.dst(centroPjX, centroPjY, centroZombiX, centroZombiY) < 45f) {
                    jugador.recibirDano(10);
                }
            }
            
            for (int k = listaObstaculos.size - 1; k >= 0; k--) {
                Obstaculos obstaculo = listaObstaculos.get(k);
                
                // 1. Actualizar el temporizador de inmunidad del barril
                obstaculo.actualizar(deltaTime);

                // 2. Comprobar colisión con la Bola de Pinchos
                for (Arma arma : jugador.arsenal) {
                    if (arma != null && arma instanceof BolaPinchos) {
                        BolaPinchos bola = (BolaPinchos) arma;

                        if (bola.getBounds().overlaps(obstaculo.getBounds())) {
                            obstaculo.recibirDano(bola.getDano());

                            if (obstaculo.estaDestruido()) {
                                ObjetoSuelo loot = obstaculo.soltarObjeto();
                                if (loot != null) {
                                    listaObjetosSuelo.add(loot);
                                }
                                listaObstaculos.removeIndex(k);
                                break; // Salimos del bucle de armas para este obstáculo ya destruido
                            }
                        }
                    }
                }
            }        } // <--- FIN DEL BLOQUE ELSE (Aquí termina la congelación)
        
       

        // 3. CÁMARA Y LÍMITES DEL MAPA
        float pjXClamped = MathUtils.clamp(jugador.getX(), 0, image.getWidth() - jugador.getAncho());
        float pjYClamped = MathUtils.clamp(jugador.getY(), 0, image.getHeight() - jugador.getAlto());
        jugador.setX(pjXClamped);
        jugador.setY(pjYClamped);

        camera.position.x = jugador.getCentroX();
        camera.position.y = jugador.getCentroY();

        float halfCamW = camera.viewportWidth / 2f;
        float halfCamH = camera.viewportHeight / 2f;
        camera.position.x = MathUtils.clamp(camera.position.x, halfCamW, image.getWidth() - halfCamW);
        camera.position.y = MathUtils.clamp(camera.position.y, halfCamH, image.getHeight() - halfCamH);
        camera.update();

        // 4. RENDERIZADO (SPRITE BATCH)
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(image, 0, 0);

     // Actualizar y dibujar objetos del suelo (Gemas, Pollos, Cofres, Imanes)
        for (int i = listaObjetosSuelo.size - 1; i >= 0; i--) {
            ObjetoSuelo objeto = listaObjetosSuelo.get(i);

            // 1. Mover hacia el jugador si está dentro del rango del imán
            jugador.getRecolector().actualizarObjeto(objeto, deltaTime, centroPjX, centroPjY);

            // 2. Comprobar si el jugador lo toca para recogerlo usando el método correcto de tu Recolector
            float distanciaJugador = Vector2.dst(centroPjX, centroPjY, objeto.getX(), objeto.getY());
            if (distanciaJugador <= 25f && !objeto.estaRecogido()) {
                jugador.getRecolector().recogerObjeto(objeto, jugador.getGestorExperiencia(), jugador.getGestorVida(),texturaCuchillo,texturaPincho,jugador);
                listaObjetosSuelo.removeIndex(i);
                continue;
            }
            switch (objeto.getTipo()) {
            case "GEMA_EXPERIENCIA": 
                if (objeto.getVariante().equals("ROJA")) {
                    texturaAElegir = texturaGemaRoja; // Asegúrate de tenerla declarada
                } else {
                    texturaAElegir = texturaGemaVerde;
                }
                break;
                
            case "CURACION":   
                if (objeto.getVariante().equals("QUESO")) {
                    texturaAElegir = texturaQueso; // Asegúrate de tenerla declarada
                } else {
                    texturaAElegir = texturaPollo;  // Asegúrate de tenerla declarada
                }
                break;
                
            case "IMAN": 			 
                texturaAElegir = texturaIman; 
                break;
                
            case "COFRE_TESORO":			 
                texturaAElegir = texturaCofre; 
                break;
        }

        batch.draw(texturaAElegir, objeto.getX(), objeto.getY());
    }

        // Enemigos, Balas y Jugador
        for (enemigos enemigo : listaEnemigos) {
            enemigo.dibujar(batch);
        }
        for (Proyectil b : listaProyectiles) {
            b.dibujar(batch);
        }

        jugador.dibujar(batch);

        // HUD Texto
        dibujarUIHUD();
        
        for (Arma arma : jugador.arsenal) {
            if (arma instanceof BolaPinchos) {
                ((BolaPinchos) arma).dibujar(batch);
            }
        }
        
        menuMejora.dibujar(batch, fuenteMenu, camera);
        
        for (Obstaculos obstaculo : listaObstaculos) {
            obstaculo.dibujar(batch);
        }
        
        batch.end();

        // 5. RENDERIZADO (SHAPE RENDERER - BARRAS SOBRE EL JUGADOR)
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        jugador.dibujarUI(shapeRenderer);
        shapeRenderer.end();
        

    }

    private void dibujarUIHUD() {
        int minutos = (int) (tiempoJuego / 60);
        int segundos = (int) (tiempoJuego % 60);
        String textoTiempo = String.format("TIEMPO: %02d:%02d", minutos, segundos);

        float textoX = camera.position.x - 380f;
        float textoY = camera.position.y + 270f;

        fuenteSurvival.setColor(Color.BLACK);
        fuenteSurvival.draw(batch, textoTiempo, textoX - 2, textoY);
        fuenteSurvival.draw(batch, textoTiempo, textoX + 2, textoY);
        fuenteSurvival.draw(batch, textoTiempo, textoX, textoY - 2);
        fuenteSurvival.draw(batch, textoTiempo, textoX, textoY + 2);

        fuenteSurvival.setColor(Color.YELLOW);
        fuenteSurvival.draw(batch, textoTiempo, textoX, textoY);

        int nivel = jugador.getGestorExperiencia().getNivelActual();
        float pantallaX = (camera.position.x - (camera.viewportWidth / 2f)) + camera.viewportWidth - 120f;
        float pantallaY = (camera.position.y - (camera.viewportHeight / 2f)) + 50f;
        fuenteTexto.draw(batch, "NIVEL: " + nivel, pantallaX, pantallaY);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        image.dispose();
        sheetEnemigo.dispose();
        texturaCuchillo.dispose();
        texturaPincho.dispose();
        if (texturaGemaVerde != null) texturaGemaVerde.dispose();
        if (texturaGemaRoja != null) texturaGemaRoja.dispose();
        if (texturaPollo != null) texturaPollo.dispose();
        if (texturaQueso != null) texturaQueso.dispose();
        if (texturaCofre != null) texturaCofre.dispose();
        if (texturaIman != null) texturaIman.dispose();
        texturaBala.dispose();
        fuenteSurvival.dispose();
        fuenteTexto.dispose();
        if (jugador != null) jugador.dispose();
        fuenteMenu.dispose();
        texturaObstaculo.dispose();
    }
}