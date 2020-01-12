package org.goodiemania;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.swagger.v3.oas.models.info.Info;
import org.goodiemania.api.models.requests.SearchRequest;
import org.goodiemania.books.services.books.GoodReadsService;
import org.goodiemania.books.services.books.GoogleBooksService;
import org.goodiemania.books.services.books.LibraryThingService;
import org.goodiemania.books.services.books.OpenLibraryService;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.impl.CachedHttpRequestServiceImpl;
import org.goodiemania.books.services.misc.BookLookupService;
import org.goodiemania.books.services.misc.StringEscapeUtils;
import org.goodiemania.books.services.xml.XmlProcessingService;


/**
 * This doc comment simply serves as a to do for me.
 *
 * <p>
 * TODO
 * Published date?
 * Description
 * Book format? Eg hard cover, paper back etcq
 * </p>
 */
public class Main {

    /**
     * Main method for invoking the book service (Used for testing).
     *
     * @param args Arguments passed to the program....
     */
    public static void main(String[] args) {
        HttpRequestService httpClient = new CachedHttpRequestServiceImpl();
        StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
        XmlProcessingService xmlProcessingService = new XmlProcessingService();

        GoodReadsService goodReadsService = new GoodReadsService(
                httpClient,
                xmlProcessingService,
                stringEscapeUtils,
                Properties.API_KEY_GOOD_READS.get().orElseThrow());
        OpenLibraryService openLibraryService = new OpenLibraryService(httpClient);
        LibraryThingService libraryThingService = new LibraryThingService(
                httpClient,
                xmlProcessingService,
                stringEscapeUtils,
                Properties.API_KEY_LIBRARY_THING.get().orElseThrow());
        GoogleBooksService googleBooksService = new GoogleBooksService(
                httpClient,
                Properties.API_KEY_GOOGLE_BOOKS.get().orElseThrow());

        BookLookupService bookLookup = new BookLookupService(
                openLibraryService,
                goodReadsService,
                libraryThingService,
                googleBooksService);

        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
            config.defaultContentType = "application/json";
            config.enableCorsForAllOrigins();

            config.accessManager((handler, ctx, permittedRoles) -> {
                System.out.println(ctx.path() + "; Authorization header:" + ctx.header("authorization"));
                handler.handle(ctx);
            });
        });


        app.routes(() -> {
            path("v1", () -> {
                path("book", () -> {
                    post(ctx -> {
                        SearchRequest searchRequest = ctx.bodyAsClass(SearchRequest.class);
                        bookLookup.byIsbn(searchRequest.getSearchTerm())
                                .ifPresentOrElse(
                                        ctx::json,
                                        () -> {
                                            ctx.status(404);
                                            ctx.json("Unable to find anything");
                                        });
                    });
                });
            });
        });

        app.start();
    }

    private static OpenApiOptions getOpenApiOptions() {
        Info applicationInfo = new Info()
                .version("1.0")
                .description("Jerome");
        return new OpenApiOptions(applicationInfo)
                .activateAnnotationScanningFor("org.goodiemania.seshat")
                .path("/swagger-docs");
    }
}
