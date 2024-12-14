package com.epam.training.gen.ai.plugin;

import java.util.List;

public record ExchangeRate(String baseCurrency, List<Rate> rates) {
}
