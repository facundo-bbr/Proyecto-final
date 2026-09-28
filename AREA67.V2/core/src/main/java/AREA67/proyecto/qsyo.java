package AREA67.proyecto;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Principal extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
	public AssetManager manager;
	private Texture img;
	private OrthographicCamera camera;
	private float pjX;
    private float pjY;
    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("mapa.jpg");
        manager = new AssetManager();
        img = new Texture(Gdx.files.internal("tilin.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        pjX = 400;
        pjY = 300;
    }


    public void render() {
float speed = 200f * Gdx.graphics.getDeltaTime();
        
    
        if (Gdx.input.isKeyPressed(Input.Keys.W)) pjY += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) pjY -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) pjX -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) pjX += speed;

      
        pjX = MathUtils.clamp(pjX, 0, image.getWidth() - img.getWidth());
        pjY = MathUtils.clamp(pjY, 0, image.getHeight() - img.getHeight());

      
        camera.position.x = pjX + (img.getWidth() / 2f);
        camera.position.y = pjY + (img.getHeight() / 2f);

       
        float halfCameraWidth = camera.viewportWidth / 2f;
        float halfCameraHeight = camera.viewportHeight / 2f;
        
        camera.position.x = MathUtils.clamp(camera.position.x, halfCameraWidth, image.getWidth() - halfCameraWidth);
        camera.position.y = MathUtils.clamp(camera.position.y, halfCameraHeight, image.getHeight() - halfCameraHeight);

   
        camera.update();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
     
        batch.draw(image, 0, 0); 
        batch.draw(img, pjX, pjY); 
        
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        img.dispose();
    }
}

