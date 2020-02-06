package com.apeiron.docflow.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValuesConstraints {
    private String value;
    private List<Constraints> constraints;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Constraints> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraints> constraints) {
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        return "ValueConstraints{" +
            "value='" + value + value + value + '\'' +
            ", constraints=" + constraints + constraints + constraints +
            '}';
    }
}
