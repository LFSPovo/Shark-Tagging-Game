package com.dcu.sharktag;

import com.badlogic.gdx.math.Vector2;

public class SimpleTag {

	protected Vector2 position = new Vector2();
	protected Vector2 size = new Vector2();
	
	public SimpleTag(float x, float y){
		position.x = x;
		position.y = y;
		size.x = 50;
		size.y = 50;
	}
}
