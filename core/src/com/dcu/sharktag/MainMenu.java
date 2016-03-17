package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

public class MainMenu extends AbstractScreen{
	
	public MainMenu(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		clearScreen();
		
		game.drawBackground(stage);
		
		super.render(delta);
	}
	
	private void buildGUI(){
		TextButton playButton = new TextButton("Play", game.getUISkin());
		playButton.setSize(game.getWidth() / 2.2f, 40);
		playButton.setPosition(uiOriginX, uiOriginY, Align.center);
		stage.addActor(playButton);
		
		TextButton tutorialButton = new TextButton("Tutorial", game.getUISkin());
		tutorialButton.setSize(game.getWidth() / 2.2f, 40);
		tutorialButton.setPosition(uiOriginX, uiOriginY - 50, Align.center);
		stage.addActor(tutorialButton);
		
		TextButton highscoreButton = new TextButton("Highscores", game.getUISkin());
		highscoreButton.setSize(game.getWidth() / 2.2f, 40);
		highscoreButton.setPosition(uiOriginX, uiOriginY - 100, Align.center);
		stage.addActor(highscoreButton);
		
		TextButton creditsButton = new TextButton("Credits", game.getUISkin());
		creditsButton.setSize(game.getWidth() / 2.2f, 40);
		creditsButton.setPosition(uiOriginX, uiOriginY - 150, Align.center);
		stage.addActor(creditsButton);
		
		TextButton logoutButton = new TextButton("Log out", game.getUISkin());
		logoutButton.setSize(game.getWidth() / 2.2f, 40);
		logoutButton.setPosition(uiOriginX, uiOriginY - 200, Align.center);
		stage.addActor(logoutButton);
		
		TextButton exitButton = new TextButton("Exit", game.getUISkin());
		exitButton.setSize(game.getWidth() / 2.2f, 40);
		exitButton.setPosition(uiOriginX, uiOriginY - 250, Align.center);
		stage.addActor(exitButton);
		
		playButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int c, int b){
				super.tap(event, x, y, c, b);
				game.setScreen(new MainGame(game));
				dispose();
			}
		});
		
		highscoreButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int c, int b){
				super.tap(event, x, y, c, b);
				game.setScreen(new HighScoreScreen(game));
				dispose();
			}
		});
		
		creditsButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int c, int b){
				super.tap(event, x, y, c, b);
				game.setScreen(new CreditsScreen(game));
				dispose();
			}
		});
		
		tutorialButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int c, int b){
				super.tap(event, x, y, c, b);
				game.getComm().setFirstTimer(true);
				game.setScreen(new MainGame(game));
				dispose();
			}
		});
		
		logoutButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int c, int b){
				super.tap(event, x, y, c, b);
				
				game.getPreferences().putBoolean("autoLogin", false);
				game.getPreferences().putString("token", "");
				game.getPreferences().flush();
				game.getComm().logOut();
				game.setScreen(new LoginScreen(game));
				dispose();
			}
		});
		
		exitButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int c, int b){
				super.tap(event, x, y, c, b);
				Gdx.app.exit();
			}
		});
	}
}
