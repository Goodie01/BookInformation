package org.goodiemania.books.services.external;

import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.HttpServiceResponse;
import org.goodiemania.books.services.misc.StringEscapeUtils;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.books.services.xml.XmlProcessingService;

public class LibraryThingService {
    private final HttpRequestService httpClient;
    private final String developerKey;
    private XmlProcessingService xmlProcessingService;
    private StringEscapeUtils stringEscapeUtils;

    /**
     * Creates a new instance of the Library Thing service.
     *
     * @param httpClient           Service to make HTTP requests
     * @param xmlProcessingService Service to process XML
     * @param stringEscapeUtils    Pre processing of strings, escaping HTML entities
     * @param developerKey         Developer key to talk to Library Thing service
     */
    public LibraryThingService(final HttpRequestService httpClient,
                               final XmlProcessingService xmlProcessingService,
                               final StringEscapeUtils stringEscapeUtils,
                               final String developerKey) {
        this.httpClient = httpClient;
        this.xmlProcessingService = xmlProcessingService;
        this.stringEscapeUtils = stringEscapeUtils;
        this.developerKey = developerKey;
    }

    /**
     * Sends a request to Library Thing API, returns the result.
     *
     * @param isbn isbn to send to Library Thing api
     * @return A XMLDocument representing the return value
     */
    public XmlDocument getBookInfoByIsbn(final String isbn) {
        String uriString = String.format("https://www.librarything.com"
                        + "/services/rest/1.1/?method=librarything.ck.getwork&isbn=%s&apikey=%s",
                isbn, developerKey);
        HttpServiceResponse httpServiceResponse = httpClient.get(uriString, true);

        if (httpServiceResponse.getStatus() != 200) {
            return null;
        }

        String response = stringEscapeUtils.escapeHtmlEntitiesInXml(httpServiceResponse.getResponse());

        if (StringUtils.isBlank(response)) {
            return null;
        }

        XmlDocument parse = xmlProcessingService.parse(response);

        if (StringUtils.isNotBlank(parse.getValueAsString("/response/err"))) {
            return null;
        }

        return parse;
    }

    /**
     * Sends a request to the Library Thing author API, returns the result.
     *
     * @param id ID of the author to send to the Library Thing API
     * @return A XML Document representing the return value
     */
    public XmlDocument getAuthorById(final String id) {
        String uriString = String.format("https://www.librarything.com"
                        + "/services/rest/1.1/?method=librarything.ck.getauthor&id=%s&apikey=%s",
                id, developerKey);
        HttpServiceResponse httpServiceResponse = httpClient.get(uriString, true);
        String response = stringEscapeUtils.escapeHtmlEntitiesInXml(httpServiceResponse.getResponse());

        return xmlProcessingService.parse(response);
    }
}
