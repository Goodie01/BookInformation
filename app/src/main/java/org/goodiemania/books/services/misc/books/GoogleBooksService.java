package org.goodiemania.books.services.misc.books;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.goodiemania.books.services.misc.misc.HttpRequestService;

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
    public JsonNode getBookInfoByIsbn(final String isbn) {
        String uriString = String.format(
                "https://www.googleapis.com/books/v1/volumes?q=isbn:%s&key=%s",
                isbn, developerKey);
        String response = httpClient.get(uriString);
        try {
            return readValue(response)
                    .map(jsonNode -> jsonNode.get("items"))
                    .map(jsonNode -> jsonNode.get(0))
                    .map(jsonNode -> jsonNode.get("selfLink"))
                    .map(JsonNode::asText)
                    .map(httpClient::get)
                    .flatMap(this::readValue)
                    .orElse(null);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(isbn, e);
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
