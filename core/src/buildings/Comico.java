package buildings;

import com.badlogic.gdx.scenes.scene2d.Stage;

import entities.Entity;

public class Comico extends Building {
	
	private static int x = 773;
	private static int y = 255;
	private static int WIDTH = 191;
	private static int HEIGHT = 185;
	private static String id = "Comico";

	public Comico(Stage stage, Entity player) {
		super(x, y, WIDTH, HEIGHT, id, stage, player);
	}

	@Override
	public void enterBuilding() {
		System.out.println("Bienvenue au Comico.");
		// TODO Auto-generated method stub
		
	}

}
