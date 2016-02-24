package com.dcu.sharktag;

import java.io.BufferedReader;
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
}