package org.goodiemania.javalin;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.BookLookupService;
import org.goodiemania.models.BookResponse;
import org.goodiemania.models.ErrorResponse;
import org.goodiemania.models.GitProperties;
import org.goodiemania.models.HealthCheckResponse;
import org.goodiemania.models.books.BookInformation;

public class JavalinWrapper {
    private final BookLookupService bookLookupService;
    private ObjectMapper objectMapper;
    private AccessManager accessManager;

    /**
     * Creates a new instance of the javalin wrapper.
     *
     * @param bookLookupService Used to look up books
     * @param objectMapper      Used to map objects to and from JSON
     * @param accessManager     Used to look up authorized users from the database
     */
    public JavalinWrapper(
            final BookLookupService bookLookupService,
            final ObjectMapper objectMapper,
            final AccessManager accessManager) {
        this.bookLookupService = bookLookupService;
        this.objectMapper = objectMapper;
        this.accessManager = accessManager;
    }

    /**
     * Start the javalin application.
     */
    public void start() {
        Javalin app = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.enableCorsForAllOrigins();

            config.accessManager(accessManager);
        });

        JavalinJackson.configure(objectMapper);

        app.routes(() -> {
            path("v1", () -> {
                path("book", () -> {
                    get(this::handleBookGet);
                });
            });
            get("health", ctx -> {
                ctx.contentType("application/health+json");
                GitProperties gitProperties = GitProperties.fromPropsFile();
                HealthCheckResponse resp = new HealthCheckResponse();
                resp.setStatus("OK");
                resp.setVersion(gitProperties.getBuildVersion());

                ctx.json(resp);
            });
        });

        app.start();
    }

    private void handleBookGet(final Context ctx) {
        String searchType = ctx.queryParam("searchType");
        String searchTerm = ctx.queryParam("searchTerm");

        if (StringUtils.isAnyBlank(searchType, searchTerm)) {
            ctx.status(501);
            ctx.json(ErrorResponse.of("Endpoint requires both a searchType query param and a searchTerm"));
            return;
        }

        List<BookInformation> bookResponse = bookLookupService.byIsbn(searchTerm);
        ctx.json(BookResponse.of(bookResponse));
    }
}
