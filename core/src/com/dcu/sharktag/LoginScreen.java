package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LoginScreen extends ScreenAdapter{
	
	private SharkTag game;
	private Stage stage;
	
	private TextField loginName;
	private TextField loginPassword;
	
	private float uiOriginX = 0;
	private float uiOriginY = 0;

	public LoginScreen(SharkTag game){
		this.game = game;
	}
	
	@Override
	public void show(){
		stage = new Stage(new FitViewport(game.WORLD_WIDTH, game.WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		
		uiOriginX = game.WORLD_WIDTH / 2;
		uiOriginY = 4 * game.WORLD_HEIGHT / 5 + 50;
		
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}
	
	@Override
	public void dispose(){
		stage.dispose();
	}
	
	private void buildGUI(){
		Label usernameLabel = new Label("Username", game.getUISkin());
		usernameLabel.setPosition(uiOriginX, uiOriginY + 30, Align.center);
		stage.addActor(usernameLabel);
		loginName = new TextField("", game.getUISkin());
		loginName.setPosition(uiOriginX, uiOriginY, Align.center);
		stage.addActor(loginName);
		
		Label passwordLabel = new Label("Password", game.getUISkin());
		passwordLabel.setPosition(uiOriginX, uiOriginY - 50, Align.center);
		stage.addActor(passwordLabel);
		loginPassword = new TextField("", game.getUISkin());
		loginPassword.setPasswordMode(true);
		loginPassword.setPasswordCharacter('*');
		loginPassword.setPosition(uiOriginX, uiOriginY - 80, Align.center);
		stage.addActor(loginPassword);
		
		TextButton loginButton = new TextButton("Login", game.getUISkin());
		loginButton.setPosition(uiOriginX, uiOriginY - 130, Align.center);
		stage.addActor(loginButton);
		
		TextButton loginRegister = new TextButton("Register", game.getUISkin());
		loginRegister.setPosition(uiOriginX, uiOriginY - 180, Align.center);
		stage.addActor(loginRegister);
		
		loginButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				
				if(loginUser(loginName.getText(), loginPassword.getText())){
					//TODO move to main menu
				}
				else{
					//TODO error message
				}
				dispose();
			}
		});
		
		loginRegister.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new RegisterScreen(game));
				dispose();
			}
		});
	}
	
	private boolean loginUser(String name, String password){
		//TODO implement connection to server
		return true;
	}
}
