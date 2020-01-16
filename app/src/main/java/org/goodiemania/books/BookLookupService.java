package org.goodiemania.books;

import com.fasterxml.jackson.databind.JsonNode;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.goodiemania.books.context.Context;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.NewBookInformation;
import org.goodiemania.books.services.external.GoodReadsService;
import org.goodiemania.books.services.external.GoogleBooksService;
import org.goodiemania.books.services.external.LibraryThingService;
import org.goodiemania.books.services.external.OpenLibraryService;
import org.goodiemania.books.services.xml.XmlDocument;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

public class BookLookupService {
    private final GoodReadsService goodReadsService;
    private final OpenLibraryService openLibraryService;
    private final LibraryThingService libraryThingService;
    private final GoogleBooksService googleBooksService;

    private final List<GoodReadsLayer> goodReadsLayers;


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

        Reflections reflections = new Reflections("org.goodiemania.books.layers");
        goodReadsLayers = getClassInstances(reflections, GoodReadsLayer.class);
    }

    @NotNull
    private <T> List<T> getClassInstances(final Reflections reflections, Class<T> classInfo) {
        //Class<Layer> classInfo = Layer.class;
        return reflections
                .getSubTypesOf(classInfo)
                .stream()
                .map(classes -> classes.getConstructors()[0])
                .flatMap(constructor -> {
                    try {

                        return Stream.of(classInfo.cast(constructor.newInstance()));
                    } catch (ClassCastException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
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
    public List<NewBookInformation> byIsbn(final String isbn) {
        List<NewBookInformation> newBookInformation = new ArrayList<>();
        goodReadsService.getBookInfoByIsbn(isbn)
                .map(xmlDocument -> {
                    NewBookInformation bookInformation = new NewBookInformation();
                    goodReadsLayers.forEach(goodReadsLayer -> {
                        goodReadsLayer.applyGoodReads(bookInformation, xmlDocument);
                    });
                    return bookInformation;
                })
                .ifPresent(newBookInformation::add);
        return newBookInformation;
    }

    private Optional<Context> createContext(final String isbn) {

        XmlDocument libraryThingResponse = libraryThingService.getBookInfoByIsbn(isbn);
        XmlDocument libraryThingAuthorResponse = Optional.ofNullable(libraryThingResponse)
                .map(document -> document.getValueAsString("/response/ltml/item/author/@id"))
                .map(libraryThingService::getAuthorById)
                .orElse(null);
        JsonNode googleBooksResponse = googleBooksService.getBookInfoByIsbn(isbn);
        JsonNode openLibraryResponse = openLibraryService.getBookInfoByIsbn(isbn);

        if (openLibraryResponse == null
                && libraryThingResponse == null
                && googleBooksResponse == null) {
            return Optional.empty();
        }

        return Optional.of(Context.build()
                .setIsbn(isbn)
                .setOpenLibraryResponse(openLibraryResponse)
                .setLibraryThingResponse(libraryThingResponse)
                .setLibraryThingAuthor(libraryThingAuthorResponse)
                .setGoogleBooksResponse(googleBooksResponse)
                .build());
    }
}
