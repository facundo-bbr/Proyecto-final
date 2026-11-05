package AREA67.proyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MenuMejora {
    
    private boolean activo = false;
    private String[] opciones = new String[3];
    private int[] tipoMejora = new int[3]; // Código para saber qué hace cada opción
    
    private GlyphLayout layout = new GlyphLayout();

    // Activa el menú y genera 3 opciones aleatorias
    public void abrir() {
        this.activo = true;
        generarOpcionesAleatorias();
    }

    public void cerrar() {
        this.activo = false;
    }

    public boolean isActivo() {
        return activo;
    }

    private void generarOpcionesAleatorias() {
        // Pool exclusivo de mejoras pasivas (sin lanzallamas por ahora)
        String[] poolDescripciones = {
            "Aumentar +20% Daño de Armas",
            "Curar Vida Completa",
            "Aumentar +15% Velocidad de Movimiento",
            "Aumentar Rango de Recoleccion de Gemas"
        };

        for (int i = 0; i < 3; i++) {
            int index = (int) (Math.random() * poolDescripciones.length);
            opciones[i] = poolDescripciones[index];
            tipoMejora[i] = index;
        }
    }

    // Gestionar la entrada del jugador (Teclado 1, 2, 3 o Clic)
    public void manejarEntrada(Personaje jugador) {
        if (!activo) return;

        int seleccion = -1;

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            seleccion = 0;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            seleccion = 1;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            seleccion = 2;
        }

        if (seleccion != -1) {
            aplicarMejora(seleccion, jugador);
            cerrar(); // Cerramos el menú y reanudamos el juego
        }
    }

    private void aplicarMejora(int index, Personaje jugador) {
        int tipo = tipoMejora[index];
        System.out.println("¡Mejora seleccionada: " + opciones[index] + " !");
        
        switch (tipo) {
            case 0:
                // Aumentar daño (puedes implementarlo iterando las armas de tu arsenal si lo deseas)
                for (Arma arma : jugador.arsenal) {
                    if (arma != null) {
                        // Si tus armas tienen un modificador de daño, puedes subirlo aquí
                    }
                }
                break;
                
            case 1:
                // --- CURACIÓN COMPLETA ---
                jugador.curarAlMaximo();
                System.out.println("¡Vida restaurada al máximo!");
                break;
                
            case 2:
                // Aumentar velocidad de movimiento pasiva
                Personaje.velocidadMovimiento += 25f;
                break;
                
            case 3:
                // Aumentamos el radio del recolector pasivamente
                if (jugador.getRecolector() != null) {
                    jugador.getRecolector().mejorarRango(25f); // Sube 25 píxeles el radio de recolección
                }
                break;
        }
    }

    // Dibujar la interfaz del menú en pantalla centrada en la cámara
    public void dibujar(SpriteBatch batch, BitmapFont fuente, OrthographicCamera camera) {
        if (!activo) return;

        float camX = camera.position.x;
        float camY = camera.position.y;

        // Título del Menú
        fuente.setColor(Color.CYAN);
        fuente.getData().setScale(2.0f);
        String titulo = "¡SUBISTE DE NIVEL! ELIGE UNA MEJORA";
        layout.setText(fuente, titulo);
        fuente.draw(batch, titulo, camX - (layout.width / 2f), camY + 120);

        // Opciones
        fuente.getData().setScale(1.4f);
        for (int i = 0; i < 3; i++) {
            fuente.setColor(i == 0 ? Color.YELLOW : (i == 1 ? Color.GREEN : Color.ORANGE));
            String textoOpcion = "[" + (i + 1) + "] " + opciones[i];
            layout.setText(fuente, textoOpcion);
            fuente.draw(batch, textoOpcion, camX - (layout.width / 2f), camY + 40 - (i * 50));
        }
    }
}