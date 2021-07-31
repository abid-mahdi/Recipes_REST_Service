package recipes.dao;

import org.springframework.data.repository.CrudRepository;
import recipes.entities.RecipeInfo;

import java.util.List;

public interface RecipeRepo extends CrudRepository<RecipeInfo, Integer> {

    void deleteById(Integer id);

    List<RecipeInfo> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<RecipeInfo> findAllByNameContainingIgnoreCaseOrderByDateDesc(String name);

}
