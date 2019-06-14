package menus;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.List;

import entities.Entity;
import world.GameMap;

public class ChooseCharac implements InputProcessor {
	
	OrthographicCamera camera;
	Vector3 touchUpcoords;
	
	ArrayList<Entity> entities;
	Rectangle[] entity_image_hitboxes; // images' hitboxes on the right
	Rectangle[] entity_squad_hitboxes; // images' hitboxes in the squad
	Entity[] selected_entity; //0 is the dragged entity, 1 is the target entity
	Vector3 touchPos = new Vector3();
	BitmapFont font = new BitmapFont();
	private Texture choose_charac_img = new Texture("menus/choose_charac_full.png");
	
	float xDraw, yDraw;
	
	List<Entity> menu_test;
	
	public ChooseCharac (ArrayList<Entity> entities) {
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.entities = entities;
		this.entity_squad_hitboxes = new Rectangle[3];
		int index_played = 0;
		for (int i = 0; i < 3; i++) { // hitboxes of the played entities
			entity_squad_hitboxes[i] = new Rectangle(240 + index_played, 350, 128, 128);
			index_played += 236;
		}
			
		this.entity_image_hitboxes = new Rectangle[entities.size()];
		int index = 0;
		for (Entity entity : entities) { // hitboxes of the entities in the scrolling menu
			if (entity != null) {
				entity_image_hitboxes[entities.indexOf(entity)] = new Rectangle(996, 658 - index, 64, 64);
			}
			index += 96;
		}
		selected_entity = new Entity[2];
		selected_entity[0] = null;
		selected_entity[1] = null;
		
		Gdx.input.setInputProcessor(this);
		
		xDraw = 0f; yDraw = 0f;
	}
	
	public void render (OrthographicCamera camera, SpriteBatch batch, float delta, Entity[] played_entities) {
		this.camera = camera;
		xDraw = camera.position.x - camera.viewportWidth/2; yDraw = camera.position.y - camera.viewportHeight/2;
		
		batch.draw(choose_charac_img, xDraw + 140, yDraw + 150);
		for (int i = 0; i < 3; i++) { // draws icons of the selected entities (3 max)
			if (selected_entity[1] != null && entity_squad_hitboxes[i].contains(touchUpcoords.x, touchUpcoords.y)) {
				if (!contains(played_entities, selected_entity[1])) {
					if (played_entities[i] != null)
						played_entities[i].setPlayed(false); // if there is an entity to change, set it to unplayed
					played_entities[i] = selected_entity[1];
					played_entities[i].setPlayed(true);
				}
				selected_entity[1] = null;
			}
		}
		int index_played = 0;
		int index = 0;
		for (Entity entity : played_entities) { // draws icons of the selected entities (3 max)
			if (entity != null) {
				batch.draw(entity.getIcon(), xDraw + 240 + index_played, yDraw + 350, 128, 128);
				index_played += 236;
			}
		}
		
		for (Entity entity : entities) { // draws entity icons on the right
			if (entity != null && !entity.isNpc()) {
				if (Gdx.input.isTouched()) { // sets touch position
					touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
					camera.unproject(touchPos);
					touchPos.add(-xDraw, -yDraw, 0);
				}
				else // if not touched
					touchPos.set(-1, -1, 0); 
				
				if ( (entity_image_hitboxes[entities.indexOf(entity)].contains(touchPos.x, touchPos.y) && selected_entity[0] == null) 
				|| ((Gdx.input.isTouched() && entity_image_hitboxes[entities.indexOf(entity)].getX() != 996 && selected_entity[0] == entity)) )
				{ // if an entity on the right is clicked when none is already chosen 
					// or if the screen is touched and the entity image is moved and the entity is chosen
					
					selected_entity[0] = entity;
					entity_image_hitboxes[entities.indexOf(entity)].setCenter(touchPos.x + xDraw, touchPos.y + yDraw);
					batch.draw(entity.getIcon(), touchPos.x + xDraw - 32, touchPos.y + yDraw - 32, 64, 64);
				}
				else {
					entity_image_hitboxes[entities.indexOf(entity)].setPosition(996, 658 - index);
					batch.draw(entity.getIcon(), xDraw + 996, yDraw + 658 - index, 64, 64);
				}
				font.draw(batch, entity.getType().getId() + " Lvl " + entity.getLvl_ent(), xDraw + 1064, yDraw + 710 - index);
				font.draw(batch, "ATK : " + entity.getATK(), xDraw + 1064, yDraw + 690 - index);
				font.draw(batch, "DEF : " + entity.getDEF(), xDraw + 1064, yDraw + 670 - index);
				
				index += 96;
			}
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { // checks if click is released in the squad's hitboxes
		if (GameMap.choose_charac_open) {
			touchUpcoords = new Vector3(screenX, screenY, 0);
			camera.unproject(touchUpcoords);
			touchUpcoords.add(- xDraw, - yDraw, 0);
			for (Rectangle squad_hitbox : entity_squad_hitboxes) {
				if (squad_hitbox.contains(touchUpcoords.x, touchUpcoords.y) && selected_entity[0] != null) {
					selected_entity[1] = selected_entity[0];
					selected_entity[0] = null;
					return true;
				}
				
			}
			selected_entity[1] = null;
			selected_entity[0] = null;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean contains(Entity[] ents, Entity ent) {
		for (int i = 0; i < ents.length; i++) {
			if (ents[i] == ent)
				return true;
		}
		return false;
	}
}
