package com.apeiron.docflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bloc {
    private String id;
    private String title;
    private String description;
    private String[] inputs;
    private boolean visibility;

    public Bloc() {
        this.visibility=true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getInputs() {
        return inputs;
    }

    public void setInputs(String[] inputs) {
        this.inputs = inputs;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "Bloc{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", inputs=" + inputs +
            ", visibility=" + visibility +
            '}';
    }
}
