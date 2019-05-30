package com.me.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.me.mygdx.game.AdventureRVB;

public class TestMode implements ApplicationListener {
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Array<Rectangle> objects;
    private Rectangle object = new Rectangle();
    private Texture objectImage;
    private float time = 0;
    private float lastSpawnTime = 0;

    private int score;
    private BitmapFont font;

    private Vector3 touchCordinates = new Vector3();
    private int i;

    @Override
    public void create(){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 400);
        batch = new SpriteBatch();
        objectImage = new Texture(Gdx.files.internal("bouleverte.png"));
        objects = new Array<Rectangle>();
        font = new BitmapFont();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        objectImage.dispose();
        font.dispose();
        batch.dispose();
    }

    @Override
    public void dispose() {

    }

    private void updateGameState() {
        time += Gdx.graphics.getDeltaTime();

        if (time - lastSpawnTime > 1) {
            createNewObject();
            lastSpawnTime = time;
        }

        for(int i=0; i<objects.size; i++){
            objects.get(i).y -= 250*Gdx.graphics.getDeltaTime();
            if(objects.get(i).y + objectImage.getHeight() < 0) {
                objects.removeIndex(i);
                score--;
            }
        }
    }

    private void createNewObject() {
        object = new Rectangle();
        object.setSize(64, 64);
        object.x = MathUtils.random(0,Gdx.graphics.getWidth() - object.getWidth());
        object.y = Gdx.graphics.getHeight();
        objects.add(object);
    }

    private void processUserInput() {
        if(Gdx.input.isTouched()){
            touchCordinates.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchCordinates);
            Rectangle touchArea = new Rectangle(touchCordinates.x, touchCordinates.y, 10, 10);

            for(i=0; i<objects.size; i++){

                if(objects.get(i).overlaps(touchArea)){
                    objects.removeIndex(i);
                    score++;
                }
            }
        }

    }

    private void drawGame() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Rectangle object : objects){
            batch.draw(objectImage, object.x, object.y);
        }
        font.draw(batch, "Score : " + score, 0,Gdx.graphics.getHeight());
        batch.end();
    }


}
