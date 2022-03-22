package com.example.Recipe;

import lombok.Data;

@Data
public class RecipeDTO {

    Long id;

    public RecipeDTO(Long id) {
        this.id = id;
    }
}
