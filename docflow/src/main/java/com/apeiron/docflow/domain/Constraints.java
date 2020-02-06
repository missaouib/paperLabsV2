package com.apeiron.docflow.domain;

import org.springframework.stereotype.Component;

@Component
public class Constraints {
    private String elementId;
    private boolean visibility;

    public Constraints() {
        this.visibility = true;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "Constraints{" +
            "elementId='" + elementId + '\'' +
            ", visibility=" + visibility +
            '}';
    }
}
