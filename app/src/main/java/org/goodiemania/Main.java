package org.goodiemania;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.http.HttpClient;
import org.goodiemania.books.BookLookupService;
import org.goodiemania.books.services.external.GoodReadsService;
import org.goodiemania.books.services.external.GoogleBooksService;
import org.goodiemania.books.services.external.LibraryThingService;
import org.goodiemania.books.services.external.OpenLibraryService;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.impl.CachedHttpRequestServiceImpl;
import org.goodiemania.books.services.misc.StringEscapeUtils;
import org.goodiemania.books.services.misc.TimerService;
import org.goodiemania.books.services.xml.XmlProcessingService;
import org.goodiemania.dao.AuthorizedUserDao;
import org.goodiemania.dao.StoredHttpRequestDao;
import org.goodiemania.javalin.JavalinWrapper;
import org.goodiemania.models.Properties;


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
        HttpClient javaHttpClient = HttpClient.newBuilder()
                .build();

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        AuthorizedUserDao authorizedUserDao = new AuthorizedUserDao("jdbc:sqlite:mainDatabase");
        authorizedUserDao.createTables();
        StoredHttpRequestDao storedHttpRequestDao = new StoredHttpRequestDao("jdbc:sqlite:mainDatabase");
        storedHttpRequestDao.createTables();

        TimerService timerService = new TimerService();

        HttpRequestService httpClient = new CachedHttpRequestServiceImpl(javaHttpClient, storedHttpRequestDao, timerService);
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

        new JavalinWrapper(bookLookup, objectMapper, authorizedUserDao).start();
    }
}
