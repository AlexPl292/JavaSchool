package com.tsystems.javaschool.util;

import org.apache.log4j.Logger;

/**
 * Created by alex on 20.08.16.
 */
public class Email {
    private final static Logger logger = Logger.getLogger("email");


    public static void sendSimpleEmail(String userEmail, String text) {
        logger.info("Password for "+userEmail+": "+text);
    }
}
