package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class ForgotPassScreen extends AbstractScreen {
	
	private TextField username;
	
	private Label codeLabel;
	private TextField code;

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
		clearScreen();
		
		game.drawBackground(stage);
		
		super.render(delta);
	}
	
	private void buildGUI(){
		
		Label usernameLabel = new Label("Username / Email", game.getUISkin());
		usernameLabel.setPosition(uiOriginX, uiOriginY + 30, Align.center);
		username = new TextField("", game.getUISkin());
		stage.addActor(usernameLabel);
		username.setWidth(game.WORLD_WIDTH / 2.2f);
		username.setPosition(uiOriginX, uiOriginY, Align.center);
		stage.addActor(username);
		
		codeLabel = new Label("Verification Code", game.getUISkin());
		codeLabel.setPosition(uiOriginX, uiOriginY - 50, Align.center);
		codeLabel.setVisible(false);
		stage.addActor(codeLabel);
		code = new TextField("", game.getUISkin());
		code.setWidth(game.WORLD_WIDTH / 2.2f);
		code.setPosition(uiOriginX, uiOriginY - 80, Align.center);
		code.setVisible(false);
		stage.addActor(code);
		
		TextButton submit = new TextButton("Submit", game.getUISkin());
		submit.setPosition(uiOriginX, uiOriginY - 130, Align.center);
		stage.addActor(submit);
		
		TextButton cancel = new TextButton("Back", game.getUISkin());
		cancel.setPosition(uiOriginX, uiOriginY - 180, Align.center);
		stage.addActor(cancel);
		
		submit.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				if(((TextButton)actor).isPressed()){
					String result = game.getComm().recoverPassword(username.getText());
					if(result.equals("")){
						//TODO third step verification
						// new text input for code
					}
					else{
						Gdx.input.setOnscreenKeyboardVisible(false);
						Dialog dialog = new Dialog("Error", game.getUISkin());
						dialog.text(result);
						dialog.button("OK");
						dialog.show(stage);
					}
				}
			}
		});
		
		cancel.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor){
				if(((TextButton)actor).isPressed()){
					game.setScreen(new MainMenu(game));
					dispose();
				}
			}
		});
	}
}
