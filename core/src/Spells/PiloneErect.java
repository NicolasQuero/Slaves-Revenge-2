package Spells;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import entities.Entity;

public class PiloneErect extends Spell {
	
	private static final int SPELL_WIDTH = 64;
	private static final int SPELL_HEIGHT = 64;
	private static final int SPELL_POWER = 3;
	private static float SPELL_ANIM_SPEED = 0.1f;
	private float SPELL_TIME = 0.8f;
	private Entity[] target = new Entity[1];
	private static Texture spell_icon;
	private static Animation<?>[] spell_img;

	public PiloneErect() {
		super(SPELL_WIDTH, SPELL_HEIGHT, SPELL_POWER);

		spell_icon = new Texture("icons/monster_spell.png");
		spell_img = new Animation[1];
		
		TextureRegion[][] spellSpriteSheet = TextureRegion.split(new Texture("spells/monster/pilone_erect.png"), 32, 32);
		
		spell_img[0] = new Animation<TextureRegion>(SPELL_ANIM_SPEED, spellSpriteSheet[0]);
		
		this.setIcon(spell_icon);
		this.setImg(spell_img);
	}

	@Override
	public void castOnEnt(SpriteBatch batch, float delta, Entity ent, Entity caster) {
		// TODO Auto-generated method stub
		
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
		if (!caster.isCasting()) {
			caster.setCasting(true);
			spellTimer = 0f;
			this.target[0] = target;
		}
		else {
			if (spellTimer < SPELL_TIME) {
				spellTimer += delta;
				if (spell_img[0] != null) {
					batch.draw((TextureRegion) spell_img[0].getKeyFrame(spellTimer, true),
						this.target[0].getCombatPos().x, 
						this.target[0].getCombatPos().y, SPELL_WIDTH, SPELL_HEIGHT);
				}
			}
			else {
				spellTimer = SPELL_TIME;
				caster.setCasting(false);
				target.addHP(-SPELL_POWER);
			}
			
		}
		
	}

}
