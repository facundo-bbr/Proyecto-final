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
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuPrincipal {
    private boolean enMenuPrincipal;
    private GlyphLayout layout;
    private ShapeRenderer shapeRenderer;
    
    private Rectangle rectPlay;
    private Rectangle rectConfig;
    private Rectangle rectSalir;

    public MenuPrincipal() {
        this.enMenuPrincipal = false;
        this.layout = new GlyphLayout();
        this.shapeRenderer = new ShapeRenderer();
        
        this.rectPlay = new Rectangle(250, 310, 300, 45);
        this.rectConfig = new Rectangle(250, 240, 300, 45);
        this.rectSalir = new Rectangle(250, 170, 300, 45);
    }

    /**
     * Maneja el renderizado y los clics del menú.
     * @return 0 = Continuar en el menú, 1 = Iniciar Juego, 2 = Abrir Opciones.
     */
    public int manejarMenu(SpriteBatch batch, OrthographicCamera camera, BitmapFont fuenteMenu, Texture fondoMap) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        if (fondoMap != null) {
            batch.draw(fondoMap, 0, 0);
        }

        if (!enMenuPrincipal) {
            fuenteMenu.setColor(Color.RED);
            fuenteMenu.getData().setScale(2.5f);
            String textoTitulo = "Area 67";
            layout.setText(fuenteMenu, textoTitulo);
            float xTitulo = (camera.viewportWidth - layout.width) / 2f;
            fuenteMenu.draw(batch, textoTitulo, xTitulo, 420);

            fuenteMenu.setColor(Color.WHITE);
            fuenteMenu.getData().setScale(1.1f);
            String textoPress = "PRESS ANY KEY OR CLICK TO START";
            layout.setText(fuenteMenu, textoPress);
            float xPress = (camera.viewportWidth - layout.width) / 2f;
            fuenteMenu.draw(batch, textoPress, xPress, 300);

            if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                enMenuPrincipal = true;
            }
        } else {
            batch.end();
            
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            
            shapeRenderer.setColor(0.1f, 0.75f, 0.2f, 0.9f);
            shapeRenderer.rect(rectPlay.x, rectPlay.y, rectPlay.width, rectPlay.height);
            shapeRenderer.rect(rectConfig.x, rectConfig.y, rectConfig.width, rectConfig.height);
            shapeRenderer.rect(rectSalir.x, rectSalir.y, rectSalir.width, rectSalir.height);
            
            shapeRenderer.end();
            batch.begin();

            fuenteMenu.setColor(Color.RED);
            fuenteMenu.getData().setScale(2.2f);
            String textoMenu = "Area 67";
            layout.setText(fuenteMenu, textoMenu);
            float xMenu = (camera.viewportWidth - layout.width) / 2f;
            fuenteMenu.draw(batch, textoMenu, xMenu, 460);

            fuenteMenu.getData().setScale(1.3f);
            fuenteMenu.setColor(Color.RED);

            String textoPlay = "Juegar";
            layout.setText(fuenteMenu, textoPlay);
            fuenteMenu.draw(batch, textoPlay, rectPlay.x + (rectPlay.width - layout.width) / 2f, rectPlay.y + 32f);

            String textoConfig = "Opciones";
            layout.setText(fuenteMenu, textoConfig);
            fuenteMenu.draw(batch, textoConfig, rectConfig.x + (rectConfig.width - layout.width) / 2f, rectConfig.y + 32f);

            String textoSalir = "Salir";
            layout.setText(fuenteMenu, textoSalir);
            fuenteMenu.draw(batch, textoSalir, rectSalir.x + (rectSalir.width - layout.width) / 2f, rectSalir.y + 32f);

            // 1. Control por Teclado
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                batch.end();
                return 1; // Iniciar Juego
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                batch.end();
                return 2; // Abrir Opciones
            }

            // 2. Control por Clic del Mouse
            if (Gdx.input.justTouched()) {
                Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);

                float mouseX = touchPos.x;
                float mouseY = touchPos.y;

                if (rectPlay.contains(mouseX, mouseY)) {
                    batch.end();
                    return 1; // Iniciar Juego
                } else if (rectConfig.contains(mouseX, mouseY)) {
                    batch.end();
                    return 2; // Abrir Opciones
                } else if (rectSalir.contains(mouseX, mouseY)) {
                    Gdx.app.exit();
                }
            }
        }

        batch.end();
        return 0; // Seguir en el menú principal
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}