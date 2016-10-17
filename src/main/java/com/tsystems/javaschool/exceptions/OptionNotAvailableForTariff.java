package com.tsystems.javaschool.exceptions;

/**
 * Created by alex on 14.10.16.
 *
 * Specified options are not available for chosen tariffs
 */
public class OptionNotAvailableForTariff extends JSException {
    public OptionNotAvailableForTariff() {
    }

    public OptionNotAvailableForTariff(String message) {
        super(message);
    }
}
