package com.dcu.sharktag.ServerRequests;

import com.badlogic.gdx.utils.Array;
import com.dcu.sharktag.SimpleTag;

public class TagRequest {
	private String token;
	private String imageId;
	private Array<SimpleTag> tags;
	
	public TagRequest(String token, String imageId, Array<SimpleTag> tags) {
		this.token = token;
		this.imageId = imageId;
		this.tags = tags;
	}
}
