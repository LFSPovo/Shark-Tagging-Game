package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.dcu.sharktag.ServerRequests.ImageRequest;
import com.dcu.sharktag.ServerRequests.LoginRequest;
import com.dcu.sharktag.ServerRequests.RegisterRequest;
import com.dcu.sharktag.ServerRequests.ServerRequestBuilder;
import com.dcu.sharktag.ServerRequests.TagRequest;

public class Communication {
	
	private String serverURL = "http://povilas.ovh:8080";

	private String sessionToken = "";
	
	public String jsonValue = "";
	
	private String imageId = "";
	
	public void setServerURL(String url){
		serverURL = url;
	}
	
	public String getServerURL(){
		return serverURL;
	}
	
	public String getSessionToken(){
		return sessionToken;
	}
	
	// Builds a HttpRequest object from a route and object data
	private HttpRequest buildRequest(String route, Object data) {
		ServerRequestBuilder reqBuilder = new ServerRequestBuilder();
		reqBuilder.newRequest();
		reqBuilder.url(serverURL + route);
		reqBuilder.method(HttpMethods.POST);
		reqBuilder.jsonContent(data);
		return reqBuilder.build();
	}
	
	public String logIn(String username, String password){
		
		String status = "ERROR";
		
		HttpRequest request = buildRequest("/login", new LoginRequest(username, password));
		
		MyHttpResponseListener customListener = new MyHttpResponseListener();
		
		Gdx.net.sendHttpRequest(request, customListener);
		
		while(!customListener.isResponseReceived());
		
		int serverResponse = customListener.getInt("success");
		String serverMessage = customListener.getString("message");
		
		if(serverResponse == 1){
			status = "";
			sessionToken = customListener.getString("token");
		}
		else{
			status = serverMessage;
		}
		
		return status;
	}
	
	public String register(String username, String email, String password){
		
		String status = "ERROR";
		
		HttpRequest request = buildRequest("/register", new RegisterRequest(username, email, password));
		
		MyHttpResponseListener customListener = new MyHttpResponseListener();
		
		Gdx.net.sendHttpRequest(request, customListener);
		
		while(!customListener.isResponseReceived());
		
		int serverResponse = customListener.getInt("success");
		String serverMessage = customListener.getString("message");
		
		if(serverResponse == 1){
			status = "";
		}
		else{
			status = serverMessage;
		}
		
		return status;
	}
	
public String requestImage(){
	
	String url = "";
	
	HttpRequest request = buildRequest("/reqimage", new ImageRequest(sessionToken));
	
	MyHttpResponseListener response = new MyHttpResponseListener();
	Gdx.net.sendHttpRequest(request, response);
	
	while(!response.isResponseReceived());
	
	int serverStatus = response.getInt("success");
	
	if(serverStatus == 1){
		url = response.getString("URL");
		imageId = response.getString("imageId");
	}
	else{
		String serverMessage = response.getString("message");
	}
	return url;
}
	
public Texture fetchImage(String url){
	
		Gdx.app.log("debug", url);
		
		Texture bucket = null;
		byte[] imageData;
		
		HttpRequest request = new HttpRequest(HttpMethods.GET);
		request.setUrl(url);
		request.setContent(null);
		
		MyHttpResponseListener customListener = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, customListener);
		
		while(!customListener.isResponseReceived());
		
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

	public boolean uploadTags(Array<Tag> tags){
		HttpRequest request = buildRequest("/submittags", new TagRequest(sessionToken, imageId, tags));
		
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int success = response.getInt("success");
		String message = response.getString("message");
		
		return success == 0;
	}
}