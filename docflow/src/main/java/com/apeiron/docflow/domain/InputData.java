package com.apeiron.docflow.domain;

public class InputData {
    private String id;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Input{" +
            "id='" + id + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
