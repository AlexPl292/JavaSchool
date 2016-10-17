package com.tsystems.javaschool.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by alex on 10.10.16.
 * <p>
 * Send email using GMail SMTP server.
 */
public class EmailHelper {

    /**
     * Send email using GMail SMTP server.
     * No cc recipients
     * Username and password are in properties file
     *
     * @param recipientEmail TO recipient
     * @param title          title of the message
     * @param message        message to be sent
     */
    public static void Send(String recipientEmail, String title, String message) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("email");
            Send(rb.getString("username"), rb.getString("password"), recipientEmail, title, message);
        } catch (EmailException | MissingResourceException e) {
            throw new RuntimeException("Please check 'Caused by' section and write to alexpl292@gmail.com if you need resource file", e);
        }
    }

    /**
     * Send email using GMail SMTP server.
     *
     * @param username       GMail username
     * @param password       GMail password
     * @param recipientEmail TO recipient
     * @param title          title of the message
     * @param message        message to be sent
     */
    public static void Send(final String username, final String password, String recipientEmail, String title, String message) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(username, password));
        email.setSSLOnConnect(true);
        email.setFrom(username + "@gmail.com");
        email.setSubject(title);
        email.setMsg(message);
        email.addTo(recipientEmail);
        email.send();
    }
}
