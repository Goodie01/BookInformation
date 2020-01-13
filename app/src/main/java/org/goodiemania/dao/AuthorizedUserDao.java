package org.goodiemania.dao;

import java.util.Optional;
import org.goodiemania.models.api.AuthorizedUser;
import org.jdbi.v3.core.Jdbi;

public class AuthorizedUserDao {
    private final Jdbi jdbi;

    public AuthorizedUserDao(final String jdbcConnectUrl) {
        jdbi = Jdbi.create(jdbcConnectUrl);
    }

    public void createTables() {
        jdbi.useHandle(handle ->
                handle.execute("CREATE TABLE IF NOT EXISTS authorized_user (authorizationKey varchar(255) UNIQUE, user varchar(255))"));
    }

    /**
     * Attempts to find a authorized user for a given key.
     *
     * @param key The supposed key for the possible user.
     * @return The found authorized user.
     */
    public Optional<AuthorizedUser> getByKey(final String key) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from authorized_user where authorizationKey = :key")
                .bind("key", key)
                .mapToBean(AuthorizedUser.class)
                .stream()
                .findFirst());
    }
}
