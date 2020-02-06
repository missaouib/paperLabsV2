package com.apeiron.paperlabs.web.rest;

import com.apeiron.docflow.domain.Constraints;
import com.apeiron.docflow.domain.StepperConfig;
import com.apeiron.docflow.service.FileManipulationService;
import com.apeiron.docflow.service.FileMapperService;
import com.apeiron.paperlabs.PaperlabsApp;
import com.apeiron.paperlabs.domain.LegalDocument;
import com.apeiron.paperlabs.service.LegalDocumentService;
import com.apeiron.paperlabs.service.dto.LegalDocumentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URI;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * This FileManipulationResource is working with FileMapperService and FileManipulationService
 * Get StepperConfig as java object
 * Get Workflow as java object
 * Get inputidlist as String Value from the Workflow Object
 * Get inputlist as Input Object from StepperConfig Object
 * Get list of constraints from Workflow according to inputId and value of constraints is selected
 */

@RestController
@RequestMapping("/api")
public class FileManipulationResource {
    private final Logger log = LoggerFactory.getLogger(FileManipulationResource.class);
    private final String TEMPLATE_STEPPER_CONFIG_PATH_PREFIX = "webapp/template-config/config/stepper/";
    private final String TEMPLATE_WORKFLOW_CONFIG_PATH_PREFIX = "webapp/template-config/config/workflow/";
    private final FileMapperService fileMapperService;
    private final FileManipulationService fileManipulationService;
    private final LegalDocumentService legalDocumentService;


    public FileManipulationResource(FileMapperService fileMapperService, FileManipulationService fileManipulationService, LegalDocumentService legalDocumentService
    ) {
        this.fileMapperService = fileMapperService;
        this.fileManipulationService = fileManipulationService;
        this.legalDocumentService = legalDocumentService;
    }

    /**
     * GET  /stepper : get stepper as java object
     * @param legalDocumentId id used when persisting legal document in db
     * @return stepper as java object
     */
    @GetMapping("/stepper")
    public ResponseEntity<StepperConfig> getStepper(@RequestParam String legalDocumentId) throws IOException {
        log.debug("get stepper config...");
        Optional<LegalDocumentDTO> legalDocument = legalDocumentService.findOne(legalDocumentId);
        URL stepperFileUrl;
        if(legalDocument.isPresent()) {
            stepperFileUrl = new File(TEMPLATE_STEPPER_CONFIG_PATH_PREFIX+legalDocument.get().getStepperConfigFilePath()).toURI().toURL();
            return ResponseEntity.ok().body(fileMapperService.jsonToJavaStepper(stepperFileUrl));
        } else {
            throw new NoSuchElementException("No legal document found with id : "+legalDocumentId);
        }
    }

    /**
     * GET  /inputidhasconstraintsasstringlist : get the list of inputId which has constraints only as string list.
     * @param legalDocumentId id used when persisting legal document in db
     * @return list of input id
     */
    @GetMapping("/inputids-with-constraints")
    public List<String> getAllInputIdWithConstraints(@RequestParam String legalDocumentId) throws IOException {
        Optional<LegalDocumentDTO> legalDocument = legalDocumentService.findOne(legalDocumentId);
        if(legalDocument.isPresent()) {
            String workflowFile = TEMPLATE_WORKFLOW_CONFIG_PATH_PREFIX+legalDocument.get().getWorkflowConfigFilePath();
            FileReader file = new FileReader(workflowFile);
            return fileManipulationService.getInputIdsWithConstraints(file);
        } else {
            throw new NoSuchElementException("No legal document found with id : "+legalDocumentId);
        }
    }

    /**
     * Get /constraints: get list of constraints according to specific inputID which has specific value.
     * @param inputId the inputId which has constraints
     * @param value the value is selected that indicate this input has constraints
     * @param legalDocumentId id used when persisting legal document in db
     * @return List<Constraints> constraints
     */
    @GetMapping("/constraints")
    public List<Constraints> getConstraintsByInputIdAndValue(@RequestParam String inputId, @RequestParam String value , @RequestParam String legalDocumentId) throws IOException {
        Optional<LegalDocumentDTO> legalDocument = legalDocumentService.findOne(legalDocumentId);

        if(legalDocument.isPresent()) {
            String workflowFile = TEMPLATE_WORKFLOW_CONFIG_PATH_PREFIX+legalDocument.get().getWorkflowConfigFilePath();
            FileReader file = new FileReader(workflowFile);
            return fileManipulationService.getConstraintsByInputIdAndValue(inputId, value, file);
        }else {
            throw new NoSuchElementException("No legal document found with id : "+legalDocumentId);
        }
    }

}
