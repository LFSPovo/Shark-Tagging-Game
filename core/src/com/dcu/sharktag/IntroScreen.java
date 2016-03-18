package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class IntroScreen extends ScreenAdapter{
	
	private SharkTag game;
	
	private Viewport viewport;
	private Camera camera;

	private SpriteBatch batch = new SpriteBatch();
	private Texture logo;
	
	private float fadein = 0;
	
	public IntroScreen(SharkTag game){
		this.game = game;
	}
	
	@Override
	public void show(){
		// This screen does not use AbstractScreen, so we need to set the
		// camera and viewport ourselves
		camera = new OrthographicCamera();
		camera.position.set(game.getWidth() / 2, game.getHeight() / 2, 0);
		camera.update();
		
		viewport = new FitViewport(game.getWidth(), game.getHeight(), camera);
		
		logo = new Texture(Gdx.files.internal("libgdx-logo.png"));
	}
	
	@Override
	public void render(float delta){
		update(delta);
		draw();
	}
	
	@Override
	public void resize(int width, int height){
		viewport.update(width, height);
	}
	
	@Override
	public void dispose(){
		logo.dispose();
	}
	
	private void draw(){
		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);
	
		clearScreen();
		
		batch.begin();
		batch.setColor(1, 1, 1, MathUtils.clamp(fadein, 0, 1));
		batch.draw(logo, game.getWidth() / 2 - logo.getWidth() / 2,
				game.getHeight() / 2 - logo.getHeight() / 2);
		batch.end();
	}
	
	private void clearScreen(){
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	private void update(float delta){
		// delta is frame rate agnostic
		fadein += delta / 2;
		
		if(fadein > 1.5 ||
			Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) ||
			Gdx.input.isTouched()){
			
			clearScreen();
			game.setScreen(new LoginScreen(game, null));
			dispose();
		}
	}
}
