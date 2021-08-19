package antifraud.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @EqualsAndHashCode.Exclude
    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @EqualsAndHashCode.Exclude
    @NotNull
    private Role role;

    @EqualsAndHashCode.Exclude
    @NotBlank
    private String password;
}
