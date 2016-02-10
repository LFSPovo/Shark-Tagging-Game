package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CreditsScreen extends ScreenAdapter{

	private SharkTag game;
	private Stage stage;
	
	private float uiOriginX = 0;
	private float uiOriginY = 0;
	
	private SpriteBatch batch;
	private Texture credits;
	
	public CreditsScreen(SharkTag game){
		this.game = game;
		
		uiOriginX = game.WORLD_WIDTH / 2;
		uiOriginY = 4.5f * game.WORLD_HEIGHT / 5;
//		Gdx.gl.
	}
	
	@Override
	public void show(){
		stage = new Stage(new FitViewport(game.WORLD_WIDTH, game.WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		
		batch = new SpriteBatch();
		credits = new Texture(Gdx.files.internal("ui/credits2.png"));
		
//		Gdx.gl.glClearColor(1, 0, 0, 1);
		
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		draw();
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
		credits.dispose();
		stage.dispose();
	}
	
	private void buildGUI(){
//		Label programmingHeading = new Label("Programming", game.getUISkin());
//		programmingHeading.setPosition(uiOriginX, uiOriginY, Align.center);
//		stage.addActor(programmingHeading);
		
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
