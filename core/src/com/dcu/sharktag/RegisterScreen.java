package com.dcu.sharktag;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class RegisterScreen extends ScreenAdapter{

	private SharkTag game;
	
	private Stage stage;
	private Skin uiSkin;
	
	private TextField username;
	private TextField email;
	private TextField email2;	//repeat
	private TextField password;
	private TextField password2;//repeat
	
	public RegisterScreen(SharkTag game){
		this.game = game;
	}
	
	@Override
	public void show(){
		
	}
	
	@Override
	public void render(float delta){
		
	}
	
	@Override
	public void dispose(){
		
	}
}
