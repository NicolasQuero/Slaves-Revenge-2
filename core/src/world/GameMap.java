package world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import buildings.Building;
import buildings.Comico;
import buildings.LeCrasseux;
import combats.Combat;
import combats.CombatTD;
import decors.Voiture;
import entities.BearNice;
import entities.BossWhite;
import entities.Entity;
import entities.GirlRedhead;
import entities.HitlerPoivrot;
import entities.MalikLePretentieux;
import entities.Player;
import entities.monsters.Controlleur;
import entities.monsters.MonstreVert1;
import items.BasChampi;
import items.CasquetteMoche;
import items.Clopes;
import items.Grec;
import items.HautChampi;
import items.SchlassCoursier;
import menus.ChooseCharac;
import menus.Inventory;
import menus.Toolbar;
import menus.UpgradeEnts;
import saves.SaveData;
import utilities.GameAssetManager;

public abstract class GameMap {
	
	private CombatTD combat;
	private CombatMapTD combatMap;
	
	private Toolbar toolbar;
	protected ChooseCharac choose_charac;
	
	private ArrayList<Entity> entities;
	protected Entity[] played_entities;
	private ArrayList<Entity> pnj_entities; // NPCs of the game
	private Entity[] ennemy;
	
	private ArrayList<Voiture> voitures;

	public static boolean choose_charac_open;

	BitmapFont font = new BitmapFont();

	private boolean in_combat;
	
	float time, regen_time;
	int ips;
	public static boolean upgrade_ents_open, inventory_open;
	private UpgradeEnts upgradeEnts;
	private Inventory inventory;
	
	private Stage stage;
	public GameAssetManager assMan;
	
	public static float GAME_TIME;
	public static String language;
	
	private ArrayList<Building> buildings;
	
	public GameMap () {
		language = "fr";
		GAME_TIME = 0f;
		time = 0f; regen_time = 0f;
		ips = 0;
		entities = new ArrayList<Entity>();
		pnj_entities = new ArrayList<Entity>();
		ennemy = new Entity[1];
		
		played_entities = new Entity[3];
		played_entities[0] = new Player(150, 150, this, true, false);
		played_entities[0].setWeapon(new SchlassCoursier());
		played_entities[0].setTopEquipment(new HautChampi());
		played_entities[0].setBotEquipment(new BasChampi());
		entities.add(played_entities[0]);
		entities.add(new MalikLePretentieux(450, 700, this, true, false));
		entities.add(new HitlerPoivrot(500, 700, this, true, false));
		entities.add(new GirlRedhead(200, 150, this, false, false));
		entities.add(new BossWhite(250, 150, this, false, false));
		entities.add(new BearNice(300,150,this,false, false));
		pnj_entities.add(new BossWhite(400, 650, this, false, true));
		pnj_entities.add(new HitlerPoivrot(500, 700, this, false, true));
		//pnj_entities.add(new MonstreVert1(800, 700, this));
		//pnj_entities.add(new Controlleur(200, 450, this));
		
		played_entities[1] = entities.get(1);
		played_entities[2] = entities.get(2);
		
		voitures = new ArrayList<Voiture>();
		voitures.add(new Voiture(500, 600));
		voitures.add(new Voiture(300,300));
		
		choose_charac = new ChooseCharac(entities);
		
		toolbar = new Toolbar(played_entities);
		
		this.in_combat = false;
		this.stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Entity.setStage(stage);
		inventory = new Inventory(stage, played_entities[0]);
		addFirstItems(inventory);
		
		buildings = new ArrayList<Building>();
		buildings.add(new LeCrasseux(stage, played_entities[0]));
		buildings.add(new Comico(stage, played_entities[0]));
		
		setListeners(stage);
		
		//assMan = new GameAssetManager();
		//assMan.queueAddImages();
		//assMan.manager.finishLoading();
	}
	
	
	public void render (OrthographicCamera camera, SpriteBatch batch, float delta) {
		changeTime(delta);
		show_ips(delta);
		if (!in_combat) {
			if (Gdx.input.isKeyJustPressed(Keys.K)) {
				entities.add(new BearNice(1000, 1000, this, false, true));
			}
			if (Gdx.input.isKeyJustPressed(Keys.M))
				played_entities[0].lvlUp();
			if (Gdx.input.isKeyJustPressed(Keys.F1)) {
				for (Entity entity : played_entities) {
					if (entity != null) {
						entity.addHP(200); entity.addMP(200); entity.ressurect();
					}
				}
			}
			toolbar.render(camera, batch, delta); // render bottom toolbar
			
			Collections.sort(entities, new Comparator<Entity>() { // 1 We rearrange here our arraylist entities
				@Override // 2 so that we render the entities that should be in the front at the end (they appear then in the foreground).
				public int compare(Entity ent0, Entity ent1) {
					if (ent0.getY() > ent1.getY()) // If ent0's Y position is higher it must be rendered before
						return -1;
					else
						return 1;
				}
			});
	
			for (Entity entity : entities) { // we render all entities after being sorted
				if (!entity.isPlayed()) {
					if (entity.isNpc()) {
						entity.render_unplayed(batch, delta);
						if (entity.getInteraction_rectangle().contains(played_entities[0].getCenter()) && entity.isAlive()) { // if the player is close to an npc
							font.draw(batch, "E", entity.getX() + 64, entity.getY() + 36);
							if ((Gdx.input.isKeyJustPressed(Keys.E) || entity.isAggro()) && isOneAlive(played_entities)) {
								combatMap = new CombatMapTD("cite");
								combat = new CombatTD(camera, combatMap, played_entities, entity);
								ennemy[0] = entity;
								combatMap.setCombat(combat);
								in_combat = true;
								break;
							}
						}
					}
				}
				else if (entity == played_entities[0]) {
					entity.render(batch, delta);
				}
				else {
					if (entity == played_entities[1] && entity.isAlive())
						entity.follow(batch, delta, played_entities[0]);
					if (entity == played_entities[2] && entity.isAlive())
						entity.follow(batch, delta, played_entities[1]);
				}
			}
			for (Entity entity : pnj_entities) { // render npcs
				if (entity != null && entity.isAlive()) {
						entity.render(batch, delta);
					if (entity.getInteraction_rectangle().contains(played_entities[0].getCenter())) { // if the player is close to an npc
						font.draw(batch, "E", entity.getX() + 64, entity.getY() + 36);
						/*if ((Gdx.input.isKeyJustPressed(Keys.E) || entity.isAggro()) && isOneAlive(played_entities)) {
							combatMap = new CombatMapTD("cite");
							combat = new CombatTD(camera, combatMap, played_entities, entity);
							ennemy[0] = entity;
							combatMap.setCombat(combat);
							in_combat = true;
							break;
						}*/
					}
				}
			}
			
			for (Voiture voiture : voitures) {
				voiture.render(batch, camera, delta);
				voiture.moveX(200*delta);
			}
			
			if (Gdx.input.isKeyJustPressed(Keys.P)) // opens choose characters interface when you press I
				choose_charac_open = false;
			if (Gdx.input.isKeyJustPressed(Keys.O) && !upgrade_ents_open && !inventory_open) {
				upgrade_ents_open = true;
				upgradeEnts = new UpgradeEnts(entities, played_entities);
			}
			if (Gdx.input.isKeyJustPressed(Keys.I) && !upgrade_ents_open && !inventory_open) {
				inventory_open = true;
				inventory.open(stage);
			}
			if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && choose_charac_open)
				choose_charac_open = false;
			if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && upgrade_ents_open) {
				upgrade_ents_open = false;
				upgradeEnts.dispose();
				Gdx.input.setInputProcessor(stage);
			}
			if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && inventory_open) {
				inventory_open = false;
				inventory.close();
			}
			
			if (Gdx.input.isKeyJustPressed(Keys.F10)) { // SAVE
				SaveData.save(entities, pnj_entities, played_entities);
				System.out.println("Saved !");
			}
			if (Gdx.input.isKeyJustPressed(Keys.F11)) { // LOAD
				entities = new ArrayList<Entity>();
				pnj_entities = new ArrayList<Entity>();
				for (Entity ent : SaveData.load(played_entities)) {
					if (ent != null)
						entities.add(ent);
				}
				for (Entity ent : SaveData.load_pnj()) {
					if (ent != null)
						pnj_entities.add(ent);
				}
				Entity.setMap(this);
				choose_charac = new ChooseCharac(entities);
				toolbar = new Toolbar(played_entities);
				played_entities = new Entity[3];
				int rk = 0;
				for (Entity entity : entities) {
					if (entity.isPlayed() && rk < 3) {
						played_entities[rk] = entity;
						rk++;
					}
				}
				System.out.println("Loaded !");
			}
			stage.act();
			stage.draw();
			if (choose_charac_open && !upgrade_ents_open) {
				choose_charac.render(camera, batch, delta, played_entities);
			}
			else if (upgrade_ents_open)
				upgradeEnts.render(camera, batch, delta);
			
			moveCamera(camera, this, played_entities[0]);
			regen_ents(played_entities, delta);
		}
		else { // In combat
			if (combatMap != null) {
				combatMap.render(camera, batch, delta);
			}
			if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE) || !ennemy[0].isAlive() || !isOneAlive(played_entities)) {
				Entity.setStage(stage);
				combatMap.dispose();
				combat.dispose();
				this.in_combat = false;
				this.combatMap = null;
				this.combat = null;
				choose_charac = new ChooseCharac(entities);
				toolbar = new Toolbar(played_entities);
				Gdx.input.setInputProcessor(stage);
			}
		}

		
	}
	
	public void dispose () {
		entities.clear();
		stage.dispose();
	}
	
	// Returns true if it collides with the map, the objects or the unplayed entities, false otherwise
	
	public boolean doesRectCollideWithMap (Entity ent, float amountX, float amountY) {
		
		float x = ent.getRekt().getX() + amountX;
		float y = ent.getRekt().getY() + amountY;
		float width = ent.getRekt().getWidth();
		float height = ent.getRekt().getHeight();
		Rectangle coll_rect = new Rectangle(x, y, width, height);
		coll_rect.setPosition(x, y); // We create a new rectangle at the position where the entity wants to go 
		
		if(x<0 || y<100 || x + width > this.getPixelWidth() || y + height > this.getPixelHeight()) { // We check whether it collides with the border or not
			System.out.println(x + " " + width + " " + y + " " + height);
			return true;
		}

		for (int row = (int) (y / TileType.TILE_SIZE_PIXEL); row < Math.ceil((y + height) / TileType.TILE_SIZE_PIXEL); row++ ) { // 1 We check if the entity can go on the potential TileTypes
			for (int col = (int) (x / TileType.TILE_SIZE_PIXEL); col < Math.ceil((x + height) / TileType.TILE_SIZE_PIXEL); col++ ) { // 2 surrounding the target position
				for (int layer = 0; layer < getLayers(); layer++ ) { // Check all layers of the TiledMaps
					TileType type = null;
					//System.out.println(layer);
					int objectLayerId = 3; // ALL THE OBJECTS MUST BE IN THE LAYER objectLayerId !
					
					if (layer != objectLayerId) { // The layer objectLayerId contains the objects, you mustn't use getTile on it !
						type = getTileTypeByCoordinates(layer, col, row); // gives the tileType at the target position
					}
					
					else if (this.getObjectCollisionWithEntity(objectLayerId, coll_rect, pnj_entities)) { // Object layer
						return true; // collision happened with an object in the object layer
					}
					if (type != null && type.isCollidable()) {
						//System.out.println(type.getName());
						return true; // checks that the TileType found in the if above isn't null and returns true if the entity collides with the type
					}
					
				}
			}
		}

		return false;
	}

	
	protected abstract boolean getObjectCollisionWithEntity(int objectLayerId, Rectangle rect, ArrayList<Entity> entities);

	public TileType getTileTypeByLocation(int layer, float x, float y) {
		return this.getTileTypeByCoordinates(layer, (int) (x / TileType.TILE_SIZE), (int) (y / TileType.TILE_SIZE));
	}
	public abstract TileType getTileTypeByCoordinates(int layer, int col, int row);
	
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract int getLayers();
	
	public int getPixelWidth() {
		return this.getWidth() * TileType.TILE_SIZE_PIXEL;
	}
	
	public int getPixelHeight() {
		return this.getHeight() * TileType.TILE_SIZE_PIXEL;
	}

	public void setIn_combat(boolean a) {
		this.in_combat = a;
	}
	
	public boolean isInCombat() {
		return in_combat;
	}
	
	public boolean isOneAlive (Entity[] ents) {
		for (Entity entity : ents) {
			if (entity != null && entity.getHP() > 0)
				return true;
		}
		return false;
	}
	
	public void moveCamera(OrthographicCamera camera, GameMap map, Entity entity_played) { // the camera follows the player
		if (entity_played.getCenter().x - (camera.position.x - camera.viewportWidth/2) - 150 < 0) // move left
			camera.translate(entity_played.getCenter().x - (camera.position.x - camera.viewportWidth/2) - 150, 0);
		if (entity_played.getCenter().x - (camera.position.x + camera.viewportWidth/2) + 150 > 0) // move right
			camera.translate(entity_played.getCenter().x - (camera.position.x + camera.viewportWidth/2) + 150, 0);
		if (entity_played.getCenter().y - (camera.position.y - camera.viewportHeight/2) - 150 < 0) // move down
			camera.translate(0, entity_played.getCenter().y - (camera.position.y - camera.viewportHeight/2) - 150);
		if (entity_played.getCenter().y - (camera.position.y + camera.viewportHeight/2) + 150 > 0) // move up
			camera.translate(0, entity_played.getCenter().y - (camera.position.y + camera.viewportHeight/2) + 150);
		
		if (camera.position.x < camera.viewportWidth/2)
			camera.position.x = 640;
		if (camera.position.x > map.getWidth() * TileType.TILE_SIZE_PIXEL - camera.viewportWidth/2)
			camera.position.x = map.getWidth() * TileType.TILE_SIZE_PIXEL - camera.viewportWidth/2;
		if (camera.position.y < camera.viewportHeight/2)
			camera.position.y = 400;
		if (camera.position.y > map.getHeight() * TileType.TILE_SIZE_PIXEL - camera.viewportHeight/2)
			camera.position.y = map.getHeight() * TileType.TILE_SIZE_PIXEL - camera.viewportHeight/2;
		camera.update();
	}
	
	public void show_ips(float delta) { // show ips
		time += delta;
		ips += 1;
		if (time >= 1f) {
			//System.out.println(stage.getWidth() + " " + stage.getHeight());
			//System.out.println(ips + " / " + time);
			ips = 0;
			time = 0f;
		}
	}
	
	public void regen_ents(Entity[] ents, float delta) { // regens played entities' hp/mp
		regen_time += delta;
		if (regen_time > 1f) {
			for (Entity entity : ents) {
				if (entity != null && entity.isAlive()) {
					entity.addHP(1);
					entity.addMP(entity.getManaRegen());
				}
			}
			regen_time = 0f;
		}
	}
	
	public void addFirstItems(Inventory inventory) {
		inventory.addItem(new Clopes());
		for (int i = 0; i < 15; i++) {
			inventory.addItem(new Grec());
		}
		inventory.addItem(new SchlassCoursier());
	}
	
	public void setListeners(Stage stage) {
		stage.addListener(new InputListener() {
				@Override
				public boolean keyDown(InputEvent event, int keycode) {
					switch (keycode) {
						case Keys.E:
							for (Building building : buildings) {
								if (building.getHitbox().contains(played_entities[0].getCenter())) {
									building.enterBuilding();
									break;
								}
							}
							for (Entity ent : pnj_entities) {
								if (ent.getInteraction_rectangle().contains(played_entities[0].getCenter())) {
									combatMap = new CombatMapTD("cite");
									combat = new CombatTD((OrthographicCamera) stage.getCamera(), combatMap, played_entities, ent);
									ennemy[0] = ent;
									combatMap.setCombat(combat);
									in_combat = true;
									break;
								}
							}
					}
					return false;
				}
		});
	}
	
	public void changeTime(float delta) {
		GAME_TIME += delta/10;
		if(GAME_TIME > 24)
			GAME_TIME = 0f;
		GAME_TIME = 12f;
	}
	
}
