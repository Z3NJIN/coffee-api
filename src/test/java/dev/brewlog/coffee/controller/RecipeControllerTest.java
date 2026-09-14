package dev.brewlog.coffee.controller;

import dev.brewlog.coffee.dto.request.RecipeRequest;
import dev.brewlog.coffee.dto.response.RecipeResponse;
import dev.brewlog.coffee.exception.ResourceNotFoundException;
import dev.brewlog.coffee.model.BrewMethod;
import dev.brewlog.coffee.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecipeService recipeService;

    private RecipeRequest buildRequest() {
        return new RecipeRequest(
                "V60 morning" , BrewMethod.V60,
                BigDecimal.valueOf(18), BigDecimal.valueOf(288),
                165, 93, "Medium-fine"
        );
    }

    private RecipeResponse buildResponse(Long id) {
        return new RecipeResponse(
                id, "V60 morning" , BrewMethod.V60,
                BigDecimal.valueOf(18), BigDecimal.valueOf(288), BigDecimal.valueOf(16.00),
                165, 93, "Medium-fine" , null
        );
    }

    @Test
    void create_validRequest_returns201WithLocationHeader() throws Exception {
        RecipeRequest request = buildRequest();
        RecipeResponse response = buildResponse(1L);

        when(recipeService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location" , "/api/recipes/1"))
                .andExpect(jsonPath("$.name").value("V60 morning"))
                .andExpect(jsonPath("$.ratio").value(16.00));
    }

    @Test
    void create_invalidRequest_returns400() throws Exception {
        String invalidJson = """
                {
                  "name": "",
                  "method": "V60",
                  "coffeeDoseGrams": -5,
                  "waterDoseGrams": 288
                }
                """;

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(recipeService, never()).create(any());
    }

    @Test
    void getAll_noFilter_returnsAllRecipes() throws Exception {
        when(recipeService.getAll()).thenReturn(List.of(buildResponse(1L), buildResponse(2L)));

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(recipeService).getAll();
        verify(recipeService, never()).getByMethod(any());
    }

    @Test
    void getAll_withMethodFilter_callsGetByMethod() throws Exception {
        when(recipeService.getByMethod(BrewMethod.V60)).thenReturn(List.of(buildResponse(1L)));

        mockMvc.perform(get("/api/recipes").param("method" , "V60"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(recipeService).getByMethod(BrewMethod.V60);
        verify(recipeService, never()).getAll();
    }

    @Test
    void getById_existingId_returns200() throws Exception {
        when(recipeService.getById(1L)).thenReturn(buildResponse(1L));

        mockMvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_unknownId_returns404() throws Exception {
        when(recipeService.getById(99L))
                .thenThrow(new ResourceNotFoundException("Recipe not found: 99"));

        mockMvc.perform(get("/api/recipes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Recipe not found: 99"));
    }

    @Test
    void update_validRequest_returns200() throws Exception {
        RecipeRequest request = buildRequest();
        when(recipeService.update(eq(1L), any())).thenReturn(buildResponse(1L));

        mockMvc.perform(put("/api/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void update_unknownId_returns404() throws Exception {
        RecipeRequest request = buildRequest();
        when(recipeService.update(eq(99L), any()))
                .thenThrow(new ResourceNotFoundException("Recipe not found: 99"));

        mockMvc.perform(put("/api/recipes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_existingId_returns204() throws Exception {
        mockMvc.perform(delete("/api/recipes/1"))
                .andExpect(status().isNoContent());

        verify(recipeService).delete(1L);
    }

    @Test
    void delete_unknownId_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Recipe not found: 99"))
                .when(recipeService).delete(99L);

        mockMvc.perform(delete("/api/recipes/99"))
                .andExpect(status().isNotFound());
    }
}