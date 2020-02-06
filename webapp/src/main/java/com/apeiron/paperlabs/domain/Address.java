package com.apeiron.paperlabs.domain;

import java.util.Objects;

public class Address {

    private static final long serialVersionUID = 3124503582933669523L;

    private Long id;

    private String line1;

    private String line2;

    private String city;

    private String postalCode;

    public Long getId() {
        return id;
        
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) &&
            Objects.equals(line1, address.line1) &&
            Objects.equals(line2, address.line2) &&
            Objects.equals(city, address.city) &&
            Objects.equals(postalCode, address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line1, line2, city, postalCode);
    }

    @Override
    public String toString() {
        return "Address{" +
            "id=" + id +
            ", line1='" + line1 + '\'' +
            ", line2='" + line2 + '\'' +
            ", city='" + city + '\'' +
            ", postalCode='" + postalCode + '\'' +
            '}';
    }
}
