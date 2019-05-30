package world;

import java.util.HashMap;

public enum TileType {

	WATER(1, true, "Water"),
	GRASS(10, false, "Grass"),
	GRASS_WATER(3, false, "GrassWater"),
	WALL(4, true, "Wall"),
	HOUSE_YELLOW_ROOF(98, false, "HouseYellowRoof"),
	HOUSE_YELLOW(127, false, "HouseYellow"),
	HOUSE_RED(191, false, "HouseRed"),
	TREE1(231, false, "Tree1");
	
	public static final int TILE_SIZE_PIXEL = 64;
	public static final int TILE_SIZE = 64;
	
	private int id;
	private boolean collidable;
	private String name;
	private float damage;
	
	private TileType (int id, boolean collidable, String name) {
		this(id, collidable, name, 0);
	}
	
	private TileType (int id, boolean collidable, String name, float damage) {
		this.id = id;
		this.collidable = collidable;
		this.name = name;
		this.damage = damage;
	}
	
	public int getId() {
		return id;
	}

	public boolean isCollidable() {
		return collidable;
	}

	public String getName() {
		return name;
	}

	public float getDamage() {
		return damage;
	}
	
	private static HashMap<Integer, TileType> tileMap;
	
	static {
		tileMap = new HashMap <Integer, TileType>();
		for(TileType tileType : TileType.values()) {
			tileMap.put(tileType.getId(), tileType);
		}
	}
	
	public static TileType getTileTypeById (int id) {
		return tileMap.get(id);
	}
}
