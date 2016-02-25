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

import sun.misc.IOUtils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Communication {
	
	private String serverURL = "http://povilas.ovh:8080";
	private String charset = "UTF-8";
	
	private String sessionToken = "";
	
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
		
		try{
			String query = String.format("username=%s&password=%s",
					URLEncoder.encode(username, charset),
					URLEncoder.encode(password, charset));
			
			URLConnection connection = new URL(serverURL + "/login").openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=" + charset);
			
			OutputStream output = connection.getOutputStream();
			output.write(query.getBytes(charset));
			
			InputStream response = connection.getInputStream();
			String line = new BufferedReader(new InputStreamReader(response)).readLine();
			
			JsonValue value = new JsonReader().parse(line);
			int serverResponse = value.get("success").asInt();
			String serverMessage = value.get("message").asString();
			sessionToken = value.get("token").asString();
			
			if(serverResponse == 1){
				status = "";
			}
			else{
				status = serverMessage;
			}
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
			status = "Unsupported encoding!";
		}
		catch(MalformedURLException e){
			e.printStackTrace();
			status = "Malformed URL!";
		}
		catch(ConnectException e){
			e.printStackTrace();
			status = "Server is unreachable!";
		}
		catch(IOException e){
			e.printStackTrace();
			status = "IO exception!";
		}
		
		return status;
	}
	
	public String register(String username, String email, String password){
		
		String status = "ERROR";
		
		try{
			String query = String.format("username=%s&email=%s&password=%s",
					URLEncoder.encode(username, charset),
					URLEncoder.encode(email, charset),
					URLEncoder.encode(password, charset));
			
			URLConnection connection = new URL(serverURL + "/register").openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=" + charset);
			
			OutputStream output = connection.getOutputStream();
			output.write(query.getBytes(charset));
			
			InputStream response = connection.getInputStream();
			String line = new BufferedReader(new InputStreamReader(response)).readLine();
			
			JsonValue value = new JsonReader().parse(line);
			int serverResponse = value.get("success").asInt();
			String serverMessage = value.get("message").asString();
			
			if(serverResponse == 1){
				status = "";
			}
			else{
				status = serverMessage;
			}
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
			status = "Unsupported encoding!";
		}
		catch(MalformedURLException e){
			e.printStackTrace();
			status = "Malformed URL!";
		}
		catch(ConnectException e){
			e.printStackTrace();
			status = "Server is unreachable!";
		}
		catch(IOException e){
			e.printStackTrace();
			status = "IO exception!";
		}
		
		return status;
	}
	
	public Texture requestImage(){
		
		Texture bucket;
		String imageData = "";
		
		try{
			String query = "";
//			String query = String.format("username=%s&email=%s&password=%s",
//					URLEncoder.encode(username, charset),
//					URLEncoder.encode(email, charset),
//					URLEncoder.encode(password, charset));
			
			URLConnection connection = new URL(serverURL + "/getimage").openConnection();
			connection.setDoOutput(false);
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=" + charset);
			
			OutputStream output = connection.getOutputStream();
			output.write(query.getBytes(charset));
			
			InputStream response = connection.getInputStream();
			String line = new BufferedReader(new InputStreamReader(response)).readLine();
			
			JsonValue value = new JsonReader().parse(line);
//			int serverResponse = value.get("success").asInt();
//			String serverMessage = value.get("message").asString();
			int imageID = value.get("id").asInt();
			imageData = value.get("image").asString();
			
//			if(serverResponse == 1){
//				status = "";
//			}
//			else{
//				status = serverMessage;
//			}
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
//			status = "Unsupported encoding!";
		}
		catch(MalformedURLException e){
			e.printStackTrace();
//			status = "Malformed URL!";
		}
		catch(ConnectException e){
			e.printStackTrace();
//			status = "Server is unreachable!";
		}
		catch(IOException e){
			e.printStackTrace();
//			status = "IO exception!";
		}
		
		byte[] decodedBytes = Base64Coder.decode(imageData);
		bucket = new Texture(new Pixmap(decodedBytes, 0, decodedBytes.length));
		
		return bucket;
	}
	
public Texture fetchImage(){
		
		Texture bucket;
		byte[] imageData = new byte[0];
		
		try{
			String query = "";
			
			URLConnection connection = new URL(serverURL + "/getimage").openConnection();
			connection.setDoOutput(false);
			
			InputStream response = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			
			int nRead;
			byte[] data = new byte[16384];
			
			while((nRead = response.read(data, 0, data.length)) != -1){
				buffer.write(data, 0, nRead);
			}
			
			buffer.flush();
			imageData = buffer.toByteArray();
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
//			status = "Unsupported encoding!";
		}
		catch(MalformedURLException e){
			e.printStackTrace();
//			status = "Malformed URL!";
		}
		catch(ConnectException e){
			e.printStackTrace();
//			status = "Server is unreachable!";
		}
		catch(IOException e){
			e.printStackTrace();
//			status = "IO exception!";
		}
		
//		byte[] decodedBytes = Base64Coder.decode(imageData);
		Pixmap pixMap = new Pixmap(imageData, 0, imageData.length);
		bucket = new Texture(pixMap);
		pixMap.dispose();
		
		return bucket;
	}
}