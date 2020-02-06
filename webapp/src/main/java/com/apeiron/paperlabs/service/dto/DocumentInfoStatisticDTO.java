package com.apeiron.paperlabs.service.dto;

public class DocumentInfoStatisticDTO {

    private Integer numberOfDownload;
    private Integer numberOfCanceledDownload;
    private Double sumDocumentRevenue;


    public Integer getNumberOfDownload() {
        return numberOfDownload;
    }

    public void setNumberOfDownload(Integer numberOfDownload) {
        this.numberOfDownload = numberOfDownload;
    }

    public Integer getNumberOfCanceledDownload() {
        return numberOfCanceledDownload;
    }

    public void setNumberOfCanceledDownload(Integer numberOfCanceledDownload) {
        this.numberOfCanceledDownload = numberOfCanceledDownload;
    }

    public Double getSumDocumentRevenue() {
        return sumDocumentRevenue;
    }

    public void setSumDocumentRevenue(Double sumDocumentRevenue) {
        this.sumDocumentRevenue = sumDocumentRevenue;
    }

    @Override
    public String toString() {
        return "DocumentInfoStatisticDTO{" +
            "numberOfDownload=" + numberOfDownload +
            ", numberOfCanceledDownload=" + numberOfCanceledDownload +
            ", sumDocumentRevenue=" + sumDocumentRevenue +
            '}';
    }
}
