package com.apeiron.paperlabs.service;

import com.apeiron.paperlabs.domain.User;

import com.apeiron.paperlabs.service.dto.ContactDTO;
import com.apeiron.paperlabs.service.dto.OrderDTO;
import io.github.jhipster.config.JHipsterProperties;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private static final String USER = "user";
    private static final String ORDER = "order";
    private static final String CONTACT = "contact";
    private static final String ORDER_ID = "orderId";
    private static final String BASE_URL = "baseUrl";
    private final Logger log = LoggerFactory.getLogger(MailService.class);
    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
                       MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmailWithAttachmant(String to, String subject, String content, boolean isMultipart, boolean isHtml, String filepath) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);
        FileSystemResource file = null;
        if (filepath != null) {
            file = new FileSystemResource(filepath);
        }
        MimeMessage mimeMessageWithAttachment = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessageWithAttachment, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            if (filepath != null) {
                message.addAttachment(file.getFilename(), file);
            }
            javaMailSender.send(mimeMessageWithAttachment);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }


    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendContactEmailFromTemplate(ContactDTO contactDTO, String emailToSend, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(CONTACT, contactDTO);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(emailToSend, subject, content, false, true);
    }

    @Async
    public void sendEmailFromTemplateWithOrderId(User user, String orderId, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(ORDER_ID, orderId);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendEmailWithAttachmentFromTemplate(OrderDTO orderDTO, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(orderDTO.getUser().getLangKey());
        Context context = new Context(locale);
        context.setVariable(ORDER, orderDTO);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        String filePath = null;
        if (orderDTO.getInvoiceFilePath() != null) {
            filePath = orderDTO.getInvoiceFilePath();
        }
        sendEmailWithAttachmant(orderDTO.getUser().getEmail(), subject, content, true, true, filePath);
    }

    @Async
    public void sendDocumentEmailWithAttachmentFromTemplate(OrderDTO orderDTO, String templateName, String titleKey) {
        Locale locale = Locale.forLanguageTag(orderDTO.getUser().getLangKey());
        Context context = new Context(locale);
        context.setVariable(ORDER, orderDTO);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        String pdfPath = null;
        if (orderDTO.getGeneratedLegalDocument() != null) {
             pdfPath = orderDTO.getGeneratedLegalDocument().getGenratedPDFFilePath();

        }
        sendEmailWithAttachmant(orderDTO.getUser().getEmail(), subject, content, true, true, pdfPath);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendActivationEmailWithOrderId(User user, String orderId) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplateWithOrderId(user, orderId, "mail/activationEmailWithOrderId", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendOrderInvoiceEmail(OrderDTO orderDTO) {
        log.debug("Sending Order Invoice email to '{}'", orderDTO.getUser().getEmail());
        sendEmailWithAttachmentFromTemplate(orderDTO, "mail/orderInvoiceEmail", "email.order.title");
    }

    @Async
    public void sendOrderDocumentEmail(OrderDTO orderDTO) {
        log.debug("Sending Order Document email to '{}'", orderDTO.getUser().getEmail());
        sendDocumentEmailWithAttachmentFromTemplate(orderDTO, "mail/orderDocumentEmail", "email.document.title");
    }

    @Async
    public void sendContactEmailToSupportTeam(ContactDTO contactDTO, String supportTeamEmail) {
        log.debug("Sending Contact email to '{}'", supportTeamEmail);
        sendContactEmailFromTemplate(contactDTO, supportTeamEmail, "mail/contactEmailSupportTeam", "email.contactSupportTeam.title");
    }

    @Async
    public void sendContactEmailToUser(ContactDTO contactDTO) {
        log.debug("Sending Contact email to '{}'", contactDTO.getEmail());
        sendContactEmailFromTemplate(contactDTO, contactDTO.getEmail(), "mail/contactEmailUser", "email.contactUser.title");
    }

    @Async
    public void sendContactExpertEmailToExpertTeam(ContactDTO contactDTO, String expertTeamEmail) {
        log.debug("Sending Contact Expert email to '{}'", expertTeamEmail);
        sendContactEmailFromTemplate(contactDTO, expertTeamEmail, "mail/contactExpertEmailExpertTeam", "email.contactExpertTeam.title");
    }

    @Async
    public void sendContactExpertEmailToUser(ContactDTO contactDTO) {
        log.debug("Sending Contact Expert email to '{}'", contactDTO.getEmail());
        sendContactEmailFromTemplate(contactDTO, contactDTO.getEmail(), "mail/contactExpertEmailUser", "email.contactExpertUser.title");
    }

}
