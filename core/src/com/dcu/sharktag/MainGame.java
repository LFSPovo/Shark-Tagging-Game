package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

public class MainGame extends AbstractScreen{
	
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private BitmapFont bitmapFont;
	
	private Texture image;
	private Vector2 imageSize = new Vector2(0, 0);	// New size for the image if
													// it doesn't fit the screen
	private float imageRatio;
	private float imageScale;
	
	private Vector2 touchPoint = new Vector2();
	
	Array<Tag> tags = new Array<Tag>();
	boolean touchDown = false;
	
	private Array<String> sharkList = new Array<String>();
	SelectBox<String> sharkSelectBox;
	
	public MainGame(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		sharkList.add("Choose a shark");
		sharkList.add("Lemon Shark");
		sharkList.add("Tiger Shark");
		sharkList.add("Blue Shark");
		
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		bitmapFont = new BitmapFont();
		
		image = fetchImage();
		
		buildGUI();
	}
	
	@Override
	public void render(float delta){
//		updateTagging();
		update();
		
		clearScreen();
		draw();
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
		Gdx.gl.glEnable(GL20.GL_BLEND);
		batch.begin();
				
		if(image != null){
			batch.draw(image,
					(game.WORLD_WIDTH - imageSize.x) / 2,	//if it's smaller than the screen,
					game.WORLD_HEIGHT - imageSize.y,
					imageSize.x, imageSize.y);
		}
		for(Tag t : tags){
			t.renderText(batch, bitmapFont);
		}
		batch.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		drawTags();
	}
	
	private void drawTags(){
		shapeRenderer.setProjectionMatrix(stage.getCamera().projection);
		shapeRenderer.setTransformMatrix(stage.getCamera().view);
		
		for(Tag t : tags){
			t.update(touchPoint);
			t.render(shapeRenderer);
		}
	}
	
	private Texture fetchImage(){
		Gdx.app.log("debug", "request image");
		String imageUrl = game.getComm().requestImage();
		Gdx.app.log("debug", imageUrl);
		Texture t = game.getComm().fetchImage(imageUrl);
		
		imageSize.x = t.getWidth();
		imageSize.y = t.getHeight();
		
		imageRatio = imageSize.x / imageSize.y;
		
		if(imageSize.y > game.WORLD_HEIGHT - 50){
			imageSize.y = game.WORLD_HEIGHT - 50;
			imageSize.x = imageSize.y * imageRatio;
		}
		
		imageScale = t.getWidth() / imageSize.x;
		
		return t;
	}
	
	private void buildGUI(){
		TextButton backButton = new TextButton("Menu", game.getUISkin());
		backButton.setPosition(10, 10);
		stage.addActor(backButton);
		
		TextButton nextButton = new TextButton("Next", game.getUISkin());
		nextButton.setPosition(game.WORLD_WIDTH - nextButton.getWidth() - 10, 10);
		stage.addActor(nextButton);
		
		TextButton addTagButton = new TextButton("+ ", game.getUISkin());
		addTagButton.setPosition(game.WORLD_WIDTH * 3 / 4, 10);
		stage.addActor(addTagButton);
		
		sharkSelectBox = new SelectBox<String>(game.getUISkin());
		sharkSelectBox.setPosition(game.WORLD_WIDTH * 1 / 4, 10);
		sharkSelectBox.setItems(sharkList);
		sharkSelectBox.pack();
		stage.addActor(sharkSelectBox);
		
		backButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new MainMenu(game));
				dispose();
			}
		});
		
		nextButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.getComm().uploadTags(tags);
				tags.clear();
				
				image.dispose();
				image = fetchImage();
			}
		});
		
		addTagButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				
				if(!emptyTagExists()){
					addTag(game.WORLD_WIDTH / 2 - 25, game.WORLD_HEIGHT / 2 - 25);
				}
			}
		});
		
		sharkSelectBox.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				updateActiveTag((String)((SelectBox<String>)actor).getSelected());
			}
		});
		
		sharkSelectBox.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
//				super.tap(event, x, y, count, button);
				
//				if(!emptyTagExists()){
//					addTag(game.WORLD_WIDTH / 2 - 25, game.WORLD_HEIGHT / 2 - 25);
//				}
			}
		});
	}
	
	private void update(){
		if(Gdx.input.isTouched()){
			touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			touchPoint = stage.getViewport().unproject(touchPoint);
			
			if(touchPoint.y > 50){
			
				for(int j = 0; j < tags.size; j++){
					
					Tag t = tags.get(j);
					
					if(t.isActive()){
						t.grabHandles(touchPoint);
					}
					else{
						if(t.contains(touchPoint) && !touchDown){
							
							for(int i = 0; i < tags.size; i++){
								tags.get(i).setActive(false);
							}
							
							t.setActive(true);
							sharkSelectBox.setSelectedIndex(t.getSharkId());
						}
						else{
							t.setActive(false);
						}
					}
				}
			}
			
			touchDown = true;
		}
		else{
			if(touchDown){
				for(Tag t : tags){
					t.releaseHandles();
				}
				sortTags();
				touchDown = false;
			}
		}
	}
	
	private void addTag(float x, float y){
		
		for(Tag t : tags){
			t.setActive(false);
		}
		Tag newTag = new Tag(x, y, imageSize, imageScale);
		sharkSelectBox.setSelectedIndex(newTag.getSharkId());
		tags.add(newTag);
	}
	
	private boolean emptyTagExists(){
		for(Tag t : tags){
			if(t.getSharkId() == 0){
				return true;
			}
		}
		
		return false;
	}
	
	private void updateActiveTag(String text){
		if(tags.size > 0){
			for(Tag t : tags){
				if(t.isActive()){
					t.setSharkId(sharkList, text);
				}
			}
		}
	}
	
	private void sortTags(){
		//sort the tags by area, with smallest first
		
		int maxIndex = tags.size - 1;
		
		for(int i = 0; i < tags.size - 1; i++){
			int index = 0;
			
			while(index < maxIndex){
				if(tags.get(index).getArea() < tags.get(index + 1).getArea()){
					Tag tmp = tags.removeIndex(index);
					tags.insert(index + 1, tmp);
				}
				index++;
			}
			
			maxIndex--;
		}
	}
}
