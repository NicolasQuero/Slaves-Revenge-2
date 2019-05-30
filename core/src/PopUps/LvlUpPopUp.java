package PopUps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import entities.Entity;
import utilities.AnimationTable;

public class LvlUpPopUp extends Actor {
	
	private Texture bg = new Texture("popups/lvl_up.png");
	private Entity ent;
	
	private float ATK_ICON_SPEED = 0.2f;
	private static TextureRegion[][] atk_icon_spritesheet = TextureRegion.split(new Texture("icons/atk_icon.png"), 16, 16);
	private Animation<?> atk_icon = new Animation<TextureRegion>(ATK_ICON_SPEED, atk_icon_spritesheet[0]);
	private float DEF_ICON_SPEED = 0.125f;
	private static TextureRegion[][] def_icon_spritesheet = TextureRegion.split(new Texture("icons/def_icon.png"), 32, 32);
	private Animation<?> def_icon = new Animation<TextureRegion>(DEF_ICON_SPEED, def_icon_spritesheet[0]);
	private AnimationTable atk_icon_t = new AnimationTable(atk_icon);
	private AnimationTable def_icon_t = new AnimationTable(def_icon);
	
	BitmapFont font = new BitmapFont();
	BitmapFont fontGreen = new BitmapFont();
	BitmapFont fontBlue = new BitmapFont();
	
	private int LVL_UP_WIDTH = 500;
	private int LVL_UP_HEIGHT = 150;
	
	private float TIME_POP_UP = 3.0f;
	private float stateTime;
	
	private TextureAtlas text_atlas;
	private TextButton closeButton;
	private int CLOSE_BUTTON_WIDTH = 16;
	private int CLOSE_BUTTON_HEIGHT = 16;
	
	private Label.LabelStyle baseStyle = new Label.LabelStyle(font, Color.WHITE);
	private Label entLvl = new Label("", baseStyle);
	private Label entAtk = new Label("", baseStyle);
	private Label entDef = new Label("", baseStyle);
	private Label entMaxHp = new Label("", baseStyle);
	private Label entMaxMp = new Label("", baseStyle);
	
	private boolean close;
	
	public LvlUpPopUp(Entity ent, Stage stage) {
		this.ent = ent;
		this.close = false;
		super.setPosition(Gdx.graphics.getWidth()/2 - LVL_UP_WIDTH/2, Gdx.graphics.getHeight()/2 - LVL_UP_HEIGHT/2);
		super.setWidth(LVL_UP_WIDTH);
		super.setHeight(LVL_UP_HEIGHT);
		stateTime = 0f;
		fontGreen.setColor(Color.GREEN);
		fontBlue.setColor(Color.BLUE);
		
		text_atlas = new TextureAtlas(Gdx.files.internal("buttons/button_square/button_square.pack"));
		Skin mySkin = new Skin(text_atlas);
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = mySkin.getDrawable("button_square_up");
		textButtonStyle.down = mySkin.getDrawable("button_square_down");
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;
		textButtonStyle.font = font;
		
		closeButton = new TextButton("X", textButtonStyle);
		closeButton.pad(10);
		closeButton.addListener(new InputListener() {
			@Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				closeButton.setChecked(true);
                close = true;
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                closeButton.setChecked(false);
                return true;
		}});
		
		closeButton.setPosition(super.getX() + LVL_UP_WIDTH - 2 * CLOSE_BUTTON_WIDTH, super.getY() + LVL_UP_HEIGHT - 2 * CLOSE_BUTTON_HEIGHT);
		closeButton.setWidth(CLOSE_BUTTON_WIDTH);
		closeButton.setHeight(CLOSE_BUTTON_HEIGHT);
		
		entLvl.setText( "Lvl " + ent.getLvl_ent() + " !");
		entAtk.setText(String.valueOf(ent.getATK()) + " (+2)");
		entDef.setText(String.valueOf(ent.getDEF()) + "(+1)");
		entMaxHp.setText("HP " + ent.getMaxHP() + " (+10)");
		entMaxMp.setText("MP " + ent.getMaxMP() + " (+10)");
		
		
		stage.addActor(this);
		stage.addActor(closeButton);
		
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(bg, super.getX(), super.getY());
		font.draw(batch, entLvl.getText(), super.getX() + 60, super.getY() + 110);
		batch.draw((TextureRegion) atk_icon.getKeyFrame(stateTime), super.getX() + 50, super.getY() + 30, 32, 32);
		font.draw(batch, entAtk.getText(), super.getX() + 86, super.getY() + 50);
		batch.draw((TextureRegion) def_icon.getKeyFrame(stateTime), super.getX() + 150, super.getY() + 30, 32, 32);
		font.draw(batch, entDef.getText(), super.getX() + 186, super.getY() + 50);		
		fontGreen.draw(batch, entMaxHp.getText(), super.getX() + 240, super.getY() + 50);
		fontBlue.draw(batch, entMaxMp.getText(), super.getX() + 350, super.getY() + 50);
		
	}
	
	@Override
	public void act(float delta) {
		stateTime += delta;
		if (close) {
			this.remove();
			closeButton.remove();
		}
	}

}
