package items;

public class CasquetteMoche extends Item {
	
	private static String srcIcon = "items/equipments/casquette_moche2.png";
	
	public CasquetteMoche() {
		super(ItemType.CASQUETTE_MOCHE, srcIcon);
		setDescription("Casquette laide portée par de nombreux habitants.");
		setAtkBonus(2);
		setDefBonus(2);
	}

}
