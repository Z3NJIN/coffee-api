package dev.brewlog.coffee.dto.request;

import dev.brewlog.coffee.model.BrewMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RecipeRequest(
        @NotBlank String name,
        @NotNull BrewMethod method,
        @NotNull @Positive BigDecimal coffeeDoseGrams,
        @NotNull @Positive BigDecimal waterDoseGrams,
        Integer extractionTimeSeconds,
        Integer temperatureCelsius,
        String grindSize
//        Long coffeeOriginId
) {
}
