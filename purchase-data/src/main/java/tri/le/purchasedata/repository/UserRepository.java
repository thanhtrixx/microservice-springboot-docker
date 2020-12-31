package tri.le.purchasedata.repository;

import org.springframework.data.repository.CrudRepository;
import tri.le.purchasedata.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
