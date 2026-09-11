package dev.brewlog.coffee.dto.response;

import dev.brewlog.coffee.dto.TastingNoteDto;
import dev.brewlog.coffee.model.BrewMethod;

import java.time.LocalDateTime;

public record BrewResponse(
        Long id,
        Long recipeId,
        String recipeName,
        BrewMethod recipeMethod,
        LocalDateTime brewedAt,
        Integer rating,
        String notes,
        TastingNoteDto tastingNote,
        LocalDateTime createdAt
) {
}