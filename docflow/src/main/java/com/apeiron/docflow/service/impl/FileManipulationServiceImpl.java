package com.apeiron.docflow.service.impl;

import com.apeiron.docflow.domain.*;
import com.apeiron.docflow.domain.Bookmark;
import com.apeiron.docflow.domain.Input;
import com.apeiron.docflow.service.FileManipulationService;
import com.apeiron.docflow.service.FileMapperService;
import com.apeiron.docflow.service.FileValidationService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This FileManipulationService is used to manipulate FileMapperService
 * getInputIdsWithConstraints As list of String
 * getInputIdsWithConstraints As Input Object
 * getAllInputs
 * getBookmarks
 * getConstraintsByInputIdAndValue
 */
@Service
public class FileManipulationServiceImpl implements FileManipulationService {
    private final FileMapperService fileMapperService;
    private final FileValidationService fileValidationService;
    private WorkflowsConfig workflowsConfig;
    private StepperConfig stepperConfig;
    private static final String FILE_READER_CLOSING_EXCEPTION_ERROR_MESSAGE = "Caught a: \"%s\", message : unable to close file : \"%s\".";
    private static final String FILE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE = "Caught a: \"%s\", message : file with url : \"%s\" was not found.";

    public FileManipulationServiceImpl(FileMapperService fileMapperService, FileValidationService fileValidationService, WorkflowsConfig workflowsConfig, StepperConfig stepperConfig) {
        this.fileMapperService = fileMapperService;
        this.fileValidationService = fileValidationService;
        this.workflowsConfig = workflowsConfig;
        this.stepperConfig = stepperConfig;
    }

    /**
     * Method to get inputIdWithConstraints as list of Strings.
     * firstly it go to FileMapperService and mapping workflow.json into workflow.java, then
     * we getInputIdsWithConstraints As list of String.
     * @condition inputId which has constraints
     * @param workflowFile the location of Workflow.json which is "workflowUrl" in DocumentModelDTO
     * @return list<String> inputIdList As String Value.
     */
    @Override
    public List<String> getInputIdsWithConstraints(FileReader workflowFile) throws IOException {
        List<String> inputIdHasConstraintsListAsString = new ArrayList<>();

        try{
            workflowsConfig = fileMapperService.jsonToJavaWorkflow(workflowFile);
            List<Workflow> workflowList = workflowsConfig.getWorkflow();
            for (Workflow workflow1 : workflowList) {
                if (!workflow1.getValuesConstraints().isEmpty())
                    inputIdHasConstraintsListAsString.add(workflow1.getInputId());
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format(FILE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE, e.getClass().getName(), workflowFile));
        } catch (IOException e) {
            throw new IOException(String.format(FILE_READER_CLOSING_EXCEPTION_ERROR_MESSAGE,e.getClass().getName(), workflowFile));
        }

        return inputIdHasConstraintsListAsString;
    }

    /**
     * Method to get inputList as Java Object inside LinkedHashMap.
     * firstly it go to FileMapperService and mapping Stepperr.json into Stepperr.java to getInputIdsWithConstraints As Input Object, then
     * it go to FileManipulationService and mapping Workflow.json into Workflow.java to getInputIdsWithConstraints As list of String
     * @condition inputIdList equals inputList for the inputs which have constraints
     * @param stepperFile the location of Stepper.json which is "stepperUrl" in DocumentModelDTO
     * @param urlWorkflow the location of Workflow.json which is "workflowUrl" in DocumentModelDTO
     * @return LinkedHashMap<String,Input> inputLinkedHashMap As Input Object.
     */
    @Override
    public Map<String, Input> getAllInputsWithConstraints(URL stepperFile, FileReader urlWorkflow) throws IOException{
        LinkedHashMap<String,Input> inputIdHasConstraintsMap = new LinkedHashMap<>();

        try{
            stepperConfig = fileMapperService.jsonToJavaStepper(stepperFile);
            List<Input> inputIdHasConstraintsList = stepperConfig.getInputs();
            List <String> inputIdHasConstraintsListAsString = getInputIdsWithConstraints(urlWorkflow);
            for (String inputId : inputIdHasConstraintsListAsString) {
                for (Input input:inputIdHasConstraintsList) {
                    if (inputId.equals(input.getId()))
                        inputIdHasConstraintsMap.put(input.getId(),input);
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format(FILE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE, e.getClass().getName(), urlWorkflow));
        } catch (IOException e) {
            throw new IOException(String.format(FILE_READER_CLOSING_EXCEPTION_ERROR_MESSAGE,e.getClass().getName(), urlWorkflow));
        }

        return inputIdHasConstraintsMap;
    }

    /**
     * Gets custom bookmark list from the stepper configuration file and tests their integrity.
     *
     * @param stepperFile the location of stepper's JSON configuration file.
     * @return allInputs list which contains all custom inputs configured in the stepper configuration file.
     */
    @Override
    public Map<String,Input> getAllInputs(URL stepperFile) throws IOException{
        HashMap<String,Input> allInputs = new HashMap<>();

        try{
            stepperConfig = fileMapperService.jsonToJavaStepper(stepperFile);
            List<Input> inputList = stepperConfig.getInputs();
            for (Input input:inputList) {
                allInputs.put(input.getId(),input);
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format(FILE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE, e.getClass().getName(), stepperFile));
        } catch (IOException e) {
            throw new IOException(String.format(FILE_READER_CLOSING_EXCEPTION_ERROR_MESSAGE,e.getClass().getName(), stepperFile));
        }

        return allInputs;
    }

    /**
     *
     * @param blocId
     * @param stepperFile
     * @return
     * @throws IOException
     */
    @Override
    public  List<String> getInputsByBlocId(String blocId, URL stepperFile) throws IOException {
        List<String> inputsByBlocId = new ArrayList<>();
            stepperConfig = fileMapperService.jsonToJavaStepper(stepperFile);
            List<Bloc> blocList = stepperConfig.getBlocs();
            blocList.forEach(res -> {
                if(blocId.equals(res.getId())) {
                    final String[] inputList = res.getInputs();
                    for(int i=0;i<inputList.length;i++) {
                        inputsByBlocId.add(inputList[i]);
                    }
                }
            });
        return inputsByBlocId;
    }

    /**
     *
     * @param inputId
     * @param stepperFile
     * @return
     * @throws IOException
     */
    @Override
    public  Input getInputByInputId(String inputId, URL stepperFile) throws IOException {
        stepperConfig = fileMapperService.jsonToJavaStepper(stepperFile);
        List<Input> inputs = stepperConfig.getInputs();
        int length = inputs.size();
        Input input = new Input();

        for(int i=0;i<length;i++) {
            if(inputs.get(i).getId().equals(inputId)) {
                input = inputs.get(i);
            }
        }
        return input;
    }

    /**
     *
     * @param bookmarkId
     * @param stepperFile
     * @return
     * @throws IOException
     */
    @Override
    public  Bookmark getBookmarkByBookmarkId(String bookmarkId, URL stepperFile) throws IOException {
        stepperConfig = fileMapperService.jsonToJavaStepper(stepperFile);
        List<Bookmark> bookmarks = stepperConfig.getBookmarks();
        int length = bookmarks.size();
        Bookmark bookmark = new Bookmark();

        for(int i=0;i<length;i++) {
            if(bookmarks.get(i).getBookmarkId().equals(bookmarkId)) {
                bookmark = bookmarks.get(i);
            }
        }
        return bookmark;
    }

    /**
     * Gets custom bookmark list from the stepper configuration file and tests their integrity.
     *
     * @param stepperFile the location of stepper's JSON configuration file.
     * @return  allBookmarks list which contains all custom bookmarks configured in the stepper configuration file.
     */
    @Override
    public Map<String, Bookmark> getAllCustomBookmarks(URL stepperFile) throws IOException{
        HashMap<String,Bookmark> allBookmarks = new HashMap<>();

        try{
            stepperConfig = fileMapperService.jsonToJavaStepper(stepperFile);
            List<Bookmark> bookmarkList = stepperConfig.getBookmarks();
            for (Bookmark bookmark : bookmarkList) {
                allBookmarks.put(bookmark.getBookmarkId(), bookmark);
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format(FILE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE, e. getClass().getName(), stepperFile));
        } catch (IOException e) {
            throw new IOException(String.format(FILE_READER_CLOSING_EXCEPTION_ERROR_MESSAGE, e.getClass().getName(), stepperFile));
        }
        fileValidationService.checkCustomBookmarksConfigurationIntegrity(allBookmarks);
        return allBookmarks;
    }

    /**
     * Method to get list of constraints according to specific inputID which has specific value.
     * firstly it go to Document Service and mapping workflow.json into workflow.java, then
     * it return a list of Constraints of this inputId if the value of valueConstraints is "selected"
     * @param inputId the inputId which has constraints
     * @param value the value is selected that indicate this input has constraints
     * @param workflowFile the location of Workflow.json which is "workflowUrl" in DocumentModelDTO
     * @return List<Constraints> constraints
     */
    @Override
    public List<Constraints> getConstraintsByInputIdAndValue(String inputId, String value, FileReader workflowFile) throws IOException {

        List<Constraints> constraintList = new ArrayList<>();
        try {
            workflowsConfig = fileMapperService.jsonToJavaWorkflow(workflowFile);
            List<Workflow> workflowList = workflowsConfig.getWorkflow();
            for (Workflow workflow1 : workflowList) {
                if (workflow1.getInputId().equals(inputId)) {
                    for (ValuesConstraints valuesConstraints : workflow1.getValuesConstraints()) {
                        if (valuesConstraints.getValue().equals(value)) {
                            constraintList.addAll(valuesConstraints.getConstraints());
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format(FILE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE, e.getClass().getName(), workflowFile));
        } catch (IOException e) {
            throw new IOException(String.format(FILE_READER_CLOSING_EXCEPTION_ERROR_MESSAGE, e.getClass().getName(), workflowFile));
        }
        return constraintList;
    }
}
