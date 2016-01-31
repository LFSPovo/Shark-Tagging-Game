package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LoginScreen extends ScreenAdapter{
	
	private SharkTag game;
	private Stage stage;
	private Skin uiSkin;
	
	private TextField loginName;
	private TextField loginPassword;

	public LoginScreen(SharkTag game){
		this.game = game;
	}
	
	@Override
	public void show(){
		stage = new Stage(new FitViewport(game.WORLD_WIDTH, game.WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		uiSkin = new Skin(Gdx.files.internal("../android/assets/ui/uiskin.json"));
		
		Label usernameLabel = new Label("Username", uiSkin);
		usernameLabel.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5 + 30, Align.right);
		stage.addActor(usernameLabel);
		loginName = new TextField("", uiSkin);
		loginName.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5, Align.center);
		stage.addActor(loginName);
		
		Label passwordLabel = new Label("Password", uiSkin);
		passwordLabel.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5 - 50, Align.right);
		stage.addActor(passwordLabel);
		loginPassword = new TextField("", uiSkin);
		loginPassword.setPasswordMode(true);
		loginPassword.setPasswordCharacter('*');
		loginPassword.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5 - 80, Align.center);
		stage.addActor(loginPassword);
		
		TextButton loginButton = new TextButton("Login", uiSkin);
		loginButton.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5 - 130, Align.center);
		stage.addActor(loginButton);
		
		TextButton loginRegister = new TextButton("Register", uiSkin);
		loginRegister.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5 - 180, Align.center);
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
				Gdx.app.exit();
				game.setScreen(new RegisterScreen(game));
				dispose();
			}
		});
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
	
	private boolean loginUser(String name, String password){
		//TODO implement connection to server
		return true;
	}
}
