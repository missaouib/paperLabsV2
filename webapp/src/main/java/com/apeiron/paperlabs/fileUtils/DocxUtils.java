package com.apeiron.paperlabs.fileUtils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.docx4j.Docx4J;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.RangeFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.ContentAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * A Class DocxUtils service.
 *
 * @author Ghandour Abdelkrim
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
@Service
public class DocxUtils {
    private final Logger log = LoggerFactory.getLogger(DocxUtils.class);

    /**
     * Method for delete the docx bookmarks
     * @param path the docx path.
     */
    public void deleteBookmarks(String path){
        log.debug("request for delete all bookmarks in the docx file : {}", path);
        WordprocessingMLPackage wordMLPackage = null;

        {
            try {
                wordMLPackage = WordprocessingMLPackage
                    .load(new java.io.File(path));
            } catch (Docx4JException e) {
                log.error("error reading docx file",e);
            }
        }

        MainDocumentPart documentPart;
        if (wordMLPackage!=null) {
            documentPart = wordMLPackage.getMainDocumentPart();
            org.docx4j.wml.Document wmlDocumentEl = documentPart
                .getJaxbElement();
            Body body = wmlDocumentEl.getBody();
            try {
                fixRange(body.getContent());

                Docx4J.save(wordMLPackage, new java.io.File(path));

            } catch (Exception e) {
                log.error("error removing bookmarks from the docx file",e);
            }
        }

    }

    /**
     * find the bookmarks in all paragraphs and delete
     * @param paragraphs list of the paragraphs in the docx
     * @throws Exception Exception
     */
    private  void fixRange(List<Object> paragraphs) throws Exception {

        RangeFinder rt = new RangeFinder("CTBookmark", "CTMarkupRange");
        new TraversalUtil(paragraphs, rt);

        for (CTBookmark bm : rt.getStarts()) {
            try {
                List<Object> theList = null;
                if (bm.getParent() instanceof List) {
                    theList = (List)bm.getParent();
                } else {
                    theList = ((ContentAccessor)(bm.getParent())).getContent();
                }
                Object deleteMe = null;
                for (Object ox : theList) {
                    if (XmlUtils.unwrap(ox).equals(bm)) {
                        deleteMe = ox;
                        break;
                    }
                }
                if (deleteMe!=null) {
                    theList.remove(deleteMe);
                }
            } catch (ClassCastException cce) {
                log.error(cce.getMessage(), cce);
            }
        }

    }

    /**
     * Method for Encrypt the pdf File
     *
     * @param pdfFile The Pdf File
     * @throws IOException IOException
     */

    public void encryptPdf(File pdfFile) throws IOException {

        PDDocument document = null;
        try {
            document = PDDocument.load(pdfFile);

            AccessPermission permission = new AccessPermission();
            permission.setCanPrint(true);
            permission.setCanPrintDegraded(false);
            permission.setCanExtractContent(false);
            permission.setCanExtractForAccessibility(false);
            permission.setCanFillInForm(true);
            permission.setCanModify(false);
            permission.setCanAssembleDocument(false);
            permission.setReadOnly();

            StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(null, null, permission);
            protectionPolicy.setEncryptionKeyLength(128);
            protectionPolicy.setPreferAES(true);
            protectionPolicy.setPermissions(permission);

            document.protect(protectionPolicy);
            document.save(pdfFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error Encrypting the pdf file for Encryption !", e);
        }
    }

}
