package com.apeiron.docflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Input {
    private String id;
    private String type;
    private String description;
    private String name;
    private String label;
    private String placeHolder;
    private String help;
    private List<String> options;
    private String value;
    private Map<String, String[]> bookmarkIdPerValue;
    private boolean visibility;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String[]> getBookmarkIdPerValue() {
        return bookmarkIdPerValue;
    }

    public void setBookmarkIdPerValue(Map<String, String[]> bookmarkIdPerValue) {
        this.bookmarkIdPerValue = bookmarkIdPerValue;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return "Input{" +
            "id='" + id + '\'' +
            ", type='" + type + '\'' +
            ", description='" + description + '\'' +
            ", name='" + name + '\'' +
            ", label='" + label + '\'' +
            ", placeHolder='" + placeHolder + '\'' +
            ", help='" + help + '\'' +
            ", options=" + options +
            ", value='" + value + '\'' +
            ", bookmarkIdPerValue=" + bookmarkIdPerValue +
            ", visibility=" + visibility +
            '}';
    }
}
