package com.dcu.sharktag;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

public class HighScoreScreen extends AbstractScreen{
	
	public HighScoreScreen(SharkTag game){
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
		TextButton backButton = new TextButton("Back", game.getUISkin());
		backButton.setPosition(uiOriginX, 50, Align.center);
		stage.addActor(backButton);
		
		backButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new MainMenu(game));
				dispose();
			}
		});
	}
}
