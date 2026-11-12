package AREA67.proyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuPrincipal {
    private boolean enMenuPrincipal;
    private GlyphLayout layout;

    public MenuPrincipal() {
         this.enMenuPrincipal = false;
        this.layout = new GlyphLayout();
    }

    /**
     * Maneja el renderizado y los clics/teclas del menú de inicio.
     * @return true si el jugador decidió iniciar el juego, false en caso contrario.
     */
    public boolean manejarMenu(SpriteBatch batch, OrthographicCamera camera, BitmapFont fuenteMenu, Texture fondoMap) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        
        // Sincronizamos la cámara
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        // Dibuja el mapa de fondo estático
        if (fondoMap != null) {
            batch.draw(fondoMap, 0, 0);
        }

        if (!enMenuPrincipal) {
            // PANTALLA 1: "Press to Play"
            fuenteMenu.setColor(Color.RED);
            fuenteMenu.getData().setScale(2.0f);
            String textoTitulo = "AREA 67 - JAVA";
            layout.setText(fuenteMenu, textoTitulo);
            float xTitulo = (camera.viewportWidth - layout.width) / 2f;
            fuenteMenu.draw(batch, textoTitulo, xTitulo, 380);

            fuenteMenu.setColor(Color.WHITE);
            fuenteMenu.getData().setScale(1.2f);
            String textoPress = "PRESS ANY KEY OR CLICK TO START";
            layout.setText(fuenteMenu, textoPress);
            float xPress = (camera.viewportWidth - layout.width) / 2f;
            fuenteMenu.draw(batch, textoPress, xPress, 300);

            // Detectar clic o tecla para pasar al menú de botones
            if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                enMenuPrincipal = true;
            }
        } else {
            // PANTALLA 2: Botones PLAY y CONFIGURACIÓN
            fuenteMenu.setColor(Color.YELLOW);
            fuenteMenu.getData().setScale(2.0f);
            String textoMenu = "MENU PRINCIPAL";
            layout.setText(fuenteMenu, textoMenu);
            float xMenu = (camera.viewportWidth - layout.width) / 2f;
            fuenteMenu.draw(batch, textoMenu, xMenu, 420);

            fuenteMenu.getData().setScale(1.5f);
            fuenteMenu.setColor(Color.WHITE);

            String textoPlay = "> PLAY <";
            layout.setText(fuenteMenu, textoPlay);
            float xPlay = (camera.viewportWidth - layout.width) / 2f;
            fuenteMenu.draw(batch, textoPlay, xPlay, 320);

            String textoConfig = "> CONFIGURACION <";
            layout.setText(fuenteMenu, textoConfig);
            float xConfig = (camera.viewportWidth - layout.width) / 2f;
            fuenteMenu.draw(batch, textoConfig, xConfig, 240);

            // 1. Control por Teclado
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                batch.end();
                return true; // ¡Arranca el juego!
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                System.out.println("Configuración abierta");
            }

            // 2. Control por Clic del Mouse mapeado con la cámara
            if (Gdx.input.justTouched()) {
                Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);

                float mouseX = touchPos.x;
                float mouseY = touchPos.y;

                if (mouseX >= 200 && mouseX <= 600) {
                    // Botón PLAY (Y: 280 a 330)
                    if (mouseY >= 280 && mouseY <= 330) {
                        batch.end();
                        return true; // ¡Arranca el juego!
                    }
                    // Botón CONFIGURACIÓN (Y: 200 a 250)
                    else if (mouseY >= 200 && mouseY <= 250) {
                        System.out.println("Configuración abierta");
                    }
                }
            }
        }

        batch.end();
        return false; // El juego sigue en el menú principal
    }
}