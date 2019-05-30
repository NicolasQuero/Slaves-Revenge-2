package menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import combats.Combat;
import combats.CombatTD;
import entities.Entity;

public class CombatToolbar {
	BitmapFont font = new BitmapFont();
	private Entity[] entities_played;
	private int selected_entity;
	private Texture toolbar_background = new Texture("menus/toolbar_combat.png");
	
	protected int bars_width = 80;
	protected static Texture hpBackground = new Texture("progressbars/hpBackground.png");
	protected static Texture hpForeground = new Texture("progressbars/hpForeground.png");
	protected static Texture mpForeground = new Texture("progressbars/mpForeground.png");
	protected static Texture enForeground = new Texture("progressbars/enForeground.png");
	protected static Texture hpBorder = new Texture("progressbars/hpBorder.png");
	
	private Texture selected_entity_image = new Texture("combat_decors/select_charac_combat.png");

	public CombatToolbar (Entity[] entities) {
		this.entities_played = entities;
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.selected_entity = 0;
	}
	
	public void render (OrthographicCamera camera, SpriteBatch batch, float delta, CombatTD combat) {
		//xDraw = camera.position.x - camera.viewportWidth; yDraw = camera.position.y - camera.viewportHeight;
		batch.draw(toolbar_background, 0, 0);
		
		int pas = 0;
		for (Entity entity : entities_played) {
			if (entity != null && entity.isPlayed()) {
				batch.draw(entity.getIcon(), /*xDraw +*/40 + pas, /*yDraw +*/49, 64, 64); // draw icons
				font.draw(batch, entity.getType().getId() + " - Lvl " + entity.getLvl_ent(), /*xDraw +*/112 + pas, /*yDraw +*/111);
				
				batch.draw(hpBackground, /*xDraw +*/112 + pas, /*yDraw +*/81, bars_width, 10); // draw hp bars
				batch.draw(hpForeground, /*xDraw +*/112 + pas, /*yDraw +*/81, bars_width * entity.getHP() / entity.getMaxHP(), 10);
				batch.draw(hpBorder, /*xDraw +*/112 + pas, /*yDraw +*/81, bars_width, 10);
				batch.draw(hpBackground, /*xDraw +*/112 + pas, /*yDraw +*/61, bars_width, 10); // draw mp bars
				batch.draw(mpForeground, /*xDraw +*/112 + pas, /*yDraw +*/61, bars_width * entity.getMP() / entity.getMaxMP(), 10);
				batch.draw(hpBorder, /*xDraw +*/112 + pas, /*yDraw +*/61, bars_width, 10);
				pas += 250;
			} else {
				font.draw(batch, "Aucun \npersonnage", /*xDraw +*/112 + pas, /*yDraw +*/111);
			}
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			if (selected_entity == 0) {
				if (entities_played[1] != null)
					selected_entity = 1;
				else if (entities_played[2] != null)
					selected_entity = 2;
			}
			else if (selected_entity == 1) {
				if (entities_played[2] != null)
					selected_entity = 2;
				else if (entities_played[0] != null)
					selected_entity = 0;
				
			}
			else if (selected_entity == 2) {
				if (entities_played[0] != null)
					selected_entity = 0;
				else if (entities_played[1] != null)
					selected_entity = 1;
			}
			combat.setSelectedEntity(selected_entity);
		}
		batch.draw(selected_entity_image, /*xDraw +*/14 + selected_entity * 250, /*yDraw +*/25);
		if (entities_played[selected_entity].getSpells() != null) {
			for (int i = 0; i < 4; i++) {
				if (entities_played[selected_entity].getSpells()[i] != null)
					entities_played[selected_entity].getSpells()[i].drawIcon(batch, delta, /*xDraw +*/825 + i * 114, /*yDraw +*/33);
			}
		}
	}
	
	public void drawHpAtPos(SpriteBatch batch, Entity entity, float x, float y, int width, int height) {
		batch.draw(hpBackground, x, y, width, height); // draw hp bars
		batch.draw(hpForeground, x, y, width * entity.getHP() / entity.getMaxHP(), height);
		batch.draw(hpBorder, x, y, width, height);
	}
	
	public void drawMpAtPos(SpriteBatch batch, Entity entity, float x, float y, int width, int height) {
		batch.draw(hpBackground, x, y, width, height); // draw hp bars
		batch.draw(mpForeground, x, y, width * entity.getMP() / entity.getMaxMP(), height);
		batch.draw(hpBorder, x, y, width, height);
	}
	
	public void drawEnAtPos(SpriteBatch batch, Entity entity, float x, float y, int width, int height) {
		batch.draw(hpBackground, x, y, width, height); // draw hp bars
		batch.draw(enForeground, x, y, width * entity.getEnergy() / 4f, height);
		batch.draw(hpBorder, x, y, width, height);
	}
}
