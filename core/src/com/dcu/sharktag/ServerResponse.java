package com.dcu.sharktag;

public class ServerResponse {

	private int status;
	private String message;
	
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
