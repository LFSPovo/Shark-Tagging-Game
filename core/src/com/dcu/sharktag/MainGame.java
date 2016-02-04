package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	
//	private mouse/
	private float touchX = 0;
	private float touchY = 0;
	private float touchStartX = 0;
	private float touchStartY = 0;
	
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
		float ratio = image.getHeight() / 480;
		float w = image.getWidth() / ratio;
		if(image != null){
			batch.draw(image, 0, 0, w, 480);
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
			// Convert screen points to virtual screen points
			Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			touchPoint = stage.getViewport().unproject(touchPoint);
			
			//TODO make better tagging mechanism
			touchStartX = touchPoint.x;
			touchStartY = touchPoint.y;
			touchX = tagStartX;
			touchY = tagStartY;
		}
		
		if(Gdx.input.isTouched()){
			// Convert screen points to virtual screen points
			Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			touchPoint = stage.getViewport().unproject(touchPoint);
			
			touchX = touchPoint.x;
			touchY = touchPoint.y;
		}
		
		if(Math.abs(touchStartX - touchX) > 50){
			tagStartX = touchStartX;
			tagEndX = touchX;
		}
		else{
			float axisX = (touchX - touchStartX) / Math.abs(touchX - touchStartX);
			tagStartX = touchStartX;
			tagEndX = touchStartX + 50 * axisX;
		}
		
		if(Math.abs(touchStartY - touchY) > 50){
			tagStartY = touchStartY;
			tagEndY = touchY;
		}
		else{
			float axisY = (touchY - touchStartY) / Math.abs(touchY - touchStartY);
			tagStartY = touchStartY;
			tagEndY = touchStartY + 50 * axisY;
		}
	}
}
