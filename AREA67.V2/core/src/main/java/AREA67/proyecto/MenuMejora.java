package AREA67.proyecto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuMejora {
    
    private boolean activo = false;
    private String[] opciones = new String[3];
    private int[] tipoMejora = new int[3]; 
    private GlyphLayout layout = new GlyphLayout();
    
    private Texture texturaCuchillo;
    private Texture texturaPincho;

    public MenuMejora(Texture texturaCuchillo, Texture texturaPincho) {
        this.texturaCuchillo = texturaCuchillo;
        this.texturaPincho = texturaPincho;
    }

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
        String[] poolDescripciones = {
            "Aumentar +20% Daño de Armas",          // Tipo 0
            "Curar Vida Completa",                  // Tipo 1
            "Aumentar +15% Velocidad de Movimiento", // Tipo 2
            "Aumentar Rango de Recoleccion de Gemas", // Tipo 3
            "Equipar Cuchillo Teledirigido",         // Tipo 4
            "Equipar Bola de Pinchos"                // Tipo 5
        };

        for (int i = 0; i < 3; i++) {
            int index = (int) (Math.random() * poolDescripciones.length);
            opciones[i] = poolDescripciones[index];
            tipoMejora[i] = index;
        }
    }

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
            cerrar(); 
        }
    }

    private void aplicarMejora(int index, Personaje jugador) {
        int tipo = tipoMejora[index];
        System.out.println("¡Mejora seleccionada: " + opciones[index] + " !");
        
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
                System.out.println("¡Vida restaurada al máximo!");
                break;
                
            case 2:
                Personaje.velocidadMovimiento += 25f;
                break;
                
            case 3:
                if (jugador.getRecolector() != null) {
                    jugador.getRecolector().mejorarRango(25f);
                }
                break;

            case 4:
                boolean equipado = false;
                for (int i = 0; i < jugador.getArsenal().length; i++) {
                    if (jugador.getArsenal()[i] == null) {
                        jugador.getArsenal()[i] = new ArmaCuchillo(texturaCuchillo);
                        equipado = true;
                        System.out.println("¡Cuchillo equipado en el espacio " + i + "!");
                        break;
                    }
                }
                if (!equipado) {
                    System.out.println("¡Arsenal lleno! No se pudo equipar el cuchillo.");
                }
                break;
                
            case 5:
                boolean equipadoBola = false;
                for (int i = 0; i < jugador.getArsenal().length; i++) {
                    if (jugador.getArsenal()[i] == null) {
                        jugador.getArsenal()[i] = new BolaPinchos(texturaPincho);
                        equipadoBola = true;
                        System.out.println("¡Bola de Pinchos equipada en el espacio " + i + "!");
                        break;
                    }
                }
                if (!equipadoBola) {
                    System.out.println("¡Arsenal lleno! No se pudo equipar la Bola de Pinchos.");
                }
                break;
        }

        // Consumimos el nivel pendiente para que el juego continúe con normalidad
        jugador.getGestorExperiencia().consumirNivelPendiente();
    }

    public void dibujar(SpriteBatch batch, BitmapFont fuente, OrthographicCamera camera) {
        if (!activo) return;

        float camX = camera.position.x;
        float camY = camera.position.y;

        fuente.setColor(Color.CYAN);
        fuente.getData().setScale(2.0f);
        String titulo = "¡SUBISTE DE NIVEL! ELIGE UNA MEJORA";
        layout.setText(fuente, titulo);
        fuente.draw(batch, titulo, camX - (layout.width / 2f), camY + 120);

        fuente.getData().setScale(1.4f);
        for (int i = 0; i < 3; i++) {
            fuente.setColor(i == 0 ? Color.YELLOW : (i == 1 ? Color.GREEN : Color.ORANGE));
            String textoOpcion = "[" + (i + 1) + "] " + opciones[i];
            layout.setText(fuente, textoOpcion);
            fuente.draw(batch, textoOpcion, camX - (layout.width / 2f), camY + 40 - (i * 50));
        }
    }
}