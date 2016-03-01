package com.dcu.sharktag;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Tag extends SimpleTag{
	
	private boolean active;
	
	public Tag(float x, float y){
		
		super(x, y);

		active = true;
	}
	
	public void update(Vector2 imgSize){
		
	}
	
	public void render(ShapeRenderer shapeRenderer){
		if(active){
			shapeRenderer.setColor(1, 0, 0, 1);
		}
		else{
			shapeRenderer.setColor(1, 0, 0, 0.3f);
		}
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
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
	
	public void setActive(boolean flag){
		active = flag;
	}
	
	public boolean isActive(){
		return active;
	}
}
