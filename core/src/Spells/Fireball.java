package Spells;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import Spells.Spell;
import entities.Entity;

public class Fireball extends Spell {
	
	private static final int SPELL_WIDTH = 96;
	private static final int SPELL_HEIGHT = 96; 
	private static int SPELL_POWER = 10;
	private static final float SPELL_ANIM_SPEED = 0.05f;
	private static final float LOAD_TIME = 1f;
	private static final float SPELL_TIME = 1f;
	private static Texture spell_icon;
	private static Animation<?>[] spell_img;

	private Vector2 start_zone;
	private Vector2 end_zone;
	private Polygon spell_hitbox;
	float angle;
	private Rectangle hitbox;
	private PolygonSpriteBatch polyBatch;
	private PolygonSprite poly;
	
	BitmapFont fontBlue = new BitmapFont();
	
	private ParticleEffect fireParticle = new ParticleEffect();
	private ParticleEffect manaParticle = new ParticleEffect();
	private ParticleEffect aimParticle = new ParticleEffect();
	
	private float dmgDealt;
	
	public Fireball() {
		super(SPELL_WIDTH, SPELL_HEIGHT, SPELL_POWER);
		this.setName("Fireball");
		this.mana_cost = 15;
		fireParticle.load(Gdx.files.internal("particles/fireball.pe"), Gdx.files.internal("particles"));
		this.manaParticle.load(Gdx.files.internal("particles/mana_particle.pe"), Gdx.files.internal("particles"));
		this.aimParticle.load(Gdx.files.internal("particles/fireball_charge.pe"), Gdx.files.internal("particles"));
		spell_icon = new Texture("icons/fireball_icon.png");
		spell_img = new Animation[1];
		TextureRegion[][] spellSpriteSheet = TextureRegion.split(new Texture("spells/tornade_enfer_1.png"), 64, 64);
		spell_img[0] = new Animation<TextureRegion>(SPELL_ANIM_SPEED, spellSpriteSheet[0]);
		
		this.setIcon(spell_icon);
		this.setImg(spell_img);
		
		this.fontBlue.setColor(Color.BLUE);
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
		// Cast fireball in the direction of the mouse, only ennemies can be hit
		if (!caster.isCasting() && caster.getMP() >= mana_cost) {
			fireParticle.reset();
			aimParticle.reset();
			manaParticle.reset();
			start_zone = caster.getCombatCenter();
			hitbox = new Rectangle(caster.getCombatCenter().x, caster.getCombatCenter().y, SPELL_WIDTH, SPELL_HEIGHT);
			manaParticle.setPosition(caster.getCombatCenter().x, caster.getCombatCenter().y);
			manaParticle.start();
			aimParticle.setPosition(mouseCoords.x, mouseCoords.y);
			aimParticle.start();
			dmgDealt = 0f;
			
			caster.setCasting(true);
			this.setCharging(true);
			caster.addMP(-mana_cost);
		}
		if (this.isCharging()) {
			spellTimer += delta;
			if (spellTimer < LOAD_TIME) {
				aimParticle.setPosition(mouseCoords.x, mouseCoords.y);
				aimParticle.draw(batch, delta);
				manaParticle.setPosition(caster.getCombatCenter().x, caster.getCombatCenter().y);
				manaParticle.draw(batch, delta);
				fontBlue.draw(batch, String.valueOf(-mana_cost), caster.getCombatPos().x + 10 + delta * 20, caster.getCombatPos().y - 32 - delta * 20);
			}
			else {
				start_zone = caster.getCombatCenter();
				fireParticle.setPosition(start_zone.x, start_zone.y);
				fireParticle.start();
				fireParticle.draw(batch, delta);
				hitbox.setCenter(start_zone);
				this.setCharging(false);
				spellTimer = 0f;
				angle = (float) (-Math.PI/2 + calcAng(new Vector2(caster.getCombatCenter().x, caster.getCombatCenter().y), mouseCoords));
				float distM = (float) Math.sqrt( Math.pow(caster.getCombatCenter().x - mouseCoords.x, 2) + Math.pow(caster.getCombatCenter().y-mouseCoords.y, 2) );
				end_zone = new Vector2(caster.getCombatCenter().x - (caster.getCombatCenter().x - mouseCoords.x)* 96 / distM,
						caster.getCombatCenter().y - (caster.getCombatCenter().y - mouseCoords.y)* 96 / distM);
			}
		}
		else if (caster.isCasting()) {
			spellTimer += delta;
			if (spellTimer < SPELL_TIME) {
				if (spellTimer < 0.3 * SPELL_TIME) {
					start_zone = caster.getCombatCenter();
					fireParticle.setPosition(start_zone.x, start_zone.y);
					fireParticle.draw(batch, delta);
				}
				else if (spellTimer < 0.6 * SPELL_TIME) {
					Vector2 center = new Vector2(start_zone.x * (2 * (0.3f / spellTimer) - 1) + end_zone.x * (1 - (2 * (0.3f / spellTimer) - 1)), 
							start_zone.y * (2 * (0.3f / spellTimer) - 1) + end_zone.y * (1 - (2 * (0.3f / spellTimer) - 1)));
					hitbox.setCenter(center.x, center.y);
					fireParticle.setPosition(center.x, center.y);
					fireParticle.draw(batch, delta);
					for (Entity ent : ennemies) {
						if (hitbox.contains(ent.getCombatCenter())) {
							ent.addHP(- delta / 0.7f * max(this.getPower() * caster.getATK() / ent.getDEF(), this.getPower()));
						}
					}
				}
				else {
					fireParticle.draw(batch, delta);
					for (Entity ent : ennemies) {
						if (hitbox.contains(ent.getCombatCenter())) {
							ent.addHP(- delta / 0.7f * max(this.getPower() * caster.getATK() / ent.getDEF(), this.getPower()));
							dmgDealt += delta / 0.7f * max(this.getPower() * caster.getATK() / ent.getDEF(), this.getPower());
						}
					}
				}
			}
			else {
				caster.setCasting(false);
				spellTimer = 0f;
				caster.setCastSpellNb(-1);
				caster.setTarget_nb(-1);
				System.out.println("FIREBALL DEALT " + dmgDealt + " DMG !!!!");
			}
		}
		
	}
	

	@Override
	public void castOnPlayer(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity caster, Entity target, Entity[] played_entities, Vector3 vector3) {
		// TODO Auto-generated method stub
		
	}
	
	public float max(float a, float b) {
		if (a > b)
			return a;
		else
			return b;
	}

}
