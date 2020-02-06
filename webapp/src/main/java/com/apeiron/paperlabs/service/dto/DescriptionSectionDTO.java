package com.apeiron.paperlabs.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.apeiron.paperlabs.domain.DescriptionSection} entity.
 */
public class DescriptionSectionDTO implements Serializable {

    private String id;

    @NotNull
    private String title;

    @NotNull
    private String content;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DescriptionSectionDTO descriptionSectionDTO = (DescriptionSectionDTO) o;
        if (descriptionSectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), descriptionSectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DescriptionSectionDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
