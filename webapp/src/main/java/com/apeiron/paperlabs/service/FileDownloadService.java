package com.apeiron.paperlabs.service;

import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;

/**
 * Interface for downloading the generated document.
 *
 * @author Mohamed Belhassen Zinelabidine
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
public interface FileDownloadService {

    /**
     * @param filename filename
     * @param response Http response.
     * @return file from system.
     */
    Resource downloadPdfFile(String filename, String orderId, HttpServletResponse response);
}


