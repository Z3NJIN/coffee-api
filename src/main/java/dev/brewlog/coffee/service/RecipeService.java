package dev.brewlog.coffee.service;

import dev.brewlog.coffee.dto.request.RecipeRequest;
import dev.brewlog.coffee.dto.response.RecipeResponse;
import dev.brewlog.coffee.model.BrewMethod;

import java.util.List;

public interface RecipeService {
    RecipeResponse create(RecipeRequest request);
    RecipeResponse getById(Long id);
    List<RecipeResponse> getAll();
    List<RecipeResponse> getByMethod(BrewMethod method);
    RecipeResponse update(Long id, RecipeRequest request);
    void delete(Long id);
}