package AREA67.proyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MenuOpciones {
    private boolean activo = false;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout layout;
    
    private Sound sonidoClick;
    private Music musicaFondo;
    private float volumenMusica = 1.0f; 

 
    private Rectangle rectBotonMenos;
    private Rectangle rectBotonMas;
    private Rectangle rectBotonVolver;

    public MenuOpciones(Sound sonidoClick) {
        this.sonidoClick = sonidoClick;
        this.shapeRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
        
        float anchoBoton = 80;
        float altoBoton = 45;
        float anchoBotonVolver = 200;

        rectBotonMenos = new Rectangle(0, 0, anchoBoton, altoBoton);
        rectBotonMas = new Rectangle(0, 0, anchoBoton, altoBoton);
        rectBotonVolver = new Rectangle(0, 0, anchoBotonVolver, altoBoton);
    }

    public boolean isActivo() {
        return activo;
    }

    public void activar(OrthographicCamera camera) {
        this.activo = true;
        
        float anchoBoton = 80;
        float altoBoton = 45;
        float anchoBotonVolver = 200;
        
        float centroX = camera.position.x;
        float centroY = camera.position.y;

        rectBotonMenos.set(centroX - 110, centroY - 20, anchoBoton, altoBoton);
        rectBotonMas.set(centroX + 30, centroY - 20, anchoBoton, altoBoton);
        rectBotonVolver.set(centroX - (anchoBotonVolver / 2f), centroY - 90, anchoBotonVolver, altoBoton);
    }

    public void desactivar() {
        this.activo = false;
    }

    public void setMusicaFondo(Music musica) {
        this.musicaFondo = musica;
        if (this.musicaFondo != null) {
            this.musicaFondo.setVolume(volumenMusica);
        }
    }

    private void actualizarVolumenMusica() {
        if (musicaFondo != null) {
            musicaFondo.setVolume(volumenMusica);
        }
        System.out.println("Volumen actualizado a: " + (int)(volumenMusica * 100) + "%");
    }

    public boolean manejarInputYRender(SpriteBatch batch, OrthographicCamera camera, BitmapFont fuente, Texture fondoMap) {
        if (!activo) return false;
        
        
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if (fondoMap != null) {
            batch.draw(fondoMap, 0, 0);
        }
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.85f, 0.1f, 0.1f, 0.85f);
        shapeRenderer.rect(rectBotonMenos.x, rectBotonMenos.y, rectBotonMenos.width, rectBotonMenos.height);
        shapeRenderer.rect(rectBotonMas.x, rectBotonMas.y, rectBotonMas.width, rectBotonMas.height);
        shapeRenderer.rect(rectBotonVolver.x, rectBotonVolver.y, rectBotonVolver.width, rectBotonVolver.height);

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(rectBotonMenos.x, rectBotonMenos.y, rectBotonMenos.width, rectBotonMenos.height);
        shapeRenderer.rect(rectBotonMas.x, rectBotonMas.y, rectBotonMas.width, rectBotonMas.height);
        shapeRenderer.rect(rectBotonVolver.x, rectBotonVolver.y, rectBotonVolver.width, rectBotonVolver.height);
        shapeRenderer.end();

        batch.begin();

        // Título del Menú
        fuente.setColor(Color.GREEN);
        fuente.getData().setScale(2.6f);
        String titulo = "CONFIGURACION";
        layout.setText(fuente, titulo);
        fuente.draw(batch, titulo, camera.position.x - (layout.width / 2f), camera.position.y + 140);

        fuente.setColor(Color.WHITE);
        fuente.getData().setScale(1.5f);
        String textoVolumen = "Volumen Musica: " + (int)(volumenMusica * 100) + "%";
        layout.setText(fuente, textoVolumen);
        fuente.draw(batch, textoVolumen, camera.position.x - (layout.width / 2f), camera.position.y + 60);

        fuente.setColor(Color.GREEN);
        
        layout.setText(fuente, "-");
        fuente.draw(batch, "-", rectBotonMenos.x + (rectBotonMenos.width - layout.width)/2f, rectBotonMenos.y + 30f);

        layout.setText(fuente, "+");
        fuente.draw(batch, "+", rectBotonMas.x + (rectBotonMas.width - layout.width)/2f, rectBotonMas.y + 30f);

        String textoVolver = "Volver";
        layout.setText(fuente, textoVolver);
        fuente.draw(batch, textoVolver, rectBotonVolver.x + (rectBotonVolver.width - layout.width)/2f, rectBotonVolver.y + 30f);

        batch.end();

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            float mouseX = touchPos.x;
            float mouseY = touchPos.y;

            if (rectBotonMenos.contains(mouseX, mouseY)) {
                if (sonidoClick != null) sonidoClick.play(1.0f);
                int porcentajeActual = Math.round(volumenMusica * 100f);
                porcentajeActual = Math.max(0, porcentajeActual - 10);
                volumenMusica = porcentajeActual / 100f;
                actualizarVolumenMusica();
            }
            else if (rectBotonMas.contains(mouseX, mouseY)) {
                if (sonidoClick != null) sonidoClick.play(1.0f);
                int porcentajeActual = Math.round(volumenMusica * 100f);
                porcentajeActual = Math.min(100, porcentajeActual + 10);
                volumenMusica = porcentajeActual / 100f;
                actualizarVolumenMusica();
            }   
            else if (rectBotonVolver.contains(mouseX, mouseY)) {
                if (sonidoClick != null) sonidoClick.play(1.0f);
                desactivar();
                return true; 
            }
        }

        return false;
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (sonidoClick != null) {
            sonidoClick.dispose();
        }
    }
}