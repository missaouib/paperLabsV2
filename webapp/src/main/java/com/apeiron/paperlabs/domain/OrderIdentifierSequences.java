package com.apeiron.paperlabs.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "orderIdentifierSequences")
public class OrderIdentifierSequences {

    @Id
    private String id;
    private double seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSeq() {
        return seq;
    }

    public void setSeq(double seq) {
        this.seq = seq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderIdentifierSequences that = (OrderIdentifierSequences) o;
        return Double.compare(that.seq, seq) == 0 &&
            Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seq);
    }

    @Override
    public String toString() {
        return "OrderIdentifierSequences{" +
            "id='" + id + '\'' +
            ", seq=" + seq +
            '}';
    }
}
