package com.dcu.sharktag;

import com.badlogic.gdx.math.Vector2;

public class SimpleTag {

	protected Vector2 position;
	protected Vector2 size;
	
	public SimpleTag(float x, float y){
		position = new Vector2(x, y);
		size = new Vector2(50, 50);
	}
}
