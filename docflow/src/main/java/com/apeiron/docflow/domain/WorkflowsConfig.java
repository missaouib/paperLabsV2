package com.apeiron.docflow.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkflowsConfig {
    private List<Workflow> workflow;

    public List<Workflow> getWorkflow() {
        return workflow;
    }

    public void setWorkflow(List<Workflow> workflow) {
        this.workflow = workflow;
    }

    @Override
    public String toString() {
        return "WorkflowsConfig{" +
            "workflow=" + workflow + workflow + workflow +
            '}';
    }
}
