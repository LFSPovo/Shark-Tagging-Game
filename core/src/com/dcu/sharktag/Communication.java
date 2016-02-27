package com.dcu.sharktag;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import sun.misc.IOUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Communication {
	
	private String serverURL = "http://povilas.ovh:8080";
	private String charset = "UTF-8";
	
	private String sessionToken = "";
	
	public String jsonValue = "";
	
	public void setServerURL(String url){
		serverURL = url;
	}
	
	public String getServerURL(){
		return serverURL;
	}
	
	public String getSessionToken(){
		return sessionToken;
	}
	
	public String logIn(String username, String password){
		
		String status = "ERROR";
		
		HttpRequest request = new HttpRequest(HttpMethods.POST);
		request.setUrl(serverURL + "/login");
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		String query = HttpParametersUtils.convertHttpParameters(params);
		request.setContent(query);
		
		MyHttpResponseListener customListener = new MyHttpResponseListener();
		
		Gdx.net.sendHttpRequest(request, customListener);
		
		while(!customListener.isResponseReceived());
		
		int serverResponse = customListener.getStatus();
		String serverMessage = customListener.getMessage();
		
		if(serverResponse == 1){
			status = "";
		}
		else{
			status = serverMessage;
		}
		
		return status;
	}
	
	public String register(String username, String email, String password){
		
		String status = "ERROR";
		
		HttpRequest request = new HttpRequest(HttpMethods.POST);
		request.setUrl(serverURL + "/register");
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("email", email);
		params.put("password", password);
		String query = HttpParametersUtils.convertHttpParameters(params);
		request.setContent(query);
		
		MyHttpResponseListener customListener = new MyHttpResponseListener();
		
		Gdx.net.sendHttpRequest(request, customListener);
		
		while(!customListener.isResponseReceived());
		
		int serverResponse = customListener.getStatus();
		String serverMessage = customListener.getMessage();
		
		if(serverResponse == 1){
			status = "";
		}
		else{
			status = serverMessage;
		}
		
		return status;
	}
	
//	public Texture requestImage(){
		
//		Texture bucket;
//		String imageData = "";
//		
//		try{
//			String query = "";
////			String query = String.format("username=%s&email=%s&password=%s",
////					URLEncoder.encode(username, charset),
////					URLEncoder.encode(email, charset),
////					URLEncoder.encode(password, charset));
//			
//			URLConnection connection = new URL(serverURL + "/getimage").openConnection();
//			connection.setDoOutput(false);
//			connection.setRequestProperty("Accept-Charset", charset);
//			connection.setRequestProperty("Content-Type",
//					"application/x-www-form-urlencoded;charset=" + charset);
//			
//			OutputStream output = connection.getOutputStream();
//			output.write(query.getBytes(charset));
//			
//			InputStream response = connection.getInputStream();
//			String line = new BufferedReader(new InputStreamReader(response)).readLine();
//			
//			JsonValue value = new JsonReader().parse(line);
////			int serverResponse = value.get("success").asInt();
////			String serverMessage = value.get("message").asString();
//			int imageID = value.get("id").asInt();
//			imageData = value.get("image").asString();
//			
////			if(serverResponse == 1){
////				status = "";
////			}
////			else{
////				status = serverMessage;
////			}
//		}
//		catch(UnsupportedEncodingException e){
//			e.printStackTrace();
////			status = "Unsupported encoding!";
//		}
//		catch(MalformedURLException e){
//			e.printStackTrace();
////			status = "Malformed URL!";
//		}
//		catch(ConnectException e){
//			e.printStackTrace();
////			status = "Server is unreachable!";
//		}
//		catch(IOException e){
//			e.printStackTrace();
////			status = "IO exception!";
//		}
//		
//		byte[] decodedBytes = Base64Coder.decode(imageData);
//		bucket = new Texture(new Pixmap(decodedBytes, 0, decodedBytes.length));
//		
//		return bucket;
//	}
	
public Texture fetchImage(){
		
		Texture bucket = null;
		byte[] imageData;
		
		HttpRequest request = new HttpRequest(HttpMethods.GET);
		request.setUrl(serverURL + "/getimage");
		request.setContent(null);
		
		MyHttpResponseListener customListener = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, customListener);
		
		while(!customListener.isResponseReceived()){
//			Gdx.app.log("debug", "WAITING");
//			
//			try{
//				wait();
//			}
//			catch(InterruptedException e){
//				
//			}
		}
		
		imageData = customListener.getData();
		
		Gdx.app.log("debug", Integer.toString(imageData.length));

		Pixmap pixMap = new Pixmap(imageData, 0, imageData.length);
//		try{
//			Pixmap pixMap = new Pixmap(new Gdx2DPixmap(imageData, 0, imageData.length, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888));
		bucket = new Texture(pixMap);
		pixMap.dispose();
//		}
//		catch(IOException e){
			
//		}

		return bucket;
	}
}