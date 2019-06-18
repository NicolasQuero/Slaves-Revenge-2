package items;

public class SchlassCoursier extends Item{
	
	private static String srcIcon = "items/equipments/schlass_du_coursier.png";
	
	public SchlassCoursier() {
		super(ItemType.SCHLASS_COURSIER, srcIcon);
		setDescription("Ce couteau a été perdu par un coursier HubertEats maladroit.");
		setAtkBonus(5);
		setDefBonus(3);
	}

}
