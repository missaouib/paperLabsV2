package com.apeiron.paperlabs.service.dto;

import java.util.Objects;

/**
 * Entity DTO for the list of the shipment package for the Invoice DTO
 *
 * @author Ghandour Abdelkrim
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
public class PackageProductsDTO {
    private long id;
    private String designation;
    private Float unitPrice;
    private int qte;
    private Double totale;


    public PackageProductsDTO() {
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public Float getTotale() {
        return qte * unitPrice;
    }


    public long getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageProductsDTO that = (PackageProductsDTO) o;
        return id == that.id &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(unitPrice, that.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, designation, unitPrice);
    }

    @Override
    public String toString() {
        return "PackageProductsDTO{" +
            "id=" + id +
            ", designation='" + designation + '\'' +
            ", unitPrice='" + unitPrice + '\'' +
            '}';
    }
}
