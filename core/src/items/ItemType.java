package items;

public enum ItemType {
	
	GREC("Grec", 5, true, true, 1), // (id, sellPrice, useInstant, consumable, rarity)
	CANET("Canet", 1, true, true, 1),
	CLOPES("Clopes", 8, false, true, 1);
	
	private String id;
	private int sellPrice, rarity;
	

	private boolean useInstant, consumable;
	
	private ItemType(String id, int sellPrice, boolean useInstant, boolean consumable, int rarity) {
		this.id = id;
		this.sellPrice = sellPrice;
		this.useInstant = useInstant;
		this.consumable = consumable;
		this.rarity = rarity;
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
