package com.apeiron.paperlabs.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DowloadHistory.
 */
@Document(collection = "dowload_history")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dowloadhistory")
public class DowloadHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Field("date")
    private Instant date;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public DowloadHistory date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DowloadHistory)) {
            return false;
        }
        return id != null && id.equals(((DowloadHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DowloadHistory{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
