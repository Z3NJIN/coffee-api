package dev.brewlog.coffee.controller;

import dev.brewlog.coffee.dto.request.BrewRequest;
import dev.brewlog.coffee.dto.response.BrewResponse;
import dev.brewlog.coffee.exception.ResourceNotFoundException;
import dev.brewlog.coffee.service.BrewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BrewController.class)
class BrewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BrewService brewService;

    @Test
    void create_validRequest_returns201WithLocationHeader() throws Exception {
        BrewRequest request = new BrewRequest(1L, null, 4, "Good balance" , null);
        BrewResponse response = new BrewResponse(
                1L, 1L, "V60 morning" , null, LocalDateTime.now(),
                4, "Good balance" , null, LocalDateTime.now()
        );

        when(brewService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/brews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location" , "/api/brews/1"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void create_invalidRating_returns400() throws Exception {
        // rating out of the 1-5 range enforced by @Min/@Max in BrewRequest
        String invalidJson = """
                {
                  "recipeId": 1,
                  "rating": 8,
                  "notes": "notes"
                }
                """;

        mockMvc.perform(post("/api/brews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(brewService, never()).create(any());
    }

    @Test
    void create_unknownRecipeId_returns404() throws Exception {
        BrewRequest request = new BrewRequest(99L, null, 4, "notes" , null);
        when(brewService.create(any()))
                .thenThrow(new ResourceNotFoundException("Recipe not found: 99"));

        mockMvc.perform(post("/api/brews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById_existingId_returns200() throws Exception {
        BrewResponse response = new BrewResponse(
                1L, 1L, "V60 morning" , null, LocalDateTime.now(),
                4, "notes" , null, LocalDateTime.now()
        );
        when(brewService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/brews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_unknownId_returns404() throws Exception {
        when(brewService.getById(99L))
                .thenThrow(new ResourceNotFoundException("Brew not found: 99"));

        mockMvc.perform(get("/api/brews/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByRecipe_returnsLog() throws Exception {
        when(brewService.getByRecipeIdDesc(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/recipes/1/brews"))
                .andExpect(status().isOk());

        verify(brewService).getByRecipeIdDesc(1L);
    }

    @Test
    void getEvolution_returnsChronologicalHistory() throws Exception {
        when(brewService.getEvolutionByRecipeId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/recipes/1/brews/evolution"))
                .andExpect(status().isOk());

        verify(brewService).getEvolutionByRecipeId(1L);
    }

    @Test
    void getAverageRating_noRatingsYet_returnsZero() throws Exception {
        when(brewService.getAverageRating(1L)).thenReturn(null);

        mockMvc.perform(get("/api/recipes/1/brews/average-rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageRating").value(0.0));
    }

    @Test
    void getAverageRating_withRatings_returnsAverage() throws Exception {
        when(brewService.getAverageRating(1L)).thenReturn(4.5);

        mockMvc.perform(get("/api/recipes/1/brews/average-rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageRating").value(4.5));
    }
}