package dev.brewlog.coffee.service;

import dev.brewlog.coffee.dto.request.BrewRequest;
import dev.brewlog.coffee.dto.response.BrewResponse;

import java.util.List;

public interface BrewService {
    BrewResponse create(BrewRequest request);
    BrewResponse getById(Long id);
    List<BrewResponse> getByRecipeIdDesc(Long recipeId);     // bitácora
    List<BrewResponse> getEvolutionByRecipeId(Long recipeId); // para el gráfico
    Double getAverageRating(Long recipeId);
}