package AREA67.proyecto;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class enemigos {
    private Vector2 posicion;
    private float velocidad = 120f;

    private Animation<TextureRegion> animacion;
    private float stateTime;
    private boolean mirandoDerecha = true;

    public enemigos(TextureRegion[] framesOriginales, float jugadorX, float jugadorY) {
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

    public void perseguir(float objetivoX, float objetivoY, float delta) {
        float dx = objetivoX - posicion.x;
        float dy = objetivoY - posicion.y;
        float distancia = (float) Math.sqrt(dx * dx + dy * dy);

        if (distancia > 0) {
            posicion.x += (dx / distancia) * velocidad * delta;
            posicion.y += (dy / distancia) * velocidad * delta;
            
            if (dx > 0) {
                mirandoDerecha = true;
            } else if (dx < 0) {
                mirandoDerecha = false;
            }
        }
        
        stateTime += delta;
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
}
