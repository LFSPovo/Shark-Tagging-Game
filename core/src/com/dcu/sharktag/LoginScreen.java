package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

public class LoginScreen extends AbstractScreen{
	
	private TextField loginName;
	private TextField loginPassword;
	
	private boolean nameWasBlank = false;
	private boolean passwordWasBlank = false;
	
	private boolean firstTime = true;

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
		update();
		
		clearScreen();
		super.render(delta);
	}
	
	private void update(){
		if(nameWasBlank == true && !loginName.getText().equals("")){
			loginName.setColor(1, 1, 1, 1);
			nameWasBlank = false;
		}
		
		if(passwordWasBlank == true && !loginPassword.getText().equals("")){
			loginPassword.setColor(1, 1, 1, 1);
			passwordWasBlank = false;
		}
	}
	
	private void buildGUI(){
		Label usernameLabel = new Label("Username", game.getUISkin());
		usernameLabel.setPosition(uiOriginX, uiOriginY + 30, Align.center);
		stage.addActor(usernameLabel);
		loginName = new TextField("", game.getUISkin());
		loginName.setWidth(game.WORLD_WIDTH / 2.2f);
		loginName.setPosition(uiOriginX, uiOriginY, Align.center);
		
		stage.addActor(loginName);
		
		Label passwordLabel = new Label("Password", game.getUISkin());
		passwordLabel.setPosition(uiOriginX, uiOriginY - 50, Align.center);
		stage.addActor(passwordLabel);
		loginPassword = new TextField("", game.getUISkin());
		loginPassword.setPasswordMode(true);
		loginPassword.setPasswordCharacter('*');
		loginPassword.setWidth(game.WORLD_WIDTH / 2.2f);
		loginPassword.setPosition(uiOriginX, uiOriginY - 80, Align.center);
		stage.addActor(loginPassword);
		
		TextButton loginButton = new TextButton("Login", game.getUISkin());
		loginButton.setPosition(uiOriginX, uiOriginY - 130, Align.center);
		stage.addActor(loginButton);
		
		TextButton loginRegister = new TextButton("Register", game.getUISkin());
		loginRegister.setPosition(uiOriginX, uiOriginY - 180, Align.center);
		stage.addActor(loginRegister);
		
		TextButton loginExit = new TextButton("Exit", game.getUISkin());
		loginExit.setPosition(uiOriginX, uiOriginY - 280, Align.center);
		stage.addActor(loginRegister);
		
		loginButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				
				if(loginUser(loginName.getText(), loginPassword.getText())){
					Gdx.input.setOnscreenKeyboardVisible(false);
					game.setScreen(new MainMenu(game));
					dispose();
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
		if(!loginName.getText().equals("") && !loginPassword.getText().equals("")){
			String status = game.getComm().logIn(loginName.getText(), loginPassword.getText());
			
			if(!status.equals("")){
				Gdx.input.setOnscreenKeyboardVisible(false);
				Dialog dialog = new Dialog("Error", game.getUISkin());
				dialog.text(status);
				dialog.button("OK");
				dialog.show(stage);
				
				return false;
			}
			
			return true;
		}
		else{
			Gdx.app.log("debug", "Some fields are empty");
			
			if(loginName.getText().equals("")){
				loginName.setColor(1, 0, 0, 1);
				nameWasBlank = true;
			}
			
			if(loginPassword.getText().equals("")){
				loginPassword.setColor(1, 0, 0, 1);
				passwordWasBlank = true;
			}
			
			Gdx.input.setOnscreenKeyboardVisible(false);
			Dialog dialog = new Dialog("Error", game.getUISkin());
			dialog.text("Some of the fields are empty");
			dialog.button("OK");
			dialog.show(stage);
			
			return false;
		}
	}
}
