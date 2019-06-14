package utilities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class BaseActor extends Actor {
	
	private Rectangle boundaryRectangle;

	public BaseActor(float x, float y, Stage stage) {
		super();
		this.setPosition(x, y);
		this.boundaryRectangle = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		stage.addActor(this);
	}
	
	public boolean isWithinDistance(float dist, BaseActor A) {
		Rectangle r1 = this.getBoundaryRectangle();
		r1.set(this.getX() - dist, this.getY() - dist, this.getWidth() + 2 * dist, this.getHeight() + 2 * dist);
		
		if(r1.overlaps(A.getBoundaryRectangle()))
			return true;
		else
			return false;
	}
	
	public Rectangle getBoundaryRectangle() {
		boundaryRectangle.setPosition(this.getX(), this.getY());
		boundaryRectangle.setWidth(this.getWidth());
		boundaryRectangle.setHeight(this.getHeight());
		return boundaryRectangle;
	}
}
