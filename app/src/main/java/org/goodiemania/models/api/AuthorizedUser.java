package org.goodiemania.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;

@Entity
public class AuthorizedUser {
    @Id
    private final String authorizationKey;
    private final String user;

    @JsonCreator
    public AuthorizedUser(
            @JsonProperty("authorizationKey") final String authorizationKey,
            @JsonProperty("user") final String user) {
        this.authorizationKey = authorizationKey;
        this.user = user;
    }

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public String getUser() {
        return user;
    }
}
