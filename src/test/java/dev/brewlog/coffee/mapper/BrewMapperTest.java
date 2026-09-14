package dev.brewlog.coffee.mapper;

import dev.brewlog.coffee.dto.TastingNoteDto;
import dev.brewlog.coffee.dto.request.BrewRequest;
import dev.brewlog.coffee.dto.response.BrewResponse;
import dev.brewlog.coffee.model.Brew;
import dev.brewlog.coffee.model.BrewMethod;
import dev.brewlog.coffee.model.Recipe;
import dev.brewlog.coffee.model.TastingNote;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrewMapperTest {

    private final BrewMapper mapper = new BrewMapper();

    @Test
    void toEntity_withTastingNote_mapsAllFields() {
        LocalDateTime brewedAt = LocalDateTime.now();
        TastingNoteDto tastingNoteDto = new TastingNoteDto(4, 4, 3, List.of("brown_sugar", "citrus"));
        BrewRequest request = new BrewRequest(1L, brewedAt, 4, "Good balance", tastingNoteDto);

        Brew result = mapper.toEntity(request);

        assertEquals(brewedAt, result.getBrewedAt());
        assertEquals(4, result.getRating());
        assertEquals("Good balance", result.getNotes());
        assertNotNull(result.getTastingNote());
        assertEquals(4, result.getTastingNote().getAcidity());
        assertEquals(4, result.getTastingNote().getSweetness());
        assertEquals(3, result.getTastingNote().getBody());
        assertEquals(List.of("brown_sugar", "citrus"), result.getTastingNote().getTags());
    }

    @Test
    void toEntity_withoutTastingNote_leavesItNull() {
        BrewRequest request = new BrewRequest(1L, null, 4, "notes", null);

        Brew result = mapper.toEntity(request);

        assertNull(result.getTastingNote());
    }

    @Test
    void toResponse_mapsAllFieldsIncludingRecipeInfoAndTastingNote() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("V60 morning");
        recipe.setMethod(BrewMethod.V60);

        TastingNote tastingNote = new TastingNote();
        tastingNote.setAcidity(4);
        tastingNote.setSweetness(4);
        tastingNote.setBody(3);
        tastingNote.setTags(List.of("brown_sugar"));

        Brew brew = new Brew();
        brew.setId(1L);
        brew.setRecipe(recipe);
        LocalDateTime brewedAt = LocalDateTime.now();
        brew.setBrewedAt(brewedAt);
        brew.setRating(4);
        brew.setNotes("Good balance");
        brew.setTastingNote(tastingNote);
        LocalDateTime createdAt = LocalDateTime.now();
        brew.setCreatedAt(createdAt);

        BrewResponse response = mapper.toResponse(brew);

        assertEquals(1L, response.id());
        assertEquals(1L, response.recipeId());
        assertEquals("V60 morning", response.recipeName());
        assertEquals(BrewMethod.V60, response.recipeMethod());
        assertEquals(brewedAt, response.brewedAt());
        assertEquals(4, response.rating());
        assertEquals("Good balance", response.notes());
        assertNotNull(response.tastingNote());
        assertEquals(4, response.tastingNote().acidity());
        assertEquals(List.of("brown_sugar"), response.tastingNote().tags());
        assertEquals(createdAt, response.createdAt());
    }

    @Test
    void toResponse_withoutTastingNote_returnsNullTastingNoteDto() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("V60 morning");
        recipe.setMethod(BrewMethod.V60);

        Brew brew = new Brew();
        brew.setId(1L);
        brew.setRecipe(recipe);
        brew.setRating(4);
        brew.setTastingNote(null);

        BrewResponse response = mapper.toResponse(brew);

        assertNull(response.tastingNote());
    }
}