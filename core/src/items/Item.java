package items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Item {
	
	private ItemType type;
	protected Texture icon;
	private String description, pano;
	private boolean isEquipment;
	private int atkBonus, defBonus;
	
	public Item(ItemType type, String srcIcon) {
		this.type = type;
		this.icon = new Texture(srcIcon);
		this.description = "Cet objet ne sert à R";
		this.atkBonus = -1;
		this.defBonus = -1;
		this.pano = "";
	}

	public void setPano(String pano) {
		this.pano = pano;
	}
	
	public String getPano() {
		return this.pano;
	}
	
	public void setAtkBonus(int bonus) {
		this.atkBonus = bonus;
	}
	
	public void setDefBonus(int bonus) {
		this.defBonus = bonus;
	}
	
	public int getDefBonus() {
		return this.defBonus;
	}
	
	public int getAtkBonus() {
		return this.atkBonus;
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
	
	public void setDescription(String description) {
		this.description = description;
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
