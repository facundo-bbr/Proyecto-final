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
    
    //Menus
    private MenuMejora menuMejora;
    private MenuDerrota menuDerrota;
    private MenuPrincipal menuPrincipal;
    private MenuPausa menuPausa;
    
    // Listas del juego
    private Array<Proyectil> listaProyectiles = new Array<>();
    private Array<enemigos> listaEnemigos = new Array<>();
    private Array<ObjetoSuelo> listaObjetosSuelo = new Array<>();
    private Array<Obstaculos> listaObstaculos = new Array<>();

    // Recursos Enemigo
    private Texture sheetZombieRojo;
    private Texture sheetZombieVerde;
    private Texture sheetEsqueleto;
    private TextureRegion[] framesZombieVerde;
    private TextureRegion[] framesZombieRojo;
    private TextureRegion[] framesEsqueleto;
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
        texturaBolafuego= new Texture ("Bolafuego.png");
        texturaCorazonCeda= new Texture ("CorazonCeda.png");
        texturaEscudovich = new Texture ("Escudovich.png");
        texturaRecatate= new Texture ("Recatate.png");
        texturaColgala = new Texture ("Colgala.png");
        texturaCorazon = new Texture ("Corazon.png");
        texturaGemaAzul = new Texture ("xpazul.png");
        texturaImancito= new Texture ("tilin.png");

        // Instancia del Personaje
        jugador = new Personaje(400, 300, texturaBala);

        // Carga de Sprites del Zombi
        sheetZombieVerde = new Texture ("zombieverdeizq.png");
        sheetZombieRojo = new Texture ("zombie der.png");
        sheetEsqueleto = new Texture ("Esqueleto_der.png");

        framesZombieVerde = cortarSprite(sheetZombieVerde, 60, 60);
        framesZombieRojo = cortarSprite(sheetZombieRojo, 60, 60);
        framesEsqueleto = cortarSprite(sheetEsqueleto, 60, 60);

        temporizadorSpawneo = 0f;
        
        fuenteMenu = new BitmapFont();
        fuenteMenu.getData().setScale(2.0f);
        
        menuMejora = new MenuMejora(texturaCuchillo,texturaPincho,texturaBolafuego,texturaColgala,texturaRecatate,texturaEscudovich,texturaCorazonCeda,texturaCorazon,texturaImancito);
        
        menuDerrota = new MenuDerrota();
        
        menuPrincipal = new MenuPrincipal();
        
        menuPausa = new MenuPausa();
        
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
        int accionMenu = menuDerrota.manejarInputYRender(batch, camera, fuenteMenu, image); // <--- Le pasamos 'image'
        if (menuDerrota.isActivo()) {
            if (accionMenu == 1) {
                reiniciarJuego();
                menuDerrota.desactivar();
            } else if (accionMenu == 2) {
                Gdx.app.exit();
            }
            return;
        }
       
        if (!juegoIniciado) {
            // Le pasamos los objetos necesarios y evaluamos si retorna true (pulsó Play)
            juegoIniciado = menuPrincipal.manejarMenu(batch, camera, fuenteMenu, image);
            return; // Detiene el bucle para que no corra la lógica del juego por detrás
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
	                // Continuar juego (se cierra solo)
	            } else if (accionPausa == 2) {
	                reiniciarJuego();
	                juegoIniciado = false; // Vuelve al menú principal
	                menuPausa.desactivar();
	            } else if (accionPausa == 3) {
	                Gdx.app.exit(); // Cierra el programa
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

        float centroPjX = jugador.getCentroX()+(jugador.getAncho() / 2f);
        float centroPjY = jugador.getCentroY()+(jugador.getAlto() / 2f);
     // 1. COMPROBAR SI HAY NIVELES PENDIENTES PARA ABRIR EL MENÚ
        if (jugador.getGestorExperiencia().tieneNivelesPendientes() && !menuMejora.isActivo()) {
            menuMejora.abrir();
        }

         centroPjX = jugador.getCentroX();
         centroPjY = jugador.getCentroY();

        // 2. CONTROLAR EL FLUJO: Menú Activo vs Lógica del Juego
        if (menuMejora.isActivo()) {
            // El menú está abierto: congelamos TODO el juego y solo manejamos el menú
            //menuMejora.manejarEntrada(jugador,camera);
        } else {
            // --- TODO ESTE BLOQUE SE CONGELA CUANDO EL MENÚ ESTÁ ABIERTO ---
            tiempoJuego += deltaTime; 

            // Actualizar movimiento, armas y cooldowns del jugador
            jugador.actualizar(deltaTime, listaEnemigos, listaProyectiles);


         // Spawneo de enemigos
            temporizadorSpawneo += deltaTime;
            if (temporizadorSpawneo > 1.5f) {
                // Elegimos un tipo de enemigo al azar (0, 1 o 2)
                int tipoEnemigo = com.badlogic.gdx.math.MathUtils.random(0, 2);

                if (tipoEnemigo == 0) {
                    listaEnemigos.add(new ZombieVerde(framesZombieVerde, centroPjX, centroPjY));
                } else if (tipoEnemigo == 1) {
                    listaEnemigos.add(new ZombieRojo(framesZombieRojo, centroPjX, centroPjY));
                } else {
                    listaEnemigos.add(new Esqueleto(framesEsqueleto, centroPjX, centroPjY));
                }

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
                        
                        // Si ES un ProyectilLlamarada, NO lo destruimos al golpear enemigos (atraviesa)
                        if (!(p instanceof ProyectilLlamarada)) {
                            p.destruir();
                            break; // Rompemos el bucle de enemigos porque la bala normal murió
                        }
                        // Las llamaradas continúan y pueden golpear a varios enemigos a la vez
                    }
                }

                // 2. Colisión contra Obstáculos (Solo si el proyectil sigue activo)
                if (p.isActivo()) {
                    for (int k = listaObstaculos.size - 1; k >= 0; k--) {
                        Obstaculos obstaculo = listaObstaculos.get(k);
                        
                        if (p.getBounds().overlaps(obstaculo.getBounds())) {
                            obstaculo.recibirDano(p.getDano());
                            impacto = true;
                            
                            // Opcional: ¿Quieres que el lanzallamas atraviese los obstáculos o choque con ellos?
                            // Si quieres que el lanzallamas tampoco muera con los obstáculos, envuélvelo igual:
                            if (!(p instanceof ProyectilLlamarada)) {
                                p.destruir(); // El proyectil normal desaparece al impactar
                            }

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
                if ((impacto && !(p instanceof ProyectilLlamarada)) || !p.isActivo()) {
                    listaProyectiles.removeIndex(i);
                }
            }
            
           
            
         // Colisiones: Persecución y Ataque del Enemigo al Jugador
            float objetivoX = centroPjX; // O ajusta si usas el centro exacto
            float objetivoY = centroPjY;

            for (int i = listaEnemigos.size - 1; i >= 0; i--) {
                enemigos enemigo = listaEnemigos.get(i);
                enemigo.actualizar(deltaTime);
                enemigo.perseguir(objetivoX, objetivoY, deltaTime,listaEnemigos); // Se mueven solo aquí

                if (enemigo.estaMuerto()) {
                    // Obtenemos el rectángulo de colisión o centro del enemigo actual de forma segura
                    float centroEnemigoX = enemigo.getX() + (enemigo.getBounds().width / 2f);
                    float centroEnemigoY = enemigo.getY() + (enemigo.getBounds().height / 2f);
                    
                    float gemaX = centroEnemigoX - (texturaGemaVerde.getWidth() / 2f);
                    float gemaY = centroEnemigoY - (texturaGemaVerde.getHeight() / 2f);

                    // --- DECIDIR QUÉ SUELTA EL ENEMIGO AL MORIR ---
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

                // Comprobar daño al jugador usando las dimensiones del enemigo actual
                float centroEnemigoX = enemigo.getX() + (enemigo.getBounds().width / 2f);
                float centroEnemigoY = enemigo.getY() + (enemigo.getBounds().height / 2f);
                
                if (Vector2.dst(centroPjX, centroPjY, centroEnemigoX, centroEnemigoY) < 45f) {
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

        // A. Fondo del mapa
        batch.draw(image, 0, 0);

        // B. Obstáculos del mapa
        for (Obstaculos obstaculo : listaObstaculos) {
            obstaculo.dibujar(batch);
        }

        // C. Objetos del suelo (Gemas, Pollos, Cofres, Imanes)
        for (int i = listaObjetosSuelo.size - 1; i >= 0; i--) {
            ObjetoSuelo objeto = listaObjetosSuelo.get(i);

            // 1. Mover hacia el jugador si está dentro del rango del imán
            jugador.getRecolector().actualizarObjeto(objeto, deltaTime, centroPjX, centroPjY);

            // 2. Comprobar si el jugador lo toca para recogerlo
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
                    } else {
                        texturaAElegir = texturaGemaVerde;
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
        
        // ¡IMPORTANTE! Quitamos el menuMejora.dibujar de aquí adentro para que no rompa el batch principal.

        // Cerramos el batch principal de forma limpia
        batch.end();

        // 5. RENDERIZADO (SHAPE RENDERER - BARRAS SOBRE EL JUGADOR)
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        jugador.dibujarUI(shapeRenderer);
        shapeRenderer.end();

        // =========================================================================
        // 6. MENÚ DE MEJORA (Se dibuja al final de todo y gestiona su propio batch/shapes)
        // =========================================================================
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
    }
    
    @Override
    public void resume() {
        super.resume();

        // 1. Recargar la textura del fondo principal (o mapa) si es la que se pone negra
        if (image != null) {
            image.dispose(); // Limpiamos la referencia anterior por seguridad
        }
        image = new Texture("mapa.jpg"); // Reemplaza con la ruta real de tu imagen de fondo
        
        if (menuDerrota != null) {
            menuDerrota.recargarRecursos();
        }
        // 2. Si tienes otras texturas globales críticas en Principal.java, recárgalas aquí también.
        // Por ejemplo, si el jugador o los enemigos cargan texturas estáticas globales, haz lo mismo:
        // if (texturaJugador != null) {
        //     texturaJugador.dispose();
        //     texturaJugador = new Texture("jugador.png");
        // }
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
        // 1. Volver al estado de menú principal y apagar el menú de derrota
        juegoIniciado = false; 
        if (menuDerrota != null) {
            menuDerrota.desactivar();
        }

        // 2. Recentrar la cámara en el origen del mapa
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        // 3. REINICIAR AL JUGADOR CREANDO UNA NUEVA INSTANCIA
        // (Ajusta los parámetros según lo que requiera el constructor de tu clase Jugador)
        float centroX = camera.viewportWidth / 2f;
        float centroY = camera.viewportHeight / 2f;
        jugador = new Personaje(centroX, centroY,texturaBala); // Esto resetea vida, armas y estadísticas automáticamente

        // 4. REINICIAR EL TIEMPO
        tiempoJuego = 0f; 
        temporizadorSpawneo = 0f;

        // 5. LIMPIAR LAS LISTAS DE ENTIDADES (Enemigos, objetos en suelo, etc.)
        if (listaEnemigos != null) {
            listaEnemigos.clear();
        }
        
        if (listaObjetosSuelo != null) {
            listaObjetosSuelo.clear();
        }

        // ¡AQUÍ ESTÁ LA LIMPIEZA DE PROYECTILES!
        if (listaProyectiles != null) {
            listaProyectiles.clear();
        }
    }
}