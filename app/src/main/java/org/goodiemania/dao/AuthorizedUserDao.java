package org.goodiemania.dao;

import java.util.Optional;
import javax.sql.DataSource;
import org.goodiemania.models.internal.AuthorizedUser;
import org.jdbi.v3.core.Jdbi;

public class AuthorizedUserDao {
    private final Jdbi jdbi;

    public AuthorizedUserDao(final String jdbcConnectUrl) {
        jdbi = Jdbi.create(jdbcConnectUrl);
    }

    public AuthorizedUserDao(final DataSource mysqlDataSource) {
        jdbi = Jdbi.create(mysqlDataSource);
    }

    public void createTables() {
        jdbi.withHandle(handle ->
        {
            int execute = handle.execute("CREATE TABLE IF NOT EXISTS authorized_user (authorizationKey varchar(255) UNIQUE, user varchar(255))");
            if (execute != 0) {
                handle.execute("CREATE UNIQUE INDEX `idx_authorized_user_authorizationKey`  ON `authorized_user` (authorizationKey) COMMENT '' ALGORITHM DEFAULT LOCK DEFAULT");
            }
            return execute;
        });
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
