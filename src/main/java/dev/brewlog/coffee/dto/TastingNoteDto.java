package dev.brewlog.coffee.dto;

import java.util.List;

public record TastingNoteDto(
        Integer acidity,
        Integer sweetness,
        Integer body,
        List<String> tags
) {
}