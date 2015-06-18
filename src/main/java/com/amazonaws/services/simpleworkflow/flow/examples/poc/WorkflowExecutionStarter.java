/*
 * Copyright 2012 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.services.simpleworkflow.flow.examples.poc;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.examples.common.ConfigHelper;
import com.amazonaws.services.simpleworkflow.flow.examples.poc.WorkFlowExecutorThread;

/**
 * This is used for launching a Workflow instance of FileProcessingWorkflowExample
 */
public class WorkflowExecutionStarter {
    
    private static AmazonSimpleWorkflow swfService;
    private static String domain;
    
    public static void main(String[] args) throws Exception {
    	
    	ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            Runnable worker = new WorkFlowExecutorThread(i);
            executor.execute(worker);
          }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    	
    	/*// Load configuration
    	ConfigHelper configHelper = ConfigHelper.createConfig();
        
        // Create the client for Simple Workflow Service
        swfService = configHelper.createSWFClient();
        domain = configHelper.getDomain();
        
        // Start Workflow instance
        String executionId = configHelper.getValueFromConfig(SWFConfigKeys.WORKFLOW_EXECUTION_ID_KEY) + UUID.randomUUID();
        
        WorkflowDeciderClientExternalFactory clientFactory = new WorkflowDeciderClientExternalFactoryImpl(swfService, domain);
        WorkflowDeciderClientExternal workflow = clientFactory.getClient(executionId);
        
        *//**
         * 1. Call api to get the jobId etc
         * 2. Pass the output to the decider
         * 3. Put a logic to check the size ( ec2 instance etx )
         *//*
        
        String jobId = "123";
        workflow.callDecider(jobId);
        System.exit(0);*/
    }    
}
