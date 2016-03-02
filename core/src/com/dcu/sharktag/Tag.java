package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Tag extends SimpleTag{
	
	private boolean active;
	
	private boolean resizing = false;
	private boolean moving = false;
	
	public Tag(float x, float y){
		
		super(x, y);

		active = true;
	}
	
	public void update(Vector2 imgSize, Vector2 point){
		if(active){
			if(resizing){
				size = point.sub(position);
			}
//			Gdx.app.log("debug", position.toString());
			else if(moving){	
				position = point;
			}
		}
	}
	
	public void render(ShapeRenderer shapeRenderer){
		
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		
		if(active){
			shapeRenderer.setColor(1, 0, 0, 1);
		}
		else{
			shapeRenderer.setColor(1, 0, 0, 0.6f);
		}
		
		shapeRenderer.rect(position.x, position.y,
				size.x, size.y);
		
		if(active){
			shapeRenderer.setColor(0, 0, 1, 1);	// Debug
			shapeRenderer.circle(position.x, position.y, 5);
			
			shapeRenderer.setColor(0, 1, 0, 1);
			shapeRenderer.circle(position.x + size.x, position.y + size.y, 5);
			
			shapeRenderer.setColor(1, 0, 1, 1);
			shapeRenderer.rect(position.x - 5, position.y - 5, 10, 10);
		}
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
	}
	
	public boolean contains(Vector2 point){
		
		float x, y, w, h;
		
		if(size.x < 0){
			x = position.x + size.x;
		}
		else{
			x = position.x;
		}
		
		if(size.y < 0){
			y = position.y + size.y;
		}
		else{
			y = position.y;
		}
		
		w = Math.abs(size.x);
		h = Math.abs(size.y);
		
		return (point.x >= x) && (point.x <= x + w) &&
				(point.y >= y) && (point.y <= y + h);
	}
	
	public void grabHandles(Vector2 point){
		Vector2 tmp = new Vector2(position);
		if(distance(tmp.add(size), point) < 25 && !moving){
			resizing = true;
		}
		
		if(distance(position, point) < 25 && !resizing){
			moving = true;
		}
	}
	
	public void releaseHandles(){
		moving = false;
		resizing = false;
	}
	
	public void setActive(boolean flag){
		active = flag;
	}
	
	public boolean isActive(){
		return active;
	}
	
	private float distance(Vector2 point1, Vector2 point2){
		//sqrt((x1 - x2) ^ 2 + (y1 - y2) ^ 2)
		return (float)Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
	}
}
