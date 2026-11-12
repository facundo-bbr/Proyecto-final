package AREA67.proyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuOpciones {
    private boolean activo;
    private GlyphLayout layout;
    private ShapeRenderer shapeRenderer;
    
    // Control de volumen (valor de 0.0f a 1.0f)
    private float volumenMusica = 0.5f; 
    private Music musicaFondo; // Referencia opcional a tu música de fondo principal

    // Botones interactivos (Menos, Más, Volver)
    private Rectangle rectBotonMenos;
    private Rectangle rectBotonMas;
    private Rectangle rectBotonVolver;

    public MenuOpciones() {
        this.activo = false;
        this.layout = new GlyphLayout();
        this.shapeRenderer = new ShapeRenderer();

        // Posiciones de los botones en la pantalla
        this.rectBotonMenos = new Rectangle(260, 290, 60, 45);
        this.rectBotonMas = new Rectangle(480, 290, 60, 45);
        this.rectBotonVolver = new Rectangle(250, 180, 300, 45);
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }

    public boolean isActivo() {
        return activo;
    }

    /**
     * Vincula la música del juego para que el volumen cambie en tiempo real.
     */
    public void setMusicaFondo(Music musica) {
        this.musicaFondo = musica;
        if (this.musicaFondo != null) {
            this.musicaFondo.setVolume(volumenMusica);
        }
    }

    public float getVolumenMusica() {
        return volumenMusica;
    }

    /**
     * Maneja el renderizado y la lógica de interacción del menú de opciones.
     * @return true si el usuario decide volver al menú anterior, false si se mantiene en opciones.
     */
    public boolean manejarInputYRender(SpriteBatch batch, OrthographicCamera camera, BitmapFont fuente, Texture fondoMap) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Dibujar mapa de fondo estático
        if (fondoMap != null) {
            batch.draw(fondoMap, 0, 0);
        }

        batch.end();

        // Dibujar formas y botones con ShapeRenderer (estilo verde y limpio)
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Fondo de los botones (Verde brillante)
        shapeRenderer.setColor(0.1f, 0.75f, 0.2f, 0.9f);
        shapeRenderer.rect(rectBotonMenos.x, rectBotonMenos.y, rectBotonMenos.width, rectBotonMenos.height);
        shapeRenderer.rect(rectBotonMas.x, rectBotonMas.y, rectBotonMas.width, rectBotonMas.height);
        shapeRenderer.rect(rectBotonVolver.x, rectBotonVolver.y, rectBotonVolver.width, rectBotonVolver.height);

        shapeRenderer.end();
        batch.begin();

        // Título del Menú
        fuente.setColor(Color.RED);
        fuente.getData().setScale(2.2f);
        String titulo = "CONFIGURACION";
        layout.setText(fuente, titulo);
        fuente.draw(batch, titulo, (camera.viewportWidth - layout.width) / 2f, 450);

        // Texto descriptivo de la música
        fuente.setColor(Color.WHITE);
        fuente.getData().setScale(1.3f);
        String textoVolumen = "Volumen Musica: " + (int)(volumenMusica * 100) + "%";
        layout.setText(fuente, textoVolumen);
        fuente.draw(batch, textoVolumen, (camera.viewportWidth - layout.width) / 2f, 360);

        // Textos dentro de los botones (-) (+) (Volver)
        fuente.setColor(Color.RED);
        
        // Botón Menos (-)
        layout.setText(fuente, "-");
        fuente.draw(batch, "-", rectBotonMenos.x + (rectBotonMenos.width - layout.width)/2f, rectBotonMenos.y + 32f);

        // Botón Más (+)
        layout.setText(fuente, "+");
        fuente.draw(batch, "+", rectBotonMas.x + (rectBotonMas.width - layout.width)/2f, rectBotonMas.y + 32f);

        // Botón Volver
        String textoVolver = "Volver";
        layout.setText(fuente, textoVolver);
        fuente.draw(batch, textoVolver, rectBotonVolver.x + (rectBotonVolver.width - layout.width)/2f, rectBotonVolver.y + 32f);

        // Control por Clic del Mouse
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            float mouseX = touchPos.x;
            float mouseY = touchPos.y;

            if (rectBotonMenos.contains(mouseX, mouseY)) {
                // Redondeamos a enteros de 10 en 10 para que baje de forma limpia (ej. 90 -> 80)
                int porcentajeActual = Math.round(volumenMusica * 100f);
                porcentajeActual = Math.max(0, porcentajeActual - 10);
                volumenMusica = porcentajeActual / 100f;
                actualizarVolumenMusica();
            }
            else if (rectBotonMas.contains(mouseX, mouseY)) {
                int porcentajeActual = Math.round(volumenMusica * 100f);
                porcentajeActual = Math.min(100, porcentajeActual + 10);
                volumenMusica = porcentajeActual / 100f;
                actualizarVolumenMusica();
            }	
            // Botón Volver
            else if (rectBotonVolver.contains(mouseX, mouseY)) {
                batch.end();
                desactivar();
                return true; // Indica que se sale de las opciones
            }
        }

        // Opcional: Control rápido con flechas del teclado
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            int porcentajeActual = Math.round(volumenMusica * 100f);
            porcentajeActual = Math.max(0, porcentajeActual - 10);
            volumenMusica = porcentajeActual / 100f;
            actualizarVolumenMusica();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            int porcentajeActual = Math.round(volumenMusica * 100f);
            porcentajeActual = Math.min(100, porcentajeActual + 10);
            volumenMusica = porcentajeActual / 100f;
            actualizarVolumenMusica();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            batch.end();
            desactivar();
            return true;
        }

        batch.end();
        return false;
    }

    private void actualizarVolumenMusica() {
        if (musicaFondo != null) {
            musicaFondo.setVolume(volumenMusica);
        }
        System.out.println("Volumen actualizado a: " + (int)(volumenMusica * 100) + "%");
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}