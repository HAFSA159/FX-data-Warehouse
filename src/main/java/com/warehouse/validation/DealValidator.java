package com.warehouse.validation;


import com.warehouse.models.Deal;
import com.warehouse.utils.CurrencyCsvReader;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DealValidator {


    private final CurrencyCsvReader csvReader;

    public String validate(Deal deal) {
        if (deal == null) {
            log.warn("Validation failed: Deal object is null");
            return "Deal is null";
        }

        if (isBlank(deal.getDealId())) {
            log.warn("Validation failed: dealId is missing or blank");
            return "Deal ID is required";
        }

        if (!isValidCurrency(deal.getFromCurrency())) {
            log.warn("Validation failed: fromCurrency is invalid or unknown. Value: '{}'", deal.getFromCurrency());
            return "Invalid fromCurrency code";
        }

        if (!isValidCurrency(deal.getToCurrency())) {
            log.warn("Validation failed: toCurrency is invalid or unknown. Value: '{}'", deal.getToCurrency());
            return "Invalid toCurrency code";
        }

        if (isInvalidAmount(deal.getAmount())) {
            log.warn("Validation failed: amount is invalid. Value: '{}'", deal.getAmount());
            return "Amount must be a positive number";
        }

        if (isInvalidTimestamp(deal.getTimestamp())) {
            log.warn("Validation failed: timestamp is invalid. Value: '{}'", deal.getTimestamp());
            return "Invalid or future timestamp";
        }

        log.info("Deal passed validation: {}", deal.getDealId());
        return null;
    }

    private boolean isBlank(String value) {
        return StringUtils.isBlank(value);
    }

    private boolean isValidCurrency(String currency) {
        return StringUtils.isNotBlank(currency) && csvReader.isValid(currency);
    }

    private boolean isInvalidTimestamp(LocalDateTime timestamp) {
        return timestamp == null || timestamp.isAfter(LocalDateTime.now());
    }

    private boolean isInvalidAmount(Float amount) {
        return amount == null || amount <= 0.0;
    }


}
