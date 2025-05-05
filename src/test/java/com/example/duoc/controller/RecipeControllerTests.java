package com.example.duoc.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.duoc.model.Recipe;
import com.example.duoc.service.RecipeService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTests {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private RecipeService recipeService;

  private Recipe testRecipe;

  @BeforeEach
  void setUp() {
    testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe.setName("Test Recipe");
    testRecipe.setDescription("Test Description");
    testRecipe.setCuisineType("Italian");
    testRecipe.setCountryOfOrigin("Italy");
    testRecipe.setDifficulty("Medium");
    testRecipe.setCookTimeMinutes(30);
    testRecipe.setIngredients(Arrays.asList("Ingredient 1", "Ingredient 2"));
    testRecipe.setInstructions("Test instructions");
    testRecipe.setViews(100);
    testRecipe.setImageUrls(Arrays.asList("/image1.jpg", "/image2.jpg"));

    Recipe testRecipe2 = new Recipe();
    testRecipe2.setId(2L);
    testRecipe2.setName("Test Recipe 2");
    testRecipe2.setDescription("Test Description 2");
    testRecipe2.setCuisineType("Mexican");
    testRecipe2.setCountryOfOrigin("Mexico");
    testRecipe2.setDifficulty("Easy");
    testRecipe2.setCookTimeMinutes(20);
    testRecipe2.setIngredients(Arrays.asList("Ingredient 3", "Ingredient 4"));
    testRecipe2.setInstructions("Test instructions 2");
    testRecipe2.setViews(50);
    testRecipe2.setImageUrls(Arrays.asList("/image3.jpg", "/image4.jpg"));

    List<Recipe> testRecipes = Arrays.asList(testRecipe, testRecipe2);

    when(recipeService.findPopularRecipes()).thenReturn(testRecipes);
    when(recipeService.findRecentRecipes()).thenReturn(testRecipes);
    when(recipeService.findRecipeById(1L)).thenReturn(Optional.of(testRecipe));
    when(recipeService.findRecipeById(999L)).thenReturn(Optional.empty());
    when(recipeService.findAllRecipes()).thenReturn(testRecipes);
    when(recipeService.searchRecipes(any(), any(), any(), any(), any())).thenReturn(testRecipes);
  }

  @Test
  void home_ShowsRecipeHomeScreen() throws Exception {
    mockMvc
        .perform(get("/home"))
        .andDo(print())
        .andExpect(content().string(containsString("Welcome to Recipe App")))
        .andExpect(status().isOk());

    verify(recipeService).findPopularRecipes();
    verify(recipeService).findRecentRecipes();
  }

  @Test
  void home_ShowsRecipeHomeScreenOnRootHost() throws Exception {
    mockMvc
        .perform(get("/"))
        .andDo(print())
        .andExpect(content().string(containsString("Welcome to Recipe App")))
        .andExpect(status().isOk());

    verify(recipeService).findPopularRecipes();
    verify(recipeService).findRecentRecipes();
  }

  @Test
  void search_WithNoParameters_ShowsAllRecipes() throws Exception {
    mockMvc
        .perform(get("/search"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("recipes"))
        .andExpect(view().name("search"));

    verify(recipeService).searchRecipes(null, null, null, null, null);
  }

  @Test
  void search_WithParameters_FiltersRecipes() throws Exception {
    mockMvc
        .perform(
            get("/search")
                .param("name", "Test")
                .param("cuisineType", "Italian")
                .param("ingredient", "Ingredient 1")
                .param("countryOfOrigin", "Italy")
                .param("difficulty", "Medium"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("recipes"))
        .andExpect(model().attribute("name", "Test"))
        .andExpect(model().attribute("cuisineType", "Italian"))
        .andExpect(model().attribute("ingredient", "Ingredient 1"))
        .andExpect(model().attribute("countryOfOrigin", "Italy"))
        .andExpect(model().attribute("difficulty", "Medium"))
        .andExpect(view().name("search"));

    verify(recipeService).searchRecipes("Test", "Italian", "Ingredient 1", "Italy", "Medium");
  }

  @Test
  void recipeDetail_WithExistingId_ShowsRecipeDetail() throws Exception {
    mockMvc
        .perform(get("/recipes/1").with(user("user1").password("password1").roles("USER")))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("recipe"))
        .andExpect(model().attribute("recipe", testRecipe))
        .andExpect(view().name("recipe-detail"));

    verify(recipeService).findRecipeById(1L);
  }

  @Test
  void recipeDetail_WithNonExistingId_RedirectsToHome() throws Exception {
    mockMvc
        .perform(get("/recipes/999").with(user("user1").password("password1").roles("USER")))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/home"));

    verify(recipeService).findRecipeById(999L);
  }

  @Test
  void recipeDetail_RedirectsToLogin() throws Exception {
    mockMvc
        .perform(get("/recipes/999"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost/login"));
  }
}
