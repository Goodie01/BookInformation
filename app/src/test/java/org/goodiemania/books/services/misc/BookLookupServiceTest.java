package org.goodiemania.books.services.misc;

import org.goodiemania.books.services.misc.books.GoodReadsService;
import org.goodiemania.books.services.misc.books.GoogleBooksService;
import org.goodiemania.books.services.misc.books.LibraryThingService;
import org.goodiemania.books.services.misc.books.OpenLibraryService;
import org.goodiemania.books.services.misc.misc.HttpRequestService;
import org.goodiemania.books.services.misc.misc.StringEscapeUtils;
import org.goodiemania.books.services.misc.xml.XmlProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookLookupServiceTest {
    private BookLookupService bookLookupService;

    @BeforeEach
    void setUp() {
        HttpRequestService httpClient = new HttpRequestServiceMock();
        StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
        XmlProcessingService xmlProcessingService = new XmlProcessingService();

        GoodReadsService goodReadsService = new GoodReadsService(
                httpClient,
                xmlProcessingService,
                stringEscapeUtils,
                "XXXXXXXX");
        OpenLibraryService openLibraryService = new OpenLibraryService(httpClient);
        LibraryThingService libraryThingService = new LibraryThingService(
                httpClient,
                xmlProcessingService,
                stringEscapeUtils,
                "XXXXXXXXX");
        GoogleBooksService googleBooksService = new GoogleBooksService(
                httpClient,
                "XXXXXXXXX");

        bookLookupService = new BookLookupService(
                openLibraryService,
                goodReadsService,
                libraryThingService,
                googleBooksService);
    }

    @Test
    public void test() {
        bookLookupService.byIsbn("9781934623497")
                .orElseThrow();
    }
}