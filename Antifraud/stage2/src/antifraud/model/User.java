package antifraud.model;

import lombok.Data;

@Data
public class User {
    private long id;
    private String name;
    private String username;
    private String password;
    private Role role;
}
