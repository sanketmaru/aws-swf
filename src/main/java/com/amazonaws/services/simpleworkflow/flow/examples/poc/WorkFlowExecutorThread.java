package com.amazonaws.services.simpleworkflow.flow.examples.poc;

import java.io.IOException;
import java.util.UUID;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.examples.common.ConfigHelper;
import com.amazonaws.services.simpleworkflow.model.ActivityTask;
import com.amazonaws.services.simpleworkflow.model.DecisionTask;

public class WorkFlowExecutorThread implements Runnable {
	
	private static AmazonSimpleWorkflow swfService;
    private static String domain;
    private int countId;
    
    public WorkFlowExecutorThread(int countID){
    	this.countId = countID;
    }
	@Override
	public void run() {
		// Load configuration
    	ConfigHelper configHelper = null;
		try {
			configHelper = ConfigHelper.createConfig();
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Create the client for Simple Workflow Service
        swfService = configHelper.createSWFClient();
        domain = configHelper.getDomain();
        
        // Start Workflow instance
        String executionId = configHelper.getValueFromConfig(SWFConfigKeys.WORKFLOW_EXECUTION_ID_KEY) + UUID.randomUUID();
        
        WorkflowDeciderClientExternalFactory clientFactory = new WorkflowDeciderClientExternalFactoryImpl(swfService, domain);
        WorkflowDeciderClientExternal workflow = clientFactory.getClient(executionId);
        String taskList = configHelper.getValueFromConfig(SWFConfigKeys.WORKFLOW_WORKER_TASKLIST);
        workflow.getSchedulingOptions().setTaskList(taskList);
        /**
         * 1. Call api to get the jobId etc
         * 2. Pass the output to the decider
         * 3. Put a logic to check the size ( ec2 instance etx )
         */
        DecisionTask t = new DecisionTask();
        
        
        
        String jobId = String.valueOf(countId);
        workflow.callDecider(jobId);
        System.exit(0);

	}

}
