package tri.le.purchasedata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tri.le.purchasedata.entity.User;
import tri.le.purchasedata.repository.UserRepository;

@Controller
@RequestMapping(path = "/demo")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @PostMapping(path = "/add")
  public @ResponseBody
  String addNewUser(@RequestParam String name, @RequestParam String email) {

    User n = new User();
    n.setName(name);
    userRepository.save(n);
    return "Saved";
  }

  @GetMapping(path = "/all")
  public @ResponseBody
  Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }
}
