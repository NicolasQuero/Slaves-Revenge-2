package dialogs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Sign extends Actor {
	
	private String text;
	private boolean viewing;
	
	public Sign (float x, float y, Stage stage) {
		super();
		super.setPosition(x, y);
		stage.addActor(this);
		this.text = " ";
		this.viewing = false;
	}
	
	public void setText(String t) {
		this.text = t;
	}
	
	public String getText() {
		return text;
	}
	
	public void setViewing(boolean a) {
		viewing = a;
	}
	
	public boolean getViewing() {
		return viewing;
	}

}
