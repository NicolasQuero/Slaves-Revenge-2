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

public abstract class Spell {

	private String name, description;
	protected Animation<?>[] spell_img;
	private Texture spell_icon;
	private int spell_width;
	private int spell_height;
	private int spell_anim_count;
	protected int mana_cost;
	
	private boolean charging;
	protected float spellTimer;
	protected float coefTime;
	
	private float cooldown;
	private int power, lvl;
	
	private float stateTime;
	
	public Spell(int width, int height, int power) {
		this.spell_width = width;
		this.spell_height = height;
		this.power = power;
		this.lvl = 1;
		this.spell_anim_count = 0;
		this.spellTimer = 0f; this.coefTime = 0f; this.cooldown = 0f;
		this.name = "Padnome";
		this.description = "Ce sort est tellement mauvais qu'il \n n'a pas de description fdp."; 
		this.charging = false;
		stateTime = 0f;
	}
	
	public Spell(String name, int width, int height, int power, String src_img, float SPELL_ANIM_SPEED, int size_img32, String src_icon) {
		this.name = name;
		this.description = "Ce sort est tellement mauvais qu'il \n n'a pas de description fdp."; 
		this.lvl = 1;
		this.spell_width = width;
		this.spell_height = height;
		this.power = power;
		this.spell_anim_count = 0;
		this.spellTimer = 0f; this.coefTime = 0f; this.cooldown = 0f;
		stateTime = 0;
		this.spell_icon = new Texture(src_icon);
		this.spell_img = new Animation[1];
		
		TextureRegion[][] spellSpriteSheet = TextureRegion.split(new Texture(src_img), size_img32*32, size_img32*32);
		
		this.spell_img[0] = new Animation<TextureRegion>(SPELL_ANIM_SPEED , spellSpriteSheet[0]);
		
	}
	
	public void drawImg(SpriteBatch batch, float delta, float x, float y, float x_end, float y_end) {
		if (spell_img[spell_anim_count] != null)
			batch.draw((TextureRegion) spell_img[spell_anim_count].getKeyFrame(stateTime, true), x, y, spell_width, spell_height);
		stateTime += delta;
	}
	
	public void drawIcon(SpriteBatch batch, float delta, float x, float y) {
		if (spell_icon != null)
			batch.draw(spell_icon, x, y, 64, 64);
		stateTime += delta;
	}
	
	public void lvlUp() {
		this.lvl += 1;
		this.power += 2;
	}
	
	public int getLvl() {
		return this.lvl;
	}
	
	public boolean isCharging() {
		return charging;
	}

	public void setCharging(boolean charging) {
		this.charging = charging;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setIcon(Texture icon) {
		this.spell_icon = icon;
	}
	
	public Texture getIcon() {
		return this.spell_icon;
	}
	
	public void setImg(Animation<?>[] spell_img) {
		this.spell_img = spell_img;
	}
	
	public int getManaCost() {
		return this.mana_cost;
	}
	
	public int getPower() {
		return this.power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public float getCooldown() {
		return this.cooldown;
	}
	
	public abstract void castOnEnt(SpriteBatch batch, float delta, Entity ent, Entity caster);
	public abstract void castOnEnt(SpriteBatch batch, float delta, ArrayList<Entity> ennemies, Entity caster, Vector3 mousePos);

	public abstract void castOnEnt(SpriteBatch batch, OrthographicCamera camera, float delta,
			ArrayList<Entity> ennemies, Entity entity, Vector3 vector3);

	public abstract void castOnEnt(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies,
			Entity caster, Entity[] played_entities, Vector3 mouseCoords);

	public abstract void castOnPlayer(SpriteBatch batch, OrthographicCamera camera, float delta,
			ArrayList<Entity> ennemies, Entity caster, Entity target, Entity[] played_entities, Vector3 vector3);
	
	protected float calcAng(Vector2 O, Vector3 M) {
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
	}
}
