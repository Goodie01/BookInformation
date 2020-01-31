package org.goodiemania;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.net.http.HttpClient;
import java.sql.SQLException;
import javax.sql.DataSource;
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
import org.goodiemania.javalin.AccessManager;
import org.goodiemania.javalin.JavalinWrapper;


/**
 * This doc comment simply serves as a to do for me.
 *
 * <p>
 * TODO
 * Published date?
 * Book format? Eg hard cover, paper back etc Size?
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

        DataSource mysqlDataSource = getMysqlDataSource();
        AuthorizedUserDao authorizedUserDao = new AuthorizedUserDao(mysqlDataSource);
        authorizedUserDao.createTables();
        StoredHttpRequestDao storedHttpRequestDao = new StoredHttpRequestDao(mysqlDataSource);
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

        AccessManager accessManager = new AccessManager(authorizedUserDao);
        new JavalinWrapper(bookLookup, objectMapper, accessManager).start();
    }


    private static DataSource getMysqlDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(Properties.DB_CONNECTION_STRING.get().orElseThrow());
        try {
            dataSource.setAllowPublicKeyRetrieval(true);
            dataSource.setAutoReconnect(true);
            dataSource.setUseSSL(false);
            dataSource.setServerTimezone("UTC");
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return dataSource;
    }
}
