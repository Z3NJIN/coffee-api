package dev.brewlog.coffee.service.impl;

import dev.brewlog.coffee.dto.request.BrewRequest;
import dev.brewlog.coffee.dto.response.BrewResponse;
import dev.brewlog.coffee.exception.ResourceNotFoundException;
import dev.brewlog.coffee.mapper.BrewMapper;
import dev.brewlog.coffee.model.Brew;
import dev.brewlog.coffee.model.Recipe;
import dev.brewlog.coffee.repository.BrewRepository;
import dev.brewlog.coffee.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrewServiceImplTest {

    @Mock
    private BrewRepository brewRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private BrewMapper brewMapper;

    @InjectMocks
    private BrewServiceImpl brewService;

    @Test
    void create_unknownRecipeId_throwsResourceNotFoundException() {
        BrewRequest request = new BrewRequest(99L, null, 4, "notas" , null);
        when(recipeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> brewService.create(request));
        verify(brewRepository, never()).save(any());
    }

    @Test
    void create_existingRecipeId_linksRecipeAndSaves() {
        BrewRequest request = new BrewRequest(1L, null, 4, "notas" , null);
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Brew mappedBrew = new Brew();

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(brewMapper.toEntity(request)).thenReturn(mappedBrew);
        when(brewRepository.save(mappedBrew)).thenReturn(mappedBrew);
        when(brewMapper.toResponse(mappedBrew)).thenReturn(null);

        brewService.create(request);

        assertEquals(recipe, mappedBrew.getRecipe());
        verify(brewRepository).save(mappedBrew);
    }

    @Test
    void create_withoutBrewedAt_setsCurrentTimestamp() {
        BrewRequest request = new BrewRequest(1L, null, 4, "notas" , null);
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Brew mappedBrew = new Brew(); // brewedAt viene null desde el mapper

        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(brewMapper.toEntity(request)).thenReturn(mappedBrew);
        when(brewRepository.save(any(Brew.class))).thenAnswer(inv -> inv.getArgument(0));
        when(brewMapper.toResponse(any(Brew.class))).thenReturn(null);

        brewService.create(request);

        assertNotNull(mappedBrew.getBrewedAt(),
                "brewedAt should be auto-assigned when not provided in the request");
    }

    @Test
    void getById_existingId_returnsResponse() {
        Brew brew = new Brew();
        brew.setId(1L);
        BrewResponse expected = new BrewResponse(
                1L, 1L, "V60" , null, null, 4, "notas" , null, null
        );

        when(brewRepository.findById(1L)).thenReturn(Optional.of(brew));
        when(brewMapper.toResponse(brew)).thenReturn(expected);

        BrewResponse result = brewService.getById(1L);

        assertEquals(expected, result);
    }

    @Test
    void getById_unknownId_throwsResourceNotFoundException() {
        when(brewRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> brewService.getById(99L));
    }

    @Test
    void getByRecipeIdDesc_callsDescendingRepositoryMethod() {
        when(brewRepository.findByRecipeIdOrderByBrewedAtDesc(1L)).thenReturn(List.of());

        brewService.getByRecipeIdDesc(1L);

        verify(brewRepository).findByRecipeIdOrderByBrewedAtDesc(1L);
        verify(brewRepository, never()).findByRecipeIdOrderByBrewedAtAsc(any());
    }

    @Test
    void getEvolutionByRecipeId_callsAscendingRepositoryMethod() {
        when(brewRepository.findByRecipeIdOrderByBrewedAtAsc(1L)).thenReturn(List.of());

        brewService.getEvolutionByRecipeId(1L);

        verify(brewRepository).findByRecipeIdOrderByBrewedAtAsc(1L);
    }

    @Test
    void getAverageRating_noRatings_returnsNull() {
        when(brewRepository.findAverageRatingByRecipeId(1L)).thenReturn(Optional.empty());

        Double result = brewService.getAverageRating(1L);

        assertNull(result);
    }

    @Test
    void getAverageRating_withRatings_returnsAverage() {
        when(brewRepository.findAverageRatingByRecipeId(1L)).thenReturn(Optional.of(4.5));

        Double result = brewService.getAverageRating(1L);

        assertEquals(4.5, result);
    }
}
