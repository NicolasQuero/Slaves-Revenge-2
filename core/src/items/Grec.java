package items;

public class Grec extends Item {
	
	private static String srcIcon = "items/grec_icon.png";
	private String chef;
	
	public Grec() {
		super(ItemType.GREC, srcIcon);
		this.chef = "Kawtar le Parangon";
		this.setDescription("Sandwich savoureux très populaire agrémenté d'une viande d'appellation origine non controllée et de nombreux légumes personnalisables.");
	}
}
