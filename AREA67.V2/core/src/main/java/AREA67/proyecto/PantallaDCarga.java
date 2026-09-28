package AREA67.proyecto;

import com.badlogic.gdx.Gdx; // Importación necesaria para detectar el input
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaDCarga implements Screen {
    private Principal juego;
    private boolean estaCargando = true;
    private SpriteBatch batch;
    private BitmapFont font;
    
    // 1. Declaramos la variable para la imagen de fondo/logo
    private Texture imagenFondo; 

    public PantallaDCarga(Principal juego) {
        this.juego = juego;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        
        // 2. Cargamos la imagen directamente (asegurate de tener "fondo.png" en assets)
        this.imagenFondo = new Texture("fondo.png");
        
        this.juego.manager.load("jugador.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (estaCargando) {
            // ESTADO 1: Carga de recursos
            if (juego.manager.update()) {
                estaCargando = false;
            } else {
                float progreso = juego.manager.getProgress();
                batch.begin();
                // Dibujamos la imagen de fondo mientras carga ocupando toda la pantalla
                batch.draw(imagenFondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                font.draw(batch, "Cargando: " + (int)(progreso * 100) + "%", 100, 100);
                batch.end();
            }
        } else {
            // ESTADO 2: Carga finalizada, esperando interacción
            batch.begin();
            // Dibujamos la imagen
            batch.draw(imagenFondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            
            // 3. Modificamos el texto para indicar la acción
            font.draw(batch, "¡Carga lista! Toca la pantalla para continuar", 100, 150);
            batch.end();

            // 4. Detectamos si el usuario hizo clic o tocó la pantalla
            if (Gdx.input.justTouched()) {
                // 5. Cambiamos a la pantalla del menú principal
                // Descomentá la siguiente línea cuando tengas creada la clase MenuPrincipal
                // juego.setScreen(new MenuPrincipal(juego));
                
                // 6. Liberamos la memoria de la pantalla de carga
                this.dispose();
            }
        }
    }

	@Override
	public void show() {}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
        // 7. Es crucial liberar la textura manual que creamos en el constructor
        batch.dispose();
        font.dispose();
        imagenFondo.dispose();
	}
}