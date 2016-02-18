package com.dcu.sharktag;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

public class LoginScreen extends AbstractScreen{
	
	private TextField loginName;
	private TextField loginPassword;

	public LoginScreen(SharkTag game){
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
		super.render(delta);
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
					game.setScreen(new MainMenu(game));
					dispose();
				}
				else{
					//TODO error message
				}
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
