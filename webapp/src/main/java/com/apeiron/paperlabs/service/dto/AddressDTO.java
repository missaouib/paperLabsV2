package com.apeiron.paperlabs.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO representing a Address of user
 *
 * @author Ghandour Abdelkrim
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
public class AddressDTO implements Serializable {
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
        if (line2 != null) {
            return line2;
        } else
            return "";
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (addressDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), addressDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
            "id=" + getId() +
            ", line1='" + getLine1() + "'" +
            ", line2='" + getLine2() + "'" +
            ", city='" + getCity() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            "}";
    }
}
