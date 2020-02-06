package com.apeiron.paperlabs.service.impl;

import com.apeiron.paperlabs.service.InvoiceService;
import com.apeiron.paperlabs.service.dto.InvoiceData;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * class implements the InvoiceService for generating reports in a PDF format.
 *
 * @author Ghandour Abdelkrim
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
@Service
public class InvoceServiceImpl extends Thread implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoceServiceImpl.class);

    private static final String LOGO_PATH = "/jasper/images/pap_logo.png";
    private static final String INVOICE_TEMPLATE = "/jasper/invoice_form.jrxml";
    private static final String FILLED_FILE_NAME_PREFIX = "webapp/src/main/resources/generated-invoice/";

    /**
     * Convert the given report data value to a <code>JRDataSource</code>.
     * <p>In the default implementation, a <code>JRDataSource</code>,
     * <code>java.util.Collection</code> or object array is detected.
     * The latter are converted to <code>JRBeanCollectionDataSource</code>
     * or <code>JRBeanArrayDataSource</code>, respectively.
     *
     * @param value the report data value to convert
     * @return the JRDataSource (never <code>null</code>)
     * @throws IllegalArgumentException if the value could not be converted
     */
    private static JRDataSource convertReportData(Object value) {
        if (value instanceof JRDataSource) {
            return (JRDataSource) value;
        } else if (value instanceof Collection) {
            return new JRBeanCollectionDataSource((Collection) value);
        } else if (value instanceof Object[]) {
            return new JRBeanArrayDataSource((Object[]) value);
        } else {
            throw new IllegalArgumentException("Value [" + value + "] cannot be converted to a JRDataSource");
        }
    }

    /**
     * Render the supplied <code>JasperPrint</code> instance using the
     * supplied <code>JRAbstractExporter</code> instance and write the results
     * to the supplied <code>OutputStream</code>.
     *
     * @param exporter     the <code>JRAbstractExporter</code> to use to render the report
     * @param print        the <code>JasperPrint</code> instance to render
     * @param outputStream the <code>OutputStream</code> to write the result
     * @throws JRException if rendering failed
     */
    public static void render(JRExporter exporter, JasperPrint print, OutputStream outputStream)
        throws JRException {

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
        exporter.exportReport();
    }

    /**
     * Render a report in PDF format using the supplied report data.
     * Writes the results to the supplied <code>OutputStream</code>.
     *
     * @param report     the <code>JasperReport</code> instance to render
     * @param parameters the parameters to use for rendering
     * @param stream     the <code>OutputStream</code> to write the rendered report to
     * @param reportData a <code>JRDataSource</code>, <code>java.util.Collection</code>
     *                   or object array (converted accordingly), representing the report data to read
     *                   fields from
     * @throws JRException if rendering failed
     * @see #convertReportData
     */
    private static void renderAsPdf(JasperReport report, Map<String, Object> parameters, Object reportData, OutputStream stream)
        throws JRException {

        JasperPrint print = JasperFillManager.fillReport(report, parameters, convertReportData(reportData));
        render(new JRPdfExporter(), print, stream);
    }


    @Override
    public String printReport(InvoiceData invoiceData, Locale locale) {

        File pdfFile = null;
        if(!Files.exists(Paths.get(FILLED_FILE_NAME_PREFIX))) {
            try {
                Files.createDirectory(Paths.get(FILLED_FILE_NAME_PREFIX));
            } catch (IOException e) {
                log.error("Error creating directory invoice file");
            }

        }
        try {
            pdfFile = File.createTempFile("ref", ".pdf", new File(FILLED_FILE_NAME_PREFIX));

                try (FileOutputStream pos = new FileOutputStream(pdfFile)) {
                    log.info("PDF File created");
                    try {
                        final JasperReport report = loadTemplate();
                        log.info("Load invoice jrxml template.");
                        final Map<String, Object> parameters = parameters(invoiceData, locale);
                        log.info("Create parameters map.");
                        final JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList("Invoice"));
                        log.info("Create an empty datasource..");
                        renderAsPdf(report, parameters, dataSource, pos);
                        log.info("Render File as PDF.");
                    } finally {
                        log.info("File Created and render as PDF.");
                    }
                }

        } catch (IOException | JRException e) {
            log.error("Error creating the empty pdf file", e);
        }

        return pdfFile.getAbsolutePath();

    }

    /**
     * method  to fill the template order parameters
     *
     * @param invoiceData the entity Data
     * @param locale     the local language FR/EN
     * @return Map of String and Object
     */
    private Map<String, Object> parameters(InvoiceData invoiceData, Locale locale) {
        final Map<String, Object> parameters = new HashMap<>();

        parameters.put("order", invoiceData);
        parameters.put("REPORT_LOCALE", locale);
        parameters.put("logo", getClass().getResourceAsStream(LOGO_PATH));
        return parameters;
    }

    /**
     * method for Load invoice jrxml template
     *
     * @return the <code>JasperReport</code> instance to render
     * @throws JRException if rendering failed
     */
    private JasperReport loadTemplate() throws JRException {

        final InputStream reportInputStream = getClass().getResourceAsStream(INVOICE_TEMPLATE);
        log.info("Load the template.jrxml");
        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);
        log.info("jasper compileReport design.");
        return JasperCompileManager.compileReport(jasperDesign);

    }
}
