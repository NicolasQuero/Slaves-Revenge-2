package menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class HPbar {
	
	public ProgressBarStyle barStyle;
	public ProgressBar bar;
	protected Stage stage;
	public ProgressBar healthBar;
	
	
	public HPbar () {

		Pixmap pixmap = new Pixmap(100, 20, Format.RGBA8888);
		pixmap.setColor(Color.RED);
		pixmap.fill();
		TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();
		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = drawable;
		
		pixmap = new Pixmap(0, 20, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();
		progressBarStyle.knob = drawable;
		 
		pixmap = new Pixmap(100, 20, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();
		progressBarStyle.knobBefore = drawable;

		stage = new Stage();
		
		healthBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, progressBarStyle);
		healthBar.setValue(1.0f);
		healthBar.setAnimateDuration(0.25f);
		healthBar.setBounds(10, 10, 100, 20);
		
		stage.addActor(healthBar);
	}
	
	public void render (OrthographicCamera camera, SpriteBatch batch, float delta) {
		stage.draw();
		stage.act();
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
