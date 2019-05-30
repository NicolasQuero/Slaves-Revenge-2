package decors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Voiture {

	protected Vector2 pos;
	
	private static final float CAR_ANIM_SPEED = 0.125f;
	private static final int CAR_WIDTH = 64;
	private static final int CAR_HEIGHT = 64;
	
	protected Animation<?>[] image;

	private float stateTime;

	private int img_count;
	
	public Voiture(float x, float y) {
		stateTime = 0f;
		this.pos = new Vector2(x,y);
		image = new Animation[5];
		TextureRegion[][] carSpriteSheet = TextureRegion.split(new Texture("decors/voitures/voiture_106/voiture_106.png"), 32, 32);
		
		image[0] = new Animation<TextureRegion>(CAR_ANIM_SPEED, carSpriteSheet[0]); // Animation droite
		image[1] = new Animation<TextureRegion>(CAR_ANIM_SPEED, carSpriteSheet[1]); // Animation immobile
		
		img_count = 0;
		
		this.setImg(image);
	}

	public void render(SpriteBatch batch, OrthographicCamera camera, float delta) {
		if (image[img_count] != null)
			batch.draw((TextureRegion) image[img_count].getKeyFrame(stateTime, true),  pos.x, pos.y, CAR_WIDTH, CAR_HEIGHT);
		stateTime += delta;
	}
	
	private void setImg(Animation<?>[] image) {
		this.image = image;
	}
	
	public void moveX(float amount) {
		pos.x += amount;
	}
	
	public void moveY(float amount) {
		pos.y += amount;
	}
}
