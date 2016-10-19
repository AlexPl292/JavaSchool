package com.tsystems.javaschool.exceptions;

/**
 * Created by alex on 14.10.16.
 *
 * Specified options are not available for chosen tariffs
 */
public class OptionNotAvailableForTariffException extends JSException {
    public OptionNotAvailableForTariffException() {
    }

    public OptionNotAvailableForTariffException(String message) {
        super(message);
    }
}
