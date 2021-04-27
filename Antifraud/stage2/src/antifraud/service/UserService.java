package antifraud.service;

import antifraud.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private Map<String, User> repository = new HashMap<>();

    public void addUser(User user) {
        if (user.getUsername() == null ||
                user.getName() == null ||
                user.getRole() == null) {
            throw new RuntimeException("Incomplete data");
        }
        if (repository.containsKey(user.getUsername())) {
            String message = String.format("Could not add the user. The username %s is already exist in the repository.");
            throw new RuntimeException(message);
        }

        repository.put(user.getUsername(), user);
    }

    public void deleteUSer(String username){
        Optional.of(repository.remove(username))
                .orElseThrow(() -> new RuntimeException("Could not find user with username "+ username));
    }

    public Collection<User> getAllUsers(){
        return repository.values();
    }
}
