package com.apeiron.docflow.service.impl;

import com.apeiron.docflow.domain.StepperConfig;
import com.apeiron.docflow.domain.WorkflowsConfig;
import com.apeiron.docflow.service.FileMapperService;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * This FileMapperService is used to mapping json files into java objects,
 * jsonToJavaStepper
 * jsonToJavaWorkflow
 */
@Service
public class FileMapperServiceImpl implements FileMapperService {
    private static final String JSON_PARSING_EXCEPTION_ERROR_MESSAGE = "Caught a: \"%s\", message : \"%s\".";

    /**
     * This method is used to convert the stepper json file into stepper java object
     * @param file the Stepper json File
     * @return StepperConfig.java
     *
     */
    @Override
    public StepperConfig jsonToJavaStepper(URL file) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        StepperConfig stepperConfig;
        try {
            // Convert JSON string from file to Object
            stepperConfig = mapper.readValue( file , StepperConfig.class);
          
            
        } catch (IOException e) {
            throw new IOException(String.format(JSON_PARSING_EXCEPTION_ERROR_MESSAGE, e.getClass().getName(), e.getMessage()));
        }
        return stepperConfig;
    }

    /**
     * This method is used to convert the Workflow json file into Workflow java object
     * @param file the Workflow json File
     * @return Workflow.java
     */
    @Override
    public WorkflowsConfig jsonToJavaWorkflow(FileReader file) throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);

        WorkflowsConfig workflowsConfig;
        try {
            // Convert JSON string from file to Object
            workflowsConfig = mapper.readValue( file , WorkflowsConfig.class);
        } catch (IOException e) {
            throw new IOException(String.format(JSON_PARSING_EXCEPTION_ERROR_MESSAGE, e.getClass().getName(), e.getMessage()));
        }
        return workflowsConfig;
    }
}

