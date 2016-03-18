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
		Array<SimpleTag> nonOverlapping = new Array<SimpleTag>();
		Array<SimpleTag> tmpArray = new Array<SimpleTag>();
		
		for(int i = 0; i < tags.size; i++){
			if(tags.get(i).getSharkId() == 0){
				continue;
			}
			tmpArray.add(tags.get(i).toSimpleTag());
		}
		
		for(int i = 0; i < tmpArray.size; i++){
			boolean overlaps = false;
			
			for(int k = i + 1; k < tmpArray.size; k++){
				if(tmpArray.get(i).overlap(tmpArray.get(k), 40)){
					overlaps = true;
				}
			}
			
			if(!overlaps){
				nonOverlapping.add(tmpArray.get(i));
			}
		}
		
		this.tags = nonOverlapping.toArray(SimpleTag.class);
	}
}
