package com.apeiron.paperlabs.service;

import com.apeiron.paperlabs.service.dto.InvoiceData;

import java.util.Locale;

/**
 * Interface for generating the invoice document.
 * @author Ghandour Abdelkrim
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
public interface InvoiceService {

    String printReport(InvoiceData invoiceData, Locale locale);
}
