package com.apeiron.docflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bookmark {

    private String bookmarkId;
    private String value;
    private String fontFamily;
    private String color;
    private Boolean bold;
    private Boolean italic;
    private Boolean capitalized;
    private String type;
    private int fontSize;
    private String underline;

    public String getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(String bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean isBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Boolean isItalic() {
        return italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean isCapitalized() {
        return capitalized;
    }

    public void setCapitalized(Boolean capitalized) {
        this.capitalized = capitalized;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getUnderline() {
        return underline;
    }

    public void setUnderline(String underline) {
        this.underline = underline;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "bookmarkId='" + bookmarkId + '\'' +
                ", value='" + value + '\'' +
                ", fontFamily='" + fontFamily + '\'' +
                ", color='" + color + '\'' +
                ", bold=" + bold +
                ", italic=" + italic +
                ", capitalized=" + capitalized +
                ", type='" + type + '\'' +
                ", fontSize=" + fontSize +
                ", underline='" + underline + '\'' +
                '}';
    }
}
