package utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

import Spells.Spell;

public class SpellActor extends Actor {
	
	BitmapFont font = new BitmapFont();
	
	public Texture bg, icon;
	private float stateTime;
	private int iconSize, padX, padY;
	private Spell spell;
	private TextArea textField;
	
	public SpellActor(String bgSrc, Spell spell, int iconSize, int padX, int padY) {
		this.bg = new Texture(bgSrc);
		this.icon = spell.getIcon();
		this.spell = spell;
		this.iconSize = iconSize;
		this.padX = padX; this.padY = padY;
		/*textField.getStyle().font = font;
		textField.setWidth(super.getWidth() - padX - iconSize - 4);
		textField.setHeight(iconSize - 20);
		textField.setPosition(padX + iconSize + 2, padY);*/
		stateTime = 0f;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(bg, super.getX(), super.getY(), super.getWidth(), super.getHeight());
		batch.draw(icon, super.getX() + padX, super.getY() + padY, iconSize, iconSize);
		font.draw(batch, spell.getName() + " Lvl : " +  spell.getLvl()
		, super.getX() + padX + iconSize + 10, super.getY() + padY + iconSize - 5);
		if (spell.getDescription() != null)
			font.draw(batch, spell.getDescription(), super.getX() + padX + iconSize + 10, super.getY() + padY + iconSize - 26);
	}
	
	@Override
	public void act (float delta) {
		this.stateTime += delta;
	}

}
