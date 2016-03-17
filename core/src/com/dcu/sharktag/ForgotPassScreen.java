package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

public class ForgotPassScreen extends AbstractScreen {
	
	private Label usernameLabel;
	private TextField username;
	
	private Label codeLabel;
	private TextField code;
	
	private Label passwordLabel;
	private TextField password;
	
	private Label passwordLabel2;
	private TextField password2;

	public ForgotPassScreen(SharkTag game) {
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
		game.drawBackground(stage);
		
		super.render(delta);
	}
	
	private void update(){
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
	
	private void buildGUI(){
		
		usernameLabel = new Label("Username / Email", game.getUISkin());
		usernameLabel.setPosition(uiOriginX, uiOriginY + 30, Align.center);
		username = new TextField("", game.getUISkin());
		stage.addActor(usernameLabel);
		username.setWidth(game.WORLD_WIDTH / 2.2f);
		username.setMaxLength(20);
		username.setPosition(uiOriginX, uiOriginY, Align.center);
		stage.addActor(username);
		
		codeLabel = new Label("Verification Code", game.getUISkin());
		codeLabel.setPosition(uiOriginX, uiOriginY + 30, Align.center);
		codeLabel.setVisible(false);
		stage.addActor(codeLabel);
		code = new TextField("", game.getUISkin());
		code.setWidth(game.WORLD_WIDTH / 2.2f);
		code.setPosition(uiOriginX, uiOriginY, Align.center);
		code.setVisible(false);
		stage.addActor(code);
		
		passwordLabel = new Label("New Password", game.getUISkin());
		passwordLabel.setPosition(uiOriginX, uiOriginY - 50, Align.center);
		passwordLabel.setVisible(false);
		stage.addActor(passwordLabel);
		password = new TextField("", game.getUISkin());
		password.setPasswordMode(true);
		password.setPasswordCharacter('*');
		password.setWidth(game.WORLD_WIDTH / 2.2f);
		password.setPosition(uiOriginX, uiOriginY - 80, Align.center);
		password.setVisible(false);
		stage.addActor(password);
		
		passwordLabel2 = new Label("Repeat Password", game.getUISkin());
		passwordLabel2.setPosition(uiOriginX, uiOriginY - 130, Align.center);
		passwordLabel2.setVisible(false);
		stage.addActor(passwordLabel2);
		password2 = new TextField("", game.getUISkin());
		password2.setPasswordMode(true);
		password2.setPasswordCharacter('*');
		password2.setWidth(game.WORLD_WIDTH / 2.2f);
		password2.setPosition(uiOriginX, uiOriginY - 160, Align.center);
		password2.setVisible(false);
		stage.addActor(password2);
		
		TextButton submit = new TextButton("Request Code", game.getUISkin());
		submit.setSize(game.WORLD_WIDTH / 2.2f, 40);
		submit.setPosition(uiOriginX, uiOriginY - 220, Align.center);
		stage.addActor(submit);
		
		TextButton cancel = new TextButton("Back", game.getUISkin());
		cancel.setSize(game.WORLD_WIDTH / 2.2f, 40);
		cancel.setPosition(uiOriginX, uiOriginY - 270, Align.center);
		stage.addActor(cancel);
		
		submit.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				
				if(code.getText().equals("")){
					ServerResponse result = game.getComm().recoverPassword(username.getText());
					Gdx.input.setOnscreenKeyboardVisible(false);
					
					if(result.getStatus() == 1){
						usernameLabel.setVisible(false);
						username.setVisible(false);
						codeLabel.setVisible(true);
						code.setVisible(true);
						passwordLabel.setVisible(true);
						password.setVisible(true);
						passwordLabel2.setVisible(true);
						password2.setVisible(true);
						((TextButton) event.getListenerActor()).setText("Submit");
					}
					
					Dialog dialog = new Dialog("", game.getUISkin());
					dialog.text(result.getMessage());
					dialog.button("OK");
					dialog.show(stage);
				}
				else{
					ServerResponse result = game.getComm().recoverPasswordChange(
							game.getComm().getTmpString(), password.getText(),
							code.getText());
					
					Dialog dialog = new Dialog("", game.getUISkin());
					dialog.text(result.getMessage());
					dialog.button("OK");
					dialog.show(stage);
				}
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
}
