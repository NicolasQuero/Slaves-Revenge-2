package utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class AnimationTable extends Actor {
	
	Animation<?> anim;
	float frameTime;
	int currentFrame;
	
	Texture bg, icon;
	
	float stateTime;
	
	float x, y;
	
	private int id;
	private int borderWidth;
	
	public AnimationTable(Animation<?> anim) {
		this.anim = anim;
		this.id = 0;
		frameTime = anim.getFrameDuration();
		currentFrame = 0;
	}
	
	public AnimationTable(String src_bg, Texture icon, int borderWidth) {
		bg = new Texture(src_bg);
		this.icon = icon;
		id = 1;
		this.borderWidth = borderWidth;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (id == 0 && anim.getKeyFrame(stateTime) != null)
			batch.draw((TextureRegion) anim.getKeyFrame(stateTime, true), super.getX(), super.getY(), super.getWidth(), super.getHeight());
		else if (id == 1 && bg != null && icon != null) {
			batch.draw(bg, super.getX(), super.getY(), super.getWidth(), super.getHeight());
			batch.draw(icon, super.getX() + borderWidth, super.getY() + borderWidth, super.getWidth() - borderWidth*2, super.getHeight() - borderWidth*2);
		}
			
	}
	
	@Override
	public void act (float delta) {
		this.stateTime += delta;
	}
	
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
