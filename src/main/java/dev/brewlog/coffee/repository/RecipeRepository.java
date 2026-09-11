package dev.brewlog.coffee.repository;

import dev.brewlog.coffee.model.BrewMethod;
import dev.brewlog.coffee.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByMethod(BrewMethod method);
//List<Recipe> findByCoffeeOriginId(Long coffeeOriginId);
}