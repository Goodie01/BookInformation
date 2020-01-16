package org.goodiemania.books;

import com.fasterxml.jackson.databind.JsonNode;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.goodiemania.books.context.Context;
import org.goodiemania.books.layers.Layer;
import org.goodiemania.books.services.external.GoodReadsService;
import org.goodiemania.books.services.external.GoogleBooksService;
import org.goodiemania.books.services.external.LibraryThingService;
import org.goodiemania.books.services.external.OpenLibraryService;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.BookInformation;
import org.reflections.Reflections;

public class BookLookupService {
    private final GoodReadsService goodReadsService;
    private final OpenLibraryService openLibraryService;
    private final LibraryThingService libraryThingService;
    private final GoogleBooksService googleBooksService;

    private final List<? extends Layer> layers;


    /**
     * default constructor creates a new instance of the book lookup service.
     *
     * @param openLibraryService  Service for talking to the Open Library
     * @param goodReadsService    Service for talking to Good Reads
     * @param libraryThingService Service for talking to Library Thing
     * @param googleBooksService  Service for talking to Google Books
     */
    public BookLookupService(final OpenLibraryService openLibraryService,
                             final GoodReadsService goodReadsService,
                             final LibraryThingService libraryThingService,
                             final GoogleBooksService googleBooksService) {
        this.goodReadsService = goodReadsService;
        this.openLibraryService = openLibraryService;
        this.libraryThingService = libraryThingService;
        this.googleBooksService = googleBooksService;

        layers = new Reflections("org.goodiemania.books.layers")
                .getSubTypesOf(Layer.class)
                .stream()
                .map(layerClasses -> layerClasses.getConstructors()[0])
                .flatMap(constructor -> {
                    try {
                        return Stream.of((Layer) constructor.newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Looks the book information up by it's ISBN.
     *
     * @param isbn String isbn
     * @return Optional possibly containing the found information
     */
    public Optional<BookInformation> byIsbn(final String isbn) {
        return createContext(isbn).map(context -> {
            layers.forEach(layer -> layer.apply(context));
            return context.getBookInformation();
        });
    }

    private Optional<Context> createContext(final String isbn) {
        XmlDocument goodReadsResponse = goodReadsService.getBookInfoByIsbn(isbn);
        XmlDocument libraryThingResponse = libraryThingService.getBookInfoByIsbn(isbn);
        XmlDocument libraryThingAuthorResponse = Optional.ofNullable(libraryThingResponse)
                .map(document -> document.getValueAsString("/response/ltml/item/author/@id"))
                .map(libraryThingService::getAuthorById)
                .orElse(null);
        JsonNode googleBooksResponse = googleBooksService.getBookInfoByIsbn(isbn);
        JsonNode openLibraryResponse = openLibraryService.getBookInfoByIsbn(isbn);

        if (goodReadsResponse == null
                && openLibraryResponse == null
                && libraryThingResponse == null
                && googleBooksResponse == null) {
            return Optional.empty();
        }

        return Optional.of(Context.build()
                .setIsbn(isbn)
                .setOpenLibraryResponse(openLibraryResponse)
                .setGoodReadsResponse(goodReadsResponse)
                .setLibraryThingResponse(libraryThingResponse)
                .setLibraryThingAuthor(libraryThingAuthorResponse)
                .setGoogleBooksResponse(googleBooksResponse)
                .build());
    }
}
