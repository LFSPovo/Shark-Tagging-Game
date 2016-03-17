package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class HighScoreScreen extends AbstractScreen{
	private Array<String> leaderboard = new Array<String>();
	
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont bitmapFont;
	private GlyphLayout textLayout;
	
	public HighScoreScreen(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		
		batch = game.getBatch();
		shapeRenderer = game.getShapeRenderer();
		bitmapFont = new BitmapFont();
		textLayout = new GlyphLayout();
		
		buildHighscore();
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		clearScreen();
		game.drawBackground(stage);
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.3f);
		shapeRenderer.rect(50, 0,
				game.getWidth() - 100, game.getHeight());
		shapeRenderer.end();
		
		batch.begin();
		
		for(int i = 0; i < leaderboard.size; i += 3){
			
			// Set local coordinates
			float x = 60;
			float y = game.getHeight() - 50 - i * 10;
			
			// Column overflows to the right after 10th position
			if(i > 10 * 3){
				x = game.getWidth() / 2 + 10;
				y = game.getHeight() - 50 - (i - 10 * 3) * 10;
			}
			
			// Position
			textLayout.setText(bitmapFont, leaderboard.get(i));
			bitmapFont.draw(batch, leaderboard.get(i), x, y);
			
			// Number of points
			textLayout.setText(bitmapFont, leaderboard.get(i + 1));
			bitmapFont.draw(batch,
							leaderboard.get(i + 1),
							x + 60 - textLayout.width / 2,
							y);
			
			// Player name
			textLayout.setText(bitmapFont, leaderboard.get(i + 2));
			bitmapFont.draw(batch, leaderboard.get(i + 2), x + 140, y);
			
			// For how to read this array, see buildHighscore()
		}
		batch.end();
		super.render(delta);
	}
	
	private void buildGUI(){
		TextButton backButton = new TextButton("Back", game.getUISkin());
		backButton.setSize(game.getWidth() / 2.2f, 40);
		backButton.setPosition(uiOriginX, 50, Align.center);
		stage.addActor(backButton);
		
		backButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int c, int b){
				super.tap(event, x, y, c, b);
				game.setScreen(new MainMenu(game));
				dispose();
			}
		});
	}
	
	/*
	 * Builds a correctly formatted array of data
	 */
	private void buildHighscore(){
		/*
		 * The array containing the high score table is built in threes
		 * The first element is the position, for example 1st or 15th
		 * The second element is the number of points of that player
		 * And the third element is the name of the player.
		 * 
		 * This means when iterating through the array, you have to increment
		 * the index by 3. And access the position, score and name by
		 * i, i+1 and i+2 respectively.
		 */
		
		// Retrieve the leader board data from the server
		// It will never contain more than 20 entries
		JsonValue table = game.getComm().requestHighscore().get("leaderboard");
		
		int place = 1;
		
		if(table != null){
			// Column headers
			leaderboard.add("Place");
			leaderboard.add("Score");
			leaderboard.add("Player");
			
			for(int i = 0; i < table.size; i++){
				leaderboard.add(Integer.toString(place) + ".");
				leaderboard.add(table.get(i).get("score").asString());
				leaderboard.add(table.get(i).get("username").asString());
				place++;
			}
		}
		else{
			Dialog dialog = new Dialog("Error", game.getUISkin());
			dialog.text("Could not retrieve the leader board");
			dialog.button("OK");
			dialog.show(stage);
		}
	}
}
