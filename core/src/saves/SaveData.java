package saves;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import entities.BearNice;
import entities.BossWhite;
import entities.Entity;
import entities.GirlRedhead;
import entities.HitlerPoivrot;
import entities.MalikLePretentieux;
import entities.Player;
import entities.monsters.Lambda1;
import entities.monsters.Monster;
import world.GameMap;

public class SaveData {

	private static String fileName = "bin/entities_data.json";
	private static String fileName2 = "bin/ennemies_data.json";
	private static FileHandle file = Gdx.files.local(fileName);
	private static FileHandle file2 = Gdx.files.local(fileName2);
	private static GameMap map;
	
	private static class EntityDescriptor {
		
		private String id;
		private int ATK;
		private int DEF;
		private float maxHP, HP;
		private int maxMP, MP;
		private boolean alive, is_played, is_npc;
		private int xp;
		private int played_rank;
		private Vector2 pos;
		
		public EntityDescriptor() {
			this.id = "none";
			this.ATK = -1;
			this.DEF = -1;
			this.maxHP = -1; this.HP = -1;
			this.maxMP = -1; this.MP = -1;
			this.xp = -1;
			this.pos = new Vector2(-1, -1);
			this.alive = false;
			this.is_played = false;
			this.is_npc = false;
			this.played_rank = -1;
		}
		
		public EntityDescriptor(Entity entity) {
			this.id = entity.getType().getId();
			this.ATK = entity.getATK();
			this.DEF = entity.getDEF();
			this.maxHP = entity.getMaxHP(); this.HP = entity.getHP();
			this.maxMP = entity.getMaxMP(); this.MP = entity.getMP();
			this.pos = new Vector2(entity.getX(), entity.getY());
			this.alive = entity.isAlive();
			this.is_played = entity.isPlayed();
			this.is_npc = entity.isNpc();
			this.xp = entity.getXp();
		}
		
		public Entity createEntity(Entity[] played_entities) {
			Entity ent_loaded = null;
			System.out.println(id);
			if (id.equals("Edena"))  {
				ent_loaded = new GirlRedhead(this.pos.x, this.pos.y, map, this.is_played, this.is_npc);
			}
			else if (id.equals("Dayuki")) {
				ent_loaded= new Player(this.pos.x, this.pos.y, map, this.is_played, this.is_npc);
			}
			else if (id.equals("Ours")) {
				ent_loaded = new BearNice(this.pos.x, this.pos.y, map, this.is_played, this.is_npc);
			}
			else if (id.equals("Artan")) {
				ent_loaded = new BossWhite(this.pos.x, this.pos.y, map, this.is_played, this.is_npc);
			}
			else if (id.equals("Malik")) {
				ent_loaded = new MalikLePretentieux(this.pos.x, this.pos.y, map, this.is_played, this.is_npc);
			}
			else if (id.equals("Hitler")) {
				ent_loaded = new HitlerPoivrot(this.pos.x, this.pos.y, map, this.is_played, this.is_npc);
			}
			else if (id.equals("Monster")) {
				ent_loaded = new Lambda1(this.pos.x, this.pos.y, map);
			}
			
			if (ent_loaded != null) {
				ent_loaded.setAlive(this.alive);
				ent_loaded.setATK(this.ATK);
				ent_loaded.setDEF(this.DEF);
				ent_loaded.setHP(this.HP);
				ent_loaded.setMP(this.MP);
				ent_loaded.setMaxHP(this.maxHP);
				ent_loaded.setMaxMP(this.maxMP);
				ent_loaded.setAlive(this.alive);
				ent_loaded.addXp(this.xp);
				ent_loaded.setPos(this.pos);
				if (played_entities != null && this.played_rank != -1) {
					played_entities[played_rank] = ent_loaded;
				}
			}
			
			return ent_loaded;
		}
		
		private void setRank(int i) {
			this.played_rank = i;
		}
		
	}

	
	private EntityDescriptor desc;

	public EntityDescriptor newEntityDescriptor(Entity entity) {
		desc.id = entity.getType().getId();
		desc.ATK = entity.getATK();
		desc.DEF = entity.getDEF();
		desc.maxHP = entity.getMaxHP(); desc.HP = entity.getHP();
		desc.maxMP = entity.getMaxMP(); desc.MP = entity.getMP();
		desc.pos = new Vector2(entity.getX(), entity.getY());
		desc.alive = entity.isAlive();
		desc.is_played = entity.isPlayed();
		desc.is_npc = entity.isNpc();
		
		return desc;
	}
	// fixer la save de played_entities
	public static void save(ArrayList<Entity> entities, ArrayList<Entity> pnj_entities, Entity[] played_entities) {
		Json json = new Json();
		json.setOutputType(OutputType.minimal);
		ArrayList<SaveData.EntityDescriptor> entDescs = new ArrayList<SaveData.EntityDescriptor>();
		for (Entity entity : entities) {
			SaveData.EntityDescriptor entDesc = new EntityDescriptor(entity);
			for (int i = 0; i<3; i++) {
				if (played_entities[i] != null && played_entities[i] == entity)
					entDesc.setRank(i);
			}
			entDescs.add(entDesc);
		}
		file.writeString(json.toJson(entDescs), false);
		
		ArrayList<SaveData.EntityDescriptor> ennemyDescs = new ArrayList<SaveData.EntityDescriptor>();
		for (Entity entity : pnj_entities) {
			SaveData.EntityDescriptor ennemyDesc = new EntityDescriptor(entity);
			for (int i = 0; i<3; i++) {
				if (played_entities[i] != null && played_entities[i] == entity)
					ennemyDesc.setRank(i);
			}
			ennemyDescs.add(ennemyDesc);
		}
		file2.writeString(json.toJson(ennemyDescs), false);
	}
	
	public static ArrayList<Entity> load_pnj() {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		ArrayList<SaveData.EntityDescriptor> ent_descs = new ArrayList<SaveData.EntityDescriptor>();
		Json json = new Json();
		Entity ent;
		ArrayList<EntityDescriptor> list = json.fromJson(ArrayList.class,
                Gdx.files.internal(fileName2).readString());
		for (EntityDescriptor v : list) {
			ent_descs.add(v);
		}
		for (SaveData.EntityDescriptor ent_desc : ent_descs) {
			ent = ent_desc.createEntity(null);
			if (ent != null) {
				entities.add(ent);
			}
			ent = null;
		}
		return entities;
	}
	
	public static ArrayList<Entity> load(Entity[] played_entities) {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		ArrayList<SaveData.EntityDescriptor> ent_descs = new ArrayList<SaveData.EntityDescriptor>();
		Json json = new Json();
		Entity ent;
		ArrayList<EntityDescriptor> list = json.fromJson(ArrayList.class,
                Gdx.files.internal(fileName).readString());
		for (EntityDescriptor v : list) {
			ent_descs.add(v);
		}
		for (SaveData.EntityDescriptor ent_desc : ent_descs) {
			ent = ent_desc.createEntity(played_entities);
			if (ent != null) {
				entities.add(ent);
			}
			ent = null;
		}
		return entities;
	}
	
}
