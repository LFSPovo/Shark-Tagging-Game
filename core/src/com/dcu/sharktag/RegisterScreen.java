package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

public class RegisterScreen extends AbstractScreen{
	
	private SpriteBatch batch;
	private Texture backgroundImage;
	
	private TextField username;
	private TextField email;
	private TextField password;
	private TextField password2;//repeat
	
	public RegisterScreen(SharkTag game){
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
		update(delta);
		
		clearScreen();
		
		batch.setProjectionMatrix(stage.getCamera().projection);
		batch.setTransformMatrix(stage.getCamera().view);
		batch.begin();
		batch.draw(backgroundImage, 0, 0, game.getWidth(), game.getHeight());
		batch.end();
		
		super.render(delta);
	}
	
	@Override
	public void dispose(){
		super.dispose();
		backgroundImage.dispose();
	}
	
	private void buildGUI(){
		//USERNAME
		Label usernameLabel = new Label("Username", game.getUISkin());
		usernameLabel.setPosition(game.getWidth() / 4,
									uiOriginY + 30, Align.center);
		stage.addActor(usernameLabel);
		username = new TextField("", game.getUISkin());
		username.setWidth(game.getWidth() / 2.2f);
		username.setMaxLength(20);
		username.setPosition(game.getWidth() / 4, uiOriginY, Align.center);
		stage.addActor(username);
		
		//EMAIL
		Label emailLabel = new Label("Email", game.getUISkin());
		emailLabel.setPosition(game.getWidth() / 4 * 3,
								uiOriginY + 30, Align.center);
		stage.addActor(emailLabel);
		email = new TextField("", game.getUISkin());
		email.setWidth(game.getWidth() / 2.2f);
		email.setPosition(game.getWidth() / 4 * 3, uiOriginY, Align.center);
		stage.addActor(email);
		
		//PASSWORD
		Label passwordLabel = new Label("Password", game.getUISkin());
		passwordLabel.setPosition(game.getWidth() / 4,
									uiOriginY - 50, Align.center);
		stage.addActor(passwordLabel);
		password = new TextField("", game.getUISkin());
		password.setPasswordCharacter('*');
		password.setPasswordMode(true);
		password.setWidth(game.getWidth() / 2.2f);
		password.setPosition(game.getWidth() / 4, uiOriginY - 80, Align.center);
		stage.addActor(password);
		
		//REPEAT PASSWORD
		Label password2Label = new Label("Repeat Password", game.getUISkin());
		password2Label.setPosition(game.getWidth() / 4 * 3,
									uiOriginY - 50, Align.center);
		stage.addActor(password2Label);
		password2 = new TextField("", game.getUISkin());
		password2.setPasswordCharacter('*');
		password2.setPasswordMode(true);
		password2.setWidth(game.getWidth() / 2.2f);
		password2.setPosition(game.getWidth() / 4 * 3,
								uiOriginY - 80, Align.center);
		stage.addActor(password2);
		
		//BUTTONS
		TextButton submit = new TextButton("Register", game.getUISkin());
		submit.setSize(game.getWidth() / 2.2f, 40);
		submit.setPosition(uiOriginX, uiOriginY - 140, Align.center);
		stage.addActor(submit);
		
		TextButton cancel = new TextButton("Cancel", game.getUISkin());
		cancel.setSize(game.getWidth() / 2.2f, 40);
		cancel.setPosition(uiOriginX, uiOriginY - 190, Align.center);
		stage.addActor(cancel);
		
		submit.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int c, int b){
				super.tap(event, x, y, c, b);
				ServerResponse result = register();
				if(result.getStatus() == 1){
					game.setScreen(new LoginScreen(game, result.getMessage()));
					dispose();
				}
			}
		});
		
		cancel.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int c, int b){
				super.tap(event, x, y, c, b);
				game.setScreen(new LoginScreen(game, null));
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

		// Check if password is typed correctly
		if(password2.getText().equals("")){
			password2.setColor(1, 1, 1, 1);
		}
		else if(!password.getText().equals(password2.getText())){
			password2.setColor(1, 0, 0, 1);
		}
		else{
			password2.setColor(0, 1, 0, 1);
		}
		
		// Do not allow @ in the username
		username.setColor(1, 1, 1, 1);
		if(username.getText().contains("@")){
			username.setColor(1, 0, 0, 1);
		}
	}
	
	private ServerResponse register(){
		if(password.getText().equals(password2.getText()) &&
				email.getText().contains("@") &&
				!username.getText().contains("@")){
		
			ServerResponse status = game.getComm().register(
					username.getText(), email.getText(), password.getText());
			
			if(status.getStatus() != 1){
				Dialog dialog = new Dialog("Error", game.getUISkin());
				dialog.text(status.getMessage());
				dialog.button("OK");
				dialog.show(stage);
			}
			
			return status;
		}
		else{
			
			Dialog dialog = new Dialog("Error", game.getUISkin());
			dialog.text("Some fields are filled incorrectly");
			dialog.button("OK");
			dialog.getContentTable().setHeight(300);
			dialog.show(stage);

			return new ServerResponse(-1, "");
		}
	}
}
