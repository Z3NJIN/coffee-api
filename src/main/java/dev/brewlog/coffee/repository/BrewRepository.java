package dev.brewlog.coffee.repository;

import dev.brewlog.coffee.model.Brew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrewRepository extends JpaRepository<Brew, Long> {
    // Para el gráfico de evolución (orden cronológico ascendente)
    List<Brew> findByRecipeIdOrderByBrewedAtAsc(Long recipeId);

    // Para la bitácora (más reciente primero)
    List<Brew> findByRecipeIdOrderByBrewedAtDesc(Long recipeId);

    @Query("SELECT AVG(b.rating) FROM Brew b WHERE b.recipe.id = :recipeId")
    Optional<Double> findAverageRatingByRecipeId(@Param("recipeId") Long recipeId);
}