package com.example.Recipe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    Optional<List<Recipe>> findByCategoryIgnoreCaseOrderByDateDesc(String category);
    Optional<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);


}