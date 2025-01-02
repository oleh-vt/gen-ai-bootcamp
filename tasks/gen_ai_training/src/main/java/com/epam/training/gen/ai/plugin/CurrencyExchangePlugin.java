package com.epam.training.gen.ai.plugin;

import com.epam.training.gen.ai.exception.UnknownCurrencyException;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CurrencyExchangePlugin {

    private final Map<String, Rate> exchangeRates;

    @DefineKernelFunction(
            name = "is_available_currency_exchange_rate",
            description = "Check if the exchange rate is available for the currency"
    )
    public boolean isAvailableCurrencyExchangeRate(
            @KernelFunctionParameter(
                    name = "currency_code"
            )
            String currencyCode
    ) {
        boolean available = Optional.ofNullable(exchangeRates.get(currencyCode))
                .map(Rate::rate).isPresent();
        log.info("Currency rate for {} is {}", currencyCode, available ? "available" : "unavailable");
        return available;
    }

    @DefineKernelFunction(
            name = "convert_currency",
            description = "Convert the given currency amount to the target currency"
    )
    public double convertCurrency(
            @KernelFunctionParameter(
                    name = "amount",
                    description = "Amount and that is being converted",
                    type = double.class
            )
            double amount,
            @KernelFunctionParameter(
                    name = "source_currency_code",
                    description = "Currency of the amount"
            )
            String sourceCurrencyCode,
            @KernelFunctionParameter(
                    name = "target_currency_code",
                    description = "Target currency"
            )
            String targetCurrencyCode

    ) {
        log.info("Converting {} from {} to {}", amount, sourceCurrencyCode, targetCurrencyCode);
        if (StringUtils.equalsIgnoreCase(sourceCurrencyCode, targetCurrencyCode)) {
            return amount;
        }
        return BigDecimal.valueOf(amount)
                .divide(getRate(sourceCurrencyCode), 3, RoundingMode.HALF_EVEN)
                .multiply(getRate(targetCurrencyCode))
                .setScale(3, RoundingMode.HALF_EVEN)
                .doubleValue();
    }

    @DefineKernelFunction(
            name = "get_currency_countries",
            description = "Get countries where the currency is used"
    )
    public Collection<String> getCurrencyCountries(
            @KernelFunctionParameter(name = "currency_code") String currencyCode
    ) {
        log.info("Get countries for: {}", currencyCode);
        return Optional.ofNullable(exchangeRates.get(currencyCode))
                .map(Rate::countries)
                .orElseGet(Collections::emptySet);
    }

    private BigDecimal getRate(String currency) {
        BigDecimal rate = Optional.ofNullable(exchangeRates.get(currency))
                .map(Rate::rate)
                .orElseThrow(() -> new UnknownCurrencyException("invalid or unsupported currency: " + currency));
        log.info("Rate {}: {}", currency, rate);
        return rate;
    }

}
