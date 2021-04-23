package antifraud.controller;

import antifraud.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antifraud/user")
public class UserController {

    @GetMapping
    public List<User> getAll(){
        return null;
    }

    @PostMapping
    public User addUser(User user){
        return null;
    }

    @DeleteMapping
    public User deleteUser(String username){
        return null;
    }

}
