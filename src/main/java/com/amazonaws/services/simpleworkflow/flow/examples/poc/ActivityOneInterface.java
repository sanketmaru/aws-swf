package com.amazonaws.services.simpleworkflow.flow.examples.poc;

import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activity;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;
import com.amazonaws.services.simpleworkflow.flow.annotations.ExponentialRetry;
import com.amazonaws.services.simpleworkflow.flow.common.FlowConstants;

@Activities()
@ActivityRegistrationOptions(
		defaultTaskHeartbeatTimeoutSeconds = FlowConstants.NONE, 
        defaultTaskScheduleToCloseTimeoutSeconds = 300, 
        defaultTaskScheduleToStartTimeoutSeconds = FlowConstants.NONE, 
        defaultTaskStartToCloseTimeoutSeconds = 300)
public interface ActivityOneInterface {
	
	
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	@Activity(name = "ActivityOneTaskOne", version = "2.0")
    @ExponentialRetry(initialRetryIntervalSeconds = 10,  maximumAttempts = 10)
    public String taskOne(String fileName, String jobId) throws Exception ;
    
	/**
     * 
     * @param fileName
     * @return
     * @throws Exception
     */
	@Activity(name = "ActivityOneTaskTwo", version = "2.0")
    @ExponentialRetry(initialRetryIntervalSeconds = 10, maximumAttempts = 10)
    public String taskTwo(int number, String jobId) throws Exception;

}
