package dev.brewlog.coffee.dto.request;

import dev.brewlog.coffee.dto.TastingNoteDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BrewRequest(
        @NotNull Long recipeId,
        LocalDateTime brewedAt,
        @NotNull @Min(1) @Max(5) Integer rating,
        String notes,
        TastingNoteDto tastingNote
) {
}