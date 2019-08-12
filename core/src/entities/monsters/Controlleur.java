package entities.monsters;

import world.GameMap;

public class Controlleur extends Monster {
	
	private static String src_img = "characters/controlleur.png";
	private float stateTime;

	public Controlleur(float x, float y, GameMap map) {
		super(x, y, map, false, true, src_img);
		stateTime = 0f;
	}

	@Override
	public void movementOutOfCombat(float delta) {
		stateTime += delta;
		if(stateTime < 1f) {
			this.moveX(delta*100); // doesn't change the animation
		}
		else if(stateTime > 2f && stateTime < 3f) {
			this.moveX(-delta*100);
		}
		else if (stateTime >= 3f)
			stateTime = 0f;
	}
	
	

}
