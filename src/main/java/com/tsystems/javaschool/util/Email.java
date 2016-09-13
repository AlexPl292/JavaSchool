package com.tsystems.javaschool.util;

import org.apache.log4j.Logger;

/**
 * Created by alex on 20.08.16.
 *
 * Email stub class. This class is used by customer creating for stubbing email send.
 */
public class Email {
    private final static Logger logger = Logger.getLogger("email");


    /**
     * Stub email sending
     * @param userEmail email of user
     * @param text text to send
     */
    public static void sendSimpleEmail(String userEmail, String text) {
        logger.info("Password for "+userEmail+": "+text);
    }
}
