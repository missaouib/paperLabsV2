package com.apeiron.paperlabs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties specific to Paperlabs.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	
	private String companyMF;
    private String companyRC;
    private String companyAddress;
    private String companyInvoiceFooterLine1 ;
    private String companyInvoiceFooterLine2;
    private Double companyTaxStamp;
    private Double companyInvoiceTva;
    private String supportTeamEmail;
    private String expertTeamEmail;

    public String getCompanyInvoiceFooterLine1() {
        return companyInvoiceFooterLine1;
    }

    public void setCompanyInvoiceFooterLine1(String companyInvoiceFooterLine1) {
        this.companyInvoiceFooterLine1 = companyInvoiceFooterLine1;
    }

    public String getCompanyInvoiceFooterLine2() {
        return companyInvoiceFooterLine2;
    }

    public void setCompanyInvoiceFooterLine2(String companyInvoiceFooterLine2) {
        this.companyInvoiceFooterLine2 = companyInvoiceFooterLine2;
    }

    public String getCompanyMF() {
        return companyMF;
    }

    public void setCompanyMF(String companyMF) {
        this.companyMF = companyMF;
    }

    public String getCompanyRC() {
        return companyRC;
    }

    public void setCompanyRC(String companyRC) {
        this.companyRC = companyRC;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public Double getCompanyTaxStamp() {
        return companyTaxStamp;
    }

    public void setCompanyTaxStamp(Double companyTaxStamp) {
        this.companyTaxStamp = companyTaxStamp;
    }

    public Double getCompanyInvoiceTva() {
        return companyInvoiceTva;
    }

    public void setCompanyInvoiceTva(Double companyInvoiceTva) {
        this.companyInvoiceTva = companyInvoiceTva;
    }

    public String getSupportTeamEmail() {
        return supportTeamEmail;
    }

    public void setSupportTeamEmail(String supportTeamEmail) {
        this.supportTeamEmail = supportTeamEmail;
    }

    public String getExpertTeamEmail() {
        return expertTeamEmail;
    }

    public void setExpertTeamEmail(String expertTeamEmail) {
        this.expertTeamEmail = expertTeamEmail;
    }
}
