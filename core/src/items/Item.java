package items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Item {
	
	private ItemType type;
	protected Texture icon;
	
	public Item(ItemType type, String srcIcon) {
		this.type = type;
		this.icon = new Texture(srcIcon);
	}
	
	public void showItem(SpriteBatch batch, int x, int y, int width, int height) {
		batch.draw(icon, x, y, width, height);
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
	
	public boolean isConsumable() {
		return type.isConsumable();
	}
	
	public boolean isUseInstant() {
		return type.isUseInstant();
	}
	
	public int getRarity() {
		return type.getRarity();
	}
	
}
