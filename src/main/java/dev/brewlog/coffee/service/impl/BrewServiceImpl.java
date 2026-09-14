package dev.brewlog.coffee.service.impl;

import dev.brewlog.coffee.dto.request.BrewRequest;
import dev.brewlog.coffee.dto.response.BrewResponse;
import dev.brewlog.coffee.exception.ResourceNotFoundException;
import dev.brewlog.coffee.mapper.BrewMapper;
import dev.brewlog.coffee.model.Brew;
import dev.brewlog.coffee.model.Recipe;
import dev.brewlog.coffee.repository.BrewRepository;
import dev.brewlog.coffee.repository.RecipeRepository;
import dev.brewlog.coffee.service.BrewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BrewServiceImpl implements BrewService {

    private final BrewRepository brewRepository;
    private final RecipeRepository recipeRepository;
    private final BrewMapper brewMapper;

    public BrewServiceImpl(BrewRepository brewRepository, RecipeRepository recipeRepository, BrewMapper brewMapper) {
        this.brewRepository = brewRepository;
        this.recipeRepository = recipeRepository;
        this.brewMapper = brewMapper;
    }

    @Override
    @Transactional
    public BrewResponse create(BrewRequest request) {
        Recipe recipe = recipeRepository.findById(request.recipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found: " + request.recipeId()));

        Brew brew = brewMapper.toEntity(request);
        brew.setRecipe(recipe);

        if (brew.getBrewedAt() == null) {
            brew.setBrewedAt(LocalDateTime.now());
        }

        return brewMapper.toResponse(brewRepository.save(brew));
    }

    @Override
    public BrewResponse getById(Long id) {
        Brew brew = brewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brew not found: " + id));

        return brewMapper.toResponse(brew);
    }

    @Override
    public List<BrewResponse> getByRecipeIdDesc(Long recipeId) {
        return brewRepository.findByRecipeIdOrderByBrewedAtDesc(recipeId).stream()
                .map(brewMapper::toResponse)
                .toList();
    }

    @Override
    public List<BrewResponse> getEvolutionByRecipeId(Long recipeId) {
        return brewRepository.findByRecipeIdOrderByBrewedAtAsc(recipeId).stream()
                .map(brewMapper::toResponse)
                .toList();
    }

    @Override
    public Double getAverageRating(Long recipeId) {
        return brewRepository.findAverageRatingByRecipeId(recipeId).orElse(null);
    }

}
