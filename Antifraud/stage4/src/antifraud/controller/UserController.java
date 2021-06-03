package antifraud.controller;

import antifraud.model.User;
import antifraud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/antifraud/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Collection<User>> getAll(/*@AuthenticationPrincipal User user*/) {
        return ResponseEntity.ok(userService.getAllUsers());

    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> addUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{username}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> deleteUser(@PathVariable String username) {
        userService.deleteUSer(username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
    public ResponseEntity<User> changePassword(@PathVariable String username, @RequestBody String password) {
        User changedPasswordUser = userService.changePassword(username, password);
        return ResponseEntity.ok(changedPasswordUser);
    }

}
