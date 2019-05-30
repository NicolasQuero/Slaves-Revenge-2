package Spells;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import entities.Entity;

public class HealZone1 extends Spell {
	
	private static final int SPELL_WIDTH = 128;
	private static final int SPELL_HEIGHT = 128; 
	private static int SPELL_POWER = 15;
	private static final float SPELL_ANIM_SPEED = 0.05f;
	private static final float CHARGE_TIME = 0.5f;
	private static final float SPELL_TIME = 1f;
	
	private Rectangle spell_hitbox;
	
	private ParticleEffect spellParticle = new ParticleEffect();
	private ParticleEffect manaParticle = new ParticleEffect();

	public HealZone1() {
		super("Altruisme", SPELL_WIDTH, SPELL_HEIGHT, SPELL_POWER, "spells/heal_zone_1.png", SPELL_ANIM_SPEED, 2, "icons/heal_zone_1_icon.png");
		this.mana_cost = 15;
		this.spellParticle.load(Gdx.files.internal("particles/healzone.pe"), Gdx.files.internal("particles"));
		this.manaParticle.load(Gdx.files.internal("particles/mana_particle.pe"), Gdx.files.internal("particles"));
	}

	@Override
	public void castOnEnt(SpriteBatch batch, float delta, Entity ent, Entity caster) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void castOnEnt(SpriteBatch batch, float delta, ArrayList<Entity> ennemies, Entity caster, Vector3 mousePos) {
		// TODO Auto-generated method stub
		
	}

	public void castOnEnt(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity caster, Entity[] played_entities, Vector3 mouseCoords) {
		if (!caster.isCasting() && caster.getMP() >= mana_cost) {
			spellParticle.reset();
			spellParticle.setPosition(caster.getCombatCenter().x, caster.getCombatCenter().y);
			manaParticle.reset();
			manaParticle.setPosition(caster.getCombatCenter().x, caster.getCombatCenter().y);
			caster.setCasting(true);
			this.setCharging(true);
			caster.addMP(-mana_cost);
			spell_hitbox = new Rectangle(caster.getCombatCenter().x - SPELL_WIDTH/2, caster.getCombatCenter().y - SPELL_HEIGHT/2, SPELL_WIDTH, SPELL_HEIGHT);
		}
		else if (this.isCharging()) {
			spellTimer += delta;
			if (spellTimer < CHARGE_TIME) {
				manaParticle.setPosition(caster.getCombatCenter().x, caster.getCombatCenter().y);
				manaParticle.draw(batch, delta);
			}
			else {
				spellTimer = 0f;
				this.setCharging(false);
			}
		}
		else if (caster.isCasting()){
			spellTimer += delta;
			coefTime = delta/SPELL_TIME;
			if (this.spellTimer < SPELL_TIME) {
				/*if (spell_img[0] != null)
					batch.draw((TextureRegion) spell_img[0].getKeyFrame(spellTimer, true),  
							spell_hitbox.x, spell_hitbox.y, SPELL_WIDTH, SPELL_HEIGHT);*/
				spellParticle.setPosition(caster.getCombatCenter().x, caster.getCombatCenter().y);
				spellParticle.draw(batch, delta);
				spell_hitbox.setCenter(caster.getCombatCenter());
				for (Entity entity : played_entities) {
					if (entity.isAlive() && spell_hitbox.contains(entity.getCombatCenter()))
						entity.addHP(this.getPower()*coefTime*entity.getATK()/10);
				}
			}
			else {
				caster.setCasting(false);
				spellTimer = 0f;
				caster.setCastSpellNb(-1);
				caster.setTarget_nb(-1);
			}
		}
		
	}

	@Override
	public void castOnEnt(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity entity, Vector3 vector3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void castOnPlayer(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity caster, Entity target, Entity[] played_entities, Vector3 vector3) {
		// TODO Auto-generated method stub
		
	}

}
