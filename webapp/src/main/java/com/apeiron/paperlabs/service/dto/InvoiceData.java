package com.apeiron.paperlabs.service.dto;

import com.apeiron.paperlabs.domain.enumeration.PaymentMethod;
import org.apache.commons.lang.RandomStringUtils;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class InvoiceData {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String invoiceNumber;
    private Date creationDate;
    private PaymentMethod paymentMethod;
    private String reciverFullName;
    private String reciverAddressLine1;
    private String reciverAddressLine2;
    private String companyFullAddres;


    private List<LegalDocumentDTO> documents;

    private Double tva;
    private Double TaxStamp;


    private String companyRC;
    private String companyMF;
    private String companyFullAdress;

    private String footerLine1;
    private String footerLine2;

    private Double totaleHt;
    private Double totaleTtc;




    public void setCompanyFullAddres(String companyFullAddres) {
        this.companyFullAddres = companyFullAddres;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return "PAP-"+RandomStringUtils.random(10, "0123456789abcdef");
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getCreationDate() {
        return new Date();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getReciverFullName() {
        return reciverFullName;
    }

    public void setReciverFullName(String reciverFullName) {
        this.reciverFullName = reciverFullName;
    }

    public String getReciverAddressLine1() {
        return reciverAddressLine1;
    }

    public void setReciverAddressLine1(String reciverAddressLine1) {
        this.reciverAddressLine1 = reciverAddressLine1;
    }

    public String getReciverAddressLine2() {
        return reciverAddressLine2;
    }

    public void setReciverAddressLine2(String reciverAddressLine2) {
        this.reciverAddressLine2 = reciverAddressLine2;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<LegalDocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<LegalDocumentDTO> documents) {
        this.documents = documents;
    }

    public void setTva(Double tva) {
        this.tva = tva;
    }



    public void setTaxStamp(Double taxStamp) {
        TaxStamp = taxStamp;
    }


    public void setCompanyRC(String companyRC) {
        this.companyRC = companyRC;
    }

    public void setCompanyMF(String companyMF) {
        this.companyMF = companyMF;
    }


    public void setCompanyFullAdress(String companyFullAdress) {
        this.companyFullAdress = companyFullAdress;
    }

    public void setFooterLine1(String footerLine1) {
        this.footerLine1 = footerLine1;
    }

    public String getCompanyFullAddres() {
        return companyFullAddres;
    }

    public Double getTva() {
        return tva;
    }
    public Double getTauxTva() {

        return Double.parseDouble(new DecimalFormat("#0.00").format((getTotaleHt() / 100) * tva).replaceAll(",","."));

    }

    public Double getTaxStamp() {
        return TaxStamp;
    }

    public String getCompanyRC() {
        return companyRC;
    }

    public String getCompanyMF() {
        return companyMF;
    }

    public String getCompanyFullAdress() {
        return companyFullAdress;
    }

    public String getFooterLine1() {
        return footerLine1;
    }

    public String getFooterLine2() {
        return footerLine2;
    }

    public void setFooterLine2(String footerLine2) {
        this.footerLine2 = footerLine2;
    }

    public Double getTotaleHt() {
        Double totale=0.0;
        for (LegalDocumentDTO  doc: documents){
            totale+=doc.getPrice();
        }

        return Double.parseDouble(new DecimalFormat("#0.000").format((totale)).replaceAll(",","."));
    }

    public void setTotaleHt(Double totaleHt) {
        this.totaleHt = totaleHt;
    }

    public Double getTotaleTtc() {
        return Double.parseDouble(new DecimalFormat("#0.000").format(getTotaleHt()+getTauxTva()+getTaxStamp()).replaceAll(",","."));
    }

    public void setTotaleTtc(Double totaleTtc) {
        this.totaleTtc = totaleTtc;
    }
}
