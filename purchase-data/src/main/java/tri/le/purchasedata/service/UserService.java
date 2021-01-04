package tri.le.purchasedata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tri.le.purchasedata.dto.GenericResponse;
import tri.le.purchasedata.entity.User;
import tri.le.purchasedata.repository.UserRepository;

import java.util.Optional;

import static tri.le.purchasedata.error.ErrorCode.NO_ERROR;
import static tri.le.purchasedata.error.ErrorCode.USER_NAME_OR_PASSWORD_INCORRECT;

@Service
public class UserService {


  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  public GenericResponse<String> login(String name, String password) {
    Optional<User> userOpt = userRepository.findByNameAndPassword(name, password);

    if (!userOpt.isPresent()) {
      return new GenericResponse(USER_NAME_OR_PASSWORD_INCORRECT, "UserName or Password incorrect", null);
    }

    User user = userOpt.get();
    String token = jwtService.createToken(user.getName(), user.getId(), System.currentTimeMillis());
    return new GenericResponse(NO_ERROR, "Login success", token);
  }

}
