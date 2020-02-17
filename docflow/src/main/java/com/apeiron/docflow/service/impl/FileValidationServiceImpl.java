package com.apeiron.docflow.service.impl;

import com.apeiron.docflow.domain.Bookmark;
import com.apeiron.docflow.service.FileValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Service class for configuration files validation.
 */
@Service
public class FileValidationServiceImpl implements FileValidationService {
    private final Logger log = LoggerFactory.getLogger(FileValidationServiceImpl.class);
    private static final String BOOKMARK_TYPE_DEFAULT = "DEFAULT";

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkCustomBookmarksConfigurationIntegrity(Map<String, Bookmark> allCustomBookmarks) {
        Bookmark bookmark;
        for(Map.Entry<String, Bookmark> bookmarkEntry: allCustomBookmarks.entrySet()) {
            bookmark = bookmarkEntry.getValue();
            if (BOOKMARK_TYPE_DEFAULT.equals(bookmark.getType()) && bookmark.getValue() == null) {
                throw new NullPointerException(String.format("Bookmark value cannot be null for %s type while configuring a bookmark in .JSON configuration file.", BOOKMARK_TYPE_DEFAULT));
            }
            if(bookmark.getBookmarkId() == null) {
                throw new NullPointerException("Bookmark id cannot be null while configuring a bookmark in .JSON configuration file.");
            }
            if(bookmark.getBookmarkId().isEmpty()) {
                throw new IllegalArgumentException("Bookmark id cannot be empty while configuring a bookmark in .JSON configuration file.");
            }
            if(bookmark.getType() == null) {
                throw new NullPointerException("Bookmark type cannot be null while configuring a bookmark in .JSON configuration file.");
            }
            if(bookmark.getType().isEmpty()) {
                throw new IllegalArgumentException("Bookmark type cannot be empty while configuring a bookmark in .JSON configuration file.");
            }
            if(bookmark.isBold() == null || bookmark.isCapitalized() == null || bookmark.isItalic() == null) {
                throw new NullPointerException("Bookmark attributes bold, capitalized and italic cannot be null while configuring a bookmark in .JSON configuration file.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkDocXFileCustomBookmarksListIntegrity(List<String> bookmarkIdListFoundInDocXFile, Map<String, Bookmark> allCustomBookmarks) {
        String bookmarkKey;
        for(Map.Entry<String, Bookmark> bookmarkEntry : allCustomBookmarks.entrySet()) {
            bookmarkKey = bookmarkEntry.getKey();
            if(!bookmarkIdListFoundInDocXFile.contains(bookmarkKey)) {
            	 System.out.println("lllllllllllllllllllll exceptionnnnnnnnnnnn");
                throw new NoSuchElementException(String.format("Caught a : \"%s\", message: bookmark with name \"%s\" not found among .docx file custom bookmark list", "NoSuchElementException", bookmarkKey));
            }
        }
    }

}
