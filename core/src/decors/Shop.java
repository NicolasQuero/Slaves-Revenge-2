package decors;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import items.Item;

public class Shop {

	private boolean shopOpen;
	private ArrayList<Item> items;
	protected Texture shopMenuBackground;
	
	public Shop(ArrayList<Item> items, String srcBg) {
		this.items = items;
		this.shopMenuBackground = new Texture(srcBg);
	}
	
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		if (shopOpen) {
			batch.draw(shopMenuBackground, camera.position.x, camera.position.y);
		}
	}
	
	public boolean isShopOpen() {
		return this.shopOpen;
	}
	
}
