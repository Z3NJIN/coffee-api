package dev.brewlog.coffee.controller;

import dev.brewlog.coffee.dto.request.BrewRequest;
import dev.brewlog.coffee.dto.response.BrewResponse;
import dev.brewlog.coffee.service.BrewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class BrewController {

    private final BrewService brewService;

    public BrewController(BrewService brewService) {
        this.brewService = brewService;
    }

    @PostMapping("/api/brews")
    public ResponseEntity<BrewResponse> create(@Valid @RequestBody BrewRequest request) {
        BrewResponse created = brewService.create(request);
        return ResponseEntity
                .created(URI.create("/api/brews/" + created.id()))
                .body(created);
    }

    @GetMapping("/api/brews/{id}")
    public ResponseEntity<BrewResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(brewService.getById(id));
    }

    // Anidado bajo recipes porque conceptualmente un brew "pertenece" a una receta
    @GetMapping("/api/recipes/{recipeId}/brews")
    public ResponseEntity<List<BrewResponse>> getByRecipe(@PathVariable Long recipeId) {
        return ResponseEntity.ok(brewService.getByRecipeIdDesc(recipeId));
    }

    @GetMapping("/api/recipes/{recipeId}/brews/evolution")
    public ResponseEntity<List<BrewResponse>> getEvolution(@PathVariable Long recipeId) {
        return ResponseEntity.ok(brewService.getEvolutionByRecipeId(recipeId));
    }

    @GetMapping("/api/recipes/{recipeId}/brews/average-rating")
    public ResponseEntity<Map<String, Double>> getAverageRating(@PathVariable Long recipeId) {
        Double average = brewService.getAverageRating(recipeId);
        return ResponseEntity.ok(Map.of("averageRating" , average != null ? average : 0.0));
    }

}
