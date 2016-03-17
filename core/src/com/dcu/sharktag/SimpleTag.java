package com.dcu.sharktag;

import com.badlogic.gdx.math.Vector2;

/*
 * This class gets serialised and sent to the server as a tag
 */
public class SimpleTag {

	protected Vector2 position = new Vector2();
	protected Vector2 size = new Vector2();
	protected int sharkId = 0;
	
	public SimpleTag(float x, float y){
		position = new Vector2(x, y);
		size = new Vector2(50, 50);
	}
	
	public int getSharkId(){
		return sharkId;
	}
	
	public void setSharkId(int id){
		sharkId = id;
	}
	
	public Vector2 getSize(){
		return size;
	}
	
	public void setSize(Vector2 s){
		size = s;
	}
}
