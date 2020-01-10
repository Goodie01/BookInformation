package org.goodiemania.books.services.misc.misc;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HttpRequestServiceImpl implements HttpRequestService {
    private final HttpClient httpClient;

    public HttpRequestServiceImpl() {
        httpClient = HttpClient.newBuilder()
                .build();
    }

    /**
     * Fetches the content at a specific URL.
     *
     * @param uriString The URL to make a (GET) request too.
     * @return The string value of the page without any processing done to it.
     */
    @Override
    public String get(final String uriString) {
        URI uri = URI.create(uriString);
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(uri)
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .build();

        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.body();
        } catch (InterruptedException | IOException e) {
            //TODO this probably shouldn't break the entire system.... unless?
            throw new IllegalStateException(uriString, e);
        }
    }
}
