package com.example.duoc.controller;

import com.example.duoc.model.Recipe;
import com.example.duoc.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    
    /**
     * Home page endpoint
     * Shows recent and popular recipes, plus commercial banners
     */
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("popularRecipes", recipeService.findPopularRecipes());
        model.addAttribute("recentRecipes", recipeService.findRecentRecipes());
        model.addAttribute("banners", generateBanners());  // Mock commercial banners
        return "home";
    }
    
    /**
     * Search page endpoint
     * Allows searching by multiple criteria
     */
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cuisineType,
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String countryOfOrigin,
            @RequestParam(required = false) String difficulty,
            Model model) {
        
        model.addAttribute("recipes", recipeService.searchRecipes(
                name, cuisineType, ingredient, countryOfOrigin, difficulty));
        
        // Add search criteria back to the model for form persistence
        model.addAttribute("name", name);
        model.addAttribute("cuisineType", cuisineType);
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("countryOfOrigin", countryOfOrigin);
        model.addAttribute("difficulty", difficulty);
        
        return "search";
    }
    
    /**
     * Recipe detail page endpoint
     * Protected, requires authentication
     */
    @GetMapping("/recipes/{id}")
    public String recipeDetail(@PathVariable Long id, Model model) {
        Optional<Recipe> recipe = recipeService.findRecipeById(id);
        
        if (recipe.isPresent()) {
            model.addAttribute("recipe", recipe.get());
            return "recipe-detail";
        } else {
            return "redirect:/home";
        }
    }
    
    
    
    /**
     * Mock method to generate commercial banners
     * In a real app, these would come from a database or ad service
     */
    private String[] generateBanners() {
        return new String[] {
            "Cooking Supplies Sale - 20% Off!",
            "Premium Cookware - Free Shipping!",
            "Join Our Cooking Class Today!"
        };
    }
}
