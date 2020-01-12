package org.goodiemania.books.services.http.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.HttpServiceResponse;
import org.goodiemania.books.services.http.ResponseType;
import org.goodiemania.books.services.misc.TimerResponse;
import org.goodiemania.books.services.misc.TimerService;

public class HttpRequestServiceImpl implements HttpRequestService {
    private final HttpClient httpClient;
    private final TimerService timerService;

    public HttpRequestServiceImpl() {
        httpClient = HttpClient.newBuilder()
                .build();
        timerService = new TimerService();
    }

    /**
     * Fetches the content at a specific URL.
     *
     * @param uriString The URL to make a (GET) request too.
     * @return The string value of the page without any processing done to it.
     */
    @Override
    public HttpServiceResponse get(final String uriString, final boolean cachedResponseAllowed) {
        URI uri = URI.create(uriString);
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(uri)
                .setHeader("User-Agent", "Jerome Java bot: https://gitlab.com/Goodie_/jerome")
                .build();

        TimerResponse<HttpResponse<String>> timedEventResponse = timerService.time(() -> {
            try {
                return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            } catch (IOException | InterruptedException e) {
                throw new IllegalStateException(uriString, e);
            }
        });

        return HttpServiceResponseImpl.of(
                timedEventResponse.getResponse().body(),
                timedEventResponse.getResponse().statusCode(),
                timedEventResponse.getTime(),
                ResponseType.LIVE);
    }
}
