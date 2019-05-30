 package world;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import entities.Entity;

public class TiledGameMap extends GameMap {

	TiledMap tiledMap;
	OrthogonalTiledMapRenderer tiledMapRenderer;
	
	public TiledGameMap(SpriteBatch batch) {
		tiledMap = new TmxMapLoader().load("maps/map_64.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
	}
	
	@Override
	public void render(OrthographicCamera camera, SpriteBatch batch, float deltaTime) {
		float render_x = camera.position.x - camera.viewportWidth * camera.zoom;
	    float render_y = camera.position.y - camera.viewportHeight * camera.zoom;

	    float render_width = camera.viewportWidth * camera.zoom * 1.5f;
	    float render_height = camera.viewportHeight * camera.zoom * 1.5f;
		if (!isInCombat()) {
			tiledMapRenderer.setView(camera.combined, render_x, render_y, render_width, render_height);
			tiledMapRenderer.render();
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			super.render(camera,  batch, deltaTime);
			batch.end();
		}
		else
			super.render(camera,  batch, deltaTime);
		
	}

	
	//Gives the TileType at the corresponding coordinates
	@Override
	public TileType getTileTypeByCoordinates(int layer, int col, int row) { // The layer with the objects has the id 4, you mustn't cast it to TiledMapTileLayer !
		Cell cell = ((TiledMapTileLayer) tiledMap.getLayers().get(layer)).getCell(col, row);
		if (cell!=null) {
			TiledMapTile tile = cell.getTile();
			if (tile!=null) {
				int id = tile.getId()-1;
				return TileType.getTileTypeById(id);
			}
		}
		return null;
	}
	
	public boolean getObjectCollisionWithEntity(int objectLayer, Rectangle rect_ent, ArrayList<Entity> entities) {
		MapLayer collisionObjectLayer = (MapLayer) tiledMap.getLayers().get(objectLayer); // we check here the collisions between the entity and the map objects
		MapObjects objects = collisionObjectLayer.getObjects();
		for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
		    Rectangle rectangle = rectangleObject.getRectangle();
		    if (Intersector.overlaps(rectangle, rect_ent)) {
		    	return true;
		        // collision happened
		    }
		}
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isPlayed() && Intersector.overlaps(entities.get(i).getRekt(), rect_ent)) { // if the entity is not played we check the collisions
				System.out.println(entities.get(i).getType().getId());
				return true;
			}
				
		}
		return false;
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

}
