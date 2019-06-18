package items;

public class Clopes extends Item {

	private static String srcIcon = "items/kilmo.png";
	private int nbClopes;
	
	public Clopes() {
		super(ItemType.CLOPES, srcIcon);
		this.nbClopes = 0;
		this.setDescription("Les cigarettes sont une monnaie d'�change dans la cit� et vous permettront d'�viter les joutes de rue ind�sirables.");
	}
	
	public void addClopes(int nb) {
		nbClopes += nb;
		if (nbClopes < 0)
			nbClopes = 0;
	}

}
