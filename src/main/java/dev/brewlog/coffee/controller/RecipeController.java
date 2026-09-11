package dev.brewlog.coffee.controller;

import dev.brewlog.coffee.dto.request.RecipeRequest;
import dev.brewlog.coffee.dto.response.RecipeResponse;
import dev.brewlog.coffee.model.BrewMethod;
import dev.brewlog.coffee.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<RecipeResponse> create(
            @Valid @RequestBody RecipeRequest request
    ) {
        RecipeResponse created = recipeService.create(request);
        return ResponseEntity
                .created(URI.create("/api/recipes/" + created.id()))
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponse>> getAll(
            @RequestParam(required = false) BrewMethod method
    ) {
        List<RecipeResponse> recipes = (method != null)
                ? recipeService.getByMethod(method)
                : recipeService.getAll();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> update(
            @PathVariable Long id, @Valid @RequestBody RecipeRequest request
    ) {
        return ResponseEntity.ok(recipeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recipeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
