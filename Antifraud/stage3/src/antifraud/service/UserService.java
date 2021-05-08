package antifraud.service;

import antifraud.exception.DataNotFoundException;
import antifraud.exception.DuplicateDataException;
import antifraud.exception.IncompletDataException;
import antifraud.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private Map<String, User> repository = new HashMap<>();

    public void addUser(User user) {
        log.debug("Adding user {} ", user.toString());
        if (isNullOrEmpty(user.getUsername()) ||
                isNullOrEmpty(user.getName()) ||
                        user.getRole() == null) {
            log.error("Could not add user {} because of incomplete date", user.toString());
            throw new IncompletDataException("Incomplete data");
        }
        if (repository.containsKey(user.getUsername())) {
            String message = String.format("Could not add the user. The username %s is already exist in the repository.", user.getUsername());
            log.error("The user with the same username already exist {} ", user.toString());
            throw new DuplicateDataException(message);
        }
        log.debug("User {} has been successfully added", user.toString());
        repository.put(user.getUsername(), user);
    }

    public void deleteUSer(String username) {
        log.debug("Deleting user {} ", username);
        Optional.ofNullable(repository.remove(username))
                .orElseThrow(() -> new DataNotFoundException("Could not find user with username " + username));
        log.debug("User {} has been deleted successfully", username);
    }

    public Collection<User> getAllUsers() {
        return repository.values();
    }

    public boolean isNullOrEmpty(String text) {
        return (text == null) ||
                (text.isBlank());
    }
}
