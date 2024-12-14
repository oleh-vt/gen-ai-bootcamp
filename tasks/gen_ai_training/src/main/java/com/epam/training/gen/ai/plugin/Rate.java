package com.epam.training.gen.ai.plugin;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;

public record Rate(
        @SerializedName("currency_code") Currency currency,
        @SerializedName("exchange_rate") BigDecimal rate,
        Set<String> countries
) {
}
