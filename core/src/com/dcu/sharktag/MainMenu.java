package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

public class MainMenu extends AbstractScreen{
	
	private SpriteBatch batch;
	private Texture backgroundImage;
	
	public MainMenu(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		
		batch = new SpriteBatch();
		backgroundImage = new Texture(Gdx.files.internal("back.jpg"));
		
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		clearScreen();
		
		batch.setProjectionMatrix(stage.getCamera().projection);
		batch.setTransformMatrix(stage.getCamera().view);
		batch.begin();
		batch.draw(backgroundImage, 0, 0, game.WORLD_WIDTH, game.WORLD_HEIGHT);
		batch.end();
		
		super.render(delta);
	}
	
	@Override
	public void dispose(){
		super.dispose();
		backgroundImage.dispose();
	}
	
	private void buildGUI(){
		TextButton playButton = new TextButton("Play", game.getUISkin());
		playButton.setPosition(uiOriginX, uiOriginY, Align.center);
		stage.addActor(playButton);
		
		TextButton optionsButton = new TextButton("Options", game.getUISkin());
		optionsButton.setPosition(uiOriginX, uiOriginY - 50, Align.center);
		stage.addActor(optionsButton);
		
		TextButton highscoreButton = new TextButton("Highscores", game.getUISkin());
		highscoreButton.setPosition(uiOriginX, uiOriginY - 100, Align.center);
		stage.addActor(highscoreButton);
		
		TextButton creditsButton = new TextButton("Credits", game.getUISkin());
		creditsButton.setPosition(uiOriginX, uiOriginY - 150, Align.center);
		stage.addActor(creditsButton);
		
		TextButton exitButton = new TextButton("Exit", game.getUISkin());
		exitButton.setPosition(uiOriginX, uiOriginY - 200, Align.center);
		stage.addActor(exitButton);
		
		playButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new MainGame(game));
				dispose();
			}
		});
		
		highscoreButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new HighScoreScreen(game));
				dispose();
			}
		});
		
		creditsButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new CreditsScreen(game));
				dispose();
			}
		});
		
		exitButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				Gdx.app.exit();
			}
		});
	}
}
