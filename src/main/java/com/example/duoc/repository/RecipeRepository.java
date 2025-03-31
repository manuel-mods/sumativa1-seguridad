package com.example.duoc.repository;

import com.example.duoc.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    // Find recipes by name containing the search text (case insensitive)
    List<Recipe> findByNameContainingIgnoreCase(String name);
    
    // Find recipes by cuisine type
    List<Recipe> findByCuisineTypeIgnoreCase(String cuisineType);
    
    // Find recipes by country of origin
    List<Recipe> findByCountryOfOriginIgnoreCase(String countryOfOrigin);
    
    // Find recipes by difficulty
    List<Recipe> findByDifficultyIgnoreCase(String difficulty);
    
    // Find recipes with specific ingredient (using JPQL to search in a collection)
    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :ingredient, '%'))")
    List<Recipe> findByIngredientContainingIgnoreCase(@Param("ingredient") String ingredient);
    
    // Find recipes by multiple criteria (will be implemented in the service)
    
    // Find most popular recipes by views
    List<Recipe> findTop5ByOrderByViewsDesc();
    
    // Find most recent recipes (assuming id increases with new entries)
    List<Recipe> findTop5ByOrderByIdDesc();
}
