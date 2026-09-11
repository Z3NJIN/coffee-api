package dev.brewlog.coffee.mapper;

import dev.brewlog.coffee.dto.request.RecipeRequest;
import dev.brewlog.coffee.dto.response.RecipeResponse;
import dev.brewlog.coffee.model.Recipe;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper {

    public Recipe toEntity(RecipeRequest request) {
        Recipe recipe = new Recipe();
        recipe.setName(request.name());
        recipe.setMethod(request.method());
        recipe.setCoffeeDoseGrams(request.coffeeDoseGrams());
        recipe.setWaterDoseGrams(request.waterDoseGrams());
        recipe.setExtractionTimeSeconds(request.extractionTimeSeconds());
        recipe.setTemperatureCelsius(request.temperatureCelsius());
        recipe.setGrindSize(request.grindSize());
        return recipe;
    }

    public void updateEntityFromRequest(RecipeRequest request, Recipe existing) {
        existing.setName(request.name());
        existing.setMethod(request.method());
        existing.setCoffeeDoseGrams(request.coffeeDoseGrams());
        existing.setWaterDoseGrams(request.waterDoseGrams());
        existing.setExtractionTimeSeconds(request.extractionTimeSeconds());
        existing.setTemperatureCelsius(request.temperatureCelsius());
        existing.setGrindSize(request.grindSize());
    }

    public RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getMethod(),
                recipe.getCoffeeDoseGrams(),
                recipe.getWaterDoseGrams(),
                recipe.getRatio(),
                recipe.getExtractionTimeSeconds(),
                recipe.getTemperatureCelsius(),
                recipe.getGrindSize(),
                recipe.getCreatedAt()
        );
    }
}
