package com.dcu.sharktag.ServerRequests;

public class RecoveryRequest {

	private String username;
	private String code;
	private String password;
	
	public RecoveryRequest(String username){
		this.username = username;
	}
	
	public RecoveryRequest(String username, String code, String password){
		this.username = username;
		this.code = code;
		this.password = password;
	}
}
