package com.dcu.sharktag.ServerRequests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class ServerRequestBuilder extends HttpRequestBuilder {

	@Override
	public HttpRequestBuilder newRequest() {
		json.setOutputType(OutputType.json);
		// Allow zero valued properties to be outputted
		json.setUsePrototypes(false);
		return super.newRequest();
	}

	@Override
	public HttpRequestBuilder jsonContent(Object content) {
		// TODO Auto-generated method stub
		Gdx.app.log("debug", json.prettyPrint(content));
		return super.jsonContent(content);
	}
	
	
}
