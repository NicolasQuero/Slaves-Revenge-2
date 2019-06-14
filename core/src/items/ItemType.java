package items;

public enum ItemType {
	
	GREC("Grec", 5, true, true, 1, 0), // (id, sellPrice, useInstant, consumable, rarity, category)
	CANET("Canet", 1, true, true, 1, 0),
	CLOPES("Clopes", 8, false, true, 1, 0);
	
	private String id;
	private int sellPrice, rarity;
	private int category; // 1 for equipments, 0 for the rest
	

	private boolean useInstant, consumable;
	
	private ItemType(String id, int sellPrice, boolean useInstant, boolean consumable, int rarity, int category) {
		this.id = id;
		this.sellPrice = sellPrice;
		this.useInstant = useInstant;
		this.consumable = consumable;
		this.rarity = rarity;
		this.category = category;
	}

	public boolean isUseInstant() {
		return useInstant;
	}

	public boolean isConsumable() {
		return consumable;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getId() {
		return id;
	}
	
	public int getRarity() {
		return rarity;
	}

}
