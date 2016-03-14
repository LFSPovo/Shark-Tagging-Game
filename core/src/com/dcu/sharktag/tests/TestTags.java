package com.dcu.sharktag.tests;

import org.junit.Test;

import junit.framework.TestCase;

import com.badlogic.gdx.math.Vector2;
import com.dcu.sharktag.Tag;

public class TestTags extends TestCase{

	Vector2 imgSize;
	float imgScale;
	
	private Tag tag1;
	private Tag tag2;
	private Tag tag3;
	
	protected void setUp(){
		imgSize = new Vector2(1280, 720);
		imgScale = 1.5f;
		
		tag1 = new Tag(0, 0, imgSize, imgScale);
		tag1.setSize(new Vector2(50, 100));
		tag2 = new Tag(500, 300, imgSize, imgScale);
		tag2.setSize(new Vector2(100, 51));
		tag3 = new Tag(800, 30, imgSize, imgScale);
		tag3.setSize(new Vector2(100, 50));
	}
	
	@Test
	public void testCompareTo(){
		System.out.println("testCompareTo()");
		
		assertTrue(tag1.compareTo(tag2) < 0);
		assertFalse(tag2.compareTo(tag1) < 0);
		assertEquals(tag1.compareTo(tag3), 0);
		assertNotSame(tag1.compareTo(tag2), 0);
	}
	
	@Test
	public void testContains(){
		System.out.println("testContains()");
		
		assertTrue(tag1.contains(new Vector2(0, 0)));	//Edge case
		assertTrue(tag1.contains(new Vector2(50, 100)));//Edge case
		assertTrue(tag1.contains(new Vector2(25, 50)));
		assertFalse(tag1.contains(new Vector2(-10, -10)));
		assertFalse(tag1.contains(new Vector2(100, 25)));
	}
}
