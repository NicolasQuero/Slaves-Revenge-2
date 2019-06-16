package items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Item {
	
	private ItemType type;
	protected Texture icon;
	private String description;
	private boolean isEquipment;
	
	public Item(ItemType type, String srcIcon) {
		this.type = type;
		this.icon = new Texture(srcIcon);
		this.description = "Cet objet ne sert à R";
	}
	
	public boolean isEquipment() {
		return type.isEquipment();
	}
	
	public int getPrice() {
		return type.getSellPrice();
	}
	
	public int getBuyPrice() {
		return type.getSellPrice()/5;
	}
	
	public String getId() {
		return type.getId();
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public boolean isConsumable() {
		return type.isConsumable();
	}
	
	public boolean isUseInstant() {
		return type.isUseInstant();
	}
	
	public int getRarity() {
		return type.getRarity();
	}
	
	public Texture getIcon() {
		return this.icon;
	}
	
}
