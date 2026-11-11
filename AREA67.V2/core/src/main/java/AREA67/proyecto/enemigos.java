package AREA67.proyecto;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class enemigos {
    protected Vector2 posicion;
    protected float velocidad;
    protected int vida;
    protected int vidaMaxima;

    protected Animation<TextureRegion> animacion;
    protected float stateTime;
    protected boolean mirandoDerecha = true;
    protected float tiempoDesdeUltimoImpacto = 0f;
    protected static final float COOLDOWN_DANIO = 0.3f;

    // Modificamos el constructor para recibir vida y velocidad iniciales
    public enemigos(TextureRegion[] framesOriginales, float jugadorX, float jugadorY, int vidaInicial, float velocidadInicial) {
        this.vida = vidaInicial;
        this.vidaMaxima = vidaInicial;
        this.velocidad = velocidadInicial;

        TextureRegion[] misFrames = new TextureRegion[framesOriginales.length];
        for (int i = 0; i < framesOriginales.length; i++) {
            misFrames[i] = new TextureRegion(framesOriginales[i]);
        }
        
        this.animacion = new Animation<TextureRegion>(0.15f, misFrames);
        this.stateTime = 0f;

        float angulo = MathUtils.random(0f, MathUtils.PI2);
        float distancia = MathUtils.random(500f, 700f);
        
        float spawnX = jugadorX + MathUtils.cos(angulo) * distancia;
        float spawnY = jugadorY + MathUtils.sin(angulo) * distancia;
        
        this.posicion = new Vector2(spawnX, spawnY);
    }
    
    public void actualizar(float delta) {
        // 1. Reducir el tiempo de inmunidad al daño (cooldown de la bola de pinchos)
        if (tiempoDesdeUltimoImpacto > 0) {
            tiempoDesdeUltimoImpacto -= delta;
        }
        
        // 2. Avanzar el tiempo de la animación aquí (y NO en perseguir)
        stateTime += delta;
    }	

    public void perseguir(float objetivoX, float objetivoY, float delta, com.badlogic.gdx.utils.Array<enemigos> todosLosEnemigos) {
        TextureRegion frameActual = animacion.getKeyFrame(stateTime, true);
        float centroEnemigoX = posicion.x + (frameActual.getRegionWidth() / 2f);
        float centroEnemigoY = posicion.y + (frameActual.getRegionHeight() / 2f);

        // 1. Vector hacia el jugador
        float dx = objetivoX - centroEnemigoX;
        float dy = objetivoY - centroEnemigoY;
        float distanciaAlJugador = (float) Math.sqrt(dx * dx + dy * dy);

        // Velocidad de movimiento real aplicada en este frame
        float movimientoX = 0;
        float movimientoY = 0;

        // Solo se acerca si está más lejos que la distancia de parada (ej: 40f)
        float distanciaMinimaJugador = 40f; 
        if (distanciaAlJugador > distanciaMinimaJugador) {
            if (distanciaAlJugador > 0) {
                movimientoX += (dx / distanciaAlJugador) * velocidad;
                movimientoY += (dy / distanciaAlJugador) * velocidad;
            }
        }

        // 2. FUERZA DE SEPARACIÓN ENTRE ENEMIGOS (Evita que se encimen)
        float fuerzaSeparacionX = 0;
        float fuerzaSeparacionY = 0;
        float radioSeparacion = 30f; // Distancia mínima que intentan mantener entre ellos

        for (int i = 0; i < todosLosEnemigos.size; i++) {
            enemigos otro = todosLosEnemigos.get(i);
            if (otro == this) continue; // No compararse a sí mismo

            float otroCentroX = otro.getX() + (otro.getBounds().width / 2f);
            float otroCentroY = otro.getY() + (otro.getBounds().height / 2f);

            float distX = centroEnemigoX - otroCentroX;
            float distY = centroEnemigoY - otroCentroY;
            float distanciaEntreEnemigos = (float) Math.sqrt(distX * distX + distY * distY);

            if (distanciaEntreEnemigos < radioSeparacion && distanciaEntreEnemigos > 0) {
                // Empujar en dirección opuesta al otro enemigo
                fuerzaSeparacionX += (distX / distanciaEntreEnemigos) * (radioSeparacion - distanciaEntreEnemigos);
                fuerzaSeparacionY += (distY / distanciaEntreEnemigos) * (radioSeparacion - distanciaEntreEnemigos);
            }
        }

        // Combinar movimiento hacia el jugador + fuerza de separación de los compañeros
        float velocidadFinalX = movimientoX + (fuerzaSeparacionX * 3f); // Multiplicador de fuerza de empuje
        float velocidadFinalY = movimientoY + (fuerzaSeparacionY * 3f);

        // Aplicar movimiento final multiplicado por delta
        posicion.x += velocidadFinalX * delta;
        posicion.y += velocidadFinalY * delta;

        // Actualizar dirección de la animación
        if (dx > 0) {
            mirandoDerecha = true;
        } else if (dx < 0) {
            mirandoDerecha = false;
        }
    }

    public void dibujar(SpriteBatch batch) {
        TextureRegion frameActual = animacion.getKeyFrame(stateTime, true);
        
        if (mirandoDerecha && frameActual.isFlipX()) {
            frameActual.flip(true, false);
        } else if (!mirandoDerecha && !frameActual.isFlipX()) {
            frameActual.flip(true, false);
        }

        batch.draw(frameActual, posicion.x, posicion.y);
    }
    
    public float getX() { 
        return posicion.x; 
    }
    
    public float getY() { 
        return posicion.y; 
    }
    
    public boolean estaMuerto() {
        return vida <= 0;
    }

    public void recibirDano(int cantidad) {
        if (estaMuerto()) return;

        // Si está en periodo de inmunidad por un golpe reciente, ignoramos el daño
        if (tiempoDesdeUltimoImpacto > 0) return;

        if (cantidad > 0 && vida > 0) {
            vida = Math.max(0, vida - cantidad);
            tiempoDesdeUltimoImpacto = COOLDOWN_DANIO; // Reiniciamos el tiempo de espera
        }
    }
    
    public Rectangle getBounds() {
        TextureRegion frameActual = animacion.getKeyFrame(stateTime, true);
        float ancho = frameActual.getRegionWidth();
        float alto = frameActual.getRegionHeight();
        
        // Debe usar posicion.x y posicion.y directamente (esquina inferior izquierda)
        return new Rectangle(posicion.x, posicion.y, ancho, alto);
    }
}