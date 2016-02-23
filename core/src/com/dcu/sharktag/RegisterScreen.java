package com.dcu.sharktag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import sun.org.mozilla.javascript.json.JsonParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class RegisterScreen extends AbstractScreen{
	
	private TextField username;
	private TextField email;
	private TextField password;
	private TextField password2;//repeat
	
	public RegisterScreen(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		update(delta);
		
		clearScreen();
		super.render(delta);
	}
	
	private void buildGUI(){
		//USERNAME
		Label usernameLabel = new Label("Username", game.getUISkin());
		usernameLabel.setPosition(uiOriginX, uiOriginY + 30, Align.center);
		stage.addActor(usernameLabel);
		username = new TextField("", game.getUISkin());
		username.setPosition(uiOriginX, uiOriginY, Align.center);
		stage.addActor(username);
		
		//EMAIL
		Label emailLabel = new Label("Email", game.getUISkin());
		emailLabel.setPosition(uiOriginX, uiOriginY - 50, Align.center);
		stage.addActor(emailLabel);
		email = new TextField("", game.getUISkin());
		email.setPosition(uiOriginX, uiOriginY - 80, Align.center);
		stage.addActor(email);
		
		//PASSWORD
		Label passwordLabel = new Label("Password", game.getUISkin());
		passwordLabel.setPosition(uiOriginX, uiOriginY - 130, Align.center);
		stage.addActor(passwordLabel);
		password = new TextField("", game.getUISkin());
		password.setPasswordCharacter('*');
		password.setPasswordMode(true);
		password.setPosition(uiOriginX, uiOriginY - 160, Align.center);
		stage.addActor(password);
		
		//REPEAT PASSWORD
		Label password2Label = new Label("Repeat Password", game.getUISkin());
		password2Label.setPosition(uiOriginX, uiOriginY - 210, Align.center);
		stage.addActor(password2Label);
		password2 = new TextField("", game.getUISkin());
		password2.setPasswordCharacter('*');
		password2.setPasswordMode(true);
		password2.setPosition(uiOriginX, uiOriginY - 240, Align.center);
		stage.addActor(password2);
		
		//BUTTONS
		TextButton submit = new TextButton("Register", game.getUISkin());
		submit.setPosition(uiOriginX, uiOriginY - 290, Align.center);
		stage.addActor(submit);
		
		TextButton cancel = new TextButton("Cancel", game.getUISkin());
		cancel.setPosition(uiOriginX, uiOriginY - 340, Align.center);
		stage.addActor(cancel);
		
		submit.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				if(register()){
					game.setScreen(new LoginScreen(game));
					dispose();
				}
			}
		});
		
		cancel.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new LoginScreen(game));
				dispose();
			}
		});
	}
	
	private void update(float delta){
		//Check if the email is a valid email address
		email.setColor(1, 1, 1, 1);
		if(email.getText().equals("")){
			email.setColor(1, 1, 1, 1);
		}
		else if(!email.getText().contains("@")){
			email.setColor(1, 0, 0, 1);
		}

		if(password2.getText().equals("")){
			password2.setColor(1, 1, 1, 1);
		}
		else if(!password.getText().equals(password2.getText())){
			password2.setColor(1, 0, 0, 1);
		}
		else{
			password2.setColor(0, 1, 0, 1);
		}
	}
	
	private boolean register(){
		String serverURL = "http://povilas.ovh:8080/register";
		String charSet = "UTF-8";
		String query = "";
		
		if(password.getText().equals(password2.getText()) &&
				email.getText().contains("@")){
		
			try{
				query = String.format("username=%s&email=%s&password=%s",
						URLEncoder.encode(username.getText(), charSet),
						URLEncoder.encode(email.getText(), charSet),
						URLEncoder.encode(password.getText(), charSet));
			
				URLConnection connection = new URL(serverURL).openConnection();
				connection.setDoOutput(true);
				
				connection.setRequestProperty("Accept-Charset", charSet);
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded;charset=" + charSet);
				
				OutputStream output = connection.getOutputStream();
				output.write(query.getBytes(charSet));
				
				InputStream response = connection.getInputStream();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(response));
				
				String line = reader.readLine();
				
				Json json = new Json();
				JsonValue value = new JsonReader().parse(line);
				
				int serverResponse = value.get("success").asInt();
				String serverMessage = value.get("message").asString();
				
				Gdx.app.log("debug", "" + serverResponse);
				Gdx.app.log("debug", serverMessage);

				if(serverResponse == 1){
					return true;
				}
				else{
					Dialog dialog = new Dialog("Error", game.getUISkin());
					dialog.text(serverMessage);
					dialog.button("OK");
					dialog.show(stage);
					
					return false;
				}
			}
			catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
			catch(MalformedURLException e){
				e.printStackTrace();
			}
			catch(IOException e){
				e.printStackTrace();
			}
			
			return false;
		}
		else{
			Gdx.app.log("debug", "Some fields are filled incorrectly");
			
			Dialog dialog = new Dialog("Error", game.getUISkin());
			dialog.text("Some field are filled incorrectly");
			dialog.button("OK");
			dialog.getContentTable().setHeight(300);
			dialog.show(stage);
			
			return false;
		}
	}
}
