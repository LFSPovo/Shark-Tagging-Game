package com.dcu.sharktag;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SharkTag extends Game {
	private float worldWidth = 854;
	private float worldHeight = 480;
	
	private Skin uiSkin;
	private Communication comm = new Communication();
	private Preferences pref;
	
	private SpriteBatch batch;				//for drawing images and text
	private ShapeRenderer shapeRenderer;	//for drawing rectangles
	
	private Texture backgroundImage;
	
	@Override
	public void create () {
		uiSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		pref = Gdx.app.getPreferences("Config");
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		backgroundImage = new Texture(Gdx.files.internal("back.jpg"));
		
		setScreen(new IntroScreen(this));
	}
	
	public Skin getUISkin(){
		return uiSkin;
	}
	
	public Communication getComm(){
		return comm;
	}
	
	public Preferences getPreferences(){
		return pref;
	}
	
	public SpriteBatch getBatch(){
		return batch;
	}
	
	public ShapeRenderer getShapeRenderer(){
		return shapeRenderer;
	}
	
	public float getWidth(){
		return worldWidth;
	}
	
	public float getHeight(){
		return worldHeight;
	}
	
	public void drawBackground(Stage s){
		batch.setProjectionMatrix(s.getCamera().projection);
		batch.setTransformMatrix(s.getCamera().view);
		batch.begin();
		batch.draw(backgroundImage, 0, 0, getWidth(), getHeight());
		batch.end();
	}
}
