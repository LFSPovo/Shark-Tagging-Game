package com.dcu.sharktag;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SharkTag extends Game {
	
	public final float WORLD_WIDTH = 800;
	public final float WORLD_HEIGHT = 600;
	
	private Skin uiSkin;
	
	@Override
	public void create () {
		uiSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		setScreen(new IntroScreen(this));
	}
	
	public Skin getUISkin(){
		return uiSkin;
	}
}
