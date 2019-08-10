package entities.monsters;

import world.GameMap;

public class Lambda1 extends Monster {

	private static String src_img = "characters/adds/lambda_1.png";
	
	public Lambda1(float x, float y, GameMap map) {
		super(x, y, map, false, true, src_img);
		this.setXpGiven(25);
	}

	@Override
	public void movementOutOfCombat(float delta) {
		// TODO Auto-generated method stub
		
	}

}
