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

public class MenuPausa {
    private boolean activo;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;
    
    private Sound sonidoSeleccion;
    
    // Rectángulos para los 4 botones (Continuar, Opciones, Volver al menú, Salir)
    private Rectangle rectContinuar;
    private Rectangle rectOpciones;
    private Rectangle rectVolverMenu;
    private Rectangle rectSalir;

    public MenuPausa(Sound sonidoSeleccion) {
        this.activo = false;
        this.sonidoSeleccion = sonidoSeleccion;
        this.shapeRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
        
        float anchoBoton = 320;
        float altoBoton = 45; // Ligeramente más compactos para que entren 4 bien
        
        rectContinuar = new Rectangle(0, 0, anchoBoton, altoBoton);
        rectOpciones = new Rectangle(0, 0, anchoBoton, altoBoton);
        rectVolverMenu = new Rectangle(0, 0, anchoBoton, altoBoton);
        rectSalir = new Rectangle(0, 0, anchoBoton, altoBoton);
    }

    public boolean isActivo() {
        return activo;
    }

    public void activar(OrthographicCamera camera) {
        this.activo = true;
        
        float anchoBoton = 320;
        float altoBoton = 45;
        
        float xBoton = camera.position.x - (anchoBoton / 2f);
        
        // Posiciones verticales más cercanas entre sí para los 4 botones
        float yContinuar = camera.position.y + 40;
        float yOpciones  = camera.position.y - 10;
        float yVolver    = camera.position.y - 60;
        float ySalir     = camera.position.y - 110;

        rectContinuar.set(xBoton, yContinuar, anchoBoton, altoBoton);
        rectOpciones.set(xBoton, yOpciones, anchoBoton, altoBoton);
        rectVolverMenu.set(xBoton, yVolver, anchoBoton, altoBoton);
        rectSalir.set(xBoton, ySalir, anchoBoton, altoBoton);
    }

    public void desactivar() {
        this.activo = false;
    }

    public int manejarInputYRender(SpriteBatch batch, OrthographicCamera camera, BitmapFont fuente, Texture fondoMapa) {
        if (!activo) return 0;

        // 0. DIBUJAR EL FONDO
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (fondoMapa != null) {
            batch.draw(fondoMapa, 0, 0);
        }
        batch.end();

        // 1. DIBUJAR LOS RECTÁNGULOS DE LOS BOTONES (Fondo Rojo)
        shapeRenderer.setProjectionMatrix(camera.combined);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.1f, 0.75f, 0.2f, 0.9f); // Fondo Rojo
        shapeRenderer.rect(rectContinuar.x, rectContinuar.y, rectContinuar.width, rectContinuar.height);
        shapeRenderer.rect(rectOpciones.x, rectOpciones.y, rectOpciones.width, rectOpciones.height);
        shapeRenderer.rect(rectVolverMenu.x, rectVolverMenu.y, rectVolverMenu.width, rectVolverMenu.height);
        shapeRenderer.rect(rectSalir.x, rectSalir.y, rectSalir.width, rectSalir.height);
        shapeRenderer.end();
        


        // 2. DIBUJAR LOS TEXTOS Y TÍTULO
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // TÍTULO "PAUSA"
        fuente.setColor(Color.RED);
        fuente.getData().setScale(2.5f);
        String textoPausa = "PAUSA";
        layout.setText(fuente, textoPausa);
        float xPausa = camera.position.x - (layout.width / 2f);
        float yPausa = camera.position.y + 150;
        fuente.draw(batch, textoPausa, xPausa, yPausa);

        // Textos de los 4 botones centrados (Letras Verdes)
        fuente.getData().setScale(1.1f);
        fuente.setColor(Color.RED);

        // Texto Continuar
        String textoCont = "Continuar";
        layout.setText(fuente, textoCont);
        float xCont = rectContinuar.x + (rectContinuar.width - layout.width) / 2f;
        float yContTexto = rectContinuar.y + (rectContinuar.height + layout.height) / 2f - 4;
        fuente.draw(batch, textoCont, xCont, yContTexto);

        // Texto Opciones
        String textoOpc = "Opciones";
        layout.setText(fuente, textoOpc);
        float xOpc = rectOpciones.x + (rectOpciones.width - layout.width) / 2f;
        float yOpcTexto = rectOpciones.y + (rectOpciones.height + layout.height) / 2f - 4;
        fuente.draw(batch, textoOpc, xOpc, yOpcTexto);

        // Texto Volver al menú
        String textoVolver = "Volver al menu";
        layout.setText(fuente, textoVolver);
        float xVolver = rectVolverMenu.x + (rectVolverMenu.width - layout.width) / 2f;
        float yVolverTexto = rectVolverMenu.y + (rectVolverMenu.height + layout.height) / 2f - 4;
        fuente.draw(batch, textoVolver, xVolver, yVolverTexto);

        // Texto Salir
        String textoSalir = "Salir";
        layout.setText(fuente, textoSalir);
        float xSalir = rectSalir.x + (rectSalir.width - layout.width) / 2f;
        float ySalirTexto = rectSalir.y + (rectSalir.height + layout.height) / 2f - 4;
        fuente.draw(batch, textoSalir, xSalir, ySalirTexto);

        batch.end();

        // 3. DETECTAR CLICS (4 opciones mapeadas)
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (rectContinuar.contains(touchPos.x, touchPos.y)) {
                if (sonidoSeleccion != null) sonidoSeleccion.play(1.0f);
                desactivar();
                return 1; // Continuar
            }
            if (rectOpciones.contains(touchPos.x, touchPos.y)) {
                if (sonidoSeleccion != null) sonidoSeleccion.play(1.0f);
                // No desactivamos el menú de pausa por completo, o devolvemos un código específico (ej. 4) para abrir Opciones
                return 4; // Abrir MenuOpciones
            }
            if (rectVolverMenu.contains(touchPos.x, touchPos.y)) {
                if (sonidoSeleccion != null) sonidoSeleccion.play(1.0f);
                desactivar();
                return 2; // Volver al menú principal
            }
            if (rectSalir.contains(touchPos.x, touchPos.y)) {
                if (sonidoSeleccion != null) sonidoSeleccion.play(1.0f);
                return 3; // Salir del juego
            }
        }

        return 0;
    }

    public void recargarRecursos() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        shapeRenderer = new ShapeRenderer();
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (sonidoSeleccion != null) {
            sonidoSeleccion.dispose();
        }
    }
}