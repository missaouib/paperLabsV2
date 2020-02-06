package com.apeiron.paperlabs.service.impl;

import com.apeiron.paperlabs.config.ApplicationProperties;
import com.apeiron.paperlabs.service.ContactService;
import com.apeiron.paperlabs.service.MailService;
import com.apeiron.paperlabs.service.dto.ContactDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * class implements the ContactService for contact emails to support team and user's request.
 *
 * @author Mohamed Belhassen Zinelabidine
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
@Service
public class ContactServiceImpl implements ContactService {

    private final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private MailService mailService;
    private ApplicationProperties applicationProperties;

    public ContactServiceImpl(MailService mailService, ApplicationProperties applicationProperties) {
        this.mailService = mailService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * <p>Sends two contact emails, one to the support team email provided in application properties
     * and the second one to the user, to confirm that his email was sent and an administrator will
     * contact him soon.</p>
     *
     * @param contactDTO the object sent by the user in the contact page
     */
    @Override
    public void sendContactEmails(ContactDTO contactDTO) {
        log.info("Sending email to : {} with Contact : {}", applicationProperties.getSupportTeamEmail(), contactDTO);
        mailService.sendContactEmailToSupportTeam(contactDTO, applicationProperties.getSupportTeamEmail());

        log.info("Sending email to : {} with Contact : {}", contactDTO.getEmail(), contactDTO);
        mailService.sendContactEmailToUser(contactDTO);
    }

    /**
     * <p>Sends two contact emails, one to the experts team email provided in application properties
     * and the second one to the user, to confirm that his email was sent and an expert will
     * contact him soon.</p>
     *
     * @param contactDTO the object sent by the user in the contact page
     */
    @Override
    public void sendContactExpertEmails(ContactDTO contactDTO) {
        log.info("Sending email to : {} with Contact : {}", applicationProperties.getExpertTeamEmail(), contactDTO);
        mailService.sendContactExpertEmailToExpertTeam(contactDTO, applicationProperties.getExpertTeamEmail());

        log.info("Sending email to : {} with Contact : {}", contactDTO.getEmail(), contactDTO);
        mailService.sendContactExpertEmailToUser(contactDTO);
    }
}
