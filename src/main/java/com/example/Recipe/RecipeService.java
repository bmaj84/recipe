package com.example.Recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeService() {
    }

    public Recipe getRecipeById(Long id) throws NotFoundException {
        try {
            if (recipeRepository.findById(id).isPresent()) {
                 return recipeRepository.findById(id).get();
            } else {
                throw new NotFoundException("Not found");
            }
        }catch (Exception e) {
            throw new NotFoundException("Not found");
        }
    }

    public ResponseEntity<Object> updateRecipeById(Recipe recipe, Recipe newRecipe) {
        newRecipe.setName(recipe.getName());
        newRecipe.setCategory(recipe.getCategory());
        newRecipe.setDescription(recipe.getDescription());
        newRecipe.setIngredients(recipe.getIngredients());
        newRecipe.setDirections(recipe.getDirections());
        recipeRepository.save(newRecipe);
        return ResponseEntity.noContent().build();
    }

    public Object searchRecipe(String category, String name) throws NotFoundException {
        if (name == null && category == null) {
            return ResponseEntity.badRequest().build();
        }
        if ( name == null) {
           if (recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category).isPresent()) {
               return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category).get();
           } else {
               throw new NotFoundException("Not found");
           }
        } else if ( category == null) {
            if (recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name).isPresent()) {
                return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name).get();
            } else {
                throw new NotFoundException("Not found");
            }
        } else
            return ResponseEntity.badRequest().build();
    }
}
