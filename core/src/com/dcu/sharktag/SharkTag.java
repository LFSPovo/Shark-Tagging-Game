package com.dcu.sharktag;

import com.badlogic.gdx.Game;

public class SharkTag extends Game {
	
	public final float WORLD_WIDTH = 800;
	public final float WORLD_HEIGHT = 600;
	
	@Override
	public void create () {
		setScreen(new IntroScreen(this));
	}
}
