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

public class BearNice extends Entity {
	private static final int BEAR_NICE_WIDTH = 64;
	private static final int BEAR_NICE_HEIGHT = 64;
	private static final float BEAR_NICE_ANIM_SPEED = 0.1f;
	
	Animation<?>[] bear_nice_img;

	public BearNice(float x, float y, GameMap map, boolean ent_played, boolean is_npc) {
		super(x, y, BEAR_NICE_WIDTH - 32, BEAR_NICE_HEIGHT - 28, EntityType.BEARNICE, map, ent_played, BEAR_NICE_WIDTH, BEAR_NICE_HEIGHT, BEAR_NICE_ANIM_SPEED);
		super.getRekt().setPosition(x + 10, y + 4); 
		this.setIcon("icons/bear_nice_icon.png");
		this.setIs_npc(is_npc);
		
		bear_nice_img = new Animation[5];
		
		TextureRegion[][] bearNiceSpriteSheet = TextureRegion.split(new Texture("characters/bear_nice.png"), 32, 32);
		
		bear_nice_img[0] = new Animation<TextureRegion>(BEAR_NICE_ANIM_SPEED*3, bearNiceSpriteSheet[0]); // Animation immobile
		bear_nice_img[1] = new Animation<TextureRegion>(BEAR_NICE_ANIM_SPEED, bearNiceSpriteSheet[1]); // Animation course bas
		bear_nice_img[2] = new Animation<TextureRegion>(BEAR_NICE_ANIM_SPEED, bearNiceSpriteSheet[2]); // Animation course gauche
		bear_nice_img[3] = new Animation<TextureRegion>(BEAR_NICE_ANIM_SPEED, bearNiceSpriteSheet[3]); // Animation course droite
		bear_nice_img[4] = new Animation<TextureRegion>(BEAR_NICE_ANIM_SPEED, bearNiceSpriteSheet[4]); // Animation course haut
		
		this.setEntImg(bear_nice_img);
		
		spells = new Spell[4];
		spells[0] = new FireAttack1();
		spells[1] = new HealZone1();
		this.setSpells(spells); 
	}


}
