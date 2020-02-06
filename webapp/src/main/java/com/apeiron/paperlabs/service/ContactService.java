package com.apeiron.paperlabs.service;

import com.apeiron.paperlabs.service.dto.ContactDTO;

/**
 * Interface for sending contact emails.
 * @author Mohamed Belhassen Zinelabidine
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
public interface ContactService {

    void sendContactEmails(ContactDTO contactDTO);

    void sendContactExpertEmails(ContactDTO contactDTO);
}
