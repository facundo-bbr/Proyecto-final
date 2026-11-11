package AREA67.proyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    
    // Rectángulos para los 3 botones restantes
    private Rectangle rectContinuar;
    private Rectangle rectVolverMenu;
    private Rectangle rectSalir;

    public MenuPausa() {
        this.activo = false;
        this.shapeRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
        
        float anchoBoton = 320;
        float altoBoton = 50;
        
        rectContinuar = new Rectangle(0, 0, anchoBoton, altoBoton);
        rectVolverMenu = new Rectangle(0, 0, anchoBoton, altoBoton);
        rectSalir = new Rectangle(0, 0, anchoBoton, altoBoton);
    }

    public boolean isActivo() {
        return activo;
    }

    public void activar(OrthographicCamera camera) {
        this.activo = true;
        
        float anchoBoton = 320;
        float altoBoton = 50;
        
        float xBoton = camera.position.x - (anchoBoton / 2f);
        
        // Posiciones verticales centradas para los 3 botones
        float yContinuar = camera.position.y + 25;
        float yVolver = camera.position.y - 45;
        float ySalir = camera.position.y - 115;

        rectContinuar.set(xBoton, yContinuar, anchoBoton, altoBoton);
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

        // 1. DIBUJAR LOS RECTÁNGULOS DE LOS BOTONES
        shapeRenderer.setProjectionMatrix(camera.combined);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.85f);
        shapeRenderer.rect(rectContinuar.x, rectContinuar.y, rectContinuar.width, rectContinuar.height);
        shapeRenderer.rect(rectVolverMenu.x, rectVolverMenu.y, rectVolverMenu.width, rectVolverMenu.height);
        shapeRenderer.rect(rectSalir.x, rectSalir.y, rectSalir.width, rectSalir.height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(rectContinuar.x, rectContinuar.y, rectContinuar.width, rectContinuar.height);
        shapeRenderer.rect(rectVolverMenu.x, rectVolverMenu.y, rectVolverMenu.width, rectVolverMenu.height);
        shapeRenderer.rect(rectSalir.x, rectSalir.y, rectSalir.width, rectSalir.height);
        shapeRenderer.end();

        // 2. DIBUJAR LOS TEXTOS Y TÍTULO
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // TÍTULO "PAUSA"
        fuente.setColor(Color.YELLOW);
        fuente.getData().setScale(2.5f);
        String textoPausa = "PAUSA";
        layout.setText(fuente, textoPausa);
        float xPausa = camera.position.x - (layout.width / 2f);
        float yPausa = camera.position.y + 150;
        fuente.draw(batch, textoPausa, xPausa, yPausa);

        // Textos de los 3 botones centrados
        fuente.getData().setScale(1.2f);
        fuente.setColor(Color.WHITE);

        // Texto Continuar
        String textoCont = "Continuar";
        layout.setText(fuente, textoCont);
        float xCont = rectContinuar.x + (rectContinuar.width - layout.width) / 2f;
        float yContTexto = rectContinuar.y + (rectContinuar.height + layout.height) / 2f - 4;
        fuente.draw(batch, textoCont, xCont, yContTexto);

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

        // 3. DETECTAR CLICS (Reasignados a 3 opciones)
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (rectContinuar.contains(touchPos.x, touchPos.y)) {
                desactivar();
                return 1; // Continuar
            }
            if (rectVolverMenu.contains(touchPos.x, touchPos.y)) {
                desactivar();
                return 2; // Volver al menú
            }
            if (rectSalir.contains(touchPos.x, touchPos.y)) {
                return 3; // Salir
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
    }
}