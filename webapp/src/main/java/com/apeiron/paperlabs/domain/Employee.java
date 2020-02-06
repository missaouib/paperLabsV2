package com.apeiron.paperlabs.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Employee.
 */
@Document(collection = "employee")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("user_id")
    private String userId;

    @Field("employee_full_name")
    private String employeeFullName;

    @Field("employee_cin_number")
    private String employeeCinNumber;

    @Field("employee_cin_delivered_date")
    private LocalDate employeeCinDeliveredDate;

    @Field("employee_cin_delivered_location")
    private String employeeCinDeliveredLocation;

    @Field("employee_full_address")
    private String employeeFullAddress;

    @Field("employee_postion")
    private String employeePostion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public Employee employeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
        return this;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public String getEmployeeCinNumber() {
        return employeeCinNumber;
    }

    public Employee employeeCinNumber(String employeeCinNumber) {
        this.employeeCinNumber = employeeCinNumber;
        return this;
    }

    public void setEmployeeCinNumber(String employeeCinNumber) {
        this.employeeCinNumber = employeeCinNumber;
    }

    public LocalDate getEmployeeCinDeliveredDate() {
        return employeeCinDeliveredDate;
    }

    public Employee employeeCinDeliveredDate(LocalDate employeeCinDeliveredDate) {
        this.employeeCinDeliveredDate = employeeCinDeliveredDate;
        return this;
    }

    public void setEmployeeCinDeliveredDate(LocalDate employeeCinDeliveredDate) {
        this.employeeCinDeliveredDate = employeeCinDeliveredDate;
    }

    public String getEmployeeCinDeliveredLocation() {
        return employeeCinDeliveredLocation;
    }

    public Employee employeeCinDeliveredLocation(String employeeCinDeliveredLocation) {
        this.employeeCinDeliveredLocation = employeeCinDeliveredLocation;
        return this;
    }

    public void setEmployeeCinDeliveredLocation(String employeeCinDeliveredLocation) {
        this.employeeCinDeliveredLocation = employeeCinDeliveredLocation;
    }

    public String getEmployeeFullAddress() {
        return employeeFullAddress;
    }

    public Employee employeeFullAddress(String employeeFullAddress) {
        this.employeeFullAddress = employeeFullAddress;
        return this;
    }

    public void setEmployeeFullAddress(String employeeFullAddress) {
        this.employeeFullAddress = employeeFullAddress;
    }

    public String getEmployeePostion() {
        return employeePostion;
    }

    public Employee employeePostion(String employeePostion) {
        this.employeePostion = employeePostion;
        return this;
    }

    public void setEmployeePostion(String employeePostion) {
        this.employeePostion = employeePostion;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id='" + id + '\'' +
            ", userId='" + userId + '\'' +
            ", employeeFullName='" + employeeFullName + '\'' +
            ", employeeCinNumber='" + employeeCinNumber + '\'' +
            ", employeeCinDeliveredDate=" + employeeCinDeliveredDate +
            ", employeeCinDeliveredLocation='" + employeeCinDeliveredLocation + '\'' +
            ", employeeFullAddress='" + employeeFullAddress + '\'' +
            ", employeePostion='" + employeePostion + '\'' +
            '}';
    }
}
