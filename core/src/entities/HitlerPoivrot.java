package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Spells.Explo1;
import Spells.FireAttack1;
import Spells.Heal1;
import Spells.HealZone1;
import Spells.Spell;
import world.GameMap;

public class HitlerPoivrot extends Entity {

	private static final float PLAYER_ANIM_SPEED = 0.5f;
	private static final float PLAYER_ANIM_SPEED_RUN = 0.1f;
	private static final int PLAYER_WIDTH = 64;
	private static final int PLAYER_HEIGHT = 64;
	
	Animation<?>[] player_img;
	
	Spell[] spells;
	

	public HitlerPoivrot(float x, float y, GameMap map, boolean ent_played, boolean is_npc) {
		super(x, y, PLAYER_WIDTH - 28, PLAYER_HEIGHT - 48, EntityType.HITLERPOIVROT, map, ent_played, PLAYER_WIDTH, PLAYER_HEIGHT, PLAYER_ANIM_SPEED_RUN);
		super.getRekt().setPosition(x + 14, y + 24); // the substractions on top and this line adjust the collision rectangle for the player
		this.setIcon("icons/hitler_poivrot_icon.png");
		this.setIs_npc(is_npc);
		player_img = new Animation[5];
		
		TextureRegion[][] playerSpriteSheet = TextureRegion.split(new Texture("characters/hitler_poivrot.png"), 32, 32);
		
		player_img[0] = new Animation<TextureRegion>(PLAYER_ANIM_SPEED, playerSpriteSheet[0]); // Animation immobile
		player_img[1] = new Animation<TextureRegion>(PLAYER_ANIM_SPEED_RUN, playerSpriteSheet[1]); // Animation course bas
		player_img[2] = new Animation<TextureRegion>(PLAYER_ANIM_SPEED_RUN, playerSpriteSheet[2]); // Animation course gauche
		player_img[3] = new Animation<TextureRegion>(PLAYER_ANIM_SPEED_RUN, playerSpriteSheet[3]); // Animation course droite
		player_img[4] = new Animation<TextureRegion>(PLAYER_ANIM_SPEED_RUN, playerSpriteSheet[4]); // Animation course haut
		
		this.setEntImg(player_img);
		
		this.ATK = 10; this.DEF = 10; this.maxHP = 100; this.HP = maxHP; this.maxMP = 100; this.MP = maxMP;
		
		spells = new Spell[4];
		spells[0] = new FireAttack1();
		spells[1] = new HealZone1();
		this.setSpells(spells); 
	}
	
}


