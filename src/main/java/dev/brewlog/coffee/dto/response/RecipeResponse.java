package dev.brewlog.coffee.dto.response;

import dev.brewlog.coffee.model.BrewMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecipeResponse(
        Long id,
        String name,
        BrewMethod method,
        BigDecimal coffeeDoseGrams,
        BigDecimal waterDoseGrams,
        BigDecimal ratio,
        Integer extractionTimeSeconds,
        Integer temperatureCelsius,
        String grindSize,
//        String coffeeOriginName,
        LocalDateTime createdAt
) {
}