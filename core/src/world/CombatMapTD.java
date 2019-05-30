package world;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import combats.Combat;
import combats.CombatTD;
import entities.Entity;

public class CombatMapTD extends GameMap {

	CombatTD combat;
	TiledMap tiledMap;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	float xDraw, yDraw;
	
	public CombatMapTD(String id) {
		combat = null;
		if (id.equals("cite"))
			tiledMap = new TmxMapLoader().load("maps/combat_td.tmx");
		else
			tiledMap = new TmxMapLoader().load("maps/combat_td.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		xDraw = -1; yDraw = -1;
	}

	@Override
	public void render(OrthographicCamera camera, SpriteBatch batch, float delta) {
		if (xDraw == -1 && yDraw == -1) {
			xDraw = camera.position.x - camera.viewportWidth/2;
			yDraw = camera.position.y - camera.viewportHeight/2;
			camera.translate(-xDraw, -yDraw);
		}
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		combat.render(batch, delta);
		batch.end();
	}

	public void setCombat(CombatTD combat) {
		this.combat = combat;
	}
	
	@Override
	protected boolean getObjectCollisionWithEntity(int objectLayerId, Rectangle rect, ArrayList<Entity> entities) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TileType getTileTypeByCoordinates(int layer, int col, int row) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWidth() {
		return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth();
	}

	@Override
	public int getHeight() {
		return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight();
	}

	@Override
	public int getLayers() {
		return tiledMap.getLayers().getCount();
	}	
	
	public void dispose(OrthographicCamera camera) {
		camera.translate(xDraw, yDraw);
	}
}