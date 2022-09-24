package org.goodiemania.books.services.misc;

import com.mysql.cj.util.StringUtils;
import kong.unirest.GetRequest;
import kong.unirest.Unirest;
import org.goodiemania.books.BookLookupService;
import org.goodiemania.books.services.external.GoodReadsService;
import org.goodiemania.books.services.external.GoogleBooksService;
import org.goodiemania.books.services.external.LibraryThingService;
import org.goodiemania.books.services.external.OpenLibraryService;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.xml.XmlProcessingService;
import org.goodiemania.models.books.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "XXXXXXX");
        OpenLibraryService openLibraryService = new OpenLibraryService(httpClient);
        LibraryThingService libraryThingService = new LibraryThingService(
                httpClient,
                xmlProcessingService,
                stringEscapeUtils,
                "XXXXXXX");
        GoogleBooksService googleBooksService = new GoogleBooksService(
                httpClient,
                "XXXXXXX");

        bookLookupService = new BookLookupService(
                openLibraryService,
                goodReadsService,
                libraryThingService,
                googleBooksService);
    }

    @Test
    public void test() {
        var test = List.of("9783961711321",
                "9781426214516",
                "9783958291034",
                "9780947503925",
                "9781934623497",
                "9781841146492",
                "9780473329693",
                "9780500021392",
                "0907812163",
                "0473052601",
                "9780500544310",
                "9781869660581",
                "9780908802234",
                "9781869664657",
                "9781841199573",
                "9780596100834",
                "9780226098753",
                "9783822805596",
                "9781857324075",
                "9781877333163",
                "9781419727535",
                "9780473277062",
                "9783822884973",
                "9780316456128",
                "9781472269058",
                "9780908802234",
                "9783822871720",
                "9780785192558",
                "9781846079863",
                "9781401324902",
                "9781853260582",
                "9780470681831",
                "9781741140408",
                "9781741143072",
                "9781741147018",
                "9781741758627",
                "9781401245252",
                "9781616551681",
                "9781616550417",
                "9780821221846",
                "0821215264",
                "0821211315",
                "9780730372035",
                "9781877333958",
                "9780141037226",
                "9780995103276",
                "9781620405550",
                "9781620408001",
                "0821211226",
                "0821219804",
                "9780817439392",
                "9781908150585",
                "9781426204319",
                "9780715302828",
                "9780240809342",
                "9781780673356",
                "9781864488159",
                "9780316117722",
                "9780141349978");

        Map<DataSource, Integer> count = new HashMap<>();
        count.put(DataSource.GOOD_READS, 0);
        count.put(DataSource.GOOGLE_BOOKS, 0);
        count.put(DataSource.LIBRARY_THING, 0);
        count.put(DataSource.OPEN_LIBRARY, 0);

        test.stream()
                .map(s -> bookLookupService.byIsbn(s))
                .flatMap(Collection::stream)
                .forEach(bookInformation -> {
                    count.computeIfPresent(bookInformation.getSource(), (dataSource, integer) -> ++integer);
                });


        count.forEach((key, value) -> System.out.println(StringUtils.padString(key.toString(), 20) + " :: " + value));
    }
}