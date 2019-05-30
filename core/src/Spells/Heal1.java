package Spells;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import entities.Entity;

public class Heal1 extends Spell {

	private static final int HEAL_1_WIDTH = 128;
	private static final int HEAL_1_HEIGHT = 128;
	private Animation<?>[] spell_img;
	private Texture spell_icon;
	private float HEAL_ANIM_SPEED = 0.1f;
	private final float HEAL_TIME = 1.5f;

	public Heal1() {
		super(HEAL_1_WIDTH, HEAL_1_HEIGHT, 10);
		this.mana_cost = 25;
		spell_icon = new Texture("icons/heal_1_icon.png");
		spell_img = new Animation[1];
		
		TextureRegion[][] spellSpriteSheet = TextureRegion.split(new Texture("spells/heal_1.png"), 32, 32);
		
		spell_img[0] = new Animation<TextureRegion>(HEAL_ANIM_SPEED, spellSpriteSheet[0]);
		
		this.setIcon(spell_icon);
		this.setImg(spell_img);
		
	}

	@Override
	public void castOnEnt(SpriteBatch batch, float delta, Entity target, Entity caster) {
		if (!caster.isCasting() && caster.getMP() >= mana_cost) {
			caster.setCasting(true);
			caster.addMP(-mana_cost);
		}
		else if (caster.isCasting()){
			spellTimer += delta;
			coefTime = spellTimer/HEAL_TIME ;
			if (this.spellTimer < HEAL_TIME) {
				if (spell_img[0] != null)
					batch.draw((TextureRegion) spell_img[0].getKeyFrame(spellTimer, true),  
							target.getCombatPos().x*coefTime + (1-coefTime)*caster.getCombatPos().x, 
							target.getCombatPos().y*coefTime + (1-coefTime)*caster.getCombatPos().y, HEAL_1_WIDTH, HEAL_1_HEIGHT);
			}
			else {
				caster.setCasting(false);
				spellTimer = 0f;
				target.addHP(this.getPower());
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
