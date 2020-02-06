package com.apeiron.docflow.domain;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class StepperConfig {


    private List<Step> steps;
    private List<Input> inputs;
    private List<Bloc> blocs;
    private List<Bookmark> bookmarks;

    public StepperConfig(List<Step> steps, List<Input> inputs, List<Bloc> blocs, List<Bookmark> bookmarks) {
        this.steps = steps;
        this.inputs = inputs;
        this.blocs = blocs;
        this.bookmarks = bookmarks;
    }

    public StepperConfig() {
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public List<Bloc> getBlocs() {
        return blocs;
    }

    public void setBlocs(List<Bloc> blocs) {
        this.blocs = blocs;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }
}
