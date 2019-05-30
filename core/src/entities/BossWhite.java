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

public class BossWhite extends Entity {

	private static final float BOSS_WHITE_ANIM_SPEED = 0.3f;
	private static final int BOSS_WHITE_WIDTH = 64;
	private static final int BOSS_WHITE_HEIGHT = 64;
	
	Animation<?>[] boss_white_img;
	
	public BossWhite(float x, float y, GameMap map, boolean ent_played, boolean is_npc) {
		super(x, y, BOSS_WHITE_WIDTH-20, BOSS_WHITE_HEIGHT-40, EntityType.BOSSWHITE, map, ent_played, BOSS_WHITE_WIDTH, BOSS_WHITE_HEIGHT, BOSS_WHITE_ANIM_SPEED);
		this.setIcon("icons/boss_blanc_icon.png");
		this.setIs_npc(is_npc);
		
		boss_white_img = new Animation[5];
		TextureRegion[][] bossWhiteSpriteSheet = TextureRegion.split(new Texture("boss_white.png"), 32, 32);
		
		boss_white_img[0] = new Animation<TextureRegion>(BOSS_WHITE_ANIM_SPEED, bossWhiteSpriteSheet[0]);
		
		this.setEntImg(boss_white_img);

		this.ATK = 3; this.DEF = 3; this.maxHP = 100; this.HP = maxHP; this.maxMP = 100; this.MP = maxMP;

		spells = new Spell[4];
		spells[0] = new FireAttack1();
		spells[1] = new HealZone1();
		this.setSpells(spells); 
	}
	
}
