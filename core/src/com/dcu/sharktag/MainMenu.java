package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenu extends ScreenAdapter{

	private SharkTag game;
	private Stage stage;
	
	private float uiOriginX = 0;
	private float uiOriginY = 0;
	
	public MainMenu(SharkTag game){
		this.game = game;
		
		uiOriginX = game.WORLD_WIDTH / 2;
		uiOriginY = 4 * game.WORLD_HEIGHT / 5;
	}
	
	@Override
	public void show(){
		stage = new Stage(new FitViewport(game.WORLD_WIDTH, game.WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
