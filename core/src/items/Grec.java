package items;

public class Grec extends Item {
	
	private static String srcIcon = "items/grec_icon.png";
	private String chef;
	
	public Grec() {
		super(ItemType.GREC, srcIcon);
		this.chef = "Kawtar le Parangon";
		this.setDescription("Sandwich savoureux tr�s populaire agr�ment� d'une viande d'appellation origine non controll�e et de nombreux l�gumes personnalisables.");
	}
}
