package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Tag extends SimpleTag implements Comparable{
	
	private boolean active;
	
	private boolean resizing = false;
	private boolean moving = false;
	
	private String text = "";
	private GlyphLayout textLayout;
	
	private Vector2 imgSize;
	private float imgScale;
	
	//where the image ends on the screen
	private float leftBoundary = 0;
	private float rightBoundary = 0;
	private float bottomBoundary = 0;
	
	public Tag(float x, float y, Vector2 imgSize, float imgScale){
		
		super(x, y);

		active = true;
		textLayout = new GlyphLayout();
		this.imgSize = imgSize;
		this.imgScale = imgScale;
		
		leftBoundary = (854 / 2) - (imgSize.x / 2);
		rightBoundary = (854 / 2) + (imgSize.x / 2);
		bottomBoundary = 50;
	}
	
	@Override
	public int compareTo(Object obj) {
		
		if(obj.getClass() != Tag.class){
			Gdx.app.log("debug", obj.getClass().toString());
			return 0;
		}
		
		Tag other = (Tag)obj;
		
		return (int)(getArea() - other.getArea());
	}

	public void update(Vector2 point){
		if(active){
			if(resizing){
				if(point.x < leftBoundary){
					point.x = leftBoundary;
				}
				if(point.x > rightBoundary){
					point.x = rightBoundary;
				}
				
				if(point.y < bottomBoundary){
					point.y = 50;
				}
				if(point.y > 480){
					point.y = 480;
				}
				
				Vector2 tmp = point.sub(position);
				
				if(tmp.x >= 0 && tmp.x <= 50){
					tmp.x = 50;
				}
				if(tmp.x < 0 && tmp.x >= -50){
					tmp.x = -50;
				}
				
				if(tmp.y >= 0 && tmp.y <= 50){
					tmp.y = 50;
				}
				if(tmp.y < 0 && tmp.y >= -50){
					tmp.y = -50;
				}
				
				size = tmp;
			}

			else if(moving){
				
				Vector2 tmp = point;
				
				if(tmp.x < (854 / 2) - (imgSize.x / 2) + Math.abs(Math.min(size.x, 0))){
					tmp.x = (854 / 2) - (imgSize.x / 2) + Math.abs(Math.min(size.x, 0));
				}
				if(tmp.x > (854 / 2) + (imgSize.x / 2) - Math.abs(Math.max(size.x, 0))){
					tmp.x = (854 / 2) + (imgSize.x / 2) - Math.abs(Math.max(size.x, 0));
				}
				
				if(tmp.y < 50 + Math.abs(Math.min(size.y, 0))){
					tmp.y = 50 + Math.abs(Math.min(size.y, 0));
				}
				if(tmp.y > 480 - Math.abs(Math.max(size.y, 0))){
					tmp.y = 480 - Math.abs(Math.max(size.y, 0));
				}
				
				position = tmp;
			}
		}
	}
	
	public void render(ShapeRenderer shapeRenderer){
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		
		if(active){
			if(sharkId > 0){
				shapeRenderer.setColor(0, 1, 0, 1);
			}
			else{
				shapeRenderer.setColor(1, 0, 0, 1);
			}
		}
		else{
			if(sharkId > 0){
				shapeRenderer.setColor(0, 1, 0, 0.6f);
			}
			else{
				shapeRenderer.setColor(1, 0, 0, 0.6f);
			}
		}
		
		shapeRenderer.rect(position.x, position.y,
				size.x, size.y);
		
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		if(active){
			shapeRenderer.setColor(0, 0, 1, 1);	// Debug
			shapeRenderer.circle(position.x, position.y, 5);
			
			shapeRenderer.setColor(0, 1, 0, 1);
			shapeRenderer.circle(position.x + size.x, position.y + size.y, 5);
			
			shapeRenderer.setColor(1, 0, 1, 1);
			shapeRenderer.rect(position.x - 1, position.y - 7, 0, 0, 10f, 10f, 1f, 1f, 45f);
		}
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		shapeRenderer.end();
	}
	
	public void renderText(Batch batch, BitmapFont bitmapFont){
		if(sharkId > 0){
			batch.setColor(1, 1, 1, 1);
			textLayout.setText(bitmapFont, text);
			bitmapFont.draw(batch, text,
					position.x, position.y);
		}
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
	
	public float getArea(){
		return size.x * size.y;
	}
	
	public void setSharkId(int id, String text){
//		sharkId = list.indexOf(text, false);
		super.setSharkId(id);
		this.text = text;
	}
	
	public SimpleTag toSimpleTag(){
		SimpleTag t = new SimpleTag(0, 0);
		t.position = getOriginalPosition(this.position);
		t.size = getOriginalSize(this.size);
		t.sharkId = this.sharkId;
		
		// Convert so that position is in the top-left corner of the tag
		// and size is always positive going bottom-right
		if(t.size.y > 0){
			t.position.y += t.size.y;
		}
		else{
			t.size.y *= -1;	// change to positive
		}
		
		if(t.size.x < 0){
			t.position.x += t.size.x;
			t.size.x *= -1;
		}
		t.position.y = imgSize.y * imgScale - t.position.y;	// invert Y axis
		return t;
	}
	
	private float distance(Vector2 point1, Vector2 point2){
		//sqrt((x1 - x2) ^ 2 + (y1 - y2) ^ 2)
		return (float)Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
	}
	
	// Convert the position and size to match the original scale of the picture
	private Vector2 getOriginalPosition(Vector2 point){
		Vector2 tmp = new Vector2(point);
		tmp.x = (tmp.x - leftBoundary) * imgScale;
		tmp.y = (tmp.y - bottomBoundary) * imgScale;
		
		return tmp;
	}
	
	private Vector2 getOriginalSize(Vector2 point){
		Vector2 tmp = new Vector2(size);
		tmp.x = tmp.x * imgScale;
		tmp.y = tmp.y * imgScale;
		
		return tmp;
	}
}
