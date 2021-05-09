package antifraud.controller;

import antifraud.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/antifraud/user")
@RequiredArgsConstructor
public class UserController {

//    private final UserService userService;

    @GetMapping
    public ResponseEntity<Collection<User>> getAll() {
//        return ResponseEntity.ok(userService.getAllUsers());
        return null;
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody User user) {
//        userService.addUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
        return null;
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable String username) {
//        userService.deleteUSer(username);
//        return ResponseEntity.ok().build();
        return null;
    }

    @PatchMapping("/username")
    public ResponseEntity<User> cheangePassword(@RequestBody String password) {
        return null;
    }

}
