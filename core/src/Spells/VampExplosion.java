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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import decors.Voiture;
import entities.Entity;

public class VampExplosion extends Spell {

	private static final int SPELL_WIDTH = 96;
	private static final int SPELL_HEIGHT = 96; 
	private static int SPELL_POWER = 30;
	private static final float SPELL_ANIM_SPEED = 0.05f;
	private static final float LOAD_TIME = 1f;
	private static final float SPELL_TIME = 3.3f;
	private static final float SPELL_TIME1 = 1.1f;
	private static final float SPELL_TIME2 = 2.2f;
	private static final float SPELL_TIME3 = 2.7f;
	private static Texture spell_icon;
	private static Animation<?>[] spell_img;

	private Vector2 start_zone;
	private Vector2 end_zone;
	private Polygon spell_hitbox;
	float angle;
	private Rectangle hitbox;
	private PolygonSpriteBatch polyBatch;
	private PolygonSprite poly;
	
	private float dmgDealt;
	
	BitmapFont fontBlue = new BitmapFont();
	
	private ParticleEffect spellParticle = new ParticleEffect();
	private ParticleEffect manaParticle = new ParticleEffect();
	private ParticleEffect aimParticle = new ParticleEffect();
	
	public VampExplosion() {
		super("Merci.", SPELL_WIDTH, SPELL_HEIGHT, SPELL_POWER, "icons/vamp_explosion_icon.png", SPELL_ANIM_SPEED, 1, "icons/vamp_explosion_icon.png");
		this.mana_cost = 50;
		fontBlue.setColor(Color.BLUE);
		this.spellParticle.load(Gdx.files.internal("particles/explo_vamp.pe"), Gdx.files.internal("particles"));
		this.manaParticle.load(Gdx.files.internal("particles/mana_particle.pe"), Gdx.files.internal("particles"));
		this.aimParticle.load(Gdx.files.internal("particles/fireball_charge.pe"), Gdx.files.internal("particles"));
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
		if (!caster.isCasting() && caster.getMP() >= mana_cost) {
			spellParticle.reset();
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
				spellParticle.setPosition(start_zone.x, start_zone.y);
				spellParticle.start();
				spellParticle.draw(batch, delta);
				this.setCharging(false);
				spellTimer = 0f;
				angle = (float) (-Math.PI/2 + calcAng(new Vector2(caster.getCombatCenter().x, caster.getCombatCenter().y), mouseCoords));
				float distM = (float) Math.sqrt( Math.pow(caster.getCombatCenter().x - mouseCoords.x, 2) + Math.pow(caster.getCombatCenter().y-mouseCoords.y, 2) );
				end_zone = new Vector2(mouseCoords.x, mouseCoords.y);
				hitbox = new Rectangle(0, 0, SPELL_WIDTH, SPELL_HEIGHT);
				hitbox.setCenter(end_zone);
			}
		}
		else if (caster.isCasting()) {
			spellTimer += delta;
			if (spellTimer < SPELL_TIME1) {
				spellParticle.setPosition(50 * (1-spellTimer/SPELL_TIME1) * (float) (Math.cos(spellTimer * 2*Math.PI)) 
						+ end_zone.x * spellTimer/SPELL_TIME1 + start_zone.x*(1-spellTimer/SPELL_TIME1), 
						50 * (1-spellTimer/SPELL_TIME1) * (float) (Math.cos(spellTimer * 2*Math.PI)) + end_zone.y * spellTimer/SPELL_TIME1 + start_zone.y*(1-spellTimer/SPELL_TIME1));
				spellParticle.draw(batch, delta);
				
			}
			else if (spellTimer < SPELL_TIME2) {
				spellParticle.draw(batch, delta);
				/*ShapeRenderer shapeRenderer = new ShapeRenderer();
				shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
				shapeRenderer.setProjectionMatrix(camera.combined);
				shapeRenderer.line(hitbox.x, hitbox.y, hitbox.x + hitbox.width, hitbox.y);
				shapeRenderer.line(hitbox.x + hitbox.width, hitbox.y, hitbox.x + hitbox.width, hitbox.y + hitbox.height);
				shapeRenderer.line(hitbox.x, hitbox.y + hitbox.height, hitbox.x + hitbox.width, hitbox.y + hitbox.height);
				shapeRenderer.line(hitbox.x, hitbox.y, hitbox.x, hitbox.y + hitbox.height);
				shapeRenderer.end();*/
				for (Entity ent : ennemies) {
					float dmgEnt;
					if (hitbox.overlaps(ent.getCombatHitbox())) {
						dmgEnt = delta / (SPELL_TIME2 - SPELL_TIME1) * max(this.getPower() * caster.getATK() / ent.getDEF(), this.getPower());
						ent.addHP(-dmgEnt);
						dmgDealt += dmgEnt;
					}
				}
			}
			else if (spellTimer < SPELL_TIME3) {
				start_zone = caster.getCombatCenter();
				spellParticle.setPosition(end_zone.x*(1-(spellTimer-SPELL_TIME2)/(SPELL_TIME3 - SPELL_TIME2)) + start_zone.x*(spellTimer-SPELL_TIME2)/(SPELL_TIME3 - SPELL_TIME2),
						end_zone.y * (1-(spellTimer-SPELL_TIME2)/(SPELL_TIME3 - SPELL_TIME2)) + start_zone.y * (spellTimer-SPELL_TIME2)/(SPELL_TIME3-SPELL_TIME2));
				spellParticle.draw(batch, delta);
			}
			else if (spellTimer < SPELL_TIME) {
				spellParticle.setPosition(caster.getCombatCenter().x, caster.getCombatCenter().y);
				spellParticle.draw(batch, delta);
				caster.addHP(dmgDealt / 10 * delta/(SPELL_TIME - SPELL_TIME3));
			}
			else {
				caster.setCasting(false);
				spellTimer = 0f;
				caster.setCastSpellNb(-1);
				caster.setTarget_nb(-1);
				System.out.println("VAMP DEALT " + dmgDealt + " DMG !!!!");
			}
		}
		
	}
	
	public float max(float a, float b) {
		if (a > b)
			return a;
		else
			return b;
	}

	@Override
	public void castOnPlayer(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity caster, Entity target, Entity[] played_entities, Vector3 vector3) {
		// TODO Auto-generated method stub
		
	}

}
