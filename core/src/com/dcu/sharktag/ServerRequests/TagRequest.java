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
		Array<SimpleTag> tmpArray = new Array<SimpleTag>();
		
		for(int i = 0; i < tags.size; i++){
			if(tags.get(i).getSharkId() == 0){
				continue;
			}
			tmpArray.add(tags.get(i).toSimpleTag());
		}
		
		this.tags = tmpArray.toArray(SimpleTag.class);
	}
}
