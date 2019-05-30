package entities;

public enum EntityType {
	PLAYER("Dayuki", 64, 64),
	BOSSWHITE("Artan", 64, 64),
	GIRLREDHEAD("Edena", 64, 64),
	BEARNICE("Ours", 64, 64), 
	MALIKLEPRETENTIEUX("Malik", 64, 64),
	HITLERPOIVROT("Hitler", 64, 64), 
	MONSTER("Monster", 32, 32);
	
	private String id;
	private int width;
	private int height;
	
	private EntityType(String id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public String getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	
	
}
