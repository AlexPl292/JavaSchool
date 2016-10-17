package com.tsystems.javaschool.exceptions;

/**
 * Created by alex on 14.10.16.
 */
public class OptionNotAvailableForTariff extends JSException {
    public OptionNotAvailableForTariff() {
    }

    public OptionNotAvailableForTariff(String message) {
        super(message);
    }
}
