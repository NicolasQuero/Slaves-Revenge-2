package items;

public class HautChampi extends Item {
	
	private static String srcIcon = "items/equipments/haut_champi.png";
	
	public HautChampi() {
		super(ItemType.HAUT_CHAMPI, srcIcon);
		setDescription("Forg� dans une cave des Kourinoux, ce haut vous pr�munira de quelques blessures.");
		setAtkBonus(7);
		setDefBonus(6);
		setPano("champi");
	}

}
