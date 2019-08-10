package entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import utilities.GameAssetManager;

import PopUps.LvlUpPopUp;
import Spells.Spell;
import items.Item;
import world.GameMap;

public abstract class Entity {
	
	private static Stage stage;

	protected boolean ent_played; // true if the entity is played by the user
	protected boolean is_npc;
	protected Vector2 pos; // pos of the entity
	protected Vector2 combat_pos;
	protected Rectangle combat_hitbox;
	protected Vector2 center;
	protected EntityType type; // type of the entity (ex : BossWhite, Player...)
	protected static GameMap map; // map of the game
	protected Rectangle rect; // rectangle in which the character resides, for collisions
	protected Rectangle interaction_rectangle;
	protected Texture icon;
	protected float dist_closest_ent;
	Entity[] target = new Entity[1];
	private Item[] equipedItems;
	
	
	public static void setMap(GameMap map) {
		Entity.map = map;
	}

	protected Spell[] spells;
	protected int castSpellNb;

	protected boolean casting;
	protected int target_nb;
	
	protected int lvl_ent, xp, xp_given;

	protected final static int[] xp_palliers = {100, 300, 600, 1000, 1500, 2500, 4000, 6000, 9000,
											13000, 18000, 24000, 31000, 39000, 48000, 58000, 70000, 85000, 100000, 120000}; // paliers de niveaux
	protected int ATK, atkBonus, DEF, defBonus;
	protected float maxHP, HP;
	protected int maxMP, MP;
	protected int mana_regen;
	protected int upPts;
	protected int upSpellPts;

	protected boolean alive;
	protected boolean aggro;
	protected float energy;

	public float getEnergy() {
		return energy;
	}

	public void addEnergy(float amount) {
		this.energy += amount;
		if (energy < 0f)
			energy = 0f;
		else if (energy > 4f)
			energy = 4f;
	}

	protected float dashAngle;
	protected Vector2 dashTarget;
	protected boolean isDashing;
	protected static final float DASH_TIME = 0.1f;
	protected final int dashRange = 128;
	protected float dashTimer;
	protected static final float DASH_ANIM_SPEED = 0.05f;
	private static final Animation<?> dash_img = new Animation<TextureRegion>(DASH_ANIM_SPEED, TextureRegion.split(new Texture("spells/dash.png"), 32, 32)[0]);
	
	private static final int SPEED = 160;
	
	protected final int ENT_WIDTH;
	protected final int ENT_HEIGHT;
	protected final float ENT_ANIM_SPEED_RUN;

	private final float dist_sprites = 48;
	private float temp[] = new float[2];
	
	char moves; // you can move only in one direction at once 'n' for none, 'l' for left, 'd' for down, 'r' for right, 'u' for up
	float stateTime;
	float courseTimer;
	int ent_anim_count;
	Animation<?>[] ent_img;
	
	
	protected Entity(float x, float y, float rectWidth, float rectHeight, EntityType type, GameMap map, boolean ent_played, int entwidth, int entheight, float entanimspeedrun) {
		this.ent_played = ent_played;
		this.rect = new Rectangle(x , y, rectWidth, rectHeight); // adjustments for better looking rectangle
		this.pos = new Vector2(x, y);
		this.combat_pos = new Vector2 (-1, -1);
		this.type = type;
		this.icon = null;
		if (map != null)
			Entity.map = map;
		this.ENT_WIDTH = entwidth;
		this.ENT_HEIGHT = entheight;
		this.combat_hitbox = new Rectangle(combat_pos.x, combat_pos.y, entwidth, entheight);
		this.ENT_ANIM_SPEED_RUN = entanimspeedrun;
		this.moves = 'n';
		this.maxHP = 1000; this.HP = maxHP; this.maxMP = 100; this.MP = maxMP; this.ATK = 10; this.DEF = 10; this.mana_regen = 2; this.alive = true;
		this.atkBonus = 0; this.defBonus = 0;
		this.aggro = false;
		this.is_npc = true;
		this.interaction_rectangle = new Rectangle(x - 25, y - 25, ENT_WIDTH + 50, ENT_WIDTH + 50);
		this.center = new Vector2(pos.x + ENT_WIDTH/2, pos.y + ENT_HEIGHT/2);
		this.lvl_ent = 1; this.xp = 0; this.xp_given = 100; this.upPts = 0; this.upSpellPts = 0;
		this.castSpellNb = -1;
		this.target_nb = -1;
		this.dist_closest_ent = 10000f;
		this.dashTimer = 0f;
		this.isDashing = false;
		this.equipedItems = new Item[3];
		
		ent_anim_count = 0;
		stateTime = 0;
		courseTimer = 0;
	}
	
	public int getAtkBonus() {
		return atkBonus;
	}
	
	public int getDefBonus() {
		return defBonus;
	}
	
	public void setWeapon(Item item) {
		if (equipedItems[0] != null) {
			this.atkBonus -= equipedItems[0].getAtkBonus();
			this.defBonus -= equipedItems[0].getDefBonus();
			this.ATK -= equipedItems[0].getAtkBonus();
			this.DEF -= equipedItems[0].getDefBonus();
		}
		this.equipedItems[0] = item;
		this.atkBonus += item.getAtkBonus();
		this.defBonus += item.getDefBonus();
		this.ATK += item.getAtkBonus();
		this.DEF += item.getDefBonus();
	}
	
	public void setTopEquipment(Item item) {
		if (equipedItems[1] != null) {
			this.atkBonus -= equipedItems[1].getAtkBonus();
			this.defBonus -= equipedItems[1].getDefBonus();
			this.ATK -= equipedItems[1].getAtkBonus();
			this.DEF -= equipedItems[1].getDefBonus();
		}
		this.equipedItems[1] = item;
		this.atkBonus += item.getAtkBonus();
		this.defBonus += item.getDefBonus();
		this.ATK += item.getAtkBonus();
		this.DEF += item.getDefBonus();
	}
	
	public void setBotEquipment(Item item) {
		if (equipedItems[2] != null) {
			this.atkBonus -= equipedItems[2].getAtkBonus();
			this.defBonus -= equipedItems[2].getDefBonus();
			this.ATK -= equipedItems[2].getAtkBonus();
			this.DEF -= equipedItems[2].getDefBonus();
		}
		this.equipedItems[2] = item;
		this.atkBonus += item.getAtkBonus();
		this.defBonus += item.getDefBonus();
		this.ATK += item.getAtkBonus();
		this.DEF += item.getDefBonus();
	}
	
	public Item[] getEquipedItems() {
		return this.equipedItems;
	}
	
	public void setPos(Vector2 pos) {
		this.pos = pos;
		this.interaction_rectangle.setPosition(pos);
	}
	
	public int getLvl_ent() {
		return lvl_ent;
	}

	public int getXp() {
		return xp;
	}

	public void addXp(int amount) {
		this.xp += amount;
		for (int i = this.lvl_ent - 1; i < xp_palliers.length; i++) {
			if (xp_palliers[i] <= xp) {
				this.lvlUp();;
			}
			else
				break;
		}
	}

	public int getUpPts() {
		return upPts;
	}
	
	public void addUpPts(int amount) {
		upPts += amount;
	}
	
	public int getUpSpellPts() {
		return this.upSpellPts;
	}
	
	public void addUpSpellPts(int amount) {
		upSpellPts += amount;
	}
	
	public void addAtk(int amount) {
		this.ATK += amount;
	}
	
	public boolean isDashing() {
		return isDashing;
	}

	public static int[] getXpPalliers() {
		return xp_palliers;
	}
	
	public int getXpBeforeNextLvl() {
		if (this.lvl_ent > 1)
			return (this.xp - Entity.getXpPalliers()[this.lvl_ent - 2]);
		else
			return this.xp;
	}
	
	public int getXpForNextLvl() {
		if (this.lvl_ent > 1)
			return (Entity.getXpPalliers()[this.lvl_ent - 1] - Entity.getXpPalliers()[this.lvl_ent - 2]);
		else
			return Entity.getXpPalliers()[this.lvl_ent - 1];
	}
	
	public void lvlUp() {
		if (this.lvl_ent < 20) {
			this.lvl_ent += 1;
			this.ATK += 2;
			this.DEF += 1;
			this.maxHP += 10;
			this.maxMP += 10;
			this.upPts += 5;
			this.upSpellPts += 1;
			if (this.getType().getId().equals("Dayuki"))
				new LvlUpPopUp(this, stage);
		}
	}
	
	public boolean isAggro() {
		return aggro;
	}

	public void setAggro(boolean aggro) {
		this.aggro = aggro;
	}

	public int getTarget_nb() {
		return target_nb;
	}

	public void setTarget_nb(int target_nb) {
		this.target_nb = target_nb;
	}

	public int getCastSpellNb() {
		return castSpellNb;
	}

	public void setCastSpellNb(int castSpellNb) {
		this.castSpellNb = castSpellNb;
	}

	public Rectangle getInteraction_rectangle() {
		return interaction_rectangle;
	}

	public boolean isNpc() {
		return is_npc;
	}


	public void setIs_npc(boolean is_npc) {
		this.is_npc = is_npc;
	}
	
	public int getXpGiven() {
		return xp_given;
	}

	public void setXpGiven(int xp_given) {
		this.xp_given = xp_given;
	}
	
	public static void setStage(Stage stage) {
		Entity.stage = stage;
	}
	
	public static Stage getStage() {
		return Entity.stage;
	}
	
	public void moveX (float amountX) {
		if (!map.doesRectCollideWithMap(this, amountX, 0)) {
			this.pos.x += amountX;
			this.rect.setX(this.rect.getX() + amountX);
			this.interaction_rectangle.setX(this.rect.getX());
		}
		if(amountX > 0)
			this.ent_anim_count = 3;
		else if (amountX < 0)
			this.ent_anim_count = 2;
	}
	
	public void moveY (float amountY) {
		if (!map.doesRectCollideWithMap(this, 0, amountY)) {
			this.pos.y += amountY;
			this.rect.setY(this.rect.getY() + amountY);
			this.interaction_rectangle.setY(this.rect.getY());
		}
		if(amountY > 0)
			this.ent_anim_count = 4;
		else if (amountY < 0)
			this.ent_anim_count = 1;
	}
	
	private void moveCombatX(float amountX) {
		//if (!map.doesRectCollideWithMap(this, 0, amountY)) {
		if (combat_pos.x + amountX < 1280 && combat_pos.x + amountX >0) {
			this.combat_pos.x += amountX;
			this.combat_hitbox.setPosition(combat_pos);
		}
			//this.rect.setY(this.rect.getY() + amountY);
			//this.interaction_rectangle.setY(this.rect.getY());
		//}
		
	}
	
	private void moveCombatY(float amountY) {
		//if (!map.doesRectCollideWithMap(this, 0, amountY)) {
		if (combat_pos.y + amountY < 800 && combat_pos.y + amountY > 0) {
			this.combat_pos.y += amountY;
			this.combat_hitbox.setPosition(combat_pos);
		}
			//this.rect.setY(this.rect.getY() + amountY);
			//this.interaction_rectangle.setY(this.rect.getY());
		//}
		
	}
	
	public int getManaRegen() {
		return this.mana_regen;
	}
	
	public void setEntImg(Animation<?>[] ent_img) {
		this.ent_img = ent_img;
	}
	
	public void setSpells(Spell[] spells) {
		this.spells = spells;
	}
	
	public Spell[] getSpells() {
		return spells;
	}
	
	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}
	
	public Vector2 getCombatPos() {
		return this.combat_pos;
	}
	
	public void setCombatPos(float x, float y) {
		this.combat_pos.x = x;
		this.combat_pos.y = y;
		this.combat_hitbox.x = x;
		this.combat_hitbox.y = y;
	}
	
	public Rectangle getCombatHitbox() {
		return combat_hitbox;
	}

	public Vector2 getCenter() {
		center.x = pos.x + ENT_WIDTH/2; center.y = pos.y + ENT_HEIGHT/2;
		return center;
	}
	
	public int getATK() {
		return this.ATK;
	}
	
	public void setATK(int Atk) {
		ATK = Atk;
	}

	public void setDEF(int Def) {
		DEF = Def;
	}

	public void setMaxHP(float maxHP) {
		this.maxHP = maxHP;
	}

	public void setHP(float hp) {
		HP = hp;
	}

	public void setMaxMP(int maxMP) {
		this.maxMP = maxMP;
	}

	public void setMP(int mp) {
		MP = mp;
	}

	public int getDEF() {
		return this.DEF;
	}
	
	public void addDef(int amount) {
		this.DEF += amount;
	}
	
	public int getMaxMP() {
		return this.maxMP;
	}
	
	public int getMP() {
		return this.MP;
	}
	
	public float getMaxHP() {
		return this.maxHP;
	}
	
	public float getHP() {
		return this.HP;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public void addHP(float amount) {
		this.HP += amount;
		if (this.HP < 0) {
			this.HP = 0;
			this.setAlive(false);
		}
		else if (this.HP > maxHP)
			this.HP = maxHP;
	}
	
	public void addMP(int amount) {
		this.MP += amount;
		if (this.MP < 0)
			this.MP = 0;
		else if (this.MP > maxMP)
			this.MP = maxMP;
	}
	
	public Rectangle getRekt() {
		return rect;
	}
	
	public boolean isPlayed() {
		return ent_played;
	}
	
	public void setPlayed(boolean a) {
		this.ent_played = a;
	}

	public EntityType getType() {
		return type;
	}
	
	public void setIcon(String chemin) {
		this.icon = new Texture(chemin);
	}
	
	public Texture getIcon() {
		return icon;
	}
	
	public int getWidth() {
		return type.getWidth();
	}
	
	public int getHeight() {
		return type.getHeight();
	}
	
	public boolean isCasting() {
		return casting;
	}

	public void setCasting(boolean casting) {
		this.casting = casting;
	}

	public void render(SpriteBatch batch, float delta) {
		if (!GameMap.choose_charac_open && !GameMap.upgrade_ents_open) {
			if (isPlayed()) {
				if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.Z)) && (moves == 'u' || moves == 'n')) {
					moveY(SPEED * delta);
					if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
						moveY(3*SPEED * delta);
					moves = 'u';
					ent_anim_count = 4;
					courseTimer -= Gdx.graphics.getDeltaTime();
					if (Math.abs(courseTimer) > ENT_ANIM_SPEED_RUN) {
						courseTimer = 0;
					}
				} else {
					if (ent_anim_count == 4) {
						courseTimer += Gdx.graphics.getDeltaTime();
						if (Math.abs(courseTimer) > 0.05) {
							moves = 'n';
							courseTimer = 0;
							ent_anim_count = 0;
						}
					}
				}
				
				if ((Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) && (moves == 'd'||moves == 'n')) {
					moveY(-SPEED * delta);
					if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
						moveY(-3*SPEED * delta);
					moves = 'd';
					ent_anim_count = 1;
					courseTimer -= Gdx.graphics.getDeltaTime();
					if (Math.abs(courseTimer) > ENT_ANIM_SPEED_RUN) {
						courseTimer = 0;
					}
				} else {
					if (ent_anim_count == 1) {
						courseTimer += Gdx.graphics.getDeltaTime();
						if (Math.abs(courseTimer) > 0.05) {
							moves = 'n';
							courseTimer = 0;
							ent_anim_count = 0;
						}
					}
				}
				
				if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.Q)) && (moves == 'l'||moves == 'n')) {
					moveX(-SPEED * delta);
					if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
						moveX(-3*SPEED * delta);
					moves = 'l';
					ent_anim_count = 2;
					courseTimer -= Gdx.graphics.getDeltaTime();
					if (Math.abs(courseTimer) > ENT_ANIM_SPEED_RUN) {
						courseTimer = 0;
					}
				} else {
					if (ent_anim_count == 2) {
						courseTimer += Gdx.graphics.getDeltaTime();
						if (Math.abs(courseTimer) > 0.05) {
							moves = 'n';
							courseTimer = 0;
							ent_anim_count = 0;
						}
					}
				}
					
				if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) && (moves == 'r'||moves == 'n')) {
					moveX(SPEED * delta);
					if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
						moveX(3*SPEED * delta);
					moves = 'r';
					ent_anim_count = 3;
					courseTimer -= Gdx.graphics.getDeltaTime();
					if (Math.abs(courseTimer) > ENT_ANIM_SPEED_RUN) {
						courseTimer = 0;
					}
				} else {
					if (ent_anim_count == 3) {
						courseTimer += Gdx.graphics.getDeltaTime();
						if (Math.abs(courseTimer) > 0.05) {
							moves = 'n';
							courseTimer = 0;
							ent_anim_count = 0;
						}
					}
				}
				if (Gdx.input.isKeyJustPressed(Keys.H) && Gdx.input.isKeyJustPressed(Keys.W)) {
					pos.x = (float) (Math.random()*1280); 
					pos.y = (float) (Math.random()*800);
					rect.setPosition(pos.x, pos.y);
					while (map.doesRectCollideWithMap(this, 0, 0)) {
						pos.x = (float) (Math.random()*1280); 
						pos.y = (float) (Math.random()*800);
						rect.setPosition(pos.x, pos.y);
					}
				}
			}
			else if(isNpc()) {
				this.render_unplayed(batch, delta);
			}
				
		}
		if (!isNpc() && ent_img[ent_anim_count] != null)
			batch.draw((TextureRegion) ent_img[ent_anim_count].getKeyFrame(stateTime, true), pos.x, pos.y, ENT_WIDTH, ENT_HEIGHT);
		stateTime += delta;
		
	}
	
	public void useAI (SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies, Entity[] played_entities) {
		int i = -1;
		for (Entity ent : played_entities) {
			if (ent != null)
				i += 1;
		}
		if (i != -1 && this.getSpells() != null) {
			if (this.getSpells()[0] != null) {
				if (target_nb == -1)
					target_nb = (int) Math.floor(Math.random()*(i+1));
				this.getSpells()[0].castOnPlayer(batch, camera, delta, ennemies, played_entities[target_nb], this, played_entities, null);
			}
		}
	}
	
	public void render_unplayed (SpriteBatch batch, float delta) {
		if (ent_img[ent_anim_count] != null && this.isAlive())
			batch.draw((TextureRegion) ent_img[ent_anim_count].getKeyFrame(stateTime, true),  pos.x, pos.y, ENT_WIDTH, ENT_HEIGHT);
		stateTime += delta;
	}
	
	public void render_atpos (SpriteBatch batch, float delta, float x, float y) {
		if (ent_img[0]!= null)
			batch.draw((TextureRegion) ent_img[0].getKeyFrame(stateTime, true),  x, y, ENT_WIDTH*2, ENT_HEIGHT*2);
		stateTime += delta;
	}
	
	public void follow (SpriteBatch batch, float delta, Entity entity_played) {
		temp[0] = entity_played.getX() - pos.x;
		temp[1] = entity_played.getY() - pos.y;
		courseTimer += Gdx.graphics.getDeltaTime();
		
		if (temp[0]*temp[0] + temp[1]*temp[1] > dist_sprites * dist_sprites * 1.7f) { //If we are too far to prevent bugs
			this.pos.x = entity_played.getX();
			this.pos.y = entity_played.getY();
			this.rect.setPosition(pos.x, pos.y);
		}
		
		if (temp[0]*temp[0] + temp[1]*temp[1] < dist_sprites * dist_sprites * (1+dist_sprites/100) && courseTimer > 0.1)
			ent_anim_count = 0;
		while (temp[0]*temp[0] + temp[1]*temp[1] > dist_sprites * dist_sprites) { // while the distance is too big, we go closer to the target
			this.moveX(temp[0]/20);
			this.moveY(temp[1]/20);
			
			
			if (entity_played.getX() - pos.x == temp[0] && entity_played.getY() - pos.y == temp[1]) // in case we couldn't move to prevent infinite loop
				break;

			if (pos.y + temp[1] - entity_played.getY() > 0.7f) {
				ent_anim_count = 4;
				courseTimer = 0;
			}
			else if (pos.y + temp[1] - entity_played.getY() < -0.7f){
				ent_anim_count = 1;
				courseTimer = 0;
			}
			else if (pos.x + temp[0] - entity_played.getX() > 0.7f){
				ent_anim_count = 3;
				courseTimer = 0;
			} 
			else if (pos.x + temp[0] - entity_played.getX() < -0.7f){
				ent_anim_count = 2;
				courseTimer = 0;
			} 
			
			temp[0] = entity_played.getX() - pos.x;
			temp[1] = entity_played.getY() - pos.y;
		}

		if (ent_img[ent_anim_count] != null)
			batch.draw((TextureRegion) ent_img[ent_anim_count].getKeyFrame(stateTime, true),  pos.x, pos.y, ENT_WIDTH, ENT_HEIGHT);
		stateTime += delta;
	}

	public void render_combat(SpriteBatch batch, float delta) {
		if (!GameMap.choose_charac_open) {
			if ((Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.Z)) && (moves == 'u' || moves == 'n')) {
				moveCombatY(SPEED * delta);
				if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
					moveCombatY(3*SPEED * delta);
				moves = 'u';
				ent_anim_count = 4;
				courseTimer -= Gdx.graphics.getDeltaTime();
				if (Math.abs(courseTimer) > ENT_ANIM_SPEED_RUN) {
					courseTimer = 0;
				}
			} else {
				if (ent_anim_count == 4) {
					courseTimer += Gdx.graphics.getDeltaTime();
					if (Math.abs(courseTimer) > 0.05) {
						moves = 'n';
						courseTimer = 0;
						ent_anim_count = 0;
					}
				}
			}
			
			if ((Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) && (moves == 'd'||moves == 'n')) {
				moveCombatY(-SPEED * delta);
				if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
					moveCombatY(-3*SPEED * delta);
				moves = 'd';
				ent_anim_count = 1;
				courseTimer -= Gdx.graphics.getDeltaTime();
				if (Math.abs(courseTimer) > ENT_ANIM_SPEED_RUN) {
					courseTimer = 0;
				}
			} else {
				if (ent_anim_count == 1) {
					courseTimer += Gdx.graphics.getDeltaTime();
					if (Math.abs(courseTimer) > 0.05) {
						moves = 'n';
						courseTimer = 0;
						ent_anim_count = 0;
					}
				}
			}
			
			if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.Q)) && (moves == 'l'||moves == 'n')) {
				moveCombatX(-SPEED * delta);
				if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
					moveCombatX(-3*SPEED * delta);
				moves = 'l';
				ent_anim_count = 2;
				courseTimer -= Gdx.graphics.getDeltaTime();
				if (Math.abs(courseTimer) > ENT_ANIM_SPEED_RUN) {
					courseTimer = 0;
				}
			} else {
				if (ent_anim_count == 2) {
					courseTimer += Gdx.graphics.getDeltaTime();
					if (Math.abs(courseTimer) > 0.05) {
						moves = 'n';
						courseTimer = 0;
						ent_anim_count = 0;
					}
				}
			}
				
			if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) && (moves == 'r'||moves == 'n')) {
				moveCombatX(SPEED * delta);
				if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
					moveCombatX(3*SPEED * delta);
				moves = 'r';
				ent_anim_count = 3;
				courseTimer -= Gdx.graphics.getDeltaTime();
				if (Math.abs(courseTimer) > ENT_ANIM_SPEED_RUN) {
					courseTimer = 0;
				}
			} else {
				if (ent_anim_count == 3) {
					courseTimer += Gdx.graphics.getDeltaTime();
					if (Math.abs(courseTimer) > 0.05) {
						moves = 'n';
						courseTimer = 0;
						ent_anim_count = 0;
					}
				}
			}
				
		}
		if (ent_img[ent_anim_count] != null)
			batch.draw((TextureRegion) ent_img[ent_anim_count].getKeyFrame(stateTime, true), combat_pos.x, combat_pos.y, ENT_WIDTH, ENT_HEIGHT);
		stateTime += delta;
		
	}
	
	public void render_combat_droite(SpriteBatch batch, float delta) {
		if (this.isAlive()) {
			if (ent_img[ent_anim_count] != null)
				batch.draw((TextureRegion) ent_img[ent_anim_count].getKeyFrame(stateTime, true),  combat_pos.x, combat_pos.y, ENT_WIDTH, ENT_HEIGHT);
			stateTime += delta;
		}
	}
	
	public void friendly_ai(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies, Entity[] played_entities) {
		for (Entity entity : ennemies) { // check closest ent
			if (entity.isAlive() && this.dist_ent(entity) <= dist_closest_ent) {
				dist_closest_ent = this.dist_ent(entity);
				target[0] = entity;
			}
		}
		
		if (dist_closest_ent > 64f) {
			float angle = calcAng(this.combat_pos, target[0].getCombatPos());
			this.moveCombatX((float) (Math.cos(angle)*50*delta));
			this.moveCombatY((float) (Math.sin(angle)*50*delta));
		}
		if (this.isCasting()) {
			this.spells[0].castOnEnt(batch, camera, delta, ennemies, this, played_entities, new Vector3(target[0].getCombatCenter(),0));
		}
		if (dist_closest_ent < 100f && !this.isCasting()) {
			this.spells[0].castOnEnt(batch, camera, delta, ennemies, this, played_entities, new Vector3(target[0].getCombatCenter(),0));
		}
		dist_closest_ent = 10000f;
	}
	
	public void basic_ai(SpriteBatch batch, OrthographicCamera camera, float delta, ArrayList<Entity> ennemies, Entity[] played_entities) {
		for (Entity entity : played_entities) { // check closest ent
			if (entity.isAlive() && this.dist_ent(entity) <= dist_closest_ent) {
				dist_closest_ent = this.dist_ent(entity);
				target[0] = entity;
			}
		}
		
		if (dist_closest_ent > 64f) {
			float angle = calcAng(this.combat_pos, target[0].getCombatPos());
			this.moveCombatX((float) (Math.cos(angle)*100*delta));
			this.moveCombatY((float) (Math.sin(angle)*100*delta));
			if (Math.abs((float) (Math.cos(angle)*100*delta)) > Math.abs((float) (Math.sin(angle)*100*delta))) {
				if (Math.cos(angle)*100*delta > 0) {
					if (moves != 'r')
						courseTimer = 0f;
					else
						courseTimer += delta;
					moves = 'r';
					ent_anim_count = 3; 
				}
				else {
					if (moves != 'l')
						courseTimer = 0f;
					else
						courseTimer += delta;
					moves = 'l';
					ent_anim_count = 2;
				}
				
			}
			else {
				if (Math.sin(angle)*100*delta > 0) {
					if (moves != 'u')
						courseTimer = 0f;
					else
						courseTimer += delta;
					moves = 'u';
					ent_anim_count = 4;
				}
				else {
					if (moves != 'd')
						courseTimer = 0f;
					else
						courseTimer += delta;
					moves = 'd';
					ent_anim_count = 1;
				}
			}
				
		}
		else
			ent_anim_count = 0;
		if (this.isCasting()) {
			this.spells[0].castOnPlayer(batch, camera, delta, ennemies, this, target[0], played_entities, new Vector3(target[0].getCombatCenter(),0));
		}
		if (dist_closest_ent < 100f && !this.isCasting()) {
			this.spells[0].castOnPlayer(batch, camera, delta, ennemies, this, target[0], played_entities, new Vector3(target[0].getCombatCenter(),0));
		}
		dist_closest_ent = 10000f;
		
	}
	
	public void dashTo(SpriteBatch batch, float delta, Vector3 mousePos) {
		if (!this.isDashing && this.energy >= 1f) {
			this.isDashing = true;
			dashTimer = 0f;
			this.dashTarget = new Vector2(mousePos.x, mousePos.y);
			dashAngle = calcAng(this.getCombatCenter(), dashTarget);
			this.addEnergy(-1f);
		}
		else if (this.isDashing){
			if (dashTimer + delta < DASH_TIME) {
				this.moveCombatX((float) (Math.cos(dashAngle)*dashRange*delta/DASH_TIME));
				this.moveCombatY((float) (Math.sin(dashAngle)*dashRange*delta/DASH_TIME));
				dashTimer += delta;
			}
			else {
				this.isDashing = false;
				this.moveCombatX((float) (Math.cos(dashAngle)*dashRange*(DASH_TIME - dashTimer)));
				this.moveCombatY((float) (Math.sin(dashAngle)*dashRange*(DASH_TIME - dashTimer)));
				dashTimer = DASH_TIME;
			}
			batch.draw((TextureRegion) dash_img.getKeyFrame(dashTimer), this.getCombatPos().x, this.getCombatPos().y, this.getWidth(), this.getHeight());
		}
	}

	public float dist_ent(Entity entity) {
		return (float) (Math.sqrt(Math.pow(this.combat_pos.x - entity.getCombatPos().x, 2) + Math.pow(this.combat_pos.y - entity.getCombatPos().y, 2)));
	}
	
	private float calcAng(Vector2 caster, Vector2 target) {
		float ang;
		if (target.y > caster.y) {
			if (target.x > caster.x)
				ang = (float) (Math.atan((target.y - caster.y) / (target.x - caster.x)));
			else if (target.x < caster.x)
				ang = (float) (Math.PI + Math.atan((target.y - caster.y) / (target.x - caster.x)));
			else
				return (float) (Math.PI/2);
		}
		else {
			if (target.x > caster.x)
				ang = (float) (Math.atan((target.y - caster.y) / (target.x - caster.x)));
			else if (target.x < caster.x)
				ang = (float) (Math.atan((target.y - caster.y) / (target.x - caster.x)) - Math.PI);
			else
				return (float) (-Math.PI/2);
		}
		return (float) (ang);
	}
	
	public Vector2 getCombatCenter() {
		return new Vector2(this.combat_pos.x + this.ENT_WIDTH/2, this.combat_pos.y + this.ENT_HEIGHT/2);
	}
	
	public void ressurect() { 
		this.alive = true;
		this.addHP(this.maxHP);
	}

	public void render_adds(SpriteBatch batch, float delta) {
		if (ent_img[ent_anim_count] != null && this.isAlive())
			batch.draw((TextureRegion) ent_img[ent_anim_count].getKeyFrame(stateTime, true),  combat_pos.x, combat_pos.y, ENT_WIDTH, ENT_HEIGHT);
		stateTime += delta;
		
	}
	
}
