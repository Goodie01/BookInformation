package org.goodiemania.books.services.books;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.goodiemania.books.services.http.HttpRequestService;

public class OpenLibraryService {
    private final HttpRequestService httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new instance of Open Library Service.
     *
     * @param httpClient Service to make HTTP requests
     */
    public OpenLibraryService(final HttpRequestService httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Sends a request to Open Library API, returns the result.
     *
     * @param isbn isbn to send to the Open Library API
     * @return A JsonNode representing the return value
     */
    public JsonNode getBookInfoByIsbn(final String isbn) {
        String searchString = String.format("ISBN:%s", isbn);
        String uriString = String.format("https://openlibrary.org/api/books?bibkeys=%s&format=json&jscmd=details", searchString);
        String response = httpClient.get(uriString, true).getResponse();

        try {
            return Optional.ofNullable(objectMapper.readTree(response))
                    .map(jsonNode -> jsonNode.get(searchString))
                    .orElse(null);
        } catch (IllegalStateException | JsonProcessingException e) {
            throw new IllegalStateException(isbn, e);
        }
    }
}
