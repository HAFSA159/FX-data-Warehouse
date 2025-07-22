package com.warehouse.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

@Component
public class CurrencyCsvReader {

    private static final Set<String> VALID_CURRENCY_CODES = new HashSet<>();

    static {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        CurrencyCsvReader.class.getClassLoader().getResourceAsStream("valid-currencies.txt")))) {

            String line;
            while ((line = reader.readLine()) != null) {
                VALID_CURRENCY_CODES.add(line.trim().toUpperCase());
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load valid currencies", e);
        }
    }


    public boolean isValid(String currencyCode) {
        if (currencyCode == null || currencyCode.length() != 3) {
            return false;
        }
        try {
            Currency.getInstance(currencyCode.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
