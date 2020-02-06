package com.apeiron.docflow.domain;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Step {
        private String id;
        private String title;
        private String description;
        private String[] blocs;
        private boolean visibility;

    public Step() {}

    public Step(String id, String title, String description, String[] blocs, boolean visibility) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.blocs = blocs;
        this.visibility = visibility;
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

    public String[] getBlocs() {
        return blocs;
    }

    public void setBlocs(String[] blocs) {
        this.blocs = blocs;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "Step{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", blocs=" + Arrays.toString(blocs) +
            ", visibility=" + visibility +
            '}';
    }
}
