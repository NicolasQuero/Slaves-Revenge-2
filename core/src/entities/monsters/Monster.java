package entities.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Spells.Explo1;
import Spells.Heal1;
import Spells.PiloneErect;
import Spells.Spell;
import entities.Entity;
import entities.EntityType;
import world.GameMap;

public abstract class Monster extends Entity {

	private static final float MONSTER_ANIM_SPEED = 0.125f;
	private static final int MONSTER_WIDTH = 64;
	private static final int MONSTER_HEIGHT = 64;
	
	Animation<?>[] player_img;
	
	Spell[] spells;
	
	public Monster(float x, float y, GameMap map, boolean ent_played, boolean is_npc, String src_img) {
		super(x, y, MONSTER_WIDTH - 14, MONSTER_HEIGHT - 24, EntityType.MONSTER, map, ent_played, MONSTER_WIDTH, MONSTER_HEIGHT, MONSTER_ANIM_SPEED);
		super.getRekt().setPosition(x + 7, y + 12); // the substractions on top and this line adjust the collision rectangle for the player
		this.setIcon("icons/monster_icon.png");
		this.setIs_npc(is_npc);
		player_img = new Animation[5];
		
		TextureRegion[][] playerSpriteSheet = TextureRegion.split(new Texture(src_img), 32, 32);
		
		player_img[0] = new Animation<TextureRegion>(MONSTER_ANIM_SPEED, playerSpriteSheet[0]); // Animation immobile
		player_img[1] = new Animation<TextureRegion>(MONSTER_ANIM_SPEED, playerSpriteSheet[1]);
		player_img[2] = new Animation<TextureRegion>(MONSTER_ANIM_SPEED, playerSpriteSheet[2]);
		player_img[3] = new Animation<TextureRegion>(MONSTER_ANIM_SPEED, playerSpriteSheet[3]);
		player_img[4] = new Animation<TextureRegion>(MONSTER_ANIM_SPEED, playerSpriteSheet[4]);
		
		this.setEntImg(player_img);
		
		this.ATK = 1; this.DEF = 1; this.maxHP = 25; this.HP = maxHP; this.maxMP = 25; this.MP = maxMP;
		
		spells = new Spell[4];
		spells[0] = new PiloneErect();
		spells[1] = new Heal1();
		this.setSpells(spells); 
	}
	
	@Override
	public void render(SpriteBatch batch, float delta) {
		movementOutOfCombat(delta);
		super.render_unplayed(batch, delta);
	}
	
	abstract public void movementOutOfCombat(float delta);
	
}
