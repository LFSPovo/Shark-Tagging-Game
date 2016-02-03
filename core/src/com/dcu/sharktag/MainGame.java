package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends ScreenAdapter{
	
	private SharkTag game;
	private Stage stage;

	private Viewport viewport;
	private Camera camera;
	
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private Texture image;
	
//	private mouse/
	private float tagStartX = 0;
	private float tagStartY = 0;
	private float tagEndX = 0;
	private float tagEndY = 0;
	
	public MainGame(SharkTag game){
		this.game = game;
	}
	
	@Override
	public void show(){
		stage = new Stage(new FitViewport(game.WORLD_WIDTH, game.WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		
		image = fetchImage();
	}
	
	@Override
	public void render(float delta){
		updateTagging();
		
		clearScreen();
		draw();
		drawTags();
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
	
	private void clearScreen(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	private void draw(){
		batch.setProjectionMatrix(stage.getCamera().projection);
		batch.setTransformMatrix(stage.getCamera().view);
		
		batch.begin();
		if(image != null){
			batch.draw(image, 0, 0);
		}
		batch.end();
	}
	
	private void drawTags(){
		shapeRenderer.setProjectionMatrix(stage.getCamera().projection);
		shapeRenderer.setTransformMatrix(stage.getCamera().view);
		
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.rect(tagStartX, tagStartY, tagEndX - tagStartX, tagEndY - tagStartY);
		shapeRenderer.end();
	}
	
	private Texture fetchImage(){
		//TODO fetch image from the server
		
		//temporary
		Texture t = new Texture(Gdx.files.internal("shark-test.jpg"));
		return t;
	}
	
	private void updateTagging(){
		
		if(Gdx.input.justTouched()){
			//TODO make better tagging mechanism
			tagStartX = Gdx.input.getX();
			tagStartY = game.WORLD_HEIGHT - Gdx.input.getY();
			tagEndX = tagStartX;
			tagEndY = tagStartY;
		}
		
		if(Gdx.input.isTouched()){
			tagEndX = Gdx.input.getX();
			tagEndY = game.WORLD_HEIGHT - Gdx.input.getY();
		}
	}
}
