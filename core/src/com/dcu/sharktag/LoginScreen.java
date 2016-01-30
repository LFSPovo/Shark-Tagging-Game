package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LoginScreen extends ScreenAdapter{
	
	private SharkTag game;
	private Stage stage;
	private Skin uiSkin;

	public LoginScreen(SharkTag game){
		this.game = game;
	}
	
	@Override
	public void show(){
		stage = new Stage(new FitViewport(game.WORLD_WIDTH, game.WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		uiSkin = new Skin(Gdx.files.internal("../android/assets/ui/uiskin.json"));
		
		TextField loginName = new TextField("username", uiSkin);
		loginName.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5, Align.center);
		stage.addActor(loginName);
		
		TextField loginPassword = new TextField("", uiSkin);
		loginPassword.setPasswordMode(true);
		loginPassword.setPasswordCharacter('*');
		System.out.println(loginPassword.isPasswordMode());
		loginPassword.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5 - 50, Align.center);
		stage.addActor(loginPassword);
		
		TextButton loginButton = new TextButton("Login", uiSkin);
		loginButton.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5 - 100, Align.center);
		stage.addActor(loginButton);
		
		TextButton loginRegister = new TextButton("Register", uiSkin);
		loginRegister.setPosition(game.WORLD_WIDTH / 2, 4 * game.WORLD_HEIGHT / 5 - 150, Align.center);
		stage.addActor(loginRegister);
	}
	
	@Override
	public void render(float delta){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}
	
	@Override
	public void dispose(){
		stage.dispose();
	}
}
