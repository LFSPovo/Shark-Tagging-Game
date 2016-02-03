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
import com.badlogic.gdx.utils.viewport.Viewport;

public class RegisterScreen extends ScreenAdapter{

	private SharkTag game;
	
	private Stage stage;
	
	private TextField username;
	private TextField email;
	private TextField password;
	private TextField password2;//repeat
	
	private float uiOriginX = 0;
	private float uiOriginY = 0;
	
	public RegisterScreen(SharkTag game){
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
		update(delta);
		
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
		//USERNAME
		Label usernameLabel = new Label("Username", game.getUISkin());
		usernameLabel.setPosition(uiOriginX, uiOriginY + 30, Align.center);
		stage.addActor(usernameLabel);
		username = new TextField("", game.getUISkin());
		username.setPosition(uiOriginX, uiOriginY, Align.center);
		stage.addActor(username);
		
		//EMAIL
		Label emailLabel = new Label("Email", game.getUISkin());
		emailLabel.setPosition(uiOriginX, uiOriginY - 50, Align.center);
		stage.addActor(emailLabel);
		email = new TextField("", game.getUISkin());
		email.setPosition(uiOriginX, uiOriginY - 80, Align.center);
		stage.addActor(email);
		
		//PASSWORD
		Label passwordLabel = new Label("Password", game.getUISkin());
		passwordLabel.setPosition(uiOriginX, uiOriginY - 130, Align.center);
		stage.addActor(passwordLabel);
		password = new TextField("", game.getUISkin());
		password.setPasswordCharacter('*');
		password.setPasswordMode(true);
		password.setPosition(uiOriginX, uiOriginY - 160, Align.center);
		stage.addActor(password);
		
		//REPEAT PASSWORD
		Label password2Label = new Label("Repeat Password", game.getUISkin());
		password2Label.setPosition(uiOriginX, uiOriginY - 210, Align.center);
		stage.addActor(password2Label);
		password2 = new TextField("", game.getUISkin());
		password2.setPasswordCharacter('*');
		password2.setPasswordMode(true);
		password2.setPosition(uiOriginX, uiOriginY - 240, Align.center);
		stage.addActor(password2);
		
		//BUTTONS
		TextButton submit = new TextButton("Register", game.getUISkin());
		submit.setPosition(uiOriginX, uiOriginY - 290, Align.center);
		stage.addActor(submit);
		
		TextButton cancel = new TextButton("Cancel", game.getUISkin());
		cancel.setPosition(uiOriginX, uiOriginY - 340, Align.center);
		stage.addActor(cancel);
		
		submit.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				//TODO register user
				dispose();
			}
		});
		
		cancel.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new LoginScreen(game));
				dispose();
			}
		});
	}
	
	private void update(float delta){
		//Check if the email is a valid email address
		email.setColor(1, 1, 1, 1);
		if(email.getText().equals("")){
			email.setColor(1, 1, 1, 1);
		}
		else if(!email.getText().contains("@")){
			email.setColor(1, 0, 0, 1);
		}

		if(password2.getText().equals("")){
			password2.setColor(1, 1, 1, 1);
		}
		else if(!password.getText().equals(password2.getText())){
			password2.setColor(1, 0, 0, 1);
		}
		else{
			password2.setColor(0, 1, 0, 1);
		}
	}
}
