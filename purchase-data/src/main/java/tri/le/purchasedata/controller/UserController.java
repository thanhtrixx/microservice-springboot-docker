package tri.le.purchasedata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tri.le.purchasedata.dto.GenericResponse;
import tri.le.purchasedata.service.UserService;

@RestController
@RequestMapping(path = "/user")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping(path = "/login")
  public GenericResponse<String> login(@RequestParam String name, @RequestParam String password) {
    return userService.login(name, password);
  }

}
