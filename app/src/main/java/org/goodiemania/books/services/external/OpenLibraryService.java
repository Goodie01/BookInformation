package org.goodiemania.books.services.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;

import kong.unirest.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.HttpServiceResponse;

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
    public Optional<JsonNode> getBookInfoByIsbn(final String isbn) {
        String searchString = String.format("ISBN:%s", isbn);
        String uriString = String.format("https://openlibrary.org/api/books?bibkeys=%s&format=json&jscmd=details", searchString);
        //HttpServiceResponse httpServiceResponse = httpClient.get(uriString, false);
        String response = Unirest.get(uriString).asString().getBody();//httpServiceResponse.getResponse();

        if (!StringUtils.equals("{}", response) || StringUtils.equals("9780141349978", isbn)) {
//            throw new IllegalStateException(isbn);
        }

        try {
            return Optional.ofNullable(objectMapper.readTree(response).get(searchString));
        } catch (IllegalStateException | JsonProcessingException e) {
            //TODO log exception here
            return Optional.empty();
        }
    }
}
