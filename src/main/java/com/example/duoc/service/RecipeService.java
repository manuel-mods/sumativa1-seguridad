package com.example.duoc.service;

import com.example.duoc.model.Recipe;
import com.example.duoc.repository.RecipeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;
    
    // Find all recipes
    public List<Recipe> findAllRecipes() {
        return recipeRepository.findAll();
    }
    
    // Find a recipe by ID
    public Optional<Recipe> findRecipeById(Long id) {
        return recipeRepository.findById(id);
    }
    
    // Find popular recipes based on views
    public List<Recipe> findPopularRecipes() {
        return recipeRepository.findTop5ByOrderByViewsDesc();
    }
    
    // Find recent recipes based on ID (assuming higher ID means more recent)
    public List<Recipe> findRecentRecipes() {
        return recipeRepository.findTop5ByOrderByIdDesc();
    }
    
    // Search recipes by various criteria
    public List<Recipe> searchRecipes(String name, String cuisineType, String ingredient, 
                                     String countryOfOrigin, String difficulty) {
        // If no search criteria, return all recipes
        if ((name == null || name.isEmpty()) && 
            (cuisineType == null || cuisineType.isEmpty()) && 
            (ingredient == null || ingredient.isEmpty()) && 
            (countryOfOrigin == null || countryOfOrigin.isEmpty()) && 
            (difficulty == null || difficulty.isEmpty())) {
            return findAllRecipes();
        }
        
        // Start with all recipes
        List<Recipe> result = findAllRecipes();
        
        // Apply filters if they are not null or empty
        if (name != null && !name.isEmpty()) {
            result = result.stream()
                    .filter(recipe -> recipe.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (cuisineType != null && !cuisineType.isEmpty()) {
            result = result.stream()
                    .filter(recipe -> recipe.getCuisineType().equalsIgnoreCase(cuisineType))
                    .collect(Collectors.toList());
        }
        
        if (ingredient != null && !ingredient.isEmpty()) {
            result = result.stream()
                    .filter(recipe -> recipe.getIngredients().stream()
                            .anyMatch(ing -> ing.toLowerCase().contains(ingredient.toLowerCase())))
                    .collect(Collectors.toList());
        }
        
        if (countryOfOrigin != null && !countryOfOrigin.isEmpty()) {
            result = result.stream()
                    .filter(recipe -> recipe.getCountryOfOrigin().equalsIgnoreCase(countryOfOrigin))
                    .collect(Collectors.toList());
        }
        
        if (difficulty != null && !difficulty.isEmpty()) {
            result = result.stream()
                    .filter(recipe -> recipe.getDifficulty().equalsIgnoreCase(difficulty))
                    .collect(Collectors.toList());
        }
        
        return result;
    }
    
    // Initialize with some mock data
    @PostConstruct
    public void init() {
        // If no recipes exist, add some sample data
        if (recipeRepository.count() == 0) {
            // Create and save sample recipes
            createSampleRecipes();
        }
    }
    
    private void createSampleRecipes() {
        // Sample recipe 1
        Recipe pasta = new Recipe();
        pasta.setName("Spaghetti Carbonara");
        pasta.setDescription("A classic Italian pasta dish with eggs, cheese, pancetta, and black pepper.");
        pasta.setCuisineType("Italian");
        pasta.setCountryOfOrigin("Italy");
        pasta.setDifficulty("Medium");
        pasta.setCookTimeMinutes(30);
        pasta.setInstructions("1. Cook spaghetti according to package instructions.\n" +
                "2. In a bowl, whisk together eggs, grated pecorino Romano, and black pepper.\n" +
                "3. In a pan, cook pancetta until crispy.\n" +
                "4. Add the hot pasta to the pancetta, then add the egg mixture, stirring quickly to create a creamy sauce.\n" +
                "5. Serve immediately with extra cheese and black pepper.");
        pasta.setViews(120);
        pasta.setIngredients(Arrays.asList("Spaghetti", "Eggs", "Pecorino Romano", "Pancetta", "Black Pepper"));
        pasta.setImageUrls(Arrays.asList("/images/carbonara1.jpg", "/images/carbonara2.jpg"));
        
        // Sample recipe 2
        Recipe tacos = new Recipe();
        tacos.setName("Chicken Tacos");
        tacos.setDescription("Delicious Mexican-style tacos with marinated chicken, fresh salsa, and guacamole.");
        tacos.setCuisineType("Mexican");
        tacos.setCountryOfOrigin("Mexico");
        tacos.setDifficulty("Easy");
        tacos.setCookTimeMinutes(45);
        tacos.setInstructions("1. Marinate chicken with lime, garlic, and spices for at least 30 minutes.\n" +
                "2. Cook chicken in a hot skillet until fully cooked.\n" +
                "3. Warm tortillas in a dry pan.\n" +
                "4. Assemble tacos with chicken, salsa, guacamole, and your favorite toppings.");
        tacos.setViews(200);
        tacos.setIngredients(Arrays.asList("Chicken", "Tortillas", "Lime", "Garlic", "Cilantro", "Avocado", "Tomatoes", "Onion"));
        tacos.setImageUrls(Arrays.asList("/images/tacos1.jpg", "/images/tacos2.jpg"));
        
        // Sample recipe 3
        Recipe curry = new Recipe();
        curry.setName("Vegetable Curry");
        curry.setDescription("A flavorful and aromatic vegetable curry with a blend of Indian spices.");
        curry.setCuisineType("Indian");
        curry.setCountryOfOrigin("India");
        curry.setDifficulty("Medium");
        curry.setCookTimeMinutes(50);
        curry.setInstructions("1. Heat oil in a large pot and sauté onions until golden.\n" +
                "2. Add garlic, ginger, and spices, cooking until fragrant.\n" +
                "3. Add vegetables and cook for a few minutes.\n" +
                "4. Pour in coconut milk and simmer until vegetables are tender.\n" +
                "5. Serve with rice or naan bread.");
        curry.setViews(150);
        curry.setIngredients(Arrays.asList("Potatoes", "Carrots", "Peas", "Cauliflower", "Coconut Milk", "Curry Powder", "Garam Masala", "Ginger", "Garlic", "Onion"));
        curry.setImageUrls(Arrays.asList("/images/curry1.jpg", "/images/curry2.jpg"));
        
        // Sample recipe 4
        Recipe sushi = new Recipe();
        sushi.setName("California Roll");
        sushi.setDescription("A sushi roll with crab, avocado, and cucumber, popular both in Japan and worldwide.");
        sushi.setCuisineType("Japanese");
        sushi.setCountryOfOrigin("Japan");
        sushi.setDifficulty("Hard");
        sushi.setCookTimeMinutes(60);
        sushi.setInstructions("1. Prepare sushi rice by mixing cooked rice with rice vinegar, sugar, and salt.\n" +
                "2. Place a sheet of nori on a bamboo mat.\n" +
                "3. Spread rice evenly on the nori, then flip so rice is facing down.\n" +
                "4. Place crab, avocado, and cucumber in the center.\n" +
                "5. Roll tightly using the bamboo mat.\n" +
                "6. Cut into pieces and serve with soy sauce and wasabi.");
        sushi.setViews(180);
        sushi.setIngredients(Arrays.asList("Sushi Rice", "Nori", "Crab Meat", "Avocado", "Cucumber", "Rice Vinegar", "Sesame Seeds"));
        sushi.setImageUrls(Arrays.asList("/images/sushi1.jpg", "/images/sushi2.jpg"));
        
        // Sample recipe 5
        Recipe paella = new Recipe();
        paella.setName("Seafood Paella");
        paella.setDescription("Traditional Spanish rice dish with an array of seafood and saffron.");
        paella.setCuisineType("Spanish");
        paella.setCountryOfOrigin("Spain");
        paella.setDifficulty("Hard");
        paella.setCookTimeMinutes(75);
        paella.setInstructions("1. Heat olive oil in a paella pan and sauté onions and garlic.\n" +
                "2. Add rice and saffron, stirring to coat the rice with oil.\n" +
                "3. Add fish stock and bring to a simmer.\n" +
                "4. Add seafood and cook until rice is tender and seafood is cooked through.\n" +
                "5. Let stand for 5 minutes before serving.");
        paella.setViews(90);
        paella.setIngredients(Arrays.asList("Arborio Rice", "Saffron", "Shrimp", "Mussels", "Clams", "Fish Stock", "Bell Peppers", "Onion", "Garlic", "Olive Oil"));
        paella.setImageUrls(Arrays.asList("/images/paella1.jpg", "/images/paella2.jpg"));
        
        // Save all recipes
        List<Recipe> recipes = Arrays.asList(pasta, tacos, curry, sushi, paella);
        recipeRepository.saveAll(recipes);
    }
}
