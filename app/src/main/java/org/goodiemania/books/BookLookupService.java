package org.goodiemania.books;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.GoogleBooksLayer;
import org.goodiemania.books.layers.LibraryThingLayer;
import org.goodiemania.books.layers.OpenLibraryLayer;
import org.goodiemania.books.services.external.GoodReadsService;
import org.goodiemania.books.services.external.GoogleBooksService;
import org.goodiemania.books.services.external.LibraryThingService;
import org.goodiemania.books.services.external.OpenLibraryService;
import org.goodiemania.models.books.BookInformation;
import org.goodiemania.models.books.DataSource;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

public class BookLookupService {
    private final GoodReadsService goodReadsService;
    private final OpenLibraryService openLibraryService;
    private final LibraryThingService libraryThingService;
    private final GoogleBooksService googleBooksService;

    private final List<GoodReadsLayer> goodReadsLayers;
    private final List<LibraryThingLayer> libraryThingLayers;
    private final List<GoogleBooksLayer> googleBookLayers;
    private final List<OpenLibraryLayer> openLibraryLayers;


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
        libraryThingLayers = getClassInstances(reflections, LibraryThingLayer.class);
        googleBookLayers = getClassInstances(reflections, GoogleBooksLayer.class);
        openLibraryLayers = getClassInstances(reflections, OpenLibraryLayer.class);
    }

    @NotNull
    private <T> List<T> getClassInstances(final Reflections reflections, Class<T> classInfo) {
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
    public List<BookInformation> byIsbn(final String isbn) {
        List<BookInformation> bookInfoList = new ArrayList<>();

        goodReadsService.getBookInfoByIsbn(isbn).map(document -> doTheThing(
                DataSource.GOOD_READS, document, goodReadsLayers,
                (givenDocument, layer, bookInfo) -> layer.applyGoodReads(bookInfo, givenDocument)))
                .ifPresent(bookInfoList::add);
        libraryThingService.getBookInfoByIsbn(isbn).map(document -> doTheThing(
                DataSource.LIBRARY_THING, document, libraryThingLayers,
                (givenDocument, layer, bookInfo) -> layer.applyLibraryThing(bookInfo, givenDocument)))
                .ifPresent(bookInfoList::add);
        googleBooksService.getBookInfoByIsbn(isbn).map(document -> doTheThing(
                DataSource.GOOGLE_BOOKS, document, googleBookLayers,
                (givenDocument, layer, bookInfo) -> layer.applyGoogleBooks(bookInfo, givenDocument)))
                .ifPresent(bookInfoList::add);
        openLibraryService.getBookInfoByIsbn(isbn).map(document -> doTheThing(
                DataSource.OPEN_LIBRARY, document, openLibraryLayers,
                (givenDocument, layer, bookInfo) -> layer.applyOpenLibrary(bookInfo, givenDocument)))
                .ifPresent(bookInfoList::add);

        return bookInfoList;
    }

    private <T, U> BookInformation doTheThing(
            final DataSource source,
            final T document,
            final List<U> layers,
            final TriConsumer<T, U, BookInformation> consumer) {
        BookInformation bookInformation = BookInformation.fromSource(source);
        layers.forEach(layer -> consumer.accept(document, layer, bookInformation));

        return bookInformation;
    }
}
