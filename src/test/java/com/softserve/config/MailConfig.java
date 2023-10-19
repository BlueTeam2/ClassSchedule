package com.softserve.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String debug;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String enable;

    @Value("${mail.enabled}")
    private String enabled;

    @Autowired
    private Environment environment;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String credentialsUsername = environment.getProperty(username);
        String credentialsPassword = environment.getProperty(password);

        if (credentialsUsername == null && credentialsPassword == null) {
            credentialsUsername = System.getenv("HEROKU_MAIL_USERNAME");
            credentialsPassword = System.getenv("HEROKU_MAIL_PASSWORD");
        }

        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(credentialsUsername);
        mailSender.setPassword(credentialsPassword);

        Properties properties = mailSender.getJavaMailProperties();

        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.debug", debug);
        properties.setProperty("mail.smtp.auth", auth);
        properties.setProperty("mail.smtp.starttls.enable", enable);
        properties.setProperty("mail.enabled", enabled);

        return mailSender;
    }
}
