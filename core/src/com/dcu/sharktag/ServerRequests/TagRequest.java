package com.dcu.sharktag.ServerRequests;

import com.badlogic.gdx.utils.Array;
import com.dcu.sharktag.SimpleTag;
import com.dcu.sharktag.Tag;

public class TagRequest {
	private String token;
	private String imageId;
	private SimpleTag[] tags;
	
	public TagRequest(String token, String imageId, Array<Tag> tags) {
		this.token = token;
		this.imageId = imageId;
		this.tags = new SimpleTag[tags.size];
		
		for(int i = 0; i < tags.size; i++){
			this.tags[i] = tags.get(i).toSimpleTag();
		}
	}
}
