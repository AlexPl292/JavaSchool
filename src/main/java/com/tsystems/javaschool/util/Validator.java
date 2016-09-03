package com.tsystems.javaschool.util;

import sun.util.resources.LocaleData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by alex on 03.09.16.
 */
public class Validator {
    private Validator(){}

    /**
     * Validate a name
     * @param name name for validating
     * @return error message or null, if name is valid
     */
    public static String name(String name) {
        if (null == name)
            return "Name is empty";
        if (name.length() < 2)
            return "Name is shorter then 2";
        if (name.length() > 25)
            return "Name is longer then 45";
        if (!name.matches("[a-zA-Z]+"))
            return "Name contains wrong letters";
        if (!Character.isUpperCase(name.charAt(0)))
            return "Name must starts with upper case";

        return null;
    }

    /**
     * Validate an email address
     * @param email email for validating
     * @return error message or null, if email is valid
     */
    public static String email(String email) {
        if (null == email)
            return "Email is empty";

        String prettyRegex = "^[\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$\n";
        if (!email.matches(prettyRegex))
            return "Email is invalid";

        return null;
    }


    /**
     * Validate a cost
     * @param cost cost for validating
     * @return error message or null, if cost is valid
     */
    public static String cost(String cost) {
        if (null == cost)
            return "Cost is empty";

        String regExp = "[0-9]+([,.][0-9]{1,2})?";

        if (!cost.matches(regExp))
            return "Cost is invalid";

        return null;
    }

    /**
     * Check if date is valid and older then 18 years
     * @param data cost for validating
     * @return error message or null, if date is valid
     */
    public static String dateOlderThen18(String data) {
        if (null == data)
            return "Date is empty";

        if (!data.matches("\\d{4}-\\d{2}-\\d{2}"))
            return "Wrong format";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.getDefault());
        LocalDate date = LocalDate.parse(data, formatter);

        LocalDate today = LocalDate.now();
        if (date.isAfter(today.minusYears(18)))
            return "Date is not older then 18 years";

        return null;
    }
}
