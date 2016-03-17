package com.dcu.sharktag;

/*
 * Container class holding important information received from the server
 */
public class ServerResponse {

	private int status;	// Has the action been successful? 1 = success
	private String message;	// A message from the server. Can be info/error
	
	public ServerResponse(int status, String msg){
		this.status = status;
		message = msg;
	}
	
	public int getStatus(){
		return status;
	}
	
	public String getMessage(){
		return message;
	}
}
