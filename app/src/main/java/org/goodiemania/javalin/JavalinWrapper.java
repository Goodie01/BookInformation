package org.goodiemania.javalin;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.core.security.AccessManager;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.BookLookupService;
import org.goodiemania.dao.AuthorizedUserDao;

public class JavalinWrapper {
    private final BookLookupService bookLookupService;
    private ObjectMapper objectMapper;
    private AuthorizedUserDao authorizedUserDao;

    /**
     * Creates a new instance of the javalin wrapper.
     *
     * @param bookLookupService Used to look up books
     * @param objectMapper      Used to map objects to and from JSON
     * @param authorizedUserDao Used to look up authorized users from the database
     */
    public JavalinWrapper(
            final BookLookupService bookLookupService,
            final ObjectMapper objectMapper,
            final AuthorizedUserDao authorizedUserDao) {
        this.bookLookupService = bookLookupService;
        this.objectMapper = objectMapper;
        this.authorizedUserDao = authorizedUserDao;
    }

    /**
     * Start the javalin application.
     */
    public void start() {
        Javalin app = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.enableCorsForAllOrigins();

            config.accessManager(checkAuthorization());
        });

        JavalinJackson.configure(objectMapper);

        app.routes(() -> {
            path("v1", () -> {
                path("book", () -> {
                    get(this::handleBookGet);
                });
            });
        });

        app.start();
    }

    private void handleBookGet(final Context ctx) {
        String searchType = ctx.queryParam("searchType");
        String searchTerm = ctx.queryParam("searchTerm");

        if (StringUtils.isAnyBlank(searchType, searchTerm)) {
            ctx.status(501);
            ctx.json("Endpoint requires both a searchType query param and a searchTerm");
            return;
        }

        bookLookupService.byIsbn(searchTerm)
                .ifPresentOrElse(
                        ctx::json,
                        () -> {
                            ctx.status(404);
                            ctx.json("Unable to find said book");
                        });
    }

    private AccessManager checkAuthorization() {
        return (handler, ctx, permittedRoles) -> {
            String authorizationCode = ctx.header("authorization");
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
                                ctx.json("Invalid user");
                                ctx.status(403);
                            });
        };
    }
}
