package items;

public enum ItemType {
	
	GREC("Grec", 5, true, true, 1, false), // (id, sellPrice, useInstant, consumable, rarity, isEquipment)
	CANET("Canet", 1, true, true, 1, false),
	CLOPES("Clopes", 8, false, true, 1, false),
	SCHLASS_COURSIER("Schlass du Coursier", 10, false, false, 2, true),
	CASQUETTE_MOCHE("Casquette Laide", 5, false, false, 2, true),
	HAUT_CHAMPI("Haut du Champi", 7, false, false, 3, true),
	BAS_CHAMPI("Bas du Champi", 6, false, false, 3, true);
	
	private String id;
	private int sellPrice, rarity;
	private boolean isEquipment; // 1 for equipments, 0 for the rest
	

	private boolean useInstant, consumable;
	
	private ItemType(String id, int sellPrice, boolean useInstant, boolean consumable, int rarity, boolean isEquipment) {
		this.id = id;
		this.sellPrice = sellPrice;
		this.useInstant = useInstant;
		this.consumable = consumable;
		this.rarity = rarity;
		this.isEquipment = isEquipment;
		
	}
	
	public boolean isEquipment() {
		return this.isEquipment;
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
