package com.example.duoc.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecipeTests {

  private Recipe recipe;
  private final Long id = 1L;
  private final String name = "Test Recipe";
  private final String description = "A test recipe description";
  private final String cuisineType = "Test Cuisine";
  private final String countryOfOrigin = "Test Country";
  private final String difficulty = "Medium";
  private final Integer cookTimeMinutes = 30;
  private final String instructions = "Test instructions for cooking";
  private final Integer views = 100;
  private final List<String> ingredients = Arrays.asList("Ingredient 1", "Ingredient 2");
  private final List<String> imageUrls = Arrays.asList("image1.jpg", "image2.jpg");

  @BeforeEach
  void setUp() {
    recipe =
        new Recipe(
            id,
            name,
            description,
            cuisineType,
            countryOfOrigin,
            difficulty,
            cookTimeMinutes,
            instructions,
            views,
            ingredients,
            imageUrls);
  }

  @Test
  void testNoArgsConstructor() {
    Recipe emptyRecipe = new Recipe();
    assertNotNull(emptyRecipe);
    assertNull(emptyRecipe.getId());
    assertNull(emptyRecipe.getName());
    assertNull(emptyRecipe.getDescription());
    assertNull(emptyRecipe.getCuisineType());
    assertNull(emptyRecipe.getCountryOfOrigin());
    assertNull(emptyRecipe.getDifficulty());
    assertNull(emptyRecipe.getCookTimeMinutes());
    assertNull(emptyRecipe.getInstructions());
    assertNull(emptyRecipe.getViews());
    assertNull(emptyRecipe.getIngredients());
    assertNull(emptyRecipe.getImageUrls());
  }

  @Test
  void testAllArgsConstructor() {
    assertEquals(id, recipe.getId());
    assertEquals(name, recipe.getName());
    assertEquals(description, recipe.getDescription());
    assertEquals(cuisineType, recipe.getCuisineType());
    assertEquals(countryOfOrigin, recipe.getCountryOfOrigin());
    assertEquals(difficulty, recipe.getDifficulty());
    assertEquals(cookTimeMinutes, recipe.getCookTimeMinutes());
    assertEquals(instructions, recipe.getInstructions());
    assertEquals(views, recipe.getViews());
    assertEquals(ingredients, recipe.getIngredients());
    assertEquals(imageUrls, recipe.getImageUrls());
  }

  @Test
  void testIdGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    testRecipe.setId(2L);
    assertEquals(2L, testRecipe.getId());
  }

  @Test
  void testNameGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    testRecipe.setName("New Recipe Name");
    assertEquals("New Recipe Name", testRecipe.getName());
  }

  @Test
  void testDescriptionGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    testRecipe.setDescription("New description");
    assertEquals("New description", testRecipe.getDescription());
  }

  @Test
  void testCuisineTypeGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    testRecipe.setCuisineType("Italian");
    assertEquals("Italian", testRecipe.getCuisineType());
  }

  @Test
  void testCountryOfOriginGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    testRecipe.setCountryOfOrigin("Italy");
    assertEquals("Italy", testRecipe.getCountryOfOrigin());
  }

  @Test
  void testDifficultyGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    testRecipe.setDifficulty("Hard");
    assertEquals("Hard", testRecipe.getDifficulty());
  }

  @Test
  void testCookTimeMinutesGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    testRecipe.setCookTimeMinutes(45);
    assertEquals(45, testRecipe.getCookTimeMinutes());
  }

  @Test
  void testInstructionsGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    testRecipe.setInstructions("New detailed instructions");
    assertEquals("New detailed instructions", testRecipe.getInstructions());
  }

  @Test
  void testViewsGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    testRecipe.setViews(200);
    assertEquals(200, testRecipe.getViews());
  }

  @Test
  void testIngredientsGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    List<String> newIngredients = Arrays.asList("Flour", "Sugar", "Eggs");
    testRecipe.setIngredients(newIngredients);
    assertEquals(newIngredients, testRecipe.getIngredients());
  }

  @Test
  void testImageUrlsGetterAndSetter() {
    Recipe testRecipe = new Recipe();
    List<String> newImageUrls = Arrays.asList("new-image1.jpg", "new-image2.jpg");
    testRecipe.setImageUrls(newImageUrls);
    assertEquals(newImageUrls, testRecipe.getImageUrls());
  }
}
