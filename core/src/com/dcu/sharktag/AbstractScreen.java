package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
 * This class abstracts away the GUI code needed by each screen
 */

public class AbstractScreen extends ScreenAdapter{

	protected SharkTag game;
	protected Stage stage;
	
	protected float uiOriginX = 0;
	protected float uiOriginY = 0;
	
	public AbstractScreen(SharkTag game){
		this.game = game;
	}
	
	@Override
	public void show(){
		stage = new Stage(new FitViewport(game.getWidth(), game.getHeight()));
		Gdx.input.setInputProcessor(stage);
		
		uiOriginX = game.getWidth() / 2;
		uiOriginY = 4 * game.getHeight() / 5 + 50;
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
	}
	
	@Override
	public void render(float delta){
		stage.act();
		stage.draw();
	}
	
	@Override
	public void resize(int width, int height){
		Viewport vp = stage.getViewport();
		// Set screen size
		vp.update(width, height);
		// Use updated viewport
		stage.setViewport(vp);
	}
	
	@Override
	public void dispose(){
		stage.dispose();
	}
	
	protected void clearScreen(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
