package com.example.duoc.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private String cuisineType;
    private String countryOfOrigin;
    private String difficulty;
    private Integer cookTimeMinutes;
    private Integer views;

    @Lob
    @Column(name = "instructions", columnDefinition = "LONGTEXT")
    private String instructions;
    
    
    @ElementCollection
    private List<String> ingredients;
    
    @ElementCollection
    private List<String> imageUrls;

    // Constructors
    public Recipe() {
    }

    public Recipe(Long id, String name, String description, String cuisineType, String countryOfOrigin, 
                 String difficulty, Integer cookTimeMinutes, String instructions, Integer views,
                 List<String> ingredients, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cuisineType = cuisineType;
        this.countryOfOrigin = countryOfOrigin;
        this.difficulty = difficulty;
        this.cookTimeMinutes = cookTimeMinutes;
        this.instructions = instructions;
        this.views = views;
        this.ingredients = ingredients;
        this.imageUrls = imageUrls;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getCookTimeMinutes() {
        return cookTimeMinutes;
    }

    public void setCookTimeMinutes(Integer cookTimeMinutes) {
        this.cookTimeMinutes = cookTimeMinutes;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
