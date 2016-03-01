package com.dcu.sharktag.ServerRequests;

import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class ServerRequestBuilder extends HttpRequestBuilder {

	@Override
	public HttpRequestBuilder newRequest() {
		json.setOutputType(OutputType.json);
		return super.newRequest();
	}
}
