package com.example.Recipe;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Validated
@RestController
public class RecipeController {


    private Recipe recipe;
    private RecipeRepository recipeRepository;
    private ShutdownService shutdownService;
    private RecipeService recipeService;
    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;
    private UserDetails userDetails;

    @Autowired
    public RecipeController(Recipe recipe, RecipeRepository recipeRepository, ShutdownService shutdownService, RecipeService recipeService, UserRepository userRepository, BCryptPasswordEncoder encoder,UserDetails userDetails) {
        this.recipe = recipe;
        this.recipeRepository = recipeRepository;
        this.shutdownService = shutdownService;
        this.recipeService = recipeService;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userDetails = userDetails;

    }

    public RecipeController() {
    }

    @PostMapping("/actuator/shutdown")
    public HttpStatus shutdownContext() {
        shutdownService.shutDown();
        return HttpStatus.OK;
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        User userByEmail = userRepository.findByEmail(user.getEmail());

        if (userByEmail != null) {
            return ResponseEntity.badRequest().build();
        } else {
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRoles(Set.of(new Role("ADMIN")));
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<?> addRecipe(@RequestBody(required = true) @Valid Recipe recipe, @AuthenticationPrincipal UserDetails userDetails) {
        String detailsUsername = userDetails.getUsername();
        User user = userRepository.findByEmail(detailsUsername);
        Recipe newRecipe = new Recipe();
        newRecipe.setDirections(recipe.getDirections());
        newRecipe.setIngredients(recipe.getIngredients());
        newRecipe.setCategory(recipe.getCategory());
        newRecipe.setDescription(recipe.getDescription());
        newRecipe.setName(recipe.getName());
        newRecipe.setUser(user);
        recipeRepository.save(newRecipe);
        return new ResponseEntity<>(new RecipeDTO(newRecipe.getId()), HttpStatus.OK);
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity<?> updateRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable @NonNull Long id, @Valid @RequestBody Recipe recipe) throws NotFoundException {
        Recipe newRecipe = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
        String username = userDetails.getUsername();
        if ( newRecipe.getUser().getEmail().equals(username)) {
            return recipeService.updateRecipeById(recipe, newRecipe);
        } else return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable @NonNull Long id) throws NotFoundException {
        return recipeService.getRecipeById(id);
    }

    @GetMapping("/api/recipe/search")
    public Object searchRecipeByCategory(@Valid @RequestParam(value = "category", required = false) String category, @Valid @RequestParam(value = "name", required = false) String name) throws NotFoundException {
        return recipeService.searchRecipe(category, name);
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable @NonNull Long id) throws NotFoundException {
        Recipe newRecipe = recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
        String username = userDetails.getUsername();
        if ( newRecipe.getUser().getEmail().equals(username)) {
            recipeRepository.delete(newRecipe);
            return ResponseEntity.noContent().build();
        } else return new ResponseEntity(HttpStatus.FORBIDDEN);

    }
}