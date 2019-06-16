package utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class AnimationTable extends Actor {
	
	Animation<?> anim;
	float frameTime;
	int currentFrame;
	
	Texture bg, icon;
	
	float stateTime;
	
	float x, y;
	
	private int id;
	private int[] borderWidth = new int[4];
	private boolean animated;
	
	public AnimationTable(Animation<?> anim) {
		this.anim = anim;
		this.id = 0;
		frameTime = anim.getFrameDuration();
		currentFrame = 0;
		this.animated = true;
	}
	
	public AnimationTable(String src_bg, Texture icon, int borderWidth) {
		bg = new Texture(src_bg);
		this.icon = icon;
		id = 1;
		for (int i = 0; i < 4; i++) {
			this.borderWidth[i] = borderWidth;
		}
		this.animated = false;
	}
	
	public AnimationTable(String src_bg, Texture icon, int padLeft, int padBot, int padRight, int padTop) {
		bg = new Texture(src_bg);
		this.icon = icon;
		id = 1;
		this.borderWidth[0] = padLeft;
		this.borderWidth[1] = padBot;
		this.borderWidth[2] = padRight;
		this.borderWidth[3] = padTop;
		this.animated = false;
	}
	
	public AnimationTable(Texture icon) {
		this.bg = null;
		this.icon = icon;
		for (int i = 0; i < 4; i++) {
			this.borderWidth[i] = 0;
		}
		this.animated = false;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (animated && anim.getKeyFrame(stateTime) != null)
			batch.draw((TextureRegion) anim.getKeyFrame(stateTime, true), super.getX(), super.getY(), super.getWidth(), super.getHeight());
		else if (!animated && icon != null) {
			if (bg != null) {
				batch.draw(bg, super.getX(), super.getY(), super.getWidth(), super.getHeight());
				batch.draw(icon, super.getX() + borderWidth[0], super.getY() + borderWidth[1]
						, super.getWidth() - borderWidth[0] - borderWidth[2], super.getHeight() - borderWidth[1] - borderWidth[3]);
			}
			else
				batch.draw(icon, super.getX(), super.getY(), super.getWidth(), super.getHeight());
		}	
	}
	
	@Override
	public void act (float delta) {
		this.stateTime += delta;
	}
	
	public void setIcon(Texture icon) {
		this.icon = icon;
	}
	
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}
}
