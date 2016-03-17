package com.dcu.sharktag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.dcu.sharktag.ServerRequests.ImageRequest;
import com.dcu.sharktag.ServerRequests.LoginRequest;
import com.dcu.sharktag.ServerRequests.RecoveryRequest;
import com.dcu.sharktag.ServerRequests.RegisterRequest;
import com.dcu.sharktag.ServerRequests.ServerRequestBuilder;
import com.dcu.sharktag.ServerRequests.TagRequest;
import com.dcu.sharktag.ServerRequests.SessionRequest;

/*
 * This class takes care of the communication between the client and the server
 * 
 * It also holds all the important variables related to the connection
 */
public class Communication {
	private String serverURL = "http://povilas.ovh:8080";

	private int playerScore = 0;
	private boolean firstTimer = false;
	private String sessionToken = "";
	
	private String imageId = "";
	
	//A temporary variable for storing server's messages
	private String tmpString = "";
	
	public String getSessionToken(){
		return sessionToken;
	}
	
	public boolean isFirstTimer(){
		return firstTimer;
	}
	
	// Used for initiating the tutorial, regardless of player's history
	public void setFirstTimer(boolean flag){
		firstTimer = flag;
	}
	
	public String getTmpString(){
		return tmpString;
	}
	
	public int getPlayerScore(){
		return playerScore;
	}
	
	public void setPlayerScore(int s){
		playerScore = s;
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
	
	/*
	 * Sends a request to authenticate the user
	 * When successful, a session token is received and used to authorise
	 * most requests. This ensures only one instance of the player device
	 * can be logged in.
	 */
	public ServerResponse logIn(String username, String password){
		HttpRequest request = buildRequest("/login",
										new LoginRequest(username, password));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int serverResponse = -1;
		String status;
		
		if(response.getHttpCode() == 200){
			serverResponse = response.getInt("success");
			status = response.getString("message");
			
			if(serverResponse == 1){
				sessionToken = response.getString("token");
				firstTimer = !response.getBoolean("tutorialFinished");
				playerScore = response.getInt("score");
			}
		}
		else{
			status = "Server could not be reached";
		}
		
		return new ServerResponse(serverResponse, status);
	}
	
	/*
	 * Registers a new user in teh database. The user will still not be able to
	 * log in until he activates the account through email
	 */
	public ServerResponse register(String username, String email, String password){
		HttpRequest request = buildRequest("/register",
								new RegisterRequest(username, email, password));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int serverResponse;
		String status;
		
		if(response.getHttpCode() == 200){
			serverResponse = response.getInt("success");
			status = response.getString("message");
		}
		else{
			serverResponse = -1;
			status = "Server could not be reached";
		}
		
		return new ServerResponse(serverResponse, status);
	}
	
	/*
	 * This method returns a URL string to the image provided by the server
	 */
	public String requestImage(){
		HttpRequest request = buildRequest("/reqimage",
											new ImageRequest(sessionToken));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int serverStatus;
		String url = "";
		
		if(response.getHttpCode() == 200){
			serverStatus = response.getInt("success");
			if(serverStatus == 1){
				url = response.getString("URL");
				imageId = response.getString("imageId");
			}
		}
		
		return url;
	}
	
	/*
	 * Use the image URL to download an image from the server
	 */
	public Texture fetchImage(String url){	
		Texture bucket = null;
		byte[] imageData;
		
		HttpRequest request = new HttpRequest(HttpMethods.GET);
		request.setUrl(url);
		request.setContent(null);
		
		MyHttpResponseListener customListener = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, customListener);
		
		while(!customListener.isResponseReceived());
		
		imageData = customListener.getData();
		
		// TODO Not supported on HTML platform
		Pixmap pixMap = new Pixmap(imageData, 0, imageData.length);

		bucket = new Texture(pixMap);
		pixMap.dispose();

		return bucket;
	}

	/*
	 * Submit all the tags to the server
	 */
	public boolean uploadTags(Array<Tag> tags){
		HttpRequest request = buildRequest("/submittags",
								new TagRequest(sessionToken, imageId, tags));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int success = -1;
		
		if(response.getHttpCode() == 200){
			success = response.getInt("success");
			response.getString("message");
			
			if(success == 1){
				// Synchronize player score with the score on the server
				playerScore = response.getInt("score");
			}
		}
		
		return success == 1;
	}
	
	/*
	 * Set a flag on the server,
	 * so that we know the player has gone through the tutorial
	 */
	public boolean finishTutorial(){
		HttpRequest request = buildRequest("/finishtutorial",
											new SessionRequest(sessionToken));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int success = -1;
		if(response.getHttpCode() == 200){
			success = response.getInt("success");
			response.getString("message");
		}
		
		if(success == 1){
			firstTimer = false;
		}
		
		return success == 1;
	}
	
	/*
	 * Auto log in using the session token stored in config file
	 */
	public boolean autoLogin(String token){
		HttpRequest request = buildRequest("/autologin",
											new SessionRequest(token));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int success = -1;
		
		if(response.getHttpCode() == 200){
			success = response.getInt("success");
			
			if(success == 1){
				sessionToken = token;
				playerScore = response.getInt("score");
			}
		}
		
		return success == 1;
	}
	
	/*
	 * Log out and remove the session token from the database
	 */
	public boolean logOut(){
		HttpRequest request = buildRequest("/logout",
											new SessionRequest(sessionToken));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int success = response.getInt("success");
		Gdx.app.log("debug", response.getString("message"));
		
		if(success == 1){
			sessionToken = "";
		}
		
		return success == 1;
	}

	/*
	 * Request a recovery code from the server
	 */
	public ServerResponse recoverPassword(String username){
		HttpRequest request = buildRequest("/recoverpassword",
												new RecoveryRequest(username));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int success;
		String message;
		
		if(response.getHttpCode() == 200){
			success = response.getInt("success");
			message = response.getString("message");
			if(success == 1){
				tmpString = response.getString("username");
			}
		}
		else{
			success = -1;
			message = "Server could not be reached";
		}
		
		return new ServerResponse(success, message);
	}
	
	/*
	 * Use the code to change the password
	 */
	public ServerResponse recoverPasswordChange(String username, String pass, String code){
		HttpRequest request = buildRequest("/recoverpasswordchange",
									new RecoveryRequest(username, code, pass));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		int success;
		String message;
		
		if(response.getHttpCode() == 200){
			success = response.getInt("success");
			message = response.getString("message");
		}
		else{
			success = -1;
			message = "Server could not be reached";
		}
		
		return new ServerResponse(success, message);
	}
	
	/*
	 * Request a list of top 20 players with their scores
	 */
	public JsonValue requestHighscore(){
		HttpRequest request = buildRequest("/leaderboard",
											new SessionRequest(sessionToken));
		MyHttpResponseListener response = new MyHttpResponseListener();
		Gdx.net.sendHttpRequest(request, response);
		
		while(!response.isResponseReceived());
		
		if(response.getHttpCode() == 200){
			int status = response.getInt("success");
			
			if(status == 1){
				return response.getJsonValue();
			}
		}
		
		return null;
	}
}