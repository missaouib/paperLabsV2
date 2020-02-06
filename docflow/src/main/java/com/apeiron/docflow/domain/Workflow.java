package com.apeiron.docflow.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Workflow {
    private String inputId;
    private List<ValuesConstraints> valuesConstraints;

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public List<ValuesConstraints> getValuesConstraints() {
        return valuesConstraints;
    }

    public void setValuesConstraints(List<ValuesConstraints> valuesConstraints) {
        this.valuesConstraints = valuesConstraints;
    }

    @Override
    public String toString() {
        return "Workflow{" +
            "inputId='" + inputId + inputId + inputId + '\'' +
            ", valuesConstraints=" + valuesConstraints + valuesConstraints + valuesConstraints +
            '}';
    }
}
