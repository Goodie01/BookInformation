package org.goodiemania.javalin;

import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.dao.AuthorizedUserDao;
import org.goodiemania.models.ErrorResponse;
import org.jetbrains.annotations.NotNull;

public class AccessManager implements io.javalin.core.security.AccessManager {
    private AuthorizedUserDao authorizedUserDao;

    public AccessManager(final AuthorizedUserDao authorizedUserDao) {
        this.authorizedUserDao = authorizedUserDao;
    }

    @Override
    public void manage(
            @NotNull final Handler handler,
            @NotNull final Context ctx,
            @NotNull final Set<Role> permittedRoles) throws Exception {

        if (StringUtils.equals(ctx.path(), "/health")) {
            handler.handle(ctx);
            return;
        }

        String authorizationCode = findAuthHeaderValue(ctx);
        authorizedUserDao.getByKey(authorizationCode)
                .ifPresentOrElse(
                        authorizedUser -> {
                            try {
                                System.out.println(ctx.path() + "; Authorization header:" + authorizationCode);
                                handler.handle(ctx);
                            } catch (Exception e) {
                                throw new IllegalStateException(e);
                            }
                        },
                        () -> {
                            System.out.printf("Access denied for: Authorization header: %s; path: %s%n", authorizationCode, ctx.path());
                            ctx.json(ErrorResponse.of("Invalid user"));
                            ctx.status(403);
                        });

    }

    private String findAuthHeaderValue(final Context ctx) {
        String authorizationCode = ctx.header("authorization");

        if (StringUtils.isBlank(authorizationCode)) {
            return ctx.header("authorization");
        }

        return authorizationCode;
    }
}
