package com.apeiron.paperlabs.web.rest;


import com.apeiron.paperlabs.service.ContactService;
import com.apeiron.paperlabs.service.dto.ContactDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST controller to send emails to paperlabs support team.
 */
@RestController
@RequestMapping("/api")
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(ContactResource.class);

    private ContactService contactService;

    public ContactResource(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * {@code POST  /contact} : Send contact object as an email to paperlabs support team.
     *
     */
    @PostMapping("/contact")
    public ResponseEntity<ContactDTO> sendContactEmail(@Valid @RequestBody ContactDTO contactDTO) {
        log.debug("REST request to send email Contact: {}", contactDTO);

        contactService.sendContactEmails(contactDTO);
        return ResponseEntity.ok().body(contactDTO);
    }

    /**
     * {@code POST  /contact} : Send contact object as an email to paperlabs experts team.
     *
     */
    @PostMapping("/contactExpert")
    public ResponseEntity<ContactDTO> sendContactExpertEmail(@Valid @RequestBody ContactDTO contactDTO) {
        log.debug("REST request to send email to experts Contact: {}", contactDTO);

        contactService.sendContactExpertEmails(contactDTO);
        return ResponseEntity.ok().body(contactDTO);
    }

}
