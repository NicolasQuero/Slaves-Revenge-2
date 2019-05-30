package dialogs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class DialogBox extends Actor {

	BitmapFont font = new BitmapFont();
	private Label.LabelStyle baseStyle = new Label.LabelStyle(font, Color.WHITE);
	private static NinePatch background = new NinePatch(new Texture("dialogbox_bg.png"), 1, 1, 1, 1);
	private Label dialogLabel;
	private float padding = 16;

	public DialogBox(float x, float y, Stage stage) {
		super();
		setPosition(x, y);
		stage.addActor(this);
		dialogLabel = new Label(" ", baseStyle);        
		dialogLabel.setWrap(true);        
		dialogLabel.setAlignment(Align.topLeft);        
		dialogLabel.setPosition( padding, padding );        
		this.setDialogSize( getWidth(), getHeight() );        
		stage.addActor(dialogLabel);    
	}
    public void setDialogSize(float width, float height) {        
    	this.setSize(width, height);        
    	dialogLabel.setWidth( width - 2 * padding );        
    	dialogLabel.setHeight( height - 2 * padding );    
    }
    public void setText(String text) {  
    	dialogLabel.setText(text);  
    }
    public void setFontScale(float scale)    {  
    	dialogLabel.setFontScale(scale);  
    }
    public void setFontColor(Color color)    {  
    	dialogLabel.setColor(color);  
    }
    public void setBackgroundColor(Color color)    {  
    	this.setColor(color);  
    }
    public void alignTopLeft()    {  
    	dialogLabel.setAlignment( Align.topLeft );  
    }
    public void alignCenter()    {  
    	dialogLabel.setAlignment( Align.center );  
    } 
}
