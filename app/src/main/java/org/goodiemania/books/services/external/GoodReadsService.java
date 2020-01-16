package org.goodiemania.books.services.external;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.HttpServiceResponse;
import org.goodiemania.books.services.misc.StringEscapeUtils;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.books.services.xml.XmlProcessingService;

public class GoodReadsService {
    private final XmlProcessingService xmlProcessingService;
    private final StringEscapeUtils stringEscapeUtils;
    private final String goodReadsKey;
    private final HttpRequestService httpClient;

    /**
     * Creates a new instance of the Good Reads service.
     *
     * @param httpClient           Service to make HTTP requests
     * @param xmlProcessingService Service to process XML
     * @param stringEscapeUtils    Pre processing of strings, escaping HTML entities
     * @param goodReadsKey         Developer key to talk to GR service
     */
    public GoodReadsService(final HttpRequestService httpClient,
                            final XmlProcessingService xmlProcessingService,
                            final StringEscapeUtils stringEscapeUtils,
                            final String goodReadsKey) {
        this.xmlProcessingService = xmlProcessingService;
        this.stringEscapeUtils = stringEscapeUtils;
        this.goodReadsKey = goodReadsKey;
        this.httpClient = httpClient;
    }

    /**
     * Sends a request to Good Reads API, returns the result.
     *
     * @param isbn isbn to send to Good Reads
     * @return A XMLDocument representing the return value
     */
    public Optional<XmlDocument> getBookInfoByIsbn(final String isbn) {
        String uriString = String.format("https://www.goodreads.com/book/isbn/%s?format=xml&key=%s",
                isbn, goodReadsKey);
        HttpServiceResponse httpServiceResponse = httpClient.get(uriString, true);
        String response = stringEscapeUtils.escapeHtmlEntitiesInXml(httpServiceResponse.getResponse());

        if (httpServiceResponse.getStatus() != 200) {
            return Optional.empty();
        }

        XmlDocument resultDocument = xmlProcessingService.parse(response);

        if (StringUtils.isNotBlank(resultDocument.getValueAsString("/error"))) {
            return Optional.empty();
        }

        return Optional.of(resultDocument);
    }
}
