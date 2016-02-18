package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

public class CreditsScreen extends AbstractScreen{
	
	private SpriteBatch batch;
	private Texture credits;
	
	public CreditsScreen(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		
		batch = new SpriteBatch();
		credits = new Texture(Gdx.files.internal("ui/credits.png"));
		
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		clearScreen();
		draw();
		super.render(delta);
	}
	
	@Override
	public void dispose(){
		credits.dispose();
		super.dispose();
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
	
	private void draw(){
		batch.setProjectionMatrix(stage.getCamera().projection);
		batch.setTransformMatrix(stage.getCamera().view);
		
		batch.begin();
		batch.draw(credits, 0, 0);
		batch.end();
	}
}
