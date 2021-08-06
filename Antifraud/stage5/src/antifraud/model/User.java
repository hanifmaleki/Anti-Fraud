package antifraud.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @EqualsAndHashCode.Exclude
    private String name;

    private String username;

    @EqualsAndHashCode.Exclude
    private Role role;

    @EqualsAndHashCode.Exclude
    private String password;
}
