package recipes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.dao.RecipeRepo;
import recipes.entities.RecipeInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    RecipeRepo recipeRepo;

    @Autowired
    public RecipeService(RecipeRepo recipeRepo) {
        this.recipeRepo = recipeRepo;
    }

    public Optional<RecipeInfo> findRecipeById(Integer id) {
        return recipeRepo.findById(id);
    }

    public void saveRecipe(RecipeInfo recipe) {
        recipeRepo.save(recipe);
    }

    public void deleteRecipe(Integer id) {
        recipeRepo.deleteById(id);
    }

    public List<RecipeInfo> findByCategory(String category) {
        return recipeRepo.findAllByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<RecipeInfo> findByName(String name) {
        return recipeRepo.findAllByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    public void updateRecipe(int id, RecipeInfo newRecipe) {
        Optional<RecipeInfo> recipe = findRecipeById(id);
        recipe.get().setName(newRecipe.getName());
        recipe.get().setCategory(newRecipe.getCategory());
        recipe.get().setDate(LocalDateTime.now());
        recipe.get().setDescription(newRecipe.getDescription());
        recipe.get().setIngredients(newRecipe.getIngredients());
        recipe.get().setDirections(newRecipe.getDirections());
        saveRecipe(recipe.get());
    }

}
