package tri.le.purchasedata.repository;

import org.springframework.data.repository.CrudRepository;
import tri.le.purchasedata.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByNameAndPassword(String name, String password);

}
