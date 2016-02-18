package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class MainGame extends AbstractScreen{
	
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	
	private Texture image;
	private Vector2 imageSize = new Vector2(0, 0);	// New size for the image if
													// it doesn't fit the screen
	
	private Vector2 touchStart = new Vector2(0, 0);
	private Vector2 touch = new Vector2(0, 0);
	private Vector2 tagStart = new Vector2(0, 0);
	private Vector2 tagEnd = new Vector2(0, 0);
	private int minTagSize = 100;	// For both horizontal and vertical sides
	private int tagGrabArea = 100;	// How close the user needs to touch near
									// the handle to grab it
	
	private boolean dragStart = false;
	private boolean dragEnd = false;
	
	public MainGame(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		
		image = fetchImage();
		
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		updateTagging();
		
		clearScreen();
		draw();
		drawTags();
		super.render(delta);
	}
	
	@Override
	public void dispose(){
		if(image != null){
			image.dispose();
		}
		super.dispose();
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
		
		shapeRenderer.setColor(0, 0, 1, 1);	// Debug
		shapeRenderer.circle(tagStart.x, tagStart.y, 5);
		
		shapeRenderer.setColor(0, 1, 0, 1);
		shapeRenderer.circle(tagEnd.x, tagEnd.y, 5);
		
		shapeRenderer.setColor(1, 0, 1, 1);
		shapeRenderer.rect(touchStart.x - 5, touchStart.y - 5, 10, 10);
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
			dragStart = false;
			dragEnd = false;
			// Convert screen points to virtual screen points
			Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			touchPoint = stage.getViewport().unproject(touchPoint);
			
			if(distance(touchPoint, tagStart) <= tagGrabArea){
				touchStart = tagEnd;
				dragStart = true;
			}
			else if(distance(touchPoint, tagEnd) <= tagGrabArea){
				touchStart = tagStart;
				dragEnd = true;
			}
			else{
				// Set new touch coordinates
				touchStart.x = touchPoint.x;
				touchStart.y = touchPoint.y;
			}
		}
		
		if(Gdx.input.isTouched()){
			// Convert screen points to virtual screen points
			Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			touchPoint = stage.getViewport().unproject(touchPoint);
			
			touch.x = touchPoint.x;
			touch.y = touchPoint.y;
		}
		
		if(Math.abs(touchStart.x - touch.x) > minTagSize){
			if(dragStart){
				tagStart.x = touch.x;
			}
			else if(dragEnd){
				tagEnd.x = touch.x;
			}
			else{
				tagStart.x = touchStart.x;
				tagEnd.x = touch.x;
			}
		}
		else{
			// Calculates a multiplier, either -1 or 1, based on
			// where the mouse is relative to the starting point of touch
			
			// This is used to make sure a tag is no smaller than minTagSize
			// on either side
			int axisX = (int)((touch.x - touchStart.x) /
					Math.abs(touch.x - touchStart.x));
			if(axisX == 0){
				axisX = 1;
			}
			
			if(dragStart){
				tagEnd.x = touchStart.x;
				tagStart.x = touchStart.x + minTagSize * axisX;
			}
			else{
				tagStart.x = touchStart.x;
				tagEnd.x = touchStart.x + minTagSize * axisX;
			}
		}
		
		if(Math.abs(touchStart.y - touch.y) > minTagSize){
			if(dragStart){
				tagStart.y = touch.y;
			}
			else if(dragEnd){
				tagEnd.y = touch.y;
			}
			else{
				tagStart.y = touchStart.y;
				tagEnd.y = touch.y;
			}
		}
		else{
			int axisY = (int)((touch.y - touchStart.y) /
					Math.abs(touch.y - touchStart.y));
			if(axisY == 0){
				axisY = 1;
			}
			
			if(dragStart){
				tagEnd.y = touchStart.y;
				tagStart.y = touchStart.y + minTagSize * axisY;
			}
			else{
				tagStart.y = touchStart.y;
				tagEnd.y = touchStart.y + minTagSize * axisY;
			}
		}
		
		//TODO do not allow size less than minTagSize even after
		// clamping the points to screen
		containPoint(tagStart);
		containPoint(tagEnd);
	}
	
	private float distance(Vector2 point1, Vector2 point2){
		//sqrt((x1 - x2) ^ 2 + (y1 - y2) ^ 2)
		return (float)Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
	}
	
	//makes sure the point does not go outside the screen
	private void containPoint(Vector2 point){
		if(point.x < 0) point.x = 0;
		if(point.x > game.WORLD_WIDTH) point.x = game.WORLD_WIDTH;
		if(point.y < 0) point.y = 0;
		if(point.y > game.WORLD_HEIGHT) point.y = game.WORLD_HEIGHT;
	}
	
	private void buildGUI(){
		TextButton backButton = new TextButton("Menu", game.getUISkin());
		backButton.setPosition(10, 10);
		stage.addActor(backButton);
		
		TextButton nextButton = new TextButton("Next", game.getUISkin());
		nextButton.setPosition(game.WORLD_WIDTH - nextButton.getWidth() - 10, 10);
		stage.addActor(nextButton);
		
		backButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new MainMenu(game));
				dispose();
			}
		});
	}
}
