package com.apeiron.paperlabs.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the contact page object.
 */
public class ContactDTO implements Serializable {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    private String email;

    private String need;

    private String message;

    private String phoneNumber;

    private String company;


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactDTO that = (ContactDTO) o;
        return Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(need, that.need) &&
            Objects.equals(message, that.message) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, need, message, phoneNumber, company);
    }

    @Override
    public String toString() {
        return "ContactDTO{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", need='" + need + '\'' +
            ", message='" + message + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", company='" + company + '\'' +
            '}';
    }
}
