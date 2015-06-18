package com.amazonaws.services.simpleworkflow.flow.examples.poc;


public class ActivityOneImpl implements ActivityOneInterface{

	@Override
	public String taskOne(String fileName, String jobId) throws Exception {
		System.out.println(jobId  + fileName);
		return null;
	}

	@Override
	public String taskTwo(int number, String jobId) throws Exception {
		System.out.println(jobId + " Activity 1 task : " + number);
		return null;
	}

	

}
