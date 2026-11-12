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
import com.badlogic.gdx.Input;

public class Principal extends ApplicationAdapter {
    
    private boolean juegoIniciado = false;
    private BitmapFont fuenteMenu;
    
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    public AssetManager manager;
    private OrthographicCamera camera;

    private float tiempoJuego;
    private float intervaloJefe = 120f;        // 120 segundos = 2 minutos
    private float tiempoUltimoJefe = 0f;       // Controla cuándo apareció el último jefe
    private BitmapFont fuenteSurvival;
    private BitmapFont fuenteTexto;
    
    private Texture texturaGemaAzul;
    private Texture texturaGemaRoja;
    private Texture texturaGemaVerde;
    private Texture texturaBala;
    private Texture texturaCuchillo;
    private Texture texturaPincho;
    private Texture texturaBolafuego;
    private Texture texturaPollo;
    private Texture texturaIman;
    private Texture texturaCofre;
    private Texture texturaQueso;
    private Texture texturaCorazonCeda;
    private Texture texturaRecatate;
    private Texture texturaImancito;
    private Texture texturaColgala;
    private Texture texturaEscudovich;
    private Texture texturaCorazon;

    // Entidad Jugador
    private Personaje jugador;
    
    // Menús
    private MenuMejora menuMejora;
    private MenuDerrota menuDerrota;
    private MenuPrincipal menuPrincipal;
    private MenuPausa menuPausa;
    private MenuOpciones menuOpciones;
    
    // Listas del juego
    private Array<Jefe> listaJefes= new Array<>();;
    private Array<Proyectil> listaProyectiles = new Array<>();
    private Array<enemigos> listaEnemigos = new Array<>();
    private Array<ObjetoSuelo> listaObjetosSuelo = new Array<>();
    private Array<Obstaculos> listaObstaculos = new Array<>();

    // Recursos Enemigo
    private Texture sheetZombieRojo;
    private Texture sheetZombieVerde;
    private Texture sheetEsqueleto;
    private	Texture sheetCaminarJefe;
    private Texture sheetAtaqueJefe;
    private Array<TextureRegion> framesJefeCaminar;
    private Array<TextureRegion> framesJefeAtaque;
    private TextureRegion[] arrCaminar;
    private TextureRegion[] arrAtaque;
    private TextureRegion[] framesZombieVerde;
    private TextureRegion[] framesZombieRojo;
    private TextureRegion[] framesEsqueleto;
    private float temporizadorSpawneo;
    private float temporizadorObstaculo = 0f;
    
    private Texture texturaObstaculo;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        
        // 1. Cargar la textura y configurar el wrap mode para repetición infinita
        image = new Texture("mapa.jpg");
        image.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        manager = new AssetManager();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        tiempoJuego = 0f;
        
        fuenteSurvival = new BitmapFont();
        fuenteSurvival.getData().setScale(1.5f);
        fuenteTexto = new BitmapFont();

        texturaGemaVerde = new Texture("xpverde.png");
        texturaBala = new Texture("bala.png");
        texturaCuchillo = new Texture("Cuchillo.png");
        texturaPincho = new Texture("Pincho.png");
        texturaPollo = new Texture("Pollo.png");
        texturaObstaculo = new Texture("Barril.png");
        texturaIman = new Texture("Iman.png");
        texturaCofre = new Texture("Cofre.png");
        texturaQueso = new Texture("Queso.png");
        texturaGemaRoja = new Texture("xprojo.png");
        texturaBolafuego = new Texture("Bolafuego.png");
        texturaCorazonCeda = new Texture("CorazonCeda.png");
        texturaEscudovich = new Texture("Escudovich.png");
        texturaRecatate = new Texture("Recatate.png");
        texturaColgala = new Texture("Colgala.png");
        texturaCorazon = new Texture("Corazon.png");
        texturaGemaAzul = new Texture("xpazul.png");
        texturaImancito = new Texture("Imancito.png");
        sheetCaminarJefe = new Texture("jefearea67.png");
        sheetAtaqueJefe = sheetCaminarJefe;

        // Instancia del Personaje
        jugador = new Personaje(400, 300, texturaBala);

        // Carga de Sprites del Zombi
        sheetZombieVerde = new Texture("zombieverdeizq.png");
        sheetZombieRojo = new Texture("zombie der.png");
        sheetEsqueleto = new Texture("Esqueleto_der.png");

        framesZombieVerde = cortarSprite(sheetZombieVerde, 60, 60);
        framesZombieRojo = cortarSprite(sheetZombieRojo, 60, 60);
        framesEsqueleto = cortarSprite(sheetEsqueleto, 60, 60);
        //arrCaminar = cortarSprite(sheetCaminarJefe, 96, 96);
        //arrAtaque = cortarSprite(sheetAtaqueJefe, 96, 96);
        TextureRegion[] arrFramesProvisionales = cortarSprite(sheetCaminarJefe, 96, 96);
        
        framesJefeCaminar = new Array<>(arrFramesProvisionales);
        framesJefeAtaque = new Array<>(arrFramesProvisionales);

        temporizadorSpawneo = 0f;
        
        fuenteMenu = new BitmapFont();
        fuenteMenu.getData().setScale(2.0f);
        
        menuMejora = new MenuMejora(texturaCuchillo, texturaPincho, texturaBolafuego, texturaColgala, texturaRecatate, texturaEscudovich, texturaCorazonCeda, texturaCorazon, texturaImancito);
        menuDerrota = new MenuDerrota();
        menuPrincipal = new MenuPrincipal();
        menuPausa = new MenuPausa();
        menuOpciones = new MenuOpciones();
        
        // Generar obstáculos iniciales distribuidos libremente alrededor del centro
        int cantidadObstaculos = 15;
        for (int i = 0; i < cantidadObstaculos; i++) {
            float posX = com.badlogic.gdx.math.MathUtils.random(-800, 800);
            float posY = com.badlogic.gdx.math.MathUtils.random(-800, 800);
            listaObstaculos.add(new Obstaculos(posX, posY, texturaObstaculo, 250));
        }
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        Texture texturaAElegir = texturaGemaVerde;
        int accionMenu = menuDerrota.manejarInputYRender(batch, camera, fuenteMenu, image);
        boolean juegoPausado = menuMejora.isActivo() || menuPausa.isActivo() || menuDerrota.isActivo() || menuOpciones.isActivo() || !juegoIniciado;        if (menuOpciones.isActivo()) {
            boolean salirOpciones = menuOpciones.manejarInputYRender(batch, camera, fuenteMenu, image);
            if (salirOpciones) {
                // Vuelve al flujo normal
            }
            return;
        }
        if (menuDerrota.isActivo()) {
            if (accionMenu == 1) {
                reiniciarJuego();
                menuDerrota.desactivar();
            } else if (accionMenu == 2) {
                Gdx.app.exit();
            }
            return;
        }
        if (menuOpciones.isActivo()) {
            boolean salirOpciones = menuOpciones.manejarInputYRender(batch, camera, fuenteMenu, image);
            if (salirOpciones) {
                // Al volver de opciones, se desacriva solo
            }
            return;
        }
        if (!juegoIniciado) {
            int accionMenuP = menuPrincipal.manejarMenu(batch, camera, fuenteMenu, image);
            if (accionMenuP == 1) {
                juegoIniciado = true; // Arranca el juego
            } else if (accionMenuP == 2) {
                menuOpciones.activar(); // ¡Abre el menú de configuración/opciones!
            }
            return; 
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && juegoIniciado && !jugador.estaMuerto() && !menuMejora.isActivo()) {
            if (menuPausa.isActivo()) {
                menuPausa.desactivar();
            } else {
                menuPausa.activar(camera);
            }
        }
        
        if (menuPausa.isActivo()) {
            int accionPausa = menuPausa.manejarInputYRender(batch, camera, fuenteMenu, image);
            if (accionPausa == 1) {
                // Continuar juego
            } else if (accionPausa == 2) {
                reiniciarJuego();
                juegoIniciado = false;
                menuPausa.desactivar();
            } else if (accionPausa == 3) {
                Gdx.app.exit();
            }
            return; 
        }

        if (jugador.estaMuerto()) {
            if (!menuDerrota.isActivo()) {
                System.out.println("¡Game Over!");
                menuDerrota.activar(camera);
            }
            return;
        }

        float centroPjX = jugador.getCentroX();
        float centroPjY = jugador.getCentroY();

        if (jugador.getGestorExperiencia().tieneNivelesPendientes() && !menuMejora.isActivo()) {
            menuMejora.abrir();
        }

        // Control del Flujo: Menú de Mejora Activo vs Lógica del Juego
        	if (!juegoPausado){
            tiempoJuego += deltaTime;
            
            if (tiempoJuego - tiempoUltimoJefe >= intervaloJefe) {
                spawnearJefe();
                tiempoUltimoJefe = tiempoJuego; // Reiniciamos el conteo para los próximos 2 minutos
            }

            // Actualizar movimiento, armas y cooldowns del jugador (movimiento libre)
            jugador.actualizar(deltaTime, listaEnemigos, listaProyectiles);

            // Spawneo de enemigos en un radio dinámico alrededor de la cámara/jugador
            temporizadorSpawneo += deltaTime;
            if (temporizadorSpawneo > 1.5f) {
                int tipoEnemigo = com.badlogic.gdx.math.MathUtils.random(0, 2);
                
                float anguloRandom = com.badlogic.gdx.math.MathUtils.random(0f, 360f * MathUtils.degreesToRadians);
                float distanciaSpawn = 600f; 
                float spawnX = centroPjX + MathUtils.cos(anguloRandom) * distanciaSpawn;
                float spawnY = centroPjY + MathUtils.sin(anguloRandom) * distanciaSpawn;

                if (tipoEnemigo == 0) {
                    listaEnemigos.add(new ZombieVerde(framesZombieVerde, spawnX, spawnY));
                } else if (tipoEnemigo == 1) {
                    listaEnemigos.add(new ZombieRojo(framesZombieRojo, spawnX, spawnY));
                } else {
                    listaEnemigos.add(new Esqueleto(framesEsqueleto, spawnX, spawnY));
                }

                temporizadorSpawneo = 0f;
            }
         // --- NUEVO: Spawneo de barriles/obstáculos cada 30 segundos cerca del jugador ---
            temporizadorObstaculo += deltaTime;
            if (temporizadorObstaculo >= 30.0f) {
                // Generar en un radio cercano (por ejemplo, entre 200 y 400 píxeles de distancia del jugador)
                float anguloRandom = com.badlogic.gdx.math.MathUtils.random(0f, 360f * MathUtils.degreesToRadians);
                float distanciaCercana = com.badlogic.gdx.math.MathUtils.random(200f, 400f);
                
                float obstaculoX = centroPjX + MathUtils.cos(anguloRandom) * distanciaCercana;
                float obstaculoY = centroPjY + MathUtils.sin(anguloRandom) * distanciaCercana;

                // Añadir a la lista con su vida inicial (ej: 250)
                listaObstaculos.add(new Obstaculos(obstaculoX, obstaculoY, texturaObstaculo, 250));

                temporizadorObstaculo = 0f; // Reiniciar el temporizador a 0
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
                        
                        if (!(p instanceof ProyectilLlamarada)) {
                            p.destruir();
                            break; 
                        }
                    }
                }

                // 2. Colisión contra Obstáculos
                if (p.isActivo()) {
                    for (int k = listaObstaculos.size - 1; k >= 0; k--) {
                        Obstaculos obstaculo = listaObstaculos.get(k);
                        
                        if (p.getBounds().overlaps(obstaculo.getBounds())) {
                            obstaculo.recibirDano(p.getDano());
                            impacto = true;
                            
                            if (!(p instanceof ProyectilLlamarada)) {
                                p.destruir();
                            }

                            if (obstaculo.estaDestruido()) {
                                ObjetoSuelo loot = obstaculo.soltarObjeto();
                                if (loot != null) {
                                    listaObjetosSuelo.add(loot);
                                }
                                listaObstaculos.removeIndex(k);
                            }
                            break;
                        }
                    }
                }
                
                if ((impacto && !(p instanceof ProyectilLlamarada)) || !p.isActivo()) {
                    listaProyectiles.removeIndex(i);
                }
            }
            
            // Colisiones: Persecución y Ataque del Enemigo al Jugador
            for (int i = listaEnemigos.size - 1; i >= 0; i--) {
                enemigos enemigo = listaEnemigos.get(i);
                enemigo.actualizar(deltaTime);
                enemigo.perseguir(centroPjX, centroPjY, deltaTime, listaEnemigos); 

                if (enemigo.estaMuerto()) {
                    float centroEnemigoX = enemigo.getX() + (enemigo.getBounds().width / 2f);
                    float centroEnemigoY = enemigo.getY() + (enemigo.getBounds().height / 2f);
                    
                    float gemaX = centroEnemigoX - (texturaGemaVerde.getWidth() / 2f);
                    float gemaY = centroEnemigoY - (texturaGemaVerde.getHeight() / 2f);

                    String varianteGema = "AZUL";
                    int valorExperiencia = 10;
                    
                    double probabilidad = Math.random();

                    if (probabilidad < 0.10) {          // 10% de probabilidad (Gema Rara)
                        varianteGema = "ROJA";
                        valorExperiencia = 50;  
                    } else if (probabilidad < 0.35) {   // 25% de probabilidad (Gema Mediana - del 10% al 35%)
                        varianteGema = "VERDE";
                        valorExperiencia = 25;  
                    } else {                            // 65% restante (Gema Común)
                        varianteGema = "AZUL";
                        valorExperiencia = 10;  
                    }

                    listaObjetosSuelo.add(new ObjetoSuelo(gemaX, gemaY, "GEMA_EXPERIENCIA", valorExperiencia, varianteGema));
                    listaEnemigos.removeIndex(i);
                    continue;
                }

                float centroEnemigoX = enemigo.getX() + (enemigo.getBounds().width / 2f);
                float centroEnemigoY = enemigo.getY() + (enemigo.getBounds().height / 2f);
                
                if (Vector2.dst(centroPjX, centroPjY, centroEnemigoX, centroEnemigoY) < 45f) {
                    jugador.recibirDano(10);
                }
            }
            
            for (int k = listaObstaculos.size - 1; k >= 0; k--) {
                Obstaculos obstaculo = listaObstaculos.get(k);
                obstaculo.actualizar(deltaTime);

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
                                break;
                            }
                        }
                    }
                }
            }
            if (listaJefes != null) {
                for (int i = 0; i < listaJefes.size; i++) {
                    Jefe jefe = listaJefes.get(i);
                    if (jefe != null) {
                        jefe.actualizar(deltaTime, centroPjX, centroPjY, jugador);
                    }
                }
            }
        
        } 
        	//------------------------------------------------------------

        // =========================================================================
        // CÁMARA E INFINITO (Sin límites ni clamp, siguiendo libremente al jugador)
        // =========================================================================
        camera.position.x = jugador.getCentroX();
        camera.position.y = jugador.getCentroY();
        camera.update();

        // =========================================================================
        // 4. RENDERIZADO (SPRITE BATCH Y MAPA INFINITO EN BUCLE)
        // =========================================================================
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // A. Fondo del mapa infinito en bucle (Tiling)
        float anchoTextura = image.getWidth();
        float altoTextura = image.getHeight();

        float camIzq = camera.position.x - (camera.viewportWidth / 2f);
        float camAbajo = camera.position.y - (camera.viewportHeight / 2f);

        float inicioX = (float) Math.floor(camIzq / anchoTextura) * anchoTextura;
        float inicioY = (float) Math.floor(camAbajo / altoTextura) * altoTextura;

        for (float x = inicioX; x < camIzq + camera.viewportWidth + anchoTextura; x += anchoTextura) {
            for (float y = inicioY; y < camAbajo + camera.viewportHeight + altoTextura; y += altoTextura) {
                batch.draw(image, x, y);
            }
        }

        // B. Obstáculos del mapa
        for (Obstaculos obstaculo : listaObstaculos) {
            obstaculo.dibujar(batch);
        }

        // C. Objetos del suelo (Gemas, Pollos, Cofres, Imanes)
        for (int i = listaObjetosSuelo.size - 1; i >= 0; i--) {
            ObjetoSuelo objeto = listaObjetosSuelo.get(i);

            jugador.getRecolector().actualizarObjeto(objeto, deltaTime, centroPjX, centroPjY);

            float distanciaJugador = Vector2.dst(centroPjX, centroPjY, objeto.getX(), objeto.getY());
            if (distanciaJugador <= 25f && !objeto.estaRecogido()) {
                jugador.getRecolector().recogerObjeto(objeto, jugador.getGestorExperiencia(), jugador.getGestorVida(), texturaCuchillo, texturaPincho, jugador, texturaBolafuego);
                listaObjetosSuelo.removeIndex(i);
                continue;
            }
            
            switch (objeto.getTipo()) {
                case "GEMA_EXPERIENCIA": 
                    if (objeto.getVariante().equals("ROJA")) {
                        texturaAElegir = texturaGemaRoja; 
                    } else if (objeto.getVariante().equals("VERDE")) {
                        texturaAElegir = texturaGemaVerde;
                    } else {
                    	texturaAElegir = texturaGemaAzul;
                    }
                    break;
                    
                case "CURACION":    
                    if (objeto.getVariante().equals("QUESO")) {
                        texturaAElegir = texturaQueso; 
                    } else {
                        texturaAElegir = texturaPollo;  
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

        // D. Enemigos, Proyectiles y Jugador
        for (enemigos enemigo : listaEnemigos) {
            enemigo.dibujar(batch);
        }
        if (listaJefes != null) {
            for (int i = 0; i < listaJefes.size; i++) {
                Jefe jefe = listaJefes.get(i);
                if (jefe != null) {
                    jefe.dibujar(batch);
                }
            }
        }
        for (Proyectil b : listaProyectiles) {
            b.dibujar(batch);
        }

        jugador.dibujar(batch);

        // E. Armas adicionales (como Bola de Pinchos)
        for (Arma arma : jugador.arsenal) {
            if (arma instanceof BolaPinchos) {
                ((BolaPinchos) arma).dibujar(batch);
            }
        }

        // F. HUD de texto del juego
        dibujarUIHUD();
        
        batch.end();

        // 5. RENDERIZADO (SHAPE RENDERER - BARRAS SOBRE EL JUGADOR)
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        jugador.dibujarUI(shapeRenderer);
        if (listaJefes != null) {
            for (int i = 0; i < listaJefes.size; i++) {
                Jefe jefe = listaJefes.get(i);
                if (jefe != null) {
                    jefe.dibujarOndaExpansiva(shapeRenderer);
                }
            }
        }	
        shapeRenderer.end();

        // 6. MENÚ DE MEJORA
        if (menuMejora.isActivo()) {
            menuMejora.manejarInputYRender(batch, camera, fuenteMenu, image, jugador);
            return;
        }
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
        if (sheetZombieVerde != null) sheetZombieVerde.dispose();
        if (sheetZombieRojo != null) sheetZombieRojo.dispose();
        if (sheetEsqueleto != null) sheetEsqueleto.dispose();
        if (texturaCuchillo != null) texturaCuchillo.dispose();
        if (texturaPincho != null) texturaPincho.dispose();
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
        if (texturaObstaculo != null) {
            texturaObstaculo.dispose();
        }
        if (menuDerrota != null) menuDerrota.dispose();
        if (texturaBolafuego != null) texturaBolafuego.dispose();
        if (menuPausa != null) {
            menuPausa.dispose();
        }
        if (sheetCaminarJefe != null) sheetCaminarJefe.dispose();
        if (sheetAtaqueJefe != null && sheetAtaqueJefe != sheetCaminarJefe) sheetAtaqueJefe.dispose();
    }
    
    @Override
    public void resume() {
        super.resume();

        if (image != null) {
            image.dispose(); 
        }
        image = new Texture("mapa.jpg"); 
        image.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        
        if (menuDerrota != null) {
            menuDerrota.recargarRecursos();
        }
    }
    
    private TextureRegion[] cortarSprite(Texture sheet, int tileWidth, int tileHeight) {
        TextureRegion[][] matrizCortes = TextureRegion.split(sheet, tileWidth, tileHeight);
        int cantFrames = sheet.getWidth() / tileWidth;
        TextureRegion[] framesResultado = new TextureRegion[cantFrames];
        for (int i = 0; i < cantFrames; i++) {
            framesResultado[i] = matrizCortes[0][i];
        }
        return framesResultado;
    }
    
    private void reiniciarJuego() {
        juegoIniciado = false; 
        if (menuDerrota != null) {
            menuDerrota.desactivar();
        }

        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        float centroX = camera.viewportWidth / 2f;
        float centroY = camera.viewportHeight / 2f;
        jugador = new Personaje(centroX, centroY, texturaBala); 

        tiempoJuego = 0f; 
        temporizadorSpawneo = 0f;

        if (listaEnemigos != null) {
            listaEnemigos.clear();
        }
        
        if (listaObjetosSuelo != null) {
            listaObjetosSuelo.clear();
        }

        if (listaProyectiles != null) {
            listaProyectiles.clear();
        }
        if (listaJefes != null) {
            listaJefes.clear();
        }
        tiempoUltimoJefe = 0f;
    }
    
    private void spawnearJefe() {
        // Protección por si los arrays no se cargaron bien
        if (framesJefeCaminar == null || framesJefeAtaque == null || listaJefes == null) {
            System.out.println("Error: Las texturas del Jefe o la lista no están inicializadas.");
            return;
        }

        float offsetX = (Math.random() > 0.5f ? 1 : -1) * (camera.viewportWidth / 2f + 100);
        float offsetY = (Math.random() > 0.5f ? 1 : -1) * (camera.viewportHeight / 2f + 100);
        
        float jefeX = jugador.getX() + offsetX;
        float jefeY = jugador.getY() + offsetY;

        Jefe nuevoJefe = new Jefe(framesJefeCaminar, framesJefeAtaque, jefeX, jefeY);
        listaJefes.add(nuevoJefe);

        System.out.println("¡AVISO: Un Jefe ha aparecido en el mapa!");
    }
    
}