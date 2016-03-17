package com.dcu.sharktag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/*
 * This class takes care of handling data coming from the server
 */
public class MyHttpResponseListener implements HttpResponseListener {
	
	private volatile boolean responseReceived = false;
	
	private byte[] data = new byte[0];
	private int httpCode = -1;
	private JsonValue jsonValue;

	@Override
	public void handleHttpResponse(HttpResponse httpResponse) {	
		httpCode = httpResponse.getStatus().getStatusCode();
		
		try{
			// Convert stream to byte array
			InputStream is = httpResponse.getResultAsStream();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
		
			int nRead;
			byte[] tmp = new byte[16384];
			
			while((nRead = is.read(tmp, 0, tmp.length)) != -1){
				os.write(tmp, 0, nRead);
			}
			
			os.flush();
			data = os.toByteArray();
			
			// Data received from the server smaller than 1kb should be
			// considered as JSON, not a binary image
			if(data.length < 1024){
				jsonValue = new JsonReader().parse(os.toString());
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		responseReceived = true;
	}

	@Override
	public void failed(Throwable t) {
		Gdx.app.log("debug", "Request failed");
		jsonValue = null;
		responseReceived = true;
	}

	@Override
	public void cancelled() {
		Gdx.app.log("debug", "Request cancelled");
		jsonValue = null;
		responseReceived = true;
	}
	
	public boolean isResponseReceived(){
		return responseReceived;
	}
	
	public int getInt(String key){
		if(jsonValue != null){
			return jsonValue.get(key).asInt();
		}
		else{
			return -1;
		}
	}
	
	public String getString(String key){
		if(jsonValue != null){
			return jsonValue.get(key).asString();
		}
		else{
			return "";
		}
	}
	
	public boolean getBoolean(String key){
		if(jsonValue != null){
			return jsonValue.get(key).asBoolean();
		}
		else{
			return false;
		}
	}
	
	public int getHttpCode(){
		return httpCode;
	}
	
	// Binary data
	public byte[] getData(){
		return data;
	}
	
	public JsonValue getJsonValue(){
		return jsonValue;
	}
}
