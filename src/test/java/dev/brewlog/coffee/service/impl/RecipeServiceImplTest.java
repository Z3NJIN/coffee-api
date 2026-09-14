package dev.brewlog.coffee.service.impl;

import dev.brewlog.coffee.dto.request.RecipeRequest;
import dev.brewlog.coffee.dto.response.RecipeResponse;
import dev.brewlog.coffee.exception.ResourceNotFoundException;
import dev.brewlog.coffee.mapper.RecipeMapper;
import dev.brewlog.coffee.model.BrewMethod;
import dev.brewlog.coffee.model.Recipe;
import dev.brewlog.coffee.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private RecipeRequest buildRequest() {
        return new RecipeRequest(
                "V60 morning" ,
                BrewMethod.V60,
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(300),
                210,
                90,
                "Media"
        );
    }

    private RecipeResponse buildResponse(long id) {
        return new RecipeResponse(
                id,
                "V60 morning" ,
                BrewMethod.V60,
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(15.00),
                210,
                90,
                "Media" ,
                null
        );
    }

    @Test
    void create_validRequest_savesRecipeAndReturnsResponse(){
        RecipeRequest request = buildRequest();
        Recipe mappedEntity = new Recipe();
        Recipe savedEntity = new Recipe();
        savedEntity.setId(1L);
        RecipeResponse expected = buildResponse(1L);

        when(recipeMapper.toEntity(request)).thenReturn(mappedEntity);
        when(recipeRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(recipeMapper.toResponse(savedEntity)).thenReturn(expected);

        RecipeResponse result = recipeService.create(request);

        assertEquals(expected, result);
        verify(recipeRepository).save(mappedEntity);
    }

    @Test
    void getById_existingId_returnsResponse() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        RecipeResponse expected = buildResponse(1L);

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeMapper.toResponse(recipe)).thenReturn(expected);

        RecipeResponse result = recipeService.getById(1L);

        assertEquals(expected, result);
    }

    @Test
    void getById_unknownId_throwsResourceNotFoundException() {
        when(recipeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.getById(99L));
        verify(recipeMapper, never()).toResponse(any());
    }

    @Test
    void getAll_returnsMappedList() {
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);

        when(recipeRepository.findAll()).thenReturn(List.of(recipe1, recipe2));
        when(recipeMapper.toResponse(recipe1)).thenReturn(buildResponse(1L));
        when(recipeMapper.toResponse(recipe2)).thenReturn(buildResponse(2L));

        List<RecipeResponse> result = recipeService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void getByMethod_filtersUsingRepository() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeRepository.findByMethod(BrewMethod.V60)).thenReturn(List.of(recipe));
        when(recipeMapper.toResponse(recipe)).thenReturn(buildResponse(1L));

        List<RecipeResponse> result = recipeService.getByMethod(BrewMethod.V60);

        assertEquals(1, result.size());
        verify(recipeRepository).findByMethod(BrewMethod.V60);
    }

    @Test
    void update_existingId_updatesAndReturnsResponse() {
        RecipeRequest request = buildRequest();
        Recipe existing = new Recipe();
        existing.setId(1L);
        RecipeResponse expected = buildResponse(1L);

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(recipeRepository.save(existing)).thenReturn(existing);
        when(recipeMapper.toResponse(existing)).thenReturn(expected);

        RecipeResponse result = recipeService.update(1L, request);

        assertEquals(expected, result);
        verify(recipeMapper).updateEntityFromRequest(request, existing);
    }

    @Test
    void update_unknownId_throwsResourceNotFoundException() {
        RecipeRequest request = buildRequest();
        when(recipeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.update(99L, request));
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void delete_existingId_deletesRecipe() {
        when(recipeRepository.existsById(1L)).thenReturn(true);

        recipeService.delete(1L);

        verify(recipeRepository).deleteById(1L);
    }

    @Test
    void delete_unknownId_throwsResourceNotFoundException() {
        when(recipeRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> recipeService.delete(99L));
        verify(recipeRepository, never()).deleteById(any());
    }

}