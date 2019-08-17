package buildings;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import entities.Entity;

public abstract class Building extends Actor {
	
	private int WIDTH;
	private int HEIGHT;
	private Rectangle hitbox;
	private Stage stage;
	private Entity player;
	ShapeRenderer shapeRenderer;
	
	protected boolean entered;
	private boolean displayText;

	public Building(float x, float y, int width, int height, String id, Stage stage, Entity player) {
		this.setPosition(x, y);
		this.setWidth(width);
		this.setHeight(height);
		this.hitbox = new Rectangle(x-20, y-20, width+40, height+40);
		this.stage = stage;
		this.player = player;
		entered = false;
		displayText = false;
		this.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("Vous entrez dans " + id);
					return false;
				}
		});
		stage.addActor(this);
		shapeRenderer = new ShapeRenderer();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		/*shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
		float x_cam = stage.getCamera().position.x - stage.getCamera().viewportWidth/2;
		float y_cam = stage.getCamera().position.y - stage.getCamera().viewportHeight/2;
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.line(getX() - x_cam, getY() - y_cam,
				getX() + getWidth() - x_cam, getY() - y_cam);
		shapeRenderer.line(getX() + getWidth() - x_cam,
				getY() - y_cam, getX() + getWidth() - x_cam, getY() + getHeight() - y_cam);
		shapeRenderer.line(getX() + getWidth() - x_cam,
				getY() + getHeight() - y_cam, getX() - x_cam, getY() + getHeight() - y_cam);
		shapeRenderer.line(getX() - x_cam, getY() + getHeight() - y_cam,
				getX() - x_cam, getY() - y_cam);
		shapeRenderer.end();*/
		if (entered) {
			
		}
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public abstract void enterBuilding();
	
	
}
