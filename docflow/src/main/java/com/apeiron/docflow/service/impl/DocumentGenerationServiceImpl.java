package com.apeiron.docflow.service.impl;

import com.apeiron.docflow.domain.Bookmark;
import com.apeiron.docflow.domain.Input;
import com.apeiron.docflow.domain.InputData;
import com.apeiron.docflow.service.DocumentGenerationService;
import com.apeiron.docflow.service.FileManipulationService;
import com.apeiron.docflow.service.FileValidationService;
import com.apeiron.docflow.service.InputProcessingService;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import java.io.*;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service class for managing document generation.
 */
@Service
public class DocumentGenerationServiceImpl implements DocumentGenerationService {
    private final FileManipulationService fileManipulationService;
    private final FileValidationService fileValidationService;
    private final InputProcessingService inputProccesingService;
    private static final String BOOKMARK_TYPE_DEFAULT = "DEFAULT";
    private static final String BOOKMARK_TYPE_PARAGRAPH_WITH_BOOKMARKS = "PARAGRAPH_WITH_BOOKMARKS";
    private static final String BOOKMARK_TYPE_PAGE_WITH_BOOKMARKS = "PAGE_WITH_BOOKMARKS";
    private static final String BOOKMARK_TYPE_USER_INPUT = "USER_INPUT";
    private static final String BOOKMARK_TYPE_INPUT_GENERATOR = "INPUT_GENERATOR";
    private static final String SIMPLE_TYPES_KEY = "text";
    private final List<String> simpleTypes = new ArrayList<>(Arrays.asList("TEXT", "EMAIL", "IMAGE", "NUMBER", "TEXTAREA", "DATE", "TIME", "COUNTRY"));
    private final List<String> multiValueTypes = new ArrayList<>(Arrays.asList("SELECT", "RADIO", "CHECKBOX", "SELECT_INPUT_GENERATOR"));
    private Map<String, InputData> stepperData; // contains inputs returned from stepper form
    private Map<String, Input> inputsWithBookmarks = new HashMap<>();
    private Map<String, InputData> stepperDataWithBookmarks;
    private Map<String, Bookmark> allCustomBookmarks = new HashMap<>();
    private Map<String, String> paragraphsWithBookmarksToDelete; // bookmark list of bookmarks that contain other bookmarks
    private Map<String, String> pagesWithBookmarksToDelete;
    private Map<String, String> customBookmarksValues;


    private int numberOfBlocs; // number of times a bloc should be duplicated
    private List<String> generatedBlocListBySelectInputGeneratorId;
    private List<String> listInputsByBlocId; // list of inputs in a given bloc
    private List<String> listConcernedInputsOfSelectInputGenerator; // list that contains all concerned input contained in concerned blocs of a given select_input_generator
    private List<Bookmark> selectInputGeneratorCustomBookmarksList;
    private List<String> bookmarkConcernedInputsList; // we convert value attribute's value (input prefix) to string list ["input_nom_prénom_associé_", "input_nombre_parts_associé_"]
    private String paragraphToDuplicate;
    private String paragraphAfterDuplication; // resulting paragraph after duplication
    private List<String> inputValuesListToFillDuplicatedParagraphWith;


    private XWPFDocument document = new XWPFDocument();
    private List<String> bookmarkIdListFoundInDocXFile;
    private URL urlStepper;
    private String filledFileName;
    
    
    private static final String FILLED_FILE_NAME_PREFIX="D:\\Rami\\"; ///var/www/html/"; 

    //private static final String FILLED_FILE_NAME_PREFIX="D:/Rami/paperlabs-master/webapp/target/classes/static/folder/";//"D:\\test_folder\\";//"folder"
    public DocumentGenerationServiceImpl(FileManipulationService fileManipulationService, FileValidationService fileValidationService, InputProcessingService inputProccesingService) {
        this.fileManipulationService = fileManipulationService;
        this.fileValidationService = fileValidationService;
        this.inputProccesingService = inputProccesingService;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String generateDocXFile(Map<String, InputData> stepperData, URL urlStepper, String urlDocument, URL urlWorkflow) throws IOException{
        String[] bookmarksReferences;
        Input inputWithBookmarks;
        this.urlStepper = urlStepper;
        this.stepperData = inputProccesingService.refactorInput(stepperData);
        paragraphsWithBookmarksToDelete = new HashMap<>();
        pagesWithBookmarksToDelete = new HashMap<>();
        customBookmarksValues= new HashMap<>();
        if(this.stepperData == null) {
            throw new NullPointerException("Unable to generate document, stepper data cannot be null.");
        }
        if(urlStepper == null) {
            throw new NullPointerException("Unable to generate document, stepper URL cannot be null.");
        }
        if(urlDocument == null) {
            throw new NullPointerException("Unable to generate document, ms Word (.docx) model URL cannot be null.");
        }
        allCustomBookmarks = this.fileManipulationService.getAllCustomBookmarks(urlStepper);
        filterInputsWithBookmarks();
        filterStepperDataInputsWithBookmarks();
        openFile(urlDocument);

        List<String> selectInputGeneratorList = getInputIdsOfTypeSelectGeneratedInput(); // get list of inputs of type select_generated_input
        selectInputGeneratorList.forEach(inputId -> {
            FileReader fileReader;
            try {
                generatedBlocListBySelectInputGeneratorId = new ArrayList<>();
                fileReader = new FileReader(urlWorkflow.getPath());
                fileManipulationService.getConstraintsByInputIdAndValue(inputId, "select_input_generator", fileReader).forEach(constraint ->  //get list of bloc constraints
                    generatedBlocListBySelectInputGeneratorId.add(constraint.getElementId())
                );
                listConcernedInputsOfSelectInputGenerator = new ArrayList<>();
                generatedBlocListBySelectInputGeneratorId.forEach(blocId -> { // get list of inputs in a given bloc
                    listInputsByBlocId = getInputIdsByBlocId(blocId);
                    // add bloc's list of inputs to the select_generated_input's related inputs list
                    listConcernedInputsOfSelectInputGenerator.addAll(listInputsByBlocId);
                });
                selectInputGeneratorCustomBookmarksList = getBookmarksByInputOfTypeSelectGeneratedInput(inputId, this.stepperData.get(inputId).getValue()); // list of select_generated_input's related bookmarks
                selectInputGeneratorCustomBookmarksList.forEach(bookmark -> {
                            List<XWPFParagraph> paragraphs = document.getParagraphs();
                            boolean found = false;
                            for (XWPFParagraph paragraph1 : paragraphs)
                            {
                                CTP ctp = paragraph1.getCTP();
                                List<CTBookmark> bookmarks = ctp.getBookmarkStartList();
                                for(CTBookmark bookmark1 : bookmarks)
                                {
                                    if(bookmark1.getName().equals(bookmark.getBookmarkId()))
                                    {
                                        paragraphToDuplicate = paragraph1.getText();
                                        found = true;
                                        break;
                                    }
                                }
                                if(found) break;
                            }
                    numberOfBlocs = Integer.parseInt(this.stepperData.get(inputId).getValue()); // get number of repetitions
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i=0; i< numberOfBlocs; i++) { // duplicate the paragraph
                        stringBuilder.append(paragraphToDuplicate);
                        stringBuilder.append("\n");
                    }
                    paragraphAfterDuplication = stringBuilder.toString();
                    bookmarkConcernedInputsList = getBookmarkValuePrefixArray(bookmark.getValue());
                    inputValuesListToFillDuplicatedParagraphWith = new ArrayList<>();
                    for(int i=1; i<=numberOfBlocs; i++) { // create list of values inputs to fill duplicated paragraph
                        final int j = i;
                        bookmarkConcernedInputsList.forEach(concernedInputPrefix ->
                            inputValuesListToFillDuplicatedParagraphWith.add(this.stepperData.get(concernedInputPrefix+j) !=null ?
                                    this.stepperData.get(concernedInputPrefix+j).getValue() : "_______________ ")
                                                                                               
                        );
                    }
                    inputValuesListToFillDuplicatedParagraphWith.forEach(res ->
                            paragraphAfterDuplication = paragraphAfterDuplication.replaceFirst("_______________ ", res) // fill duplicated paragraph with data
                    );
                    updateGeneratedInputBookmarksInCustomBookmarksList(bookmark.getBookmarkId(), paragraphAfterDuplication); // update bookmark value with new one in all custom bookmarks list
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        for(Map.Entry<String, InputData> stepperDataWithBookmarkDataEntry : this.stepperDataWithBookmarks.entrySet()) {
            inputWithBookmarks = this.inputsWithBookmarks.get(stepperDataWithBookmarkDataEntry.getKey());
            bookmarksReferences = getBookmarksReferencesPerInputByInputValue(inputWithBookmarks, stepperDataWithBookmarkDataEntry.getValue().getValue());
            groupInputBookmarksByBookmarkType(bookmarksReferences, stepperDataWithBookmarkDataEntry.getValue().getValue());
        }
        filledFileName = new SimpleDateFormat("'docx_filled_'yyyyMMddHHmmss'.docx'").format(new Date());
        getBookmarkIdListFoundInDocXFile();
        fileValidationService.checkDocXFileCustomBookmarksListIntegrity(bookmarkIdListFoundInDocXFile, allCustomBookmarks);
        insertAtBookmark();
        saveAs(filledFileName);
        return FILLED_FILE_NAME_PREFIX+filledFileName;
    }

    /**
     * For each stepperDataWithBookmark object get the bookmarks references array from inputsWithBookmarks.
     * <p>
     * In case of input of type text, number or text area, input's bookmarkIdPerValue attribute contains only
     * one entry with "text" as key and get("text") will return String array of bookmarkReferences of that
     * input if it has any. (see any text input is stepper JSON config).
     *
     * @param inputWithBookmarks input object to get bookmark references extracted from
     * @param stepperDataInputValue input value returned from stepper
     * @return bookmarksReferences array of the given input's bookmark references
     * */
    private String[] getBookmarksReferencesPerInputByInputValue(Input inputWithBookmarks, String stepperDataInputValue) {
        String[] bookmarksReferences;
        if(simpleTypes.contains(inputWithBookmarks.getType().toUpperCase())) {
            bookmarksReferences = inputWithBookmarks.getBookmarkIdPerValue().get(SIMPLE_TYPES_KEY);
        }else if(multiValueTypes.contains(inputWithBookmarks.getType().toUpperCase())){
            bookmarksReferences = inputWithBookmarks.getBookmarkIdPerValue().get(stepperDataInputValue); // get input's bookmarkReferences String array using input's value as key.
        }else {
            throw new NoSuchElementException(String.format("Unknown input type: \"%s\"", inputWithBookmarks.getType()));
        }
        return bookmarksReferences;
    }

    /**
     * Gets the list of bookmarks returned from the .docx file.
     * */
    private void getBookmarkIdListFoundInDocXFile() {
        bookmarkIdListFoundInDocXFile = new ArrayList<>();
        this.document.getParagraphs().forEach(p -> p.getCTP().getBookmarkStartList().forEach(bookmark ->
                bookmarkIdListFoundInDocXFile.add(bookmark.getName())
        ));
    }

    /**
     * Check if bookmark is of type DEFAULT(default text needs to be used) or USER_INPUT (user's text needs to be used).
     * <p>
     * When bookmark is of type DEFAULT, default value will be used and in this case it can't be null. (empty String is accepted but not null)
     *
     * @param bookmarksReferences bookmarksReferences array of the given input's bookmark references
     * @param stepperDataInputValue input value returned from stepper
     * */
    private void groupInputBookmarksByBookmarkType(String[] bookmarksReferences, String stepperDataInputValue) {
        Bookmark bookmark;
        String bookmarkValue;

        for (String bookmarksReference : bookmarksReferences) {
            bookmark = this.allCustomBookmarks.get(bookmarksReference);
            switch (bookmark.getType()) {
                case BOOKMARK_TYPE_DEFAULT:
                    bookmarkValue = bookmark.getValue();
                    customBookmarksValues.put(bookmark.getBookmarkId(), bookmarkValue);
                    break;
                case BOOKMARK_TYPE_USER_INPUT:
                    customBookmarksValues.put(bookmark.getBookmarkId(), stepperDataInputValue);
                    break;
                case BOOKMARK_TYPE_PARAGRAPH_WITH_BOOKMARKS:
                    bookmarkValue = bookmark.getValue();
                    paragraphsWithBookmarksToDelete.put(bookmark.getBookmarkId(), bookmarkValue);
                    break;
                case BOOKMARK_TYPE_INPUT_GENERATOR:
                    customBookmarksValues.put(bookmark.getBookmarkId(), bookmark.getValue());
                    break;
                case BOOKMARK_TYPE_PAGE_WITH_BOOKMARKS:
                    bookmarkValue = bookmark.getValue();
                    pagesWithBookmarksToDelete.put(bookmark.getBookmarkId(), bookmarkValue);
                    break;
                default:
                    throw new NullPointerException("Bookmark type cannot be null while configuring a bookmark in .JSON configuration file.");
            }
        }
    }

    /**
     * Filters all inputs to keep only those with bookmarks
     * */
    private void filterInputsWithBookmarks()throws IOException{
        Map<String, Input> allInputs = this.fileManipulationService.getAllInputs(urlStepper);
        for(Map.Entry<String, Input> inputEntry : allInputs.entrySet()) {
            if(inputEntry.getValue().getBookmarkIdPerValue().size() != 0) {
                this.inputsWithBookmarks.put(inputEntry.getKey(), inputEntry.getValue());
            }
        }
    }

    /**
     * Filters stepperDataInputs to keep only those with bookmarks using inputsWithBookmarks
     * */
    private void filterStepperDataInputsWithBookmarks(){
        stepperDataWithBookmarks = new HashMap<>();
        for(Map.Entry<String, InputData> inputEntry : this.stepperData.entrySet()) {
            if(this.inputsWithBookmarks.get(inputEntry.getKey()) != null) {
                this.stepperDataWithBookmarks.put(inputEntry.getKey(), inputEntry.getValue());
            }
        }
    }

    /**
     * Opens a .docx file given a url.
     *
     * @param fileName name of the model file.
     * */
    private void openFile(String fileName) throws IOException {
        File file = new File(fileName);
        try (FileInputStream fis = new FileInputStream(file)) {
            this.document = new XWPFDocument(fis);
        }
    }

    /**
     * Generates a .docx file.
     *
     * @param fileName name of file to generate.
     * */
    private void saveAs(String fileName) throws IOException {
        File generatedDocDirectoryPath = new File(FILLED_FILE_NAME_PREFIX);
        boolean directoryIsCreated = true;
        if (!generatedDocDirectoryPath.exists()) {
            directoryIsCreated = generatedDocDirectoryPath.mkdir();
        }
        if (directoryIsCreated) {
            File file = new File(FILLED_FILE_NAME_PREFIX+fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                this.document.write(fos);
            }
        }
        else {
            throw new FileSystemException("Error creating directory with path: " + FILLED_FILE_NAME_PREFIX);
        }
    }

    /**
     * Deletes the given paragraph.
     *
     * @param para the paragraph to delete.
     * */
    private void deleteParagraphWithBookmarks(XWPFParagraph para) throws IOException{
        this.document.removeBodyElement(this.document.getPosOfParagraph(para));
        try (OutputStream fos = new FileOutputStream(filledFileName)){
            this.document.write(fos);
        }
    }

    /**
     * Configures the given run according to the configuration of the given custom bookmark.
     *
     * @param run run that contains text to replace bookmark's one.
     * @param configuredBookmark custom bookmark configured in the stepper configuration file.
     * */
    private void createCustomRun(XWPFRun run, Bookmark configuredBookmark) {
        run.setBold(configuredBookmark.isBold());
        run.setCapitalized(configuredBookmark.isCapitalized());
        run.setItalic(configuredBookmark.isItalic());
        run.setUnderline(getUnderlinePatternsByName(configuredBookmark.getUnderline()));
        if(configuredBookmark.getFontSize() != 0) {
            run.setFontSize(configuredBookmark.getFontSize());
        }
        if(configuredBookmark.getFontFamily() != null) {
            run.setFontFamily(configuredBookmark.getFontFamily());
        }
        if(configuredBookmark.getColor() != null) {
            run.setColor(configuredBookmark.getColor());
        }
    }

    private UnderlinePatterns getUnderlinePatternsByName(String underlinePatternsName) {
        if (underlinePatternsName != null) {
            switch (underlinePatternsName.toUpperCase()) {
                case "SINGLE":
                    return UnderlinePatterns.SINGLE;
                case "DOUBLE":
                    return UnderlinePatterns.DOUBLE;
                case "DASH":
                    return UnderlinePatterns.DASH;
                default:
                    return UnderlinePatterns.NONE;
            }
        }
        else {
            return UnderlinePatterns.NONE;
        }
    }

    /**
     * Returns the list of input ids for a given bloc id
     *
     * @param blocId bloc id
     * @return list of bloc's inputs
     */
    private List<String> getInputIdsByBlocId(String blocId) {
        List<String> inputIdByBlocId = new ArrayList<>();
        try {
            inputIdByBlocId = this.fileManipulationService.getInputsByBlocId(blocId, urlStepper);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputIdByBlocId;
    }

    /**
     * Returns list of input ids of type 'select_input_generator'
     *
     * @return list of input ids of type 'select_input_generator'
     */
    private List<String> getInputIdsOfTypeSelectGeneratedInput() {
        List<String> inputIdOfTypeSelectGeneratedInput = new ArrayList<>();

        for (Map.Entry<String, InputData> input : this.stepperData.entrySet()) {
            try {
                Input in = this.fileManipulationService.getInputByInputId(input.getKey(), urlStepper);
                if ("select_input_generator".equals(in.getType())) {
                    inputIdOfTypeSelectGeneratedInput.add(in.getId());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return inputIdOfTypeSelectGeneratedInput;
    }

    /**
     * Returns list of bookmarks related to a given input id
     *
     * @return list of bookmarks
     */
    private List<Bookmark> getBookmarksByInputOfTypeSelectGeneratedInput(String inputId, String inputValue) {
        String[] array;
        List<Bookmark> list = new ArrayList<>();
        try {
            array = this.fileManipulationService.getInputByInputId(inputId, urlStepper).getBookmarkIdPerValue().get(inputValue);
            for(String arrayEntry: array) {
                list.add(this.fileManipulationService.getBookmarkByBookmarkId(arrayEntry, urlStepper));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  list;
    }

    /**
     * Returns list of generated input id prefixes extracted from a bookmark's value attribute
     *
     * @param value bookmark's value attribute
     * @return list of generated input id prefixes
     */
    private List<String> getBookmarkValuePrefixArray(String value) {
        List<String> list = new ArrayList<>();
        String[] separated = value.split(" ");
        Collections.addAll(list, separated);
        return list;
    }

    /**
     * Update bookmarks related to a generated input value in the list containing all custom bookmarks
     *
     * @param bookmarkId bookmark id
     * @param bookmarkValue bookmark's value attribute
     */
    private void updateGeneratedInputBookmarksInCustomBookmarksList(String bookmarkId, String bookmarkValue) {
        Bookmark bookmark = allCustomBookmarks.get(bookmarkId);
        bookmark.setValue(bookmarkValue);
        allCustomBookmarks.replace(bookmarkId, bookmark);
    }

    /**
     * Applies the actual changes (bookmarkValue) on the actual bookmark(bookmarkName) for each bookmark in the .docx file that matches a bookmark of the given list
     * DocX file contain generated bookmarks (example _GoBack bookmark) that needs to be ignored.
     * <p>
     * There are three types of custom bookmarks, USER_INPUT, DEFAULT and PARAGRAPH_WITH_BOOKMARKS.
     * <ul>
     * <li>USER_INPUT : when the bookmark is applied, used input is used.</li>
     * <li>DEFAULT : when the bookmark is applied, its default value is used.</li>
     * <li>PARAGRAPH_WITH_BOOKMARKS : when the bookmark's content to delete contains other bookmarks. </li>
     * <p>
     * .docx file may contain other bookmarks other than the custom ones manually configured.
     * </ul>
     */
    private void insertAtBookmark() throws IOException{
        LinkedList<XWPFParagraph> paraList;
        ListIterator<XWPFParagraph> paraIter;
        XWPFParagraph para;
        List<CTBookmark> bookmarkListFoundInOneDocXFileParagraph;
        Iterator<CTBookmark> bookmarkIter;
        CTBookmark docXBookmark;
        Bookmark customBookmark;
        XWPFRun run;
        paraList = new LinkedList<>(this.document.getParagraphs());
        paraIter = paraList.listIterator();
        bookmarkIdListFoundInDocXFile = new ArrayList<>();

        while (paraIter.hasNext()) {
            para = paraIter.next();
            bookmarkListFoundInOneDocXFileParagraph = para.getCTP().getBookmarkStartList();
            bookmarkIter = bookmarkListFoundInOneDocXFileParagraph.iterator();
            Node nextNode;
            while (bookmarkIter.hasNext()) {
                docXBookmark = bookmarkIter.next();
                customBookmark = allCustomBookmarks.get(docXBookmark.getName());
                if (customBookmarksValues.containsKey(docXBookmark.getName())) {
                    run = para.createRun();

                    if(customBookmarksValues.get(docXBookmark.getName()).contains("\n")) {
                        String[] separated = customBookmarksValues.get(docXBookmark.getName()).split("\n");
                        for(String sep: separated) {
                            if(sep.contains("-")) {
                                run.addTab();
                            }
                            run.setText(sep);
                            run.addCarriageReturn();
                        }
                    }else {
                         run.setText(customBookmarksValues.get(docXBookmark.getName()));
                    }
                    createCustomRun(run, customBookmark);
                    nextNode = docXBookmark.getDomNode().getNextSibling();
                    while (!(nextNode.getNodeName().contains("bookmarkEnd"))) {
                        para.getCTP().getDomNode().removeChild(nextNode);
                        nextNode = docXBookmark.getDomNode().getNextSibling();
                    }
                    para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), docXBookmark.getDomNode());
                } else if (paragraphsWithBookmarksToDelete.containsKey(docXBookmark.getName())) {
                    deleteParagraphWithBookmarks(para);
                    paraIter.remove();
                    if (paraIter.hasNext()) {
                        XWPFParagraph nextPara = paraIter.next();
                        if (nextPara.getText().isEmpty()) {
                            deleteParagraphWithBookmarks(nextPara);
                            paraIter.remove();
                            paraIter.next();
                        }
                        paraIter.previous();
                    }
                    break;
                } else if (pagesWithBookmarksToDelete.containsKey(docXBookmark.getName())) {
                    while (paraIter.hasNext()) {
                        deleteParagraphWithBookmarks(para);
                        XWPFParagraph nextPara = paraIter.next();
                        paraIter.previous();
                        if (!nextPara.getRuns().isEmpty() && nextPara.getRuns().get(0).getText(0) == null) {
                            break;
                        }
                        para = paraIter.next();
                        paraIter.remove();
                    }
                    if (!paraIter.hasNext()) {
                        deleteParagraphWithBookmarks(para);
                    }
                    break;
                }
            }
        }
    }

}
