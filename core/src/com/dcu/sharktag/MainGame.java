package com.dcu.sharktag;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

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
	private HashMap<String, Integer> species = new HashMap<String, Integer>();
	
	// Main interface buttons
	private TextButton backButton;
	private TextButton nextButton;
	private TextButton addTagButton;
	private SelectBox<String> sharkSelectBox;
	
	// Tutorial buttons and text
	private TextButton tutorialBack;
	private TextButton tutorialNext;
	private Array<String> tutorialText = new Array<String>();
	private GlyphLayout tutorialLayout;
	
	private int currentTutorialProgress = 0;
	private int maxTutorialProgress = 0;
	
	public MainGame(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		Gdx.gl.glClearColor(0, 0, 0, 1);

		buildSpecies("species.json");
		buildTutorial();
		
		shapeRenderer = game.getShapeRenderer();
		batch = game.getBatch();
		bitmapFont = new BitmapFont();
		tutorialLayout = new GlyphLayout();
		
		image = fetchImage();
		
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		update();
		
		clearScreen();
		draw();
		if(game.getComm().isFirstTimer()){
			drawTutorial();
		}
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
					(game.WORLD_WIDTH - imageSize.x) / 2,	//if it's smaller 
					game.WORLD_HEIGHT - imageSize.y,		//than the screen
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
	
	private void drawTutorial(){
		shapeRenderer.setProjectionMatrix(stage.getCamera().projection);
		shapeRenderer.setTransformMatrix(stage.getCamera().view);
		
		// Needed in order to draw with transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.3f);
		shapeRenderer.rect(50, game.WORLD_HEIGHT - 150, 300, 130);
		shapeRenderer.end();
		
		batch.begin();
		batch.setColor(1, 1, 1, 1);
		tutorialLayout.setText(bitmapFont,
				tutorialText.get(currentTutorialProgress));
		bitmapFont.draw(batch,
				tutorialText.get(currentTutorialProgress), 55, game.WORLD_HEIGHT - 25);
		batch.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	private Texture fetchImage(){
		Texture t;
		if(game.getComm().isFirstTimer()){
			t = new Texture(Gdx.files.internal("shark-test.jpg"));
		}
		else{
			String imageUrl = game.getComm().requestImage();
			t = game.getComm().fetchImage(imageUrl);
		}
		
		// Make the image fit the screen while keeping its aspect ratio
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
		backButton = new TextButton("Menu", game.getUISkin());
		backButton.setPosition(10, 10);
		stage.addActor(backButton);
		
		nextButton = new TextButton("Next", game.getUISkin());
		nextButton.setPosition(
				game.WORLD_WIDTH - nextButton.getWidth() - 10, 10);
		stage.addActor(nextButton);
		
		addTagButton = new TextButton("+ ", game.getUISkin());
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
				if(game.getComm().isFirstTimer()){
					game.getComm().finishTutorial();
					game.setScreen(new MainMenu(game));
					dispose();
				}
				else{
					game.getComm().uploadTags(tags);
					tags.clear();
					
					image.dispose();
					image = fetchImage();
				}
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
		
		if(game.getComm().isFirstTimer()){
			tutorialBack = new TextButton("<<", game.getUISkin());
			tutorialBack.setPosition(200 - 100, game.WORLD_HEIGHT - 130, Align.center);
			tutorialBack.setColor(1, 1, 1, 0.3f);
			tutorialBack.setTouchable(Touchable.disabled);
			stage.addActor(tutorialBack);
			
			tutorialNext= new TextButton(">>", game.getUISkin());
			tutorialNext.setPosition(200 + 100, game.WORLD_HEIGHT - 130, Align.center);
			stage.addActor(tutorialNext);
			
			tutorialNext.addListener(new ActorGestureListener(){
				@Override
				public void tap(InputEvent event, float x, float y, int count, int button){
					super.tap(event, x, y, count, button);
					if(currentTutorialProgress < tutorialText.size - 1){
						currentTutorialProgress++;
						if(maxTutorialProgress < currentTutorialProgress){
							maxTutorialProgress = currentTutorialProgress;
						}
						
						tutorialBack.setColor(1, 1, 1, 1f);
						tutorialBack.setTouchable(Touchable.enabled);
						
						if(currentTutorialProgress == tutorialText.size - 1){
							tutorialNext.setColor(1, 1, 1, 0.3f);
							tutorialNext.setTouchable(Touchable.disabled);
						}
					}
				}
			});
			
			tutorialBack.addListener(new ActorGestureListener(){
				@Override
				public void tap(InputEvent event, float x, float y, int count, int button){
					super.tap(event, x, y, count, button);
					if(currentTutorialProgress > 0){
						currentTutorialProgress--;
						tutorialNext.setColor(1, 1, 1, 1f);
						tutorialNext.setTouchable(Touchable.enabled);
						
						if(currentTutorialProgress == 0){
							tutorialBack.setColor(1, 1, 1, 0.3f);
							tutorialBack.setTouchable(Touchable.disabled);
						}
					}
				}
			});
		}
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
							
							sharkSelectBox.setSelected(t.getText());
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
					t.setSharkId(species.get(text), text);
				}
			}
		}
	}
	
	private void sortTags(){
		//sort the tags by area, with smallest first
		tags.sort();
		tags.reverse();
	}
	
	private void buildTutorial(){
		tutorialText.add("Hello and welcome to Shark Tagging Game.\n" +
						"You will now learn how to play this game.");
		tutorialText.add("The '+' button creates a new unnamed tag\n" +
						"You can only have one unnamed tag on the\n" +
						"screen at any time. Unnamed tags are not\n" +
						"counted towards your score, so don't worry\n" +
						"when you have one left");
		tutorialText.add("Now create a new tag\n" + 
						"Grab the pink handle to move it around\n" +
						"Grab the green handle to resize it\n" +
						"Try it now");
		tutorialText.add("Once you're done with positioning the tag,\n" +
						"use the selection box to select the correct\n" +
						"species of shark");
		tutorialText.add("You can add as many tags as you think\n" +
						"necessary. You receive points for tags similar\n" +
						"to other people's tags\nOnce you have enough tags,\n" +
						"submit them by clicking 'Next'");
	}
	
	// Parse JSON file to populate the selection box
	private void buildSpecies(String file){
		Json json = new Json();
		JsonValue mainJson = new JsonReader().parse(Gdx.files.internal("species.json"));
		JsonValue spec = mainJson.get("species");
		
		// Insert all species into the list, but ignore the first entry
		for(int i = 1; i < spec.size; i++){
			species.put(spec.get(i).getString("name"), spec.get(i).getInt("id"));
			sharkList.add(spec.get(i).getString("name"));
		}
		
		sharkList.sort();
		
		// The first entry must not be sorted, but be on the top of the list
		species.put(spec.get(0).getString("name"), spec.get(0).getInt("id"));
		sharkList.insert(0, spec.get(0).getString("name"));;
	}
}
