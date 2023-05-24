package com.pandev.currency.helper;

import java.util.Currency;

public class CurrencyHelper {

    public static final String TELEGRAM_CURRENCY_REGEX = "^ *\\d+ \\b[A-Z]{3}\\b$";

    public static boolean guard(String message) {
        if (message.matches(TELEGRAM_CURRENCY_REGEX)) {
            String currency = getCurrency(message);
            return Currency.getAvailableCurrencies().stream()
                .anyMatch(javaCurrency -> javaCurrency
                    .getCurrencyCode()
                    .equals(currency));
        }
        return false;
    }

    public static String getCurrency(String message) {
        return message.split(" ")[1];
    }
    public static int getAmount(String message) {
        return Integer.parseInt(message.split(" ")[0]);
    }

}
