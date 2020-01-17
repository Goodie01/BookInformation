package org.goodiemania.books.services.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.HttpServiceResponse;

public class GoogleBooksService {
    private final HttpRequestService httpClient;
    private final String developerKey;
    private final ObjectMapper mapper;

    /**
     * Creates a new instance of the Google Books service.
     *
     * @param httpClient   Service to make HTTP requests
     * @param developerKey Developer key to talk to GB service
     */
    public GoogleBooksService(final HttpRequestService httpClient, final String developerKey) {
        this.httpClient = httpClient;
        this.developerKey = developerKey;

        mapper = new ObjectMapper();
    }

    /**
     * Sends a request to Google Books API, returns the result.
     *
     * @param isbn isbn to send to the Google Books API
     * @return A JsonNode representing the return value
     */
    public Optional<JsonNode> getBookInfoByIsbn(final String isbn) {
        String uriString = String.format(
                "https://www.googleapis.com/books/v1/volumes?q=isbn:%s&key=%s",
                isbn, developerKey);
        //TODO we're not actually logging how long this request takes
        HttpServiceResponse httpServiceResponse = httpClient.get(uriString, true);

        try {
            //TODO we should iterate and find all the results
            JsonNode jsonNodeValue = readValue(httpServiceResponse.getResponse())
                    .map(jsonNode -> jsonNode.get("items"))
                    .map(jsonNode -> jsonNode.get(0))
                    .map(jsonNode -> jsonNode.get("selfLink"))
                    .map(JsonNode::asText)
                    .map(individualSearchResultUri -> httpClient.get(individualSearchResultUri, true))
                    .flatMap((HttpServiceResponse value) -> readValue(value.getResponse()))
                    .orElse(null);
            return Optional.ofNullable(jsonNodeValue);
        } catch (IllegalStateException e) {
            //TODO log exception
            return Optional.empty();
        }
    }

    private Optional<JsonNode> readValue(final String value) {
        try {
            return Optional.ofNullable(mapper.readTree(value));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
