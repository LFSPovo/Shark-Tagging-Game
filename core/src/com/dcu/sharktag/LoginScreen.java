package com.dcu.sharktag;

import java.awt.image.ImagingOpException;
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class LoginScreen extends AbstractScreen{
	
	private TextField loginName;
	private TextField loginPassword;
	
	private boolean nameWasBlank = false;
	private boolean passwordWasBlank = false;

	public LoginScreen(SharkTag game){
		super(game);
	}
	
	@Override
	public void show(){
		super.show();
		buildGUI();
	}
	
	@Override
	public void render(float delta){
		update();
		
		clearScreen();
		super.render(delta);
	}
	
	private void update(){
		if(nameWasBlank == true && !loginName.getText().equals("")){
			loginName.setColor(1, 1, 1, 1);
			nameWasBlank = false;
		}
		
		if(passwordWasBlank == true && !loginPassword.getText().equals("")){
			loginPassword.setColor(1, 1, 1, 1);
			passwordWasBlank = false;
		}
	}
	
	private void buildGUI(){
		Label usernameLabel = new Label("Username", game.getUISkin());
		usernameLabel.setPosition(uiOriginX, uiOriginY + 30, Align.center);
		stage.addActor(usernameLabel);
		loginName = new TextField("", game.getUISkin());
		loginName.setPosition(uiOriginX, uiOriginY, Align.center);
		stage.addActor(loginName);
		
		Label passwordLabel = new Label("Password", game.getUISkin());
		passwordLabel.setPosition(uiOriginX, uiOriginY - 50, Align.center);
		stage.addActor(passwordLabel);
		loginPassword = new TextField("", game.getUISkin());
		loginPassword.setPasswordMode(true);
		loginPassword.setPasswordCharacter('*');
		loginPassword.setPosition(uiOriginX, uiOriginY - 80, Align.center);
		stage.addActor(loginPassword);
		
		TextButton loginButton = new TextButton("Login", game.getUISkin());
		loginButton.setPosition(uiOriginX, uiOriginY - 130, Align.center);
		stage.addActor(loginButton);
		
		TextButton loginRegister = new TextButton("Register", game.getUISkin());
		loginRegister.setPosition(uiOriginX, uiOriginY - 180, Align.center);
		stage.addActor(loginRegister);
		
		loginButton.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				
				if(loginUser(loginName.getText(), loginPassword.getText())){
					game.setScreen(new MainMenu(game));
					dispose();
				}
				else{
					//TODO error message
				}
			}
		});
		
		loginRegister.addListener(new ActorGestureListener(){
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button){
				super.tap(event, x, y, count, button);
				game.setScreen(new RegisterScreen(game));
				dispose();
			}
		});
	}
	
	private boolean loginUser(String name, String password){
		String serverURL = "http://povilas.ovh:8080/login";
		String charSet = "UTF-8";
		String query = "";
		
		if(!loginName.getText().equals("") && !loginPassword.getText().equals("")){
			
			try{
				query = String.format("username=%s&password=%s",
						URLEncoder.encode(loginName.getText(), charSet),
						URLEncoder.encode(loginPassword.getText(), charSet));
				
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
				String sessionToken = value.get("token").asString();
				
				Gdx.app.log("debug", "" + serverResponse);
				Gdx.app.log("debug", serverMessage);
				Gdx.app.log("debug", sessionToken);
				
				if(serverResponse == 1){
					return true;
				}
				else{
					loginName.setColor(1, 0, 0, 1);
//					loginName.setText("");
					nameWasBlank = true;
					
					loginPassword.setColor(1, 0, 0, 1);
					loginPassword.setText("");
					passwordWasBlank = true;
					
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
		}
		Gdx.app.log("debug", "Some fields are empty");
		
		if(loginName.getText().equals("")){
			loginName.setColor(1, 0, 0, 1);
			nameWasBlank = true;
		}
		
		if(loginPassword.getText().equals("")){
			loginPassword.setColor(1, 0, 0, 1);
			passwordWasBlank = true;
		}
		
		Dialog dialog = new Dialog("Error", game.getUISkin());
		dialog.text("Some of the fields are empty");
		dialog.button("OK");
		dialog.show(stage);
		
		return false;
	}
}
