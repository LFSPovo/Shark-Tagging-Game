package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends ScreenAdapter{
	
	private SharkTag game;
	private Stage stage;
	
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	
	private Texture image;
	private Vector2 imageSize = new Vector2(0, 0);	// New size for the image if
													// it doesn't fit the screen
	
	private Vector2 touchStart = new Vector2(0, 0);
	private Vector2 touch = new Vector2(0, 0);
	private Vector2 tagStart = new Vector2(0, 0);
	private Vector2 tagEnd = new Vector2(0, 0);
	private int minTagSize = 50;	// For both horizontal and vertical sides
	
	public MainGame(SharkTag game){
		this.game = game;
	}
	
	@Override
	public void show(){
		stage = new Stage(new FitViewport(game.WORLD_WIDTH, game.WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
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
			batch.draw(image,
					(game.WORLD_WIDTH - imageSize.x) / 2,	//if it's smaller than the screen,
					(game.WORLD_HEIGHT - imageSize.y) / 2,	//center it
					imageSize.x, imageSize.y);
		}
		batch.end();
	}
	
	private void drawTags(){
		shapeRenderer.setProjectionMatrix(stage.getCamera().projection);
		shapeRenderer.setTransformMatrix(stage.getCamera().view);
		
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.rect(tagStart.x, tagStart.y,
				tagEnd.x - tagStart.x, tagEnd.y - tagStart.y);
		shapeRenderer.end();
	}
	
	private Texture fetchImage(){
		//TODO fetch image from the server
		
		// Temporary image loading
		Texture t = new Texture(Gdx.files.internal("resolution-test1.jpg"));
		
		// Correct image height relative to image width to fit the
		// image on the screen
		if(t.getWidth() > game.WORLD_WIDTH){
			float ratio = 854f / t.getWidth();
			imageSize.y = (int)(t.getHeight() * ratio);
		}
		else{
			imageSize.y = t.getHeight();
		}
		
		// If the image was in any other aspect ratio than 16:9,
		// it still needs correction of the width relative
		// to its height
		if(imageSize.y > game.WORLD_HEIGHT){
			float ratio = 480f / imageSize.y;
			imageSize.x = (int)(imageSize.y * ratio);
		}
		else{
			// There is a possibility that the image is genuinely smaller
			// than the screen resolution
			imageSize.x = Math.min(t.getWidth(), game.WORLD_WIDTH);
		}
		
		return t;
	}
	
	private void updateTagging(){
		if(Gdx.input.justTouched()){
			// Convert screen points to virtual screen points
			Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			touchPoint = stage.getViewport().unproject(touchPoint);
			
			// Set new touch coordinates
			touchStart.x = touchPoint.x;
			touchStart.y = touchPoint.y;
			touch.x = tagStart.x;
			touch.y = tagStart.y;
		}
		
		if(Gdx.input.isTouched()){
			// Convert screen points to virtual screen points
			Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			touchPoint = stage.getViewport().unproject(touchPoint);
			
			touch.x = touchPoint.x;
			touch.y = touchPoint.y;
		}
		
		if(Math.abs(touchStart.x - touch.x) > minTagSize){
			tagStart.x = touchStart.x;
			tagEnd.x = touch.x;
		}
		else{
			// Calculates a multiplier, either -1 or 1, based on
			// where the mouse is relative to the starting point of touch
			float axisX = (touch.x - touchStart.x) /
					Math.abs(touch.x - touchStart.x);
			tagStart.x = touchStart.x;
			tagEnd.x = touchStart.x + minTagSize * axisX;
		}
		
		if(Math.abs(touchStart.y - touch.y) > minTagSize){
			tagStart.y = touchStart.y;
			tagEnd.y = touch.y;
		}
		else{
			float axisY = (touch.y - touchStart.y) /
					Math.abs(touch.y - touchStart.y);
			tagStart.y = touchStart.y;
			tagEnd.y = touchStart.y + minTagSize * axisY;
		}
	}
}
