package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class ForgotPassScreen extends AbstractScreen {
	
	private TextField username;

	public ForgotPassScreen(SharkTag game) {
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		
		buildGUI();
	}
	
	private void buildGUI(){
		
		username = new TextField("Username / Email", game.getUISkin());
		username.setPosition(uiOriginX, uiOriginY, Align.center);
		stage.addActor(username);
		
		TextButton submit = new TextButton("Submit", game.getUISkin());
		submit.setPosition(uiOriginX, uiOriginY - 100, Align.center);
		stage.addActor(submit);
		
		TextButton cancel = new TextButton("Back", game.getUISkin());
		cancel.setPosition(uiOriginX, uiOriginY - 150, Align.center);
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
