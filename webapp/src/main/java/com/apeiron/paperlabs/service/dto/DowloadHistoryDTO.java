package com.apeiron.paperlabs.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.apeiron.paperlabs.domain.DowloadHistory} entity.
 */
public class DowloadHistoryDTO implements Serializable {

    private String id;

    @NotNull
    private Instant date;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DowloadHistoryDTO dowloadHistoryDTO = (DowloadHistoryDTO) o;
        if (dowloadHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dowloadHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DowloadHistoryDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
