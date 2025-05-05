package com.example.duoc.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.example.duoc.model.Recipe;
import com.example.duoc.repository.RecipeRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTests {

  @Mock private RecipeRepository recipeRepository;

  @InjectMocks private RecipeService recipeService;

  @Captor private ArgumentCaptor<List<Recipe>> recipeListCaptor;

  private Recipe testRecipe1;
  private Recipe testRecipe2;
  private List<Recipe> testRecipes;

  @BeforeEach
  void setUp() {
    testRecipe1 = new Recipe();
    testRecipe1.setId(1L);
    testRecipe1.setName("Spaghetti Carbonara");
    testRecipe1.setDescription("Italian pasta dish");
    testRecipe1.setCuisineType("Italian");
    testRecipe1.setCountryOfOrigin("Italy");
    testRecipe1.setDifficulty("Medium");
    testRecipe1.setCookTimeMinutes(30);
    testRecipe1.setIngredients(Arrays.asList("Pasta", "Eggs", "Cheese", "Bacon"));
    testRecipe1.setInstructions("Cook pasta, mix with eggs, cheese and bacon");
    testRecipe1.setViews(150);
    testRecipe1.setImageUrls(Arrays.asList("/image1.jpg", "/image2.jpg"));

    testRecipe2 = new Recipe();
    testRecipe2.setId(2L);
    testRecipe2.setName("Chicken Tacos");
    testRecipe2.setDescription("Mexican tacos with chicken");
    testRecipe2.setCuisineType("Mexican");
    testRecipe2.setCountryOfOrigin("Mexico");
    testRecipe2.setDifficulty("Easy");
    testRecipe2.setCookTimeMinutes(45);
    testRecipe2.setIngredients(Arrays.asList("Chicken", "Tortillas", "Salsa", "Avocado"));
    testRecipe2.setInstructions("Cook chicken, warm tortillas, assemble tacos");
    testRecipe2.setViews(200);
    testRecipe2.setImageUrls(Arrays.asList("/image3.jpg", "/image4.jpg"));

    testRecipes = Arrays.asList(testRecipe1, testRecipe2);
  }

  @Test
  void findAllRecipes_ReturnsAllRecipes() {
    when(recipeRepository.findAll()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.findAllRecipes();

    assertEquals(2, result.size());
    assertEquals(testRecipe1.getName(), result.get(0).getName());
    assertEquals(testRecipe2.getName(), result.get(1).getName());
    verify(recipeRepository).findAll();
  }

  @Test
  void findRecipeById_WithExistingId_ReturnsRecipe() {
    when(recipeRepository.findById(1L)).thenReturn(Optional.of(testRecipe1));

    Optional<Recipe> result = recipeService.findRecipeById(1L);

    assertTrue(result.isPresent());
    assertEquals(testRecipe1.getName(), result.get().getName());
    verify(recipeRepository).findById(1L);
  }

  @Test
  void findRecipeById_WithNonExistingId_ReturnsEmptyOptional() {
    when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

    Optional<Recipe> result = recipeService.findRecipeById(999L);

    assertFalse(result.isPresent());
    verify(recipeRepository).findById(999L);
  }

  @Test
  void findPopularRecipes_ReturnsTopRecipesByViews() {
    when(recipeRepository.findTop5ByOrderByViewsDesc()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.findPopularRecipes();

    assertEquals(2, result.size());
    verify(recipeRepository).findTop5ByOrderByViewsDesc();
  }

  @Test
  void findRecentRecipes_ReturnsTopRecipesById() {
    when(recipeRepository.findTop5ByOrderByIdDesc()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.findRecentRecipes();

    assertEquals(2, result.size());
    verify(recipeRepository).findTop5ByOrderByIdDesc();
  }

  @Test
  void searchRecipes_WithNoParameters_ReturnsAllRecipes() {
    when(recipeRepository.findAll()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.searchRecipes(null, null, null, null, null);

    assertEquals(2, result.size());
    verify(recipeRepository).findAll();
  }

  @Test
  void searchRecipes_WithNameParameter_FiltersRecipesByName() {
    when(recipeRepository.findAll()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.searchRecipes("spaghetti", null, null, null, null);

    assertEquals(1, result.size());
    assertEquals("Spaghetti Carbonara", result.get(0).getName());
    verify(recipeRepository).findAll();
  }

  @Test
  void searchRecipes_WithCuisineTypeParameter_FiltersRecipesByCuisineType() {

    when(recipeRepository.findAll()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.searchRecipes(null, "mexican", null, null, null);

    assertEquals(1, result.size());
    assertEquals("Mexican", result.get(0).getCuisineType());
    verify(recipeRepository).findAll();
  }

  @Test
  void searchRecipes_WithIngredientParameter_FiltersRecipesByIngredient() {

    when(recipeRepository.findAll()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.searchRecipes(null, null, "cheese", null, null);

    assertEquals(1, result.size());
    assertEquals("Spaghetti Carbonara", result.get(0).getName());
    verify(recipeRepository).findAll();
  }

  @Test
  void searchRecipes_WithCountryOfOriginParameter_FiltersRecipesByCountry() {

    when(recipeRepository.findAll()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.searchRecipes(null, null, null, "italy", null);

    assertEquals(1, result.size());
    assertEquals("Italy", result.get(0).getCountryOfOrigin());
    verify(recipeRepository).findAll();
  }

  @Test
  void searchRecipes_WithDifficultyParameter_FiltersRecipesByDifficulty() {

    when(recipeRepository.findAll()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.searchRecipes(null, null, null, null, "easy");

    assertEquals(1, result.size());
    assertEquals("Easy", result.get(0).getDifficulty());
    verify(recipeRepository).findAll();
  }

  @Test
  void searchRecipes_WithMultipleParameters_FiltersRecipesByAllCriteria() {

    when(recipeRepository.findAll()).thenReturn(testRecipes);

    List<Recipe> result =
        recipeService.searchRecipes("chicken", "mexican", "tortillas", "mexico", "easy");

    assertEquals(1, result.size());
    assertEquals("Chicken Tacos", result.get(0).getName());
    verify(recipeRepository).findAll();
  }

  @Test
  void searchRecipes_WithNonMatchingParameters_ReturnsEmptyList() {

    when(recipeRepository.findAll()).thenReturn(testRecipes);

    List<Recipe> result = recipeService.searchRecipes("nonexisting", null, null, null, null);

    assertTrue(result.isEmpty());
    verify(recipeRepository).findAll();
  }

  @Test
  void init_WithNoRecipes_CreatesSampleRecipes() {
    when(recipeRepository.count()).thenReturn(0L);

    recipeService.init();

    verify(recipeRepository).saveAll(recipeListCaptor.capture());

    List<Recipe> capturedRecipes = recipeListCaptor.getValue();

    assertEquals(5, capturedRecipes.size());

    assertTrue(capturedRecipes.stream().anyMatch(r -> "Spaghetti Carbonara".equals(r.getName())));
    assertTrue(capturedRecipes.stream().anyMatch(r -> "Chicken Tacos".equals(r.getName())));
    assertTrue(capturedRecipes.stream().anyMatch(r -> "Vegetable Curry".equals(r.getName())));
    assertTrue(capturedRecipes.stream().anyMatch(r -> "California Roll".equals(r.getName())));
    assertTrue(capturedRecipes.stream().anyMatch(r -> "Seafood Paella".equals(r.getName())));
  }

  @Test
  void init_WithExistingRecipes_DoesNotCreateSampleRecipes() {
    when(recipeRepository.count()).thenReturn(10L);

    recipeService.init();

    verify(recipeRepository, never()).saveAll(anyList());
  }
}
