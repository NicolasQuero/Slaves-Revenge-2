package buildings;

import com.badlogic.gdx.scenes.scene2d.Stage;

import entities.Entity;

public class LeCrasseux extends Building {

	// GREC 384, 576 161x166
	private static int x = 399;
	private static int y = 514;
	private static int width = 161;
	private static int height = 166;
	private static String id = "Le Crasseux";

	public LeCrasseux(Stage stage, Entity player) {
		super(x, y, width, height, id, stage, player);
	}

	@Override
	public void enterBuilding() {
		System.out.println("Bienvenue chez le Crasseux.");
		// TODO Auto-generated method stub
		
	}

}
