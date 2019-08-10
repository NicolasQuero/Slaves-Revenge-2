package combats;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import entities.Entity;
import entities.monsters.Controlleur;
import entities.monsters.Lambda1;
import menus.CombatToolbar;
import world.CombatMap;
import world.CombatMapTD;

public class CombatTD {

	BitmapFont font = new BitmapFont();
	Entity[] played_entities;
	Entity[] ennemy;
	ArrayList<Entity> ennemies;
	OrthographicCamera camera;
	CombatMap combatMap;
	CombatToolbar combat_toolbar;
	float combatTime;
	float[] regen_count; // timer for the MP regen of each entity
	private int selected_entity; // index of the selected entity in played_entities
	
	Vector3[] mouseCoords; // where we aimed to cast a spell on each entity
	
	public float xDraw, yDraw;
	
	public CombatTD(OrthographicCamera camera, CombatMapTD map, Entity[] played_entities, Entity ennemy) {
		this.camera = camera;
		this.played_entities = played_entities;
		this.ennemy = new Entity[1];
		this.ennemy[0] = ennemy;
		ennemies = new ArrayList<Entity>();
		for (int i = 0; i < 50; i++) {
			ennemies.add(new Controlleur(i/10 * 100, i%10 * 100, this.combatMap));
			ennemies.get(i).setCombatPos(i/10 * 100, i%10 * 100);
		}
		ennemies.add(ennemy);
		this.combatTime = 0;
		this.combat_toolbar = new CombatToolbar(this.played_entities);
		this.selected_entity = 0;
		this.regen_count = new float[4];
		regen_count[0] = 0; regen_count[1] = 0; regen_count[2] = 0; regen_count[3] = 0;
		mouseCoords = new Vector3[3];
		xDraw = camera.position.x; yDraw = camera.position.y;
		//camera.translate(-xDraw, -yDraw);
		
		if(played_entities[0] != null)
			played_entities[0].setCombatPos(/*xDraw +*/256, /*yDraw +*/288);
		if(played_entities[1] != null)
			played_entities[1].setCombatPos(/*xDraw +*/544, /*yDraw +*/256);
		if(played_entities[2] != null && played_entities[2].isAlive())
			played_entities[2].setCombatPos(/*xDraw +*/832, /*yDraw +*/288);
		if(ennemy != null)
			ennemy.setCombatPos(/*xDraw +*/544, /*yDraw +*/480);
				
		for (Entity entity : played_entities) {
			if (entity != null) {
				entity.setCasting(false);
				entity.setCastSpellNb(-1);
				entity.setTarget_nb(-1);
				entity.addEnergy(4f);
			}
		}
	}
	
	public void render(SpriteBatch batch, float delta) {
		combat_toolbar.render(camera, batch, delta, this);
		for (Entity entity : ennemies) {
			if (entity.getType().getId().equals("Monster") && entity.isAlive()) {
				entity.render_adds(batch, delta);
				combat_toolbar.drawHpAtPos(batch, entity, entity.getCombatPos().x + 2, entity.getCombatPos().y - 6, 28, 3);
			}
		}
		ennemy[0].render_combat_droite(batch, delta);
		combat_toolbar.drawHpAtPos(batch, ennemy[0], ennemy[0].getCombatPos().x, ennemy[0].getCombatPos().y - 12, 64, 8);
		batch.draw(ennemy[0].getIcon(), Gdx.graphics.getWidth()/2 - 140, Gdx.graphics.getHeight() - 84, 32, 32);
		combat_toolbar.drawHpAtPos(batch, ennemy[0], Gdx.graphics.getWidth()/2 - 100, Gdx.graphics.getHeight() - 100, 200, 50);
		font.draw(batch, (int) ennemy[0].getHP() + " / " + (int) ennemy[0].getMaxHP(), Gdx.graphics.getWidth()/2 - 20, Gdx.graphics.getHeight() - 70);
		for (int i = 0; i < 3; i++) {
			if (played_entities[i] != null) {
				if (selected_entity == i && played_entities[i].isAlive())
					played_entities[selected_entity].render_combat(batch, delta);
				else if (played_entities[i].isAlive())
					played_entities[i].render_combat_droite(batch, delta);
				combat_toolbar.drawHpAtPos(batch, played_entities[i], played_entities[i].getCombatPos().x, played_entities[i].getCombatPos().y - 12, 64, 8);
				combat_toolbar.drawEnAtPos(batch, played_entities[i], played_entities[i].getCombatPos().x, played_entities[i].getCombatPos().y - 22, 64, 8);
			}
		}

		if ( (Gdx.input.isKeyPressed(Keys.NUM_1) && played_entities[selected_entity].getSpells()[0] != null 
				&& played_entities[selected_entity].getMP() >= played_entities[selected_entity].getSpells()[0].getManaCost()) 
			 && played_entities[selected_entity].getCastSpellNb() == -1) {
			played_entities[selected_entity].setCastSpellNb(0);
			mouseCoords[selected_entity] = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
			camera.unproject(mouseCoords[selected_entity]);
			played_entities[selected_entity].getSpells()[0]
					.castOnEnt(batch, camera, delta, ennemies, played_entities[selected_entity], played_entities, mouseCoords[selected_entity]);
			played_entities[selected_entity].setTarget_nb(3);
		}
			
		if ( (Gdx.input.isKeyPressed(Keys.NUM_2) && played_entities[selected_entity].getSpells()[1] != null 
				&& played_entities[selected_entity].getMP() >= played_entities[selected_entity].getSpells()[1].getManaCost()) 
			 && played_entities[selected_entity].getCastSpellNb() == -1) {
			played_entities[selected_entity].setCastSpellNb(1);
			mouseCoords[selected_entity] = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
			camera.unproject(mouseCoords[selected_entity]);
			played_entities[selected_entity].getSpells()[1]
					.castOnEnt(batch, camera, delta, ennemies, played_entities[selected_entity], played_entities, mouseCoords[selected_entity]);
			played_entities[selected_entity].setTarget_nb(3);
		}
		
		if ( (Gdx.input.isKeyPressed(Keys.NUM_4) && played_entities[selected_entity].getSpells()[3] != null 
				&& played_entities[selected_entity].getMP() >= played_entities[selected_entity].getSpells()[3].getManaCost()) 
			 && played_entities[selected_entity].getCastSpellNb() == -1) {
			played_entities[selected_entity].setCastSpellNb(3);
			mouseCoords[selected_entity] = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
			camera.unproject(mouseCoords[selected_entity]);
			played_entities[selected_entity].getSpells()[3]
					.castOnEnt(batch, camera, delta, ennemies, played_entities[selected_entity], played_entities, mouseCoords[selected_entity]);
			played_entities[selected_entity].setTarget_nb(3);
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F) || played_entities[selected_entity].isDashing()) {
			mouseCoords[selected_entity] = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
			camera.unproject(mouseCoords[selected_entity]);
			played_entities[selected_entity].dashTo(batch, delta, mouseCoords[selected_entity]);
		}
		
		
		for (int i = 0; i < 3; i++) {
			regen_count[i] += delta;
			played_entities[i].addEnergy(delta/2);
			if (played_entities[i] != null && played_entities[i].isAlive()) {
				if ( regen_count[i] > (float) 1/played_entities[i].getManaRegen() ) {
					played_entities[i].addMP(10); // Regen MP
					regen_count[i] = 0f;
				}
				
				if (played_entities[i].isCasting()) {//(played_entities[i].getCastSpellNb() != -1) {
					if (played_entities[i].getTarget_nb() != 3 && played_entities[i].getTarget_nb() != -1)
						played_entities[i].getSpells()[played_entities[i].getCastSpellNb()]
								.castOnEnt(batch, camera, delta, ennemies, played_entities[i], played_entities, mouseCoords[i]);
					if (played_entities[i].getTarget_nb() == 3)
						played_entities[i].getSpells()[played_entities[i].getCastSpellNb()]
								.castOnEnt(batch, camera, delta, ennemies, played_entities[i], played_entities, 
										camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
				}
				else {
					played_entities[i].setCastSpellNb(-1);
					played_entities[i].setTarget_nb(-1);
				}
			}
		}
		
		for (Entity ent : ennemies) {
			if (ent.isAlive())
				ent.basic_ai(batch, camera, delta, ennemies, played_entities);
		}
		for (int i = 0; i < 3; i++) {
			if (i != selected_entity && played_entities[i].isAlive()) {
				played_entities[i].friendly_ai(batch, camera, delta, ennemies, played_entities);
			}
		}
		regen_count[3] += delta;
		if (regen_count[3] > 1 / ennemy[0].getManaRegen()) {
			ennemy[0].addMP(1); // Regen MP
			regen_count[3] = 0f;
		}
	
	}
	
	
	public void dispose() {
		int xp = 0;
		for (Entity ennemy : ennemies) {
			xp += ennemy.getXpGiven();
		}
		for (Entity entity : played_entities) {
			entity.addXp(xp);
		}
	}
	
	public void setSelectedEntity(int selected_entity) {
		this.selected_entity = selected_entity;
	}
}
