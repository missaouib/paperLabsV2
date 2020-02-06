package com.apeiron.docflow.service;

import com.apeiron.docflow.domain.StepperConfig;
import com.apeiron.docflow.domain.WorkflowsConfig;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public interface FileMapperService {
    StepperConfig jsonToJavaStepper(URL file) throws IOException;
    WorkflowsConfig jsonToJavaWorkflow(FileReader file) throws IOException;
}
