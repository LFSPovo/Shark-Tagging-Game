package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

public class CreditsScreen extends AbstractScreen{
	
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont bitmapFont;
	private GlyphLayout layout;
	
	public CreditsScreen(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		batch = game.getBatch();
		shapeRenderer = game.getShapeRenderer();
		bitmapFont = new BitmapFont();
		layout = new GlyphLayout();
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
		super.dispose();
	}
	
	private void buildGUI(){
		TextButton backButton = new TextButton("Back", game.getUISkin());
		backButton.setSize(game.getWidth() / 2.2f, 40);
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
		game.drawBackground(stage);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.3f);
		shapeRenderer.rect(50, 0,
				game.getWidth() - 100, game.getHeight());
		shapeRenderer.end();
		
		game.getBatch().begin();
		// Development
		layout.setText(bitmapFont, "Povilas Auskalnis");
		bitmapFont.draw(batch, "Povilas Auskalnis", uiOriginX - layout.width / 2, uiOriginY);
		layout.setText(bitmapFont, "Jerzy Baran");
		bitmapFont.draw(batch, "Jerzy Baran", uiOriginX - layout.width / 2, uiOriginY - 50);
		
		// Supervisor
		layout.setText(bitmapFont, "Brian Stone");
		bitmapFont.draw(batch, "Brian Stone", uiOriginX - layout.width / 2, 100);
		batch.end();
	}
}
