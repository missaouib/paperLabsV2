package com.apeiron.docflow.service;

import com.apeiron.docflow.domain.Bookmark;

import java.util.List;
import java.util.Map;

/**
 * Service class for configuration files validation.
 */
public interface FileValidationService {
    /**
     * Checks the integrity of the custom bookmark object configuration in the stepper configuration file.
     *
     * @param allCustomBookmarks list of custom bookmarks configured in the configuration file.
     */
    void checkCustomBookmarksConfigurationIntegrity(Map<String, Bookmark> allCustomBookmarks);

    /**
     * Checks if the custom bookmark list configured in the .docx file matches the one in the configuration file.
     *
     * @param bookmarkIdListFoundInDocXFile list of custom and default bookmarks found in .docx file
     * @param allCustomBookmarks list of custom bookmarks configured in the configuration file.
     */
    void checkDocXFileCustomBookmarksListIntegrity(List<String> bookmarkIdListFoundInDocXFile, Map<String, Bookmark> allCustomBookmarks);
}
