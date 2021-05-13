package antifraud.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("support")
    SUPPORT,
    @JsonProperty("user")
    USER

}
