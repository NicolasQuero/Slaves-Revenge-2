package menus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Entity;

public class Toolbar {
	
	BitmapFont font = new BitmapFont();
	private Entity[] entities_played;
	private Texture toolbar = new Texture("menus/toolbar.png");
	
	protected int bars_width = 80;
	protected static Texture hpBackground = new Texture("progressbars/hpBackground.png");
	protected static Texture hpForeground = new Texture("progressbars/hpForeground.png");
	protected static Texture mpForeground = new Texture("progressbars/mpForeground.png");
	protected static Texture hpBorder = new Texture("progressbars/hpBorder.png");

	public Toolbar (Entity[] entities) {
		this.entities_played = entities;
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void render (OrthographicCamera camera, SpriteBatch batch, float delta) {
		float x_cam = camera.position.x - camera.viewportWidth/2;
		float y_cam = camera.position.y - camera.viewportHeight/2;
		batch.draw(toolbar, x_cam, y_cam);
		
		int pas = 0;
		for (Entity entity : entities_played) {
			if (entity != null && entity.isPlayed()) {
				batch.draw(entity.getIcon(), x_cam + 20 + pas, y_cam + 18, 64, 64); // draw icons
				font.draw(batch, entity.getType().getId() + " - Lvl " + entity.getLvl_ent(), x_cam + 92 + pas, y_cam + 80);
				
				batch.draw(hpBackground, x_cam + 92 + pas, y_cam + 50, bars_width, 10); // draw hp bars
				batch.draw(hpForeground, x_cam + 92 + pas, y_cam + 50, bars_width * entity.getHP() / entity.getMaxHP(), 10);
				batch.draw(hpBorder, x_cam + 92 + pas, y_cam + 50, bars_width, 10);
				batch.draw(hpBackground, x_cam + 92 + pas, y_cam + 30, bars_width, 10); // draw mp bars
				batch.draw(mpForeground, x_cam + 92 + pas, y_cam + 30, bars_width * entity.getMP() / entity.getMaxMP(), 10);
				batch.draw(hpBorder, x_cam + 92 + pas, y_cam + 30, bars_width, 10);
				batch.draw(hpBackground, x_cam + 20 + pas, y_cam + 9, 160, 5); // draw xp bar
				batch.draw(mpForeground, x_cam + 20 + pas, y_cam + 9, 160 * entity.getXpBeforeNextLvl()/ entity.getXpForNextLvl(), 5);
				batch.draw(hpBorder, x_cam + 20 + pas, y_cam + 9, 160, 5);
				pas += 250;
			} else {
				font.draw(batch, "Aucun \npersonnage", x_cam + 92 + pas, y_cam + 80);
			}
		}
	}
}
