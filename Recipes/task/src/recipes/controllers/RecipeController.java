package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.entities.RecipeInfo;
import recipes.services.RecipeService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
@Validated
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}")
    public RecipeInfo getRecipe(HttpServletResponse response, @PathVariable int id) {
        response.addHeader("Content-type", "application/json");
        Optional<RecipeInfo> recipe = recipeService.findRecipeById(id);
        if (recipe.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found.");
        return recipe.get();
    }

    @PostMapping("/recipe/new")
    public String addRecipeID(HttpServletResponse response, @Valid @RequestBody RecipeInfo recipe) {
        response.addHeader("Content-type", "application/json");
        RecipeInfo newRecipe = new RecipeInfo(recipe.getName(), recipe.getCategory(), LocalDateTime.now(),recipe.getDescription(),
                recipe.getIngredients(), recipe.getDirections());
        newRecipe.setEmail(getLoggedInUser());
        recipeService.saveRecipe(newRecipe);
        return String.format("{ \"id\":%s }", newRecipe.getId());
    }

    @DeleteMapping("/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable int id) {
        Optional<RecipeInfo> recipe = recipeService.findRecipeById(id);
        if (recipe.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found.");
        if (!recipe.get().getEmail().equals(getLoggedInUser()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own recipes.");
        recipeService.deleteRecipe(id);
    }

    @PutMapping("/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(@PathVariable int id,
                             @Valid @RequestBody RecipeInfo newRecipe) {
        Optional<RecipeInfo> recipe = recipeService.findRecipeById(id);

        if (recipe.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found.");

        if (!recipeService.findRecipeById(id).get().getEmail().equals(getLoggedInUser()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own recipes.");

        recipeService.updateRecipe(id, newRecipe);
    }

    @GetMapping(value = "/recipe/search", params = "category")
    public List<RecipeInfo> getRecipeByCategory(@RequestParam("category") String category) {
        return recipeService.findByCategory(category);
    }

    @GetMapping(value = "/recipe/search", params = "name")
    public List<RecipeInfo> getRecipeByName(@RequestParam("name") String name) {
        return recipeService.findByName(name);
    }

    public String getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal().toString();
    }

}