package recipes.dao;

import org.springframework.data.repository.CrudRepository;
import recipes.entities.User;

public interface UserRepo extends CrudRepository<User, String> {

}
