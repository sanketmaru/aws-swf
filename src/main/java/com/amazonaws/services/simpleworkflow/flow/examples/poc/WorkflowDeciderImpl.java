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

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflow;
import com.amazonaws.services.simpleworkflow.flow.ActivitySchedulingOptions;
import com.amazonaws.services.simpleworkflow.flow.DecisionContextProviderImpl;
import com.amazonaws.services.simpleworkflow.flow.WorkflowContext;
import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.core.Settable;
import com.amazonaws.services.simpleworkflow.flow.core.TryCatchFinally;
import com.amazonaws.services.simpleworkflow.flow.examples.common.ConfigHelper;
import com.amazonaws.services.simpleworkflow.model.DecisionTask;
import com.amazonaws.services.simpleworkflow.model.PollForDecisionTaskRequest;
import com.amazonaws.services.simpleworkflow.model.TaskList;

/**
 * This implementation of FileProcessingWorkflow downloads the file, zips it and uploads it back to S3 
 */
public class WorkflowDeciderImpl implements WorkflowDecider {
	private final ActivityOneInterfaceClient activityOneStore;
	private final ActivityTwoInterfaceClient activityTwoStore;
	private final WorkflowContext workflowContext;

	
	public WorkflowDeciderImpl(){
		// Create activity clients
		this.activityOneStore = new ActivityOneInterfaceClientImpl();
		this.activityTwoStore = new ActivityTwoInterfaceClientImpl();
		workflowContext = (new DecisionContextProviderImpl()).getDecisionContext().getWorkflowContext();
	}
	
    @Override
    public void callDecider(String jobId) throws Exception {    	
    	// Settable to store the worker specific task list returned by the activity
    	final Settable<String> taskList = new Settable<String>();
    	
        System.out.println("job is : "+ jobId);
        activityOne(jobId, taskList);
        //activityTwo(jobId, taskList);    	     	

    }
    
    private void activityOne(final String jobId, Settable<String> taskList){
    	
    	
    	new TryCatchFinally() {
            @Override
            protected void doTry() throws Throwable {
            	
            	Promise<String> taskOne = activityOneStore.taskOne(" Activity 1 task 1", jobId);
            	
            	String activityTaskList = activityOneStore.getSchedulingOptions().getTaskList();            	
            	ActivitySchedulingOptions options = new ActivitySchedulingOptions();
                options.setTaskList(activityTaskList);
            	activityOneStore.taskTwo(2, jobId, options);
                
            }

            @Override
            protected void doFinally() throws Throwable {
            	// check if this is needed or not, for now commenting out
                if (taskList.isReady()) { // File was downloaded
                	                	
                	
                	// Call deleteLocalFile activity using the host sepcific task list
                    //store.deleteLocalFile(localSourceFilename, options);
                    //store.deleteLocalFile(localTargetFilename, options);
                }
            }

			@Override
			protected void doCatch(Throwable paramThrowable) throws Throwable {
				System.out.println("Error while calling activity one " + paramThrowable.getMessage() + " stacktrace :- " 
						+ paramThrowable.getStackTrace());
				
			}
        };
    	
    }
    
    private void activityTwo(final String jobId, Settable<String> taskList){
    	
    	new TryCatchFinally() {
            @Override
            protected void doTry() throws Throwable {
            	
            	Promise<String> taskOne = activityTwoStore.taskOne(" Activity 2 task 1", jobId);
            	//taskList.chain(taskOne);
            	
            	
            }

            @Override
            protected void doFinally() throws Throwable {
            	// check if this is needed or not, for now commenting out
                if (taskList.isReady()) { // File was downloaded
                	                	
                	// Set option to schedule activity in worker specific task list
                	ActivitySchedulingOptions options = new ActivitySchedulingOptions().withTaskList(taskList.get());
                	activityTwoStore.taskTwo(2, jobId, options);
                	// Call deleteLocalFile activity using the host sepcific task list
                    //store.deleteLocalFile(localSourceFilename, options);
                    //store.deleteLocalFile(localTargetFilename, options);
                }
            }

			@Override
			protected void doCatch(Throwable paramThrowable) throws Throwable {
				System.out.println("Error while calling activity two " + paramThrowable.getMessage() + " stacktrace :- " 
						+ paramThrowable.getStackTrace());
				
			}
        };
    	
    }
    
}
