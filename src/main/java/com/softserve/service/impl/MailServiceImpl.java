package com.softserve.service.impl;

import com.softserve.dto.EmailMessageDTO;
import com.softserve.entity.TemporarySchedule;
import com.softserve.exception.MessageNotSendException;
import com.softserve.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Service
@PropertySource("classpath:mail.properties")
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final Environment environment;
    private final SpringTemplateEngine springTemplateEngine;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${mail.enabled:true}")
    private boolean enabled;
    private String credentialsUsername;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender,
                           Environment environment,
                           SpringTemplateEngine springTemplateEngine) {
        this.mailSender = mailSender;
        this.environment = environment;
        this.springTemplateEngine = springTemplateEngine;
    }

    @PostConstruct
    private void postConstruct() {
        credentialsUsername = environment.getProperty(username);
        if (credentialsUsername == null) {
            credentialsUsername = System.getenv("HEROKU_MAIL_USERNAME");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void send(String receiver, String subject, String message) {
        log.info("Enter into send method with receiver {}, subject {}", receiver, subject);

        if (enabled) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(credentialsUsername);
            mailMessage.setTo(receiver);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            mailSender.send(mailMessage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(String sender, EmailMessageDTO emailMessageDTO) {
        log.info("Enter into send method with sender - {}, emailMessageDTO - {}", sender, emailMessageDTO);
        try {
            MimeMessage message = this.mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(credentialsUsername, sender);
            messageHelper.setSubject(emailMessageDTO.getSubject());
            messageHelper.setText(emailMessageDTO.getText());
            messageHelper.setTo(emailMessageDTO.getReceivers().toArray(String[]::new));

            if (emailMessageDTO.getAttachments() != null) {
                for (MultipartFile attachment : emailMessageDTO.getAttachments()) {
                    messageHelper.addAttachment(Objects.requireNonNull(attachment.getOriginalFilename()), attachment);
                }
            }

            mailSender.send(messageHelper.getMimeMessage());
        } catch (IOException | MessagingException e) {
            throw new MessageNotSendException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(String fileName, String receiver, String subject, String message, ByteArrayOutputStream bos) throws MessagingException {
        log.info("Enter into send method with emailTo - {}, subject - {}", receiver, subject);

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();

        mimeMessage.setFrom(new InternetAddress(credentialsUsername));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

        mimeMessage.setSubject(subject);

        DataSource fds = new ByteArrayDataSource(bos.toByteArray(), "application/pdf");

        Multipart multipart = new MimeMultipart();

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(message, "utf-8", "html");
        multipart.addBodyPart(messageBodyPart);

        MimeBodyPart fileBodyPart = new MimeBodyPart();
        fileBodyPart.setDataHandler(new DataHandler(fds));
        fileBodyPart.setFileName(fileName);
        multipart.addBodyPart(fileBodyPart);

        mimeMessage.setContent(multipart);
        this.mailSender.send(mimeMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void send(String emailTo, String subject,
                     TemporarySchedule temporarySchedule, String emailTemplate) throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context(Locale.UK);
        ctx.setVariable("temporarySchedule", temporarySchedule);
        //ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject(subject);
        message.setFrom(credentialsUsername);
        message.setTo(emailTo);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.springTemplateEngine.process(emailTemplate, ctx);
        message.setText(htmlContent, true); // true = isHtml
        // Send mail
        this.mailSender.send(mimeMessage);
    }
}
