package com.dcu.sharktag.tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class JUnitTest {
	public static void main(String args[]){
		Result result = JUnitCore.runClasses(TestTags.class);
		
		for(Failure failure : result.getFailures()){
			System.out.println(failure.toString());
		}
		
		if(result.wasSuccessful()){
			System.out.println("All tests were successfull");
		}
	}
}
