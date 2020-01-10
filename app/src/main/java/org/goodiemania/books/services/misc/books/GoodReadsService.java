package org.goodiemania.books.services.misc.books;

import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.services.misc.misc.HttpRequestService;
import org.goodiemania.books.services.misc.misc.StringEscapeUtils;
import org.goodiemania.books.services.misc.xml.XmlDocument;
import org.goodiemania.books.services.misc.xml.XmlProcessingService;

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
    public XmlDocument getBookInfoByIsbn(final String isbn) {
        String uriString = String.format("https://www.goodreads.com/book/isbn/%s?format=xml&key=%s",
                isbn, goodReadsKey);
        String response = stringEscapeUtils.escapeHtmlEntitiesInXml(httpClient.get(uriString));

        if (StringUtils.containsAny(
                response,
                "An unexpected error occurred. We will investigate this problem as soon as possible",
                "Goodreads is over capacity.")) {
            return null;
        }

        XmlDocument resultDocument = xmlProcessingService.parse(response);

        if (StringUtils.isNotBlank(resultDocument.getValueAsString("/error"))) {
            return null;
        }

        return resultDocument;
    }
}
