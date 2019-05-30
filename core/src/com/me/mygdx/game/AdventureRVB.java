package com.me.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import world.GameMap;
import world.TiledGameMap;


public class AdventureRVB extends Game {
	SpriteBatch batch;
	OrthographicCamera camera;
	GameMap gameMap;
	
	Texture blank;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 800);
		camera.update();
		
		gameMap = new TiledGameMap(batch);
		Gdx.graphics.setResizable(true);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		gameMap.render(camera, batch, Gdx.graphics.getDeltaTime());
	}
	
	
	@Override
	public void dispose () {
		batch.dispose();
		gameMap.dispose();
	}
	
	
}
