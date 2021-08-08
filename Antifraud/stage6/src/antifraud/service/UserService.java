package antifraud.service;

import antifraud.exception.DataNotFoundException;
import antifraud.exception.DuplicateDataException;
import antifraud.exception.IncompletDataException;
import antifraud.exception.PasswordNotStrong;
import antifraud.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private Map<String, User> repository = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

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
        final String encodedPassword = checkValidityAndEncodePassword(user.getPassword());
        user.setPassword(encodedPassword);
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

    public User getUser(String username) {
        return repository.get(username);
    }

    public User changePassword(String username, String password) {
        log.debug("Changing password for user {}", username);
        final User user = Optional.ofNullable(repository.get(username))
                .orElseThrow(() -> new DataNotFoundException("Could not find user with username " + username));
        final String encodePassword = checkValidityAndEncodePassword(password);
        user.setPassword(encodePassword);
        log.debug("Password of the user {} has been changed successfully", username);
        return user;
    }

    private String checkValidityAndEncodePassword(final String password) {
        checkPasswordToBeStrongEnough(password);
        return encodePassword(password);
    }


    private String encodePassword(final String password) {
        return passwordEncoder.encode(password);
    }

    private void checkPasswordToBeStrongEnough(String password) {
        log.debug("Checking password strength");
        if(StringUtils.isEmpty(password)){
            final int length = password.length();
            log.error("Password with length {} us not strong enough.", length);
            throw new PasswordNotStrong(length);
        }
    }
}
