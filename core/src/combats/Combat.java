package combats;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Entity;
import menus.CombatToolbar;
import world.CombatMap;

public class Combat {
	Entity[] played_entities;
	Entity[] ennemy;
	OrthographicCamera camera;
	CombatMap combatMap;
	CombatToolbar combat_toolbar;
	float combatTime;
	float[] regen_count; // timer for the MP regen of each entity
	private int selected_entity; // index of the selected entity in played_entities
	
	public float xDraw, yDraw;
	

	public Combat(SpriteBatch batch, OrthographicCamera camera, Entity[] played_entities, Entity ennemy) {
		this.camera = camera;
		this.played_entities = played_entities;
		this.ennemy = new Entity[1];
		this.ennemy[0] = ennemy;
		this.combatTime = 0;
		this.combat_toolbar = new CombatToolbar(this.played_entities);
		this.selected_entity = 0;
		this.regen_count = new float[4];
		regen_count[0] = 0; regen_count[1] = 0; regen_count[2] = 0; regen_count[3] = 0;
		
		xDraw = camera.position.x; yDraw = camera.position.y;
		//camera.translate(-xDraw, -yDraw);
		
		if(played_entities[0] != null)
			played_entities[0].setCombatPos(/*xDraw +*/256, /*yDraw +*/288);
		if(played_entities[1] != null)
			played_entities[1].setCombatPos(/*xDraw +*/544, /*yDraw +*/256);
		if(played_entities[2] != null)
			played_entities[2].setCombatPos(/*xDraw +*/832, /*yDraw +*/288);
		if(ennemy != null)
			ennemy.setCombatPos(/*xDraw +*/544, /*yDraw +*/480);
				
		for (Entity entity : played_entities) {
			if (entity != null) {
				entity.setCasting(false);
				entity.setCastSpellNb(-1);
				entity.setTarget_nb(-1);
			}
		}
		
	}

	public void render(SpriteBatch batch, float delta) {
		//combat_toolbar.render(camera, batch, delta, this);
		for (int i = 0; i < played_entities.length; i++) { // draw entities and their HP/MP bars
			if (i == 0 && played_entities[i] != null) {
				played_entities[i].render_atpos(batch, delta, /*xDraw +*/256, /*yDraw +*/288);
				combat_toolbar.drawHpAtPos(batch, played_entities[i], /*xDraw +*/266, /*yDraw +*/274, 108, 10);
				combat_toolbar.drawMpAtPos(batch, played_entities[i], /*xDraw +*/266, /*yDraw +*/260, 108, 10);
			}
			else if (i == 1 && played_entities[i] != null) {
				played_entities[i].render_atpos(batch, delta, /*xDraw +*/544, /*yDraw +*/256);
				combat_toolbar.drawHpAtPos(batch, played_entities[i], /*xDraw +*/554, /*yDraw +*/242, 108, 10);
				combat_toolbar.drawMpAtPos(batch, played_entities[i], /*xDraw +*/554, /*yDraw +*/228, 108, 10);
			}	
			
			else if (i == 2 && played_entities[i] != null) {
				played_entities[i].render_atpos(batch, delta, /*xDraw +*/832, /*yDraw +*/288);
				combat_toolbar.drawHpAtPos(batch, played_entities[i], /*xDraw +*/842, /*yDraw +*/274, 108, 10);
				combat_toolbar.drawMpAtPos(batch, played_entities[i], /*xDraw +*/842, /*yDraw +*/260, 108, 10);
			}
		}
		ennemy[0].render_atpos(batch, delta, /*xDraw +*/544, /*yDraw +*/480);
		combat_toolbar.drawHpAtPos(batch, ennemy[0], /*xDraw +*/554, /*yDraw +*/466, 108, 10);
		combat_toolbar.drawMpAtPos(batch, ennemy[0], /*xDraw +*/554, /*yDraw +*/452, 108, 10);
		
		if ((Gdx.input.isKeyJustPressed(Keys.NUM_1) && played_entities[selected_entity].getSpells()[0] != null // cast spell 2 on ennemy
				&& played_entities[selected_entity].getMP() >= played_entities[selected_entity].getSpells()[0].getManaCost()) 
			&& played_entities[selected_entity].getCastSpellNb() == -1) {
			played_entities[selected_entity].setCastSpellNb(0);
			played_entities[selected_entity].getSpells()[0].castOnEnt(batch, delta, ennemy[0], played_entities[selected_entity]);
			played_entities[selected_entity].setTarget_nb(3);
		}
		else if ( (Gdx.input.isKeyJustPressed(Keys.NUM_2) && played_entities[selected_entity].getSpells()[1] != null 
				&& played_entities[selected_entity].getMP() >= played_entities[selected_entity].getSpells()[1].getManaCost()) 
				 && played_entities[selected_entity].getCastSpellNb() == -1) {
			played_entities[selected_entity].setCastSpellNb(1);
			played_entities[selected_entity].getSpells()[1].castOnEnt(batch, delta, ennemy[0], played_entities[selected_entity]);
			played_entities[selected_entity].setTarget_nb(selected_entity);
		}
		
		ennemy[0].useAI(batch, camera, delta, null, played_entities);
		
		for (int i = 0; i < 3; i++) {
			regen_count[i] += delta;
			
			if (played_entities[i] != null && played_entities[i].isAlive()) {
				if ( regen_count[i] > (float) 1/played_entities[i].getManaRegen() ) {
					played_entities[i].addMP(1); // Regen MP
					regen_count[i] = 0f;
				}
				
				if (played_entities[i].isCasting()) {//(played_entities[i].getCastSpellNb() != -1) {
					if (played_entities[i].getTarget_nb() != 3 && played_entities[i].getTarget_nb() != -1)
						played_entities[i].getSpells()[played_entities[i].getCastSpellNb()].castOnEnt(batch, delta, played_entities[played_entities[i].getTarget_nb()], played_entities[i]);
					if (played_entities[i].getTarget_nb() == 3)
						played_entities[i].getSpells()[played_entities[i].getCastSpellNb()].castOnEnt(batch, delta, ennemy[0], played_entities[i]);
				}
				else {
					played_entities[i].setCastSpellNb(-1);
					played_entities[i].setTarget_nb(-1);
				}
			}
		}
		regen_count[3] += delta;
		if (regen_count[3] > 1 / ennemy[0].getManaRegen()) {
			ennemy[0].addMP(1); // Regen MP
			regen_count[3] = 0f;
		}
		
		combatTime += delta;
		
		
	}
	
	public void setSelectedEntity(int selected_entity) {
		this.selected_entity = selected_entity;
	}
	
	public void dispose() {
		if (!this.ennemy[0].isAlive()) {
			for (Entity ent : played_entities) {
				ent.addXp(ennemy[0].getXpGiven());
				System.out.println(ent.getType() + " " + ent.getXp());
			}
		}
	}
	
}
