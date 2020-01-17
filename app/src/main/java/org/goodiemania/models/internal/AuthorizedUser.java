package org.goodiemania.models.internal;

public class AuthorizedUser {
    private String authorizationKey;
    private String user;

    public AuthorizedUser() {
    }

    public String getAuthorizationKey() {
        return authorizationKey;
    }

    public void setAuthorizationKey(final String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }
}
