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

public class GirlRedhead extends Entity {
	
	private static final int GIRL_REDHEAD_WIDTH = 64;
	private static final int GIRL_REDHEAD_HEIGHT = 64;
	private static final float GIRL_REDHEAD_ANIM_SPEED = 0.1f;
	
	Animation<?>[] girl_redhead_img;

	public GirlRedhead(float x, float y, GameMap map, boolean ent_played, boolean is_npc) {
		super(x, y, GIRL_REDHEAD_WIDTH - 42, GIRL_REDHEAD_HEIGHT - 46, EntityType.GIRLREDHEAD, map, ent_played, GIRL_REDHEAD_WIDTH, GIRL_REDHEAD_HEIGHT, GIRL_REDHEAD_ANIM_SPEED);
		super.getRekt().setPosition(x + 14, y + 4); // the substractions on top and this line adjust the collision rectangle for the girl
		
		this.setIcon("icons/girl_redhead_icon.png");
		this.setIs_npc(is_npc);
		
		girl_redhead_img = new Animation[5];
		
		TextureRegion[][] girlRedheadSpriteSheet = TextureRegion.split(new Texture("characters/girl_redhead_dress.png"), 32, 32);
		
		girl_redhead_img[0] = new Animation<TextureRegion>(GIRL_REDHEAD_ANIM_SPEED*3, girlRedheadSpriteSheet[0]); // Animation immobile
		girl_redhead_img[1] = new Animation<TextureRegion>(GIRL_REDHEAD_ANIM_SPEED, girlRedheadSpriteSheet[1]); // Animation course bas
		girl_redhead_img[2] = new Animation<TextureRegion>(GIRL_REDHEAD_ANIM_SPEED, girlRedheadSpriteSheet[2]); // Animation course gauche
		girl_redhead_img[3] = new Animation<TextureRegion>(GIRL_REDHEAD_ANIM_SPEED, girlRedheadSpriteSheet[3]); // Animation course droite
		girl_redhead_img[4] = new Animation<TextureRegion>(GIRL_REDHEAD_ANIM_SPEED, girlRedheadSpriteSheet[4]); // Animation course haut
		
		this.setEntImg(girl_redhead_img);
		
		spells = new Spell[4];
		spells[0] = new FireAttack1();
		spells[1] = new HealZone1();
		this.setSpells(spells); 
	}
	
}
