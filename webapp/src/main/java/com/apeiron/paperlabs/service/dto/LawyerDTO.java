package com.apeiron.paperlabs.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.apeiron.paperlabs.domain.Lawyer} entity.
 */
public class LawyerDTO implements Serializable {

    private String id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String mobile;

    private String tel;

    private String email;

    private String address;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LawyerDTO lawyerDTO = (LawyerDTO) o;
        if (lawyerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lawyerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LawyerDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", tel='" + getTel() + "'" +
            ", email='" + getEmail() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
