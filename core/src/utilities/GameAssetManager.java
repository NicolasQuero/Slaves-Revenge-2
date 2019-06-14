package utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class GameAssetManager {
	
	public final AssetManager manager = new AssetManager();
	
	public final String dashAnim = "spells/dash.png";
	
	public void queueAddImages() {
		manager.load(dashAnim, Texture.class);
	}

}
