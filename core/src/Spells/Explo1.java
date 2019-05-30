package Spells;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import entities.Entity;

public class Explo1 extends Spell{
	
	private static final int EXPLO_WIDTH = 128;
	private static final int EXPLO_HEIGHT = 128;
	private Animation<?>[] spell_img;
	private Texture spell_icon;
	private float EXPLO_ANIM_SPEED = 0.1f;
	

	private static final float EXPLO_TIME = 1.5f;

	public Explo1() {
		super(EXPLO_WIDTH, EXPLO_HEIGHT, 10);
		this.mana_cost = 10;
		spell_icon = new Texture("icons/explosion_1_icon.png");
		spell_img = new Animation[1];
		
		TextureRegion[][] spellSpriteSheet = TextureRegion.split(new Texture("spells/explosion_1.png"), 32, 32);
		
		spell_img[0] = new Animation<TextureRegion>(EXPLO_ANIM_SPEED , spellSpriteSheet[0]);
		
		this.setIcon(spell_icon);
		this.setImg(spell_img);
		
	}

	@Override
	public void castOnEnt(SpriteBatch batch, float delta, Entity target, Entity caster) {
		if (!caster.isCasting() && caster.getMP() >= mana_cost) {
			caster.setCasting(true);
			caster.addMP(-mana_cost);
		}
		else {
			spellTimer += delta;
			coefTime = spellTimer/EXPLO_TIME;
			if (this.spellTimer < EXPLO_TIME) {
				if (spell_img[0] != null)
					batch.draw((TextureRegion) spell_img[0].getKeyFrame(spellTimer, true),  
							target.getCombatPos().x*coefTime + (1-coefTime)*caster.getCombatPos().x, 
							target.getCombatPos().y*coefTime + (1-coefTime)*caster.getCombatPos().y, EXPLO_WIDTH, EXPLO_HEIGHT);
			}
			else {
				caster.setCasting(false);
				spellTimer = 0f;
				target.addHP(-this.getPower());
				caster.setCastSpellNb(-1);
				caster.setTarget_nb(-1);
			}
		}
			
	}

	@Override
	public void castOnEnt(SpriteBatch batch, float delta, ArrayList<Entity> ennemies, Entity caster, Vector3 mousePos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void castOnEnt(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity entity, Vector3 vector3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void castOnEnt(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity caster, Entity[] played_entities, Vector3 mouseCoords) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void castOnPlayer(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity caster, Entity target, Entity[] played_entities, Vector3 vector3) {
		// TODO Auto-generated method stub
		
	}
	
}
