package items;

public class BasChampi extends Item {
	
	private static String srcIcon = "items/equipments/bas_champi.png";
	
	public BasChampi() {
		super(ItemType.BAS_CHAMPI, srcIcon);
		setDescription("Forg� dans une cave des Kourinoux, ce bas vous pr�munira de quelques blessures.");
		setAtkBonus(5);
		setDefBonus(8);
		setPano("champi");
	}

}
