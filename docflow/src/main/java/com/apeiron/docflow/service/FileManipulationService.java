package com.apeiron.docflow.service;

import com.apeiron.docflow.domain.Bookmark;
import com.apeiron.docflow.domain.Constraints;
import com.apeiron.docflow.domain.Input;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface FileManipulationService {
    List<String> getInputIdsWithConstraints(FileReader workflowFile) throws IOException;

    Map<String, Input> getAllInputsWithConstraints(URL urlStepper, FileReader workflowFile) throws IOException;

    Map<String,Input> getAllInputs(URL urlStepper) throws IOException;

    Map<String, Bookmark> getAllCustomBookmarks(URL urlStepper) throws IOException;

    List<Constraints> getConstraintsByInputIdAndValue(String inputId, String value, FileReader workflowFile) throws IOException;

    List<String> getInputsByBlocId(String blocId, URL stepperFile) throws IOException;

    Input getInputByInputId(String inputId, URL stepperFile) throws IOException;

    Bookmark getBookmarkByBookmarkId(String bookmarkId, URL stepperFile) throws IOException;
}
