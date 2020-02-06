package com.apeiron.docflow.service;

import com.apeiron.docflow.domain.InputData;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * Service class for managing document generation.
 */
public interface DocumentGenerationService {
    /**
     * Given the user inputs and the .docx file with the blank spaces, it filters all inputs to keep only those that contain bookmarks
     * then filters user inputs using the previously filtered list to end up with the same list of inputs in both.
     * <p>
     * For each input of the stepper data, it gets the related custom bookmarks using the bookmark references array and groups them by bookmark type.
     * Generate filled .docx file.
     *
     * @param stepperData user inputs from stepper form
     * @param urlStepper url of the stepper configuration file
     * @param urlDocument url of the .docx file to be filled with user inputs
     */
    String generateDocXFile(Map<String, InputData> stepperData, URL urlStepper, String urlDocument, URL urlWorkflow) throws IOException;
}
