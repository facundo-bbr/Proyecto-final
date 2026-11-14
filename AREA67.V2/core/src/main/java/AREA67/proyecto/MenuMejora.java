package AREA67.proyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MenuMejora {
    
    private boolean activo = false;
    private String[] opciones = new String[3];
    private int[] tipoMejora = new int[3]; 
    private GlyphLayout layout = new GlyphLayout();
    private ShapeRenderer shapeRenderer;
    
    private Sound sonidoLevelUp;
    private Sound sonidoSeleccion;
    
    private Rectangle[] rectTarjetas = new Rectangle[3];
    
    private Texture texturaCuchillo;
    private Texture texturaPincho;
    private Texture texturaBolafuego;
    private Texture texturaColgala; 
    private Texture texturaRecatate;
    private Texture texturaEscudovich;
    private Texture texturaCorazonCeda;
    private Texture texturaCorazon;
    private Texture texturaImancito;
    private Texture texturaBotella;
    private Texture texturaLanzallamas;
    private Texture texturaFusil;

    public MenuMejora(Texture texturaCuchillo, Texture texturaPincho, Texture texturaBolafuego, Texture texturaColgala,
    				  Texture texturaRecatate,Texture texturaEscudovich,Texture texturaCorazonCeda,Texture texturaCorazon,
    				  Texture texturaImancito,Texture texturaBotella, Texture texturaLanzallamas,Texture texturaFusil,Sound sonidoLevelUp,Sound sonidoSeleccion) {
        this.texturaCuchillo = texturaCuchillo;
        this.texturaPincho = texturaPincho;
        this.texturaBolafuego = texturaBolafuego;
        this.texturaColgala = texturaColgala;
        this.texturaRecatate = texturaRecatate;
        this.texturaEscudovich = texturaEscudovich;
        this.texturaCorazonCeda = texturaCorazonCeda;
        this.texturaCorazon= texturaCorazon;
        this.texturaImancito= texturaImancito;
        this.texturaBotella = texturaBotella;
        this.texturaLanzallamas = texturaLanzallamas;
        this.texturaFusil=texturaFusil;
        this.sonidoLevelUp = sonidoLevelUp;
        this.sonidoSeleccion=sonidoSeleccion;
        this.shapeRenderer = new ShapeRenderer();
        
        for (int i = 0; i < 3; i++) {
            rectTarjetas[i] = new Rectangle(0, 0, 460, 65);
        }
    }

    public void abrir() {
        this.activo = true;
        generarOpcionesAleatorias();
        if (sonidoLevelUp != null) {
            sonidoLevelUp.play(1.0f); 
        }
    }

    public void cerrar() {
        this.activo = false;
    }

    public boolean isActivo() {
        return activo;
    }

    private void generarOpcionesAleatorias() {
        String[] poolDescripciones = {
            "Recatate (+10 Daño de Armas)",          
            "Curar Vida Completa",                  
            "Mentira Colgala(+15 Velocidad de Movimiento)", 
            "Aumentar Rango de Recoleccion",        
            "Cuchillos",        
            "Bola de Pinchos",             
            "Lanzallamas",                   
            "Escudovich(Reducir Daño Recibido un 10%)",
            "Corazon de Ceda (+20 HP max)",
            "La peligroza",
            "Fusil"
        };

        com.badlogic.gdx.utils.Array<Integer> indicesDisponibles = new com.badlogic.gdx.utils.Array<>();
        for (int i = 0; i < poolDescripciones.length; i++) {
            indicesDisponibles.add(i);
        }
        indicesDisponibles.shuffle();

        for (int i = 0; i < 3; i++) {
            int index = indicesDisponibles.get(i);
            opciones[i] = poolDescripciones[index];
            tipoMejora[i] = index;
        }
    }

    private Texture obtenerIconoParaTipo(int tipo) {
        switch (tipo) {
        	case 0: return texturaRecatate;
        	case 1: return texturaCorazon;
        	case 2: return texturaColgala;
            case 4: return texturaCuchillo;
            case 5: return texturaPincho;
            case 6: return texturaLanzallamas;
            case 7: return texturaEscudovich;
            case 8: return texturaCorazonCeda;
            default: return texturaImancito; 
            case 9: return texturaBotella;
            case 10: return texturaFusil;
        }
    }

    public void manejarInputYRender(SpriteBatch batch, OrthographicCamera camera, BitmapFont fuente, Texture fondoMapa, Personaje jugador) {
        if (!activo) return;

        camera.update();
        float camX = camera.position.x;
        float camY = camera.position.y;
       

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (fondoMapa != null) {
            batch.draw(fondoMapa, 0, 0); 
        }
        batch.end();

        float anchoCard = 460;
        float altoCard = 65;
        float xCard = camX - (anchoCard / 2f);
        float[] yCards = {
            camY + 45,   
            camY - 25,   
            camY - 95    
        };

        for (int i = 0; i < 3; i++) {
            rectTarjetas[i].set(xCard, yCards[i], anchoCard, altoCard);
        }

        shapeRenderer.setProjectionMatrix(camera.combined);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.35f, 0.35f, 0.45f, 1f); 
        for (int i = 0; i < 3; i++) {
            shapeRenderer.rect(rectTarjetas[i].x, rectTarjetas[i].y, rectTarjetas[i].width, rectTarjetas[i].height);
        }
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.85f, 0.75f, 0.3f, 1f);
        for (int i = 0; i < 3; i++) {
            shapeRenderer.rect(rectTarjetas[i].x, rectTarjetas[i].y, rectTarjetas[i].width, rectTarjetas[i].height);
        }
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        fuente.setColor(Color.WHITE);
        fuente.getData().setScale(2.2f);
        String titulo = "Level Up!";
        layout.setText(fuente, titulo);
        float xTitulo = camX - (layout.width / 2f);
        float yTitulo = camY + 160;
        fuente.draw(batch, titulo, xTitulo, yTitulo);

        fuente.getData().setScale(1.1f);

        for (int i = 0; i < 3; i++) {
            float tarjetaX = rectTarjetas[i].x;
            float tarjetaY = rectTarjetas[i].y;

            Texture icono = obtenerIconoParaTipo(tipoMejora[i]);
            if (icono != null) {
                float iconoSize = 45;
                float iconoX = tarjetaX + 12;
                float iconoY = tarjetaY + (altoCard - iconoSize) / 2f;
                batch.draw(icono, iconoX, iconoY, iconoSize, iconoSize);
            }

            fuente.setColor(Color.WHITE);
            String textoOpcion = "[" + (i + 1) + "] " + opciones[i];
            
            float textoX = tarjetaX + 70;
            float textoY = tarjetaY + (altoCard / 2f) + 5;
            fuente.draw(batch, textoOpcion, textoX, textoY);
        }

        batch.end();

        int seleccion = -1;

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccion = 0;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            seleccion = 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccion = 2;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            for (int i = 0; i < 3; i++) {
                if (rectTarjetas[i].contains(touchPos.x, touchPos.y)) {
                    seleccion = i;
                    break;
                }
            }
        }

        if (seleccion != -1) {
        	if (sonidoSeleccion != null) {
                sonidoSeleccion.play(1.0f);
            }
            aplicarMejora(seleccion, jugador);
            cerrar();
        }
    }

    private void aplicarMejora(int index, Personaje jugador) {
        int tipo = tipoMejora[index];
        
        switch (tipo) {
            case 0:
                for (Arma arma : jugador.getArsenal()) {
                    if (arma != null) {
                        arma.mejorarDano(10);
                    }
                }
                break;
            case 1:
                jugador.curarAlMaximo();
                break;
            case 2:
                Personaje.velocidadMovimiento += 15f;
                break;
            case 3:
                if (jugador.getRecolector() != null) {
                    jugador.getRecolector().mejorarRango(15f);
                }
                break;
            case 4: 
                ArmaCuchillo cuchilloExistente = null;
                for (Arma arma : jugador.getArsenal()) {
                    if (arma instanceof ArmaCuchillo) {
                        cuchilloExistente = (ArmaCuchillo) arma;
                        break;
                    }
                }
                
                if (cuchilloExistente != null) {
                    cuchilloExistente.aplicarMejoraProgresiva(); 
                } else {
                    for (int i = 0; i < jugador.getArsenal().length; i++) {
                        if (jugador.getArsenal()[i] == null) {
                            jugador.getArsenal()[i] = new ArmaCuchillo(texturaCuchillo);
                            break;
                        }
                    }
                }
                break;
           
            case 5:
                BolaPinchos pinchosExistente = null;
                for (Arma arma : jugador.getArsenal()) {
                    if (arma instanceof BolaPinchos) {
                        pinchosExistente = (BolaPinchos) arma;
                        break;
                    }
                }
                
                if (pinchosExistente != null) {
                    pinchosExistente.aplicarMejoraProgresiva();
                } else {
                    for (int i = 0; i < jugador.getArsenal().length; i++) {
                        if (jugador.getArsenal()[i] == null) {
                            jugador.getArsenal()[i] = new BolaPinchos(texturaPincho);
                            break;
                        }
                    }
                }
                break;
                
            case 6:
                ArmaLanzallamas lanzallamasExistente = null;
                for (Arma arma : jugador.getArsenal()) {
                    if (arma instanceof ArmaLanzallamas) {
                        lanzallamasExistente = (ArmaLanzallamas) arma;
                        break;
                    }
                }
                
                if (lanzallamasExistente != null) {
                    lanzallamasExistente.aplicarMejoraProgresiva();
                } else {
                    for (int i = 0; i < jugador.getArsenal().length; i++) {
                        if (jugador.getArsenal()[i] == null) {
                            jugador.getArsenal()[i] = new ArmaLanzallamas(texturaBolafuego);
                            break;
                        }
                    }
                }
                break;
                
            case 7: 
                if (jugador.getContadorReducionDano() < 3) {
                    jugador.aplicarReducionDano(0.10f);
                    jugador.incrementarContadorReducionDano();
                } else {
                    System.out.println("Llegaste al limite de reduccion de daño");
                }
                break;
            case 8:
                
                if (jugador.getGestorVida() != null) {
                    jugador.getGestorVida().aumentarVidaMaxima(20);
                }
                break;
            case 9:
            	Molotov molotovExistente = null;
            	for (Arma arma : jugador.getArsenal()) { 
            	    if (arma instanceof Molotov) {
            	        molotovExistente = (Molotov) arma;
            	        break;
            	    }
            	}

            	
            	if (molotovExistente != null) {
            	    molotovExistente.aplicarMejoraProgresiva();
            	    System.out.println("¡Molotov mejorado de nivel!");
            	} else {
            	   
            	    for (int i = 0; i < jugador.getArsenal().length; i++) {
            	        if (jugador.getArsenal()[i] == null) {
            	            jugador.getArsenal()[i] = new Molotov(texturaBotella); 
            	            System.out.println("¡Nuevo Molotov añadido al arsenal!");
            	            break;
            	        }
            	    }
            	}
            	break;
            case 10:
            	Fusil rifleExistente = null;
                for (Arma arma : jugador.getArsenal()) { 
                    if (arma instanceof Fusil) {
                        rifleExistente = (Fusil) arma;
                        break;
                    }
                }

                
                if (rifleExistente != null) {
                    rifleExistente.aplicarMejoraProgresiva();
                    System.out.println("¡Rifle mejorado de nivel!");
                } else {
                   
                    for (int i = 0; i < jugador.getArsenal().length; i++) {
                        if (jugador.getArsenal()[i] == null) {
                            jugador.getArsenal()[i] = new Fusil(texturaFusil); 
                            System.out.println("¡Nuevo Rifle añadido al arsenal!");
                            break;
                        }
                    }
                }
                break;
        }

        jugador.getGestorExperiencia().consumirNivelPendiente();
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (sonidoLevelUp != null) {
            sonidoLevelUp.dispose();
        }
        if (sonidoSeleccion != null) { 
            sonidoSeleccion.dispose();
        }
    }
}