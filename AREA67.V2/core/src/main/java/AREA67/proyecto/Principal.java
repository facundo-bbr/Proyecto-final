package AREA67.proyecto;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    
    private float temporizadorTilin = 0f;
    
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Texture image;
    public AssetManager manager;
    private OrthographicCamera camera;

    private float tiempoJuego;
    private float intervaloJefe = 120f;       
    private float tiempoUltimoJefe = 0f;      
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
    private Texture texturaBotella;
    private Texture texturaLanzallamas;
    private Texture texturaFusil;

    private Personaje jugador;
    
    private Music musicaDeFondo;
    private Sound sonidoSubidaDeNivel;
    private Sound sonidoSeleccion;
    
    private MenuMejora menuMejora;
    private MenuDerrota menuDerrota;
    private MenuPrincipal menuPrincipal;
    private MenuPausa menuPausa;
    private MenuOpciones menuOpciones;
    
    private Array<Jefe> listaJefes= new Array<>();;
    private Array<Proyectil> listaProyectiles = new Array<>();
    private Array<Enemigos> listaEnemigos = new Array<>();
    private Array<ObjetoSuelo> listaObjetosSuelo = new Array<>();
    private Array<Obstaculos> listaObstaculos = new Array<>();

    private Texture texturaTilin;
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
    private TextureRegion[] framesTilin;
    private float temporizadorSpawneo;
    private float temporizadorObstaculo = 0f;
    
    private Texture texturaObstaculo;

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
        texturaCuchillo = new Texture("Cuchillo.png");
        texturaPincho = new Texture("Pincho.png");
        texturaPollo = new Texture("Pollo.png");
        texturaObstaculo = new Texture("Barril.png");
        texturaIman = new Texture("Iman.png");
        texturaCofre = new Texture("Cofre.png");
        texturaQueso = new Texture("Queso.png");
        texturaGemaRoja = new Texture("xprojo.png");
        texturaBolafuego = new Texture("Bolafuego.png");
        texturaLanzallamas = new Texture("Icono Lanzallamas.png");
        texturaCorazonCeda = new Texture("CorazonCeda.png");
        texturaEscudovich = new Texture("Escudovich.png");
        texturaRecatate = new Texture("Recatate.png");
        texturaColgala = new Texture("Colgala.png");
        texturaCorazon = new Texture("Corazon.png");
        texturaGemaAzul = new Texture("xpazul.png");
        texturaImancito = new Texture("Imancito.png");
        sheetCaminarJefe = new Texture("jefearea67.png");
        sheetAtaqueJefe = new Texture ("JefeAtacando.png");
        texturaBotella = new Texture ("Botella.png");
        texturaTilin= new Texture ("tilin.png");
        texturaFusil=new Texture ("Fusil.png");

        jugador = new Personaje(2048, 2048, texturaBala);
        
        musicaDeFondo = Gdx.audio.newMusic(Gdx.files.internal("Musica de fondo 1.mp3"));
        musicaDeFondo.setLooping(true);
        musicaDeFondo.setVolume(1f);
        musicaDeFondo.play();

        sonidoSubidaDeNivel = Gdx.audio.newSound(Gdx.files.internal("Sonido de subida de nivel.ogg"));
        sonidoSeleccion = Gdx.audio.newSound(Gdx.files.internal("seleccionar opcion.ogg"));
         
        

        sheetZombieVerde = new Texture("zombieverdeizq.png");
        sheetZombieRojo = new Texture("zombie der.png");
        sheetEsqueleto = new Texture("Esqueleto_der.png");

        framesZombieVerde = cortarSprite(sheetZombieVerde, 60, 60);
        framesZombieRojo = cortarSprite(sheetZombieRojo, 60, 60);
        framesEsqueleto = cortarSprite(sheetEsqueleto, 60, 60);
        arrCaminar = cortarSprite(sheetCaminarJefe, 96, 96);
        arrAtaque = cortarSprite(sheetAtaqueJefe, 96, 96);
        framesTilin = cortarSprite(texturaTilin,32,32);
        
        framesJefeCaminar = new Array<>(arrCaminar);
        framesJefeAtaque = new Array<>(arrAtaque);

        temporizadorSpawneo = 0f;
        
        fuenteMenu = new BitmapFont();
        fuenteMenu.getData().setScale(2.0f);
        
        menuMejora = new MenuMejora(texturaCuchillo, texturaPincho, texturaBolafuego, texturaColgala, texturaRecatate, texturaEscudovich, texturaCorazonCeda, texturaCorazon, texturaImancito,texturaBotella, texturaLanzallamas,texturaFusil,sonidoSubidaDeNivel,sonidoSeleccion);
        menuDerrota = new MenuDerrota(sonidoSeleccion);
        menuPrincipal = new MenuPrincipal(sonidoSeleccion);
        menuPausa = new MenuPausa(sonidoSeleccion);
        menuOpciones = new MenuOpciones(sonidoSeleccion);
        
        int cantidadObstaculos = 15;
        for (int i = 0; i < cantidadObstaculos; i++) {
            float posX = com.badlogic.gdx.math.MathUtils.random(50f, 4046f); // Margen de 50px por los bordes
            float posY = com.badlogic.gdx.math.MathUtils.random(50f, 4046f);
            listaObstaculos.add(new Obstaculos(posX, posY, texturaObstaculo, 250));
        }
    }

    @Override

	    public void render() {
	    float deltaTime = Gdx.graphics.getDeltaTime();
	    Texture texturaAElegir = texturaGemaVerde;
	    boolean juegoPausado = menuMejora.isActivo() || menuPausa.isActivo() || menuDerrota.isActivo() || menuOpciones.isActivo() || !juegoIniciado; if (menuOpciones.isActivo()) {
	    if (menuOpciones.isActivo()) {
	    	menuOpciones.manejarInputYRender(batch, camera, fuenteMenu, image);
	    	menuOpciones.setMusicaFondo(musicaDeFondo);
	    return;
	    }
	    boolean salirOpciones = menuOpciones.manejarInputYRender(batch, camera, fuenteMenu, image);
	    if (salirOpciones) {
	    }
	    	return;
	    }
	    if (menuDerrota.isActivo()) {
	    	int accionMenu = menuDerrota.manejarInputYRender(batch, camera, fuenteMenu, image);
	    	if (accionMenu == 1) {
	    		reiniciarJuego();
	    		menuDerrota.desactivar();
	    		} else if (accionMenu == 2) {
	    			Gdx.app.exit();
	    		}
	    	return;
	    	}
	    if (!juegoIniciado) {
	    	int accionMenuP = menuPrincipal.manejarMenu(batch, camera, fuenteMenu, image);
	    	if (accionMenuP == 1) {
	    		juegoIniciado = true; 
	    	} else if (accionMenuP == 2) {
	    		menuOpciones.activar(camera);
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
	    	} else if (accionPausa == 2) {
	    		reiniciarJuego();
	    		juegoIniciado = false;
	    		menuPausa.desactivar();
	    	} else if (accionPausa == 3) {
	    		Gdx.app.exit();
	    	} else if (accionPausa == 4){
	    		menuPausa.desactivar();
	    		menuOpciones.activar(camera);
	    		menuPausa.activar(camera);
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

        	if (!juegoPausado){
            tiempoJuego += deltaTime;
            
            if (tiempoJuego >= 600f) {

                temporizadorTilin += deltaTime;
                if (temporizadorTilin > 1.0f) { 
                    float anguloRandom = com.badlogic.gdx.math.MathUtils.random(0f, 360f * MathUtils.degreesToRadians);
                    float distanciaSpawn = 700f; 
                    float spawnX = centroPjX + MathUtils.cos(anguloRandom) * distanciaSpawn;
                    float spawnY = centroPjY + MathUtils.sin(anguloRandom) * distanciaSpawn;

                    spawnX = MathUtils.clamp(spawnX, 50f, 4046f);
                    spawnY = MathUtils.clamp(spawnY, 50f, 4046f);

                     listaEnemigos.add(new Tilin(framesTilin, spawnX, spawnY));
                     
                     

                     listaJefes.clear();

                    temporizadorTilin = 0f;
                }

            } else {
               
                if (tiempoJuego - tiempoUltimoJefe >= intervaloJefe) {
                    spawnearJefe();
                    tiempoUltimoJefe = tiempoJuego; 
                }

                temporizadorSpawneo += deltaTime;
                if (temporizadorSpawneo > 1.5f) {
                    int tipoEnemigo = com.badlogic.gdx.math.MathUtils.random(0, 2);
                    
                    float anguloRandom = com.badlogic.gdx.math.MathUtils.random(0f, 360f * MathUtils.degreesToRadians);
                    float distanciaSpawn = 600f; 
                    float spawnX = centroPjX + MathUtils.cos(anguloRandom) * distanciaSpawn;
                    float spawnY = centroPjY + MathUtils.sin(anguloRandom) * distanciaSpawn;

                    spawnX = MathUtils.clamp(spawnX, 50f, 4046f);
                    spawnY = MathUtils.clamp(spawnY, 50f, 4046f);

                    if (tipoEnemigo == 0) {
                        listaEnemigos.add(new ZombieVerde(framesZombieVerde, spawnX, spawnY));
                    } else if (tipoEnemigo == 1) {
                        listaEnemigos.add(new ZombieRojo(framesZombieRojo, spawnX, spawnY));
                    } else {
                        listaEnemigos.add(new Esqueleto(framesEsqueleto, spawnX, spawnY));
                    }

                    temporizadorSpawneo = 0f;
                }

                temporizadorObstaculo += deltaTime;
                if (temporizadorObstaculo >= 30.0f) {
                    float anguloRandom = com.badlogic.gdx.math.MathUtils.random(0f, 360f * MathUtils.degreesToRadians);
                    float distanciaCercana = com.badlogic.gdx.math.MathUtils.random(200f, 1000f);
                    
                    float obstaculoX = centroPjX + MathUtils.cos(anguloRandom) * distanciaCercana;
                    float obstaculoY = centroPjY + MathUtils.sin(anguloRandom) * distanciaCercana;

                    obstaculoX = MathUtils.clamp(obstaculoX, 0f, 4096f - 50f);
                    obstaculoY = MathUtils.clamp(obstaculoY, 0f, 4096f - 50f);

                    listaObstaculos.add(new Obstaculos(obstaculoX, obstaculoY, texturaObstaculo, 250));
                    temporizadorObstaculo = 0f;
                }
            }

            jugador.actualizar(deltaTime, listaEnemigos, listaProyectiles,listaJefes);
            float minX = 0f;
            float minY = 0f;
            float maxX = 4096f - jugador.getAncho();
            float maxY = 4096f - jugador.getAlto();

            jugador.setX(MathUtils.clamp(jugador.getX(), minX, maxX));
            jugador.setY(MathUtils.clamp(jugador.getY(), minY, maxY));

  

            for (int i = listaProyectiles.size - 1; i >= 0; i--) {
                Proyectil p = listaProyectiles.get(i);
                if (p instanceof ProyectilMolotov) {
                    ((ProyectilMolotov) p).actualizarConJefes(deltaTime, listaEnemigos, listaJefes);
                } else if (p instanceof Cuchillo) {
                    ((Cuchillo) p).actualizarConJefes(deltaTime, listaEnemigos, listaJefes);
                } else {
                    p.actualizar(deltaTime, listaEnemigos);
                }
                if (!p.isActivo()) {
                    listaProyectiles.removeIndex(i);
                    continue;
                }

                boolean impacto = false;

                for (int j = listaEnemigos.size - 1; j >= 0; j--) {
                    Enemigos enemigo = listaEnemigos.get(j);
                    if (p.getBounds().overlaps(enemigo.getBounds())) {
                        if (!(p instanceof ProyectilMolotov)) {
                            enemigo.recibirDano(p.getDano());
                        }
                        impacto = true;
                        
                        if (!(p instanceof ProyectilLlamarada) && !(p instanceof ProyectilMolotov)) {
                            p.destruir();
                            break;
                        }
                    }
                }
                if (p.isActivo() && listaJefes != null) {
                    for (int j = listaJefes.size - 1; j >= 0; j--) {
                        Jefe jefe = listaJefes.get(j);
                        if (jefe != null && p.getBounds().overlaps(jefe.getBounds())) {
                            if (!(p instanceof ProyectilMolotov)) {
                                jefe.recibirDano(p.getDano());
                            }
                            impacto = true;
                            
                            if (!(p instanceof ProyectilLlamarada) && !(p instanceof ProyectilMolotov)) {
                                p.destruir();
                                break;
                            }
                        }
                    }
                }

                if (p.isActivo()) {
                    for (int k = listaObstaculos.size - 1; k >= 0; k--) {
                        Obstaculos obstaculo = listaObstaculos.get(k);
                        
                        if (p.getBounds().overlaps(obstaculo.getBounds())) {
                            if (!(p instanceof ProyectilMolotov)) {
                                obstaculo.recibirDano(p.getDano());
                            }
                            impacto = true;
                            
                            if (!(p instanceof ProyectilLlamarada) && !(p instanceof ProyectilMolotov)) {
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
                
                if ((impacto && !(p instanceof ProyectilLlamarada) && !(p instanceof ProyectilMolotov)) || !p.isActivo()) {
                    listaProyectiles.removeIndex(i);
                }
            }
            
            for (int i = listaEnemigos.size - 1; i >= 0; i--) {
                Enemigos enemigo = listaEnemigos.get(i);
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

                    if (probabilidad < 0.10) {          
                        varianteGema = "ROJA";
                        valorExperiencia = 50;  
                    } else if (probabilidad < 0.35) {   
                        varianteGema = "VERDE";
                        valorExperiencia = 25;  
                    } else {                           
                        varianteGema = "AZUL";
                        valorExperiencia = 10;  
                    }

                    listaObjetosSuelo.add(new ObjetoSuelo(gemaX, gemaY, "GEMA_EXPERIENCIA", valorExperiencia, varianteGema));
                    listaEnemigos.removeIndex(i);
                    continue;
                }

                float centroEnemigoX = enemigo.getX() + (enemigo.getBounds().width / 2f);
                float centroEnemigoY = enemigo.getY() + (enemigo.getBounds().height / 2f);
                
                if (enemigo instanceof Tilin) { 
                    if (Vector2.dst(centroPjX, centroPjY, centroEnemigoX, centroEnemigoY) < 50f) {
                        jugador.recibirDano(1000); 
                    }
                }else {
                
                if (Vector2.dst(centroPjX, centroPjY, centroEnemigoX, centroEnemigoY) < 45f) {
                    jugador.recibirDano(10);
                }
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
                for (int i = listaJefes.size - 1; i >= 0; i--) {
                    Jefe jefe = listaJefes.get(i);
                    if (jefe == null) continue;

                    jefe.actualizar(deltaTime, centroPjX, centroPjY, jugador);

                    if (jefe.estaMuerto()) {
                        float centroJefeX = jefe.getX() + (jefe.getBounds().width / 2f);
                        float centroJefeY = jefe.getY() + (jefe.getBounds().height / 2f);

                        float lootX = centroJefeX - (texturaCofre.getWidth() / 2f);
                        float lootY = centroJefeY - (texturaCofre.getHeight() / 2f);
                        
                        listaObjetosSuelo.add(new ObjetoSuelo(lootX, lootY, "GEMA_EXPERIENCIA", 200, "ROJA"));
                        
                        

                        listaJefes.removeIndex(i);
                        continue;
                    }

                    float centroJefeX = jefe.getX() + (jefe.getBounds().width / 2f);
                    float centroJefeY = jefe.getY() + (jefe.getBounds().height / 2f);

                    if (Vector2.dst(centroPjX, centroPjY, centroJefeX, centroJefeY) < 60f) {
                        jugador.recibirDano(20); 
                    }
                }
            }
        
        } 
        	//------------------------------------------------------------

       
        	float mitadViewportW = camera.viewportWidth / 2f;
            float mitadViewportH = camera.viewportHeight / 2f;

            camera.position.x = MathUtils.clamp(jugador.getCentroX(), mitadViewportW, 4096f - mitadViewportW);
            camera.position.y = MathUtils.clamp(jugador.getCentroY(), mitadViewportH, 4096f - mitadViewportH);
            
            camera.update();

      
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(image, 0, 0, 4096,4096 );
        // A. Fondo del mapa infinito en bucle (Tiling)
        float anchoTextura = image.getWidth();
        float altoTextura = image.getHeight();

        float camIzq = camera.position.x - (camera.viewportWidth / 2f);
        float camAbajo = camera.position.y - (camera.viewportHeight / 2f);

        float inicioX = (float) Math.floor(camIzq / anchoTextura) * anchoTextura;
        float inicioY = (float) Math.floor(camAbajo / altoTextura) * altoTextura;

        for (float x = inicioX; x < camIzq + camera.viewportWidth + anchoTextura; x += anchoTextura) {
            for (float y = inicioY; y < camAbajo + camera.viewportHeight + altoTextura; y += altoTextura) {
            }
        }

        for (Obstaculos obstaculo : listaObstaculos) {
            obstaculo.dibujar(batch);
        }

        for (int i = listaObjetosSuelo.size - 1; i >= 0; i--) {
            ObjetoSuelo objeto = listaObjetosSuelo.get(i);

            jugador.getRecolector().actualizarObjeto(objeto, deltaTime, centroPjX, centroPjY);

            float distanciaJugador = Vector2.dst(centroPjX, centroPjY, objeto.getX(), objeto.getY());
            if (distanciaJugador <= 25f && !objeto.estaRecogido()) {
                jugador.getRecolector().recogerObjeto(objeto, jugador.getGestorExperiencia(), jugador.getGestorVida(), texturaCuchillo, texturaPincho, jugador, texturaBolafuego,texturaBotella);
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

        for (Enemigos enemigo : listaEnemigos) {
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

        for (Arma arma : jugador.arsenal) {
            if (arma instanceof BolaPinchos) {
                ((BolaPinchos) arma).dibujar(batch);
            }
        }


        dibujarUIHUD();
        
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        Gdx.gl.glEnable(com.badlogic.gdx.graphics.GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
         for (Proyectil p : listaProyectiles) {
            if (p instanceof ProyectilMolotov) {
                ((ProyectilMolotov) p).dibujarFuegoVisual(shapeRenderer);
            }
        }
         
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
        if (texturaBotella != null) {
            texturaBotella.dispose();
            texturaBotella = null;
        }
        //musica y sonido
        if (musicaDeFondo != null) {
            musicaDeFondo.dispose();
        }
        if (sonidoSubidaDeNivel != null) {
            sonidoSubidaDeNivel.dispose();
        }
        if (sonidoSeleccion != null) {
        	sonidoSeleccion.dispose();
        }
        
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

       
        jugador = new Personaje(2048, 2048, texturaBala); 

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