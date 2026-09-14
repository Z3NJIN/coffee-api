package dev.brewlog.coffee.mapper;

import dev.brewlog.coffee.dto.request.RecipeRequest;
import dev.brewlog.coffee.dto.response.RecipeResponse;
import dev.brewlog.coffee.model.BrewMethod;
import dev.brewlog.coffee.model.Recipe;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RecipeMapperTest {

    private final RecipeMapper mapper = new RecipeMapper();

    @Test
    void toEntity_mapsAllFieldsFromRequest() {
        RecipeRequest request = new RecipeRequest(
                "V60 morning", BrewMethod.V60,
                BigDecimal.valueOf(18), BigDecimal.valueOf(288),
                165, 93, "Medium-fine"
        );

        Recipe result = mapper.toEntity(request);

        assertEquals("V60 morning", result.getName());
        assertEquals(BrewMethod.V60, result.getMethod());
        assertEquals(BigDecimal.valueOf(18), result.getCoffeeDoseGrams());
        assertEquals(BigDecimal.valueOf(288), result.getWaterDoseGrams());
        assertEquals(165, result.getExtractionTimeSeconds());
        assertEquals(93, result.getTemperatureCelsius());
        assertEquals("Medium-fine", result.getGrindSize());
    }

    @Test
    void updateEntityFromRequest_overwritesFieldsButKeepsId() {
        Recipe existing = new Recipe();
        existing.setId(1L);
        existing.setName("Old name");

        RecipeRequest request = new RecipeRequest(
                "V60 evening", BrewMethod.V60,
                BigDecimal.valueOf(20), BigDecimal.valueOf(320),
                180, 91, "Medium"
        );

        mapper.updateEntityFromRequest(request, existing);

        assertEquals(1L, existing.getId(), "id must not be touched by an update");
        assertEquals("V60 evening", existing.getName());
        assertEquals(BigDecimal.valueOf(20), existing.getCoffeeDoseGrams());
        assertEquals(BigDecimal.valueOf(320), existing.getWaterDoseGrams());
        assertEquals(180, existing.getExtractionTimeSeconds());
        assertEquals(91, existing.getTemperatureCelsius());
        assertEquals("Medium", existing.getGrindSize());
    }

    @Test
    void toResponse_mapsAllFieldsIncludingCalculatedRatio() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("V60 morning");
        recipe.setMethod(BrewMethod.V60);
        recipe.setCoffeeDoseGrams(BigDecimal.valueOf(18));
        recipe.setWaterDoseGrams(BigDecimal.valueOf(288));
        recipe.setExtractionTimeSeconds(165);
        recipe.setTemperatureCelsius(93);
        recipe.setGrindSize("Medium-fine");
        LocalDateTime createdAt = LocalDateTime.now();
        recipe.setCreatedAt(createdAt);

        RecipeResponse response = mapper.toResponse(recipe);

        assertEquals(1L, response.id());
        assertEquals("V60 morning", response.name());
        assertEquals(BrewMethod.V60, response.method());
        assertEquals(BigDecimal.valueOf(18), response.coffeeDoseGrams());
        assertEquals(BigDecimal.valueOf(288), response.waterDoseGrams());
        assertEquals(recipe.getRatio(), response.ratio()); // ratio is computed by the entity itself
        assertEquals(165, response.extractionTimeSeconds());
        assertEquals(93, response.temperatureCelsius());
        assertEquals("Medium-fine", response.grindSize());
        assertEquals(createdAt, response.createdAt());
    }
}