package AREA67.proyecto;

import com.badlogic.gdx.Gdx;
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

public class MenuDerrota {
	
	private Sound sonidoSeleccion;
	
    private Rectangle rectVolverMenu;
    private Rectangle rectSalir;
    private ShapeRenderer shapeRenderer;
    private boolean activo;
    private GlyphLayout layout;

    public MenuDerrota(Sound sonidoSeleccion) {
        this.activo = false;
        this.sonidoSeleccion=sonidoSeleccion;
        this.shapeRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
        rectVolverMenu = new Rectangle(0, 0, 320, 50);
        rectSalir = new Rectangle(0, 0, 320, 50);
    }

    public void activar(OrthographicCamera camera) {
        this.activo = true;
        
        float anchoBoton = 320;
        float altoBoton = 50;
        
        float xBoton = camera.position.x - (anchoBoton / 2f);
        float yVolver = camera.position.y + 15;
        float ySalir = camera.position.y - 65;

        rectVolverMenu.set(xBoton, yVolver, anchoBoton, altoBoton);
        rectSalir.set(xBoton, ySalir, anchoBoton, altoBoton);
    }

    public void desactivar() {
        this.activo = false;
    }

    public boolean isActivo() {
        return activo;
    }

    public int manejarInputYRender(SpriteBatch batch, OrthographicCamera camera, BitmapFont fuente, Texture fondoMapa) {
        if (!activo) return 0;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (fondoMapa != null) {
            batch.draw(fondoMapa, 0, 0); 
        }
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.85f); 
        shapeRenderer.rect(rectVolverMenu.x, rectVolverMenu.y, rectVolverMenu.width, rectVolverMenu.height);
        shapeRenderer.rect(rectSalir.x, rectSalir.y, rectSalir.width, rectSalir.height);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(rectVolverMenu.x, rectVolverMenu.y, rectVolverMenu.width, rectVolverMenu.height);
        shapeRenderer.rect(rectSalir.x, rectSalir.y, rectSalir.width, rectSalir.height);
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        fuente.setColor(Color.RED);
        fuente.getData().setScale(2.5f);
        String textoPerdiste = "PERDISTE";
        layout.setText(fuente, textoPerdiste);
        float xPerdiste = camera.position.x - (layout.width / 2f);
        float yPerdiste = camera.position.y + 130;
        fuente.draw(batch, textoPerdiste, xPerdiste, yPerdiste);

        fuente.getData().setScale(1.2f);

        fuente.setColor(Color.WHITE);
        String textoVolver = "Volver al menu";
        layout.setText(fuente, textoVolver);
        float xVolver = rectVolverMenu.x + (rectVolverMenu.width - layout.width) / 2f;
        float yVolverTexto = rectVolverMenu.y + (rectVolverMenu.height + layout.height) / 2f - 4;
        fuente.draw(batch, textoVolver, xVolver, yVolverTexto);

        String textoSalir = "Salir";
        layout.setText(fuente, textoSalir);
        float xSalir = rectSalir.x + (rectSalir.width - layout.width) / 2f;
        float ySalirTexto = rectSalir.y + (rectSalir.height + layout.height) / 2f - 4;
        fuente.draw(batch, textoSalir, xSalir, ySalirTexto);

        batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (rectVolverMenu.contains(touchPos.x, touchPos.y)) {
            	 if (sonidoSeleccion != null) {
                     sonidoSeleccion.play(1.0f);
                 }
                return 1;
            }
            if (rectSalir.contains(touchPos.x, touchPos.y)) {
            	 if (sonidoSeleccion != null) {
                     sonidoSeleccion.play(1.0f);
                 }
                return 2;
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