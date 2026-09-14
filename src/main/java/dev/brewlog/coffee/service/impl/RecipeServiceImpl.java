package dev.brewlog.coffee.service.impl;

import dev.brewlog.coffee.dto.request.RecipeRequest;
import dev.brewlog.coffee.dto.response.RecipeResponse;
import dev.brewlog.coffee.exception.ResourceNotFoundException;
import dev.brewlog.coffee.mapper.RecipeMapper;
import dev.brewlog.coffee.model.BrewMethod;
import dev.brewlog.coffee.model.Recipe;
import dev.brewlog.coffee.repository.RecipeRepository;
import dev.brewlog.coffee.service.RecipeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    @Override
    @Transactional
    public RecipeResponse create(RecipeRequest request) {
        Recipe recipe = recipeMapper.toEntity(request);
        Recipe saved = recipeRepository.save(recipe);
        return recipeMapper.toResponse(saved);
    }

    @Override
    public RecipeResponse getById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found: " + id));
        return recipeMapper.toResponse(recipe);
    }

    @Override
    public List<RecipeResponse> getAll() {
        return recipeRepository.findAll().stream()
                .map(recipeMapper::toResponse)
                .toList();
    }

    @Override
    public List<RecipeResponse> getByMethod(BrewMethod method) {
        return recipeRepository.findByMethod(method).stream()
                .map(recipeMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public RecipeResponse update(Long id, RecipeRequest request) {
        Recipe existing = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found: " + id));
        recipeMapper.updateEntityFromRequest(request, existing);
        return recipeMapper.toResponse(recipeRepository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recipe not found " + id);
        }
        recipeRepository.deleteById(id);
    }
}
