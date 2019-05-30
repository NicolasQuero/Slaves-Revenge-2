package Spells;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ShortArray;

import entities.Entity;

public class FireAttack1 extends Spell {
	
	private static final int SPELL_WIDTH = 96;
	private static final int SPELL_HEIGHT = 96; 
	private static int SPELL_POWER = 10;
	private static final float SPELL_ANIM_SPEED = 0.05f;
	private static final float SPELL_TIME = 1.25f;
	private static Texture spell_icon;
	private static Animation<?>[] spell_img;

	private Vector2 start_zone;
	private Vector2 end_zone;
	private Polygon spell_hitbox;
	float angle;
	private float[] hitbox;
	private PolygonSpriteBatch polyBatch;
	private PolygonSprite poly;
	
	private ParticleEffect fireParticle = new ParticleEffect();
	private ParticleEffect manaParticle = new ParticleEffect();
	
	

	public FireAttack1() {
		super(SPELL_WIDTH, SPELL_HEIGHT, SPELL_POWER);
		this.setName("Hébétude");
		this.mana_cost = 10;
		fireParticle.load(Gdx.files.internal("particles/fireball.pe"), Gdx.files.internal("particles"));
		this.manaParticle.load(Gdx.files.internal("particles/mana_particle.pe"), Gdx.files.internal("particles"));
		spell_icon = new Texture("icons/tornade_enfer_1_icon.png");
		spell_img = new Animation[1];
		TextureRegion[][] spellSpriteSheet = TextureRegion.split(new Texture("spells/tornade_enfer_1.png"), 64, 64);
		spell_img[0] = new Animation<TextureRegion>(SPELL_ANIM_SPEED, spellSpriteSheet[0]);
		
		this.setIcon(spell_icon);
		this.setImg(spell_img);
	}

	public void castOnEnt(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies, Entity caster, Entity[] played_entities, Vector3 mousePos) {
		if (!caster.isCasting() && caster.getMP() >= mana_cost) {
			angle = (float) (-Math.PI/2 + calcAng(new Vector2(caster.getCombatCenter().x, caster.getCombatCenter().y), mousePos));
			float distM = (float) Math.sqrt( Math.pow(caster.getCombatCenter().x - mousePos.x, 2) + Math.pow(caster.getCombatCenter().y-mousePos.y, 2) );
			start_zone = caster.getCombatCenter();
			end_zone = new Vector2(caster.getCombatCenter().x - (caster.getCombatCenter().x - mousePos.x)* 96 / distM,
					caster.getCombatCenter().y - (caster.getCombatCenter().y - mousePos.y)* 96 / distM);
			
			spell_hitbox = new Polygon(new float[] {
				(float) (caster.getCombatCenter().x - 32*Math.cos(angle)), (float) (caster.getCombatCenter().y - 32*Math.sin(angle)),
				(float) (caster.getCombatCenter().x + 32*Math.cos(angle)), (float) (caster.getCombatCenter().y + 32*Math.sin(angle)),
				(float) (end_zone.x + 48*Math.cos(angle)), (float) (end_zone.y + 48*Math.sin(angle)),
				(float) (end_zone.x - 48*Math.cos(angle)), (float) (end_zone.y - 48*Math.sin(angle))
			});
			hitbox = new float[] {
					(float) (caster.getCombatCenter().x - 32*Math.cos(angle)), (float) (caster.getCombatCenter().y - 32*Math.sin(angle)),
					(float) (caster.getCombatCenter().x + 32*Math.cos(angle)), (float) (caster.getCombatCenter().y + 32*Math.sin(angle)),
					(float) (end_zone.x + 48*Math.cos(angle)), (float) (end_zone.y + 48*Math.sin(angle)),
					(float) (end_zone.x - 48*Math.cos(angle)), (float) (end_zone.y - 48*Math.sin(angle))};
			fireParticle.start();
			manaParticle.setPosition(caster.getCombatCenter().x, caster.getCombatCenter().y);
			manaParticle.start();
			
			/*// DRAW INSIDE OF POLY
			polyBatch = new PolygonSpriteBatch(); // To assign at the beginning
			Texture textureSolid;

			// Creating the color filling (but textures would work the same way)
			Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			pix.setColor(0xFF33691E); // DE is red, AD is green and BE is blue.
			pix.fill();
			textureSolid = new Texture(pix);
			EarClippingTriangulator triangulator = new EarClippingTriangulator();
			ShortArray triangleIndices = triangulator.computeTriangles(hitbox);
			PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid), hitbox, triangleIndices.toArray());
			poly = new PolygonSprite(polyReg);*/
			
			caster.setCasting(true);
			caster.addMP(-mana_cost);
		}
		else {
			/*ShapeRenderer shapeRenderer = new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.line(hitbox[0], hitbox[1], hitbox[2], hitbox[3]);
			shapeRenderer.line(hitbox[4], hitbox[5], hitbox[2], hitbox[3]);
			shapeRenderer.line(hitbox[6], hitbox[7], hitbox[4], hitbox[5]);
			shapeRenderer.line(hitbox[0], hitbox[1], hitbox[6], hitbox[7]);
			shapeRenderer.end();*/
			
			spellTimer += delta;
			coefTime = spellTimer/SPELL_TIME;
			if (this.spellTimer < SPELL_TIME) {
				/*batch.end();
				polyBatch.begin();
				polyBatch.setProjectionMatrix(camera.combined);
				poly.draw(polyBatch);
				polyBatch.end();
				batch.begin();*/
				
				manaParticle.setPosition(caster.getCombatCenter().x, caster.getCombatCenter().y);
				manaParticle.draw(batch, delta);
				
				if (spell_img[0] != null) {
					//batch.draw((TextureRegion) (spell_img[0].getKeyFrame(spellTimer, true)), (float) (start_zone.x + 48*Math.cos(angle)), 
						//	(float) (start_zone.y + 48*Math.sin(angle)), 0f, 0f,
							//SPELL_WIDTH, SPELL_HEIGHT, 1f, 1f, (float) ((Math.PI/2 + angle)*180f/Math.PI), true);
					fireParticle.setPosition((float) (start_zone.x + 48*Math.cos(angle)), (float) (start_zone.y + 48*Math.sin(angle)));
					fireParticle.draw(batch, delta);
					/*batch.draw((TextureRegion) spell_img[0].getKeyFrame(spellTimer, true),
							end_zone.x*coefTime + (1-coefTime)*(start_zone.x - SPELL_WIDTH/2), 
							end_zone.y*coefTime + (1-coefTime)*start_zone.y, SPELL_WIDTH, SPELL_HEIGHT);*/
				}
				
				
			}
			else {
				caster.setCasting(false);
				for (Entity entity : ennemies) {
					if (isCollision(spell_hitbox, entity.getCombatHitbox()))
						entity.addHP(- this.getPower() * caster.getATK() / entity.getDEF());
				}
				spellTimer = 0f;
				caster.setCastSpellNb(-1);
				caster.setTarget_nb(-1);
				manaParticle.reset();
				fireParticle.reset();
			}
		}
		
	}

	@Override
	public void castOnEnt(SpriteBatch batch, float delta, Entity ent, Entity caster) {
		// TODO Auto-generated method stub
		
	}
	
	/*private float calcAng(Vector2 O, Vector3 M) {
		float ang;
		if (M.y >= O.y) {
			if (M.x >= O.x)
				ang = (float) (Math.atan((M.y - O.y) / (M.x - O.x))); // mouse top right
			else
				ang = (float) (Math.PI - Math.atan((M.y - O.y) / (-M.x + O.x))); // mouse top left
		}
		else {
			if (M.x >= O.x)
				ang = (float) (Math.atan((M.y - O.y) / (M.x - O.x))); // mouse bottom right
			else
				ang = (float) (-Math.PI + Math.atan((M.y - O.y) / (M.x - O.x))); // mouse bottom left
		}
		return (float) (ang);
	}*/

	@Override
	public void castOnEnt(SpriteBatch batch, float delta, ArrayList<Entity> ennemies, Entity caster, Vector3 mousePos) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isCollision(Polygon p, Rectangle r) {
	    Polygon rPoly = new Polygon(new float[] { 0, 0, r.width, 0, r.width,
	            r.height, 0, r.height });
	    rPoly.setPosition(r.x, r.y);
	    if (Intersector.overlapConvexPolygons(rPoly, p))
	        return true;
	    return false;
	}

	@Override
	public void castOnEnt(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity caster, Vector3 mouseCoords) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void castOnPlayer(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity caster, Entity target, Entity[] played_entities, Vector3 mousePos) {
		if (!caster.isCasting()) {
			if (caster.getMP() >= mana_cost) {
			float distM = (float) Math.sqrt( Math.pow(caster.getCombatCenter().x - target.getCombatCenter().x, 2) + Math.pow(caster.getCombatCenter().y-target.getCombatCenter().y, 2) );
			float angle = calcAng(new Vector2(caster.getCombatCenter().x, caster.getCombatCenter().y),
					new Vector3(target.getCombatCenter(), 0));
			start_zone = caster.getCombatCenter();
			end_zone = new Vector2(caster.getCombatCenter().x - (caster.getCombatCenter().x - target.getCombatCenter().x)* 128 / distM,
					caster.getCombatCenter().y - (caster.getCombatCenter().y - target.getCombatCenter().y)* 128 / distM);
			
			spell_hitbox = new Polygon(new float[] {
				(float) (caster.getCombatCenter().x - 32*Math.cos(angle)), (float) (caster.getCombatCenter().y - 32*Math.sin(angle)),
				(float) (caster.getCombatCenter().x + 32*Math.cos(angle)), (float) (caster.getCombatCenter().y + 32*Math.sin(angle)),
				(float) (end_zone.x + 128*Math.cos(angle)), (float) (end_zone.y + 128*Math.sin(angle)),
				(float) (end_zone.x - 128*Math.cos(angle)), (float) (end_zone.y - 128*Math.sin(angle)),
			});
			
			caster.setCasting(true);
			caster.addMP(-mana_cost);
			}
		}
		else {/*
			ShapeRenderer shapeRenderer = new ShapeRenderer();
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.polygon(spell_hitbox.getTransformedVertices());
			shapeRenderer.end();
			ShapeRenderer shapeRenderer2 = new ShapeRenderer();
			shapeRenderer2.setProjectionMatrix(camera.combined);
			shapeRenderer2.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer2.line(new Vector3(caster.getCombatCenter().x, caster.getCombatCenter().y, 0), mousePos);
			shapeRenderer2.end();*/
			
			//System.out.println(spell_hitbox.getBoundingRectangle().toString());
			//System.out.println(caster.getCombatCenter().x + " " + caster.getCombatCenter().y);
			//System.out.println(target.getCombatCenter().x + " " + target.getCombatCenter().y);
			
			spellTimer += delta;
			coefTime = spellTimer/SPELL_TIME;
			if (this.spellTimer < SPELL_TIME) {
				if (spell_img[0] != null) { 
					/*PolygonSprite poly; // DRAW INSIDE OF POLY
					PolygonSpriteBatch polyBatch = new PolygonSpriteBatch(); // To assign at the beginning
					Texture textureSolid;

					// Creating the color filling (but textures would work the same way)
					Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
					pix.setColor(0xFF33691E); // DE is red, AD is green and BE is blue.
					pix.fill();
					textureSolid = new Texture(pix);
					EarClippingTriangulator triangulator = new EarClippingTriangulator();
					ShortArray triangleIndices = triangulator.computeTriangles(spell_hitbox.getVertices());
					PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid), spell_hitbox.getVertices(), triangleIndices.toArray());
					poly = new PolygonSprite(polyReg);
					batch.end();
					polyBatch.begin();
					polyBatch.setProjectionMatrix(camera.combined);
					poly.draw(polyBatch);
					polyBatch.end();
					batch.begin();*/
					
					
					batch.draw((TextureRegion) spell_img[0].getKeyFrame(spellTimer, true),
							end_zone.x*coefTime + (1-coefTime)*start_zone.x, 
							end_zone.y*coefTime + (1-coefTime)*start_zone.y, SPELL_WIDTH, SPELL_HEIGHT);
				}
			}
			else {
				caster.setCasting(false);
				for (Entity entity : played_entities) {
					//System.out.println("Spell : " + spell_hitbox.toString());
					//System.out.println("Target : " + target.getCombatHitbox().toString());
					if (isCollision(spell_hitbox, entity.getCombatHitbox()))
						entity.addHP(- this.getPower());
				}
				spellTimer = 0f;
				caster.setCastSpellNb(-1);
				caster.setTarget_nb(-1);
			}
		}
		
	}
	
	

}
