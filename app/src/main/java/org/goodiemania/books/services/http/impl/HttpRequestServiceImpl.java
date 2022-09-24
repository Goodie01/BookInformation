package org.goodiemania.books.services.http.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.HttpServiceResponse;
import org.goodiemania.books.services.http.ResponseType;
import org.goodiemania.books.services.misc.TimerResponse;
import org.goodiemania.books.services.misc.TimerService;
import org.goodiemania.models.http.HttpServiceResponseImpl;

public class HttpRequestServiceImpl implements HttpRequestService {
    private final HttpClient httpClient;
    private final TimerService timerService;

    public HttpRequestServiceImpl() {
        httpClient = HttpClient.newBuilder().build();
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
                //.setHeader("Cookie", "donation-identifier=MC44MjkwMjM4MzI0MzAxOTM2; session=/people/thomas_goodwin535%2C2022-
                // 09-24T02%3A58%3A37%2C008ff%240f5168fcbe8c177d5d6a6a192aef74e3")
                .setHeader("Accept-Encoding", "gzip, deflate, br")
                .setHeader("Accept-Language", "en-US,en;q=0.5")
                .setHeader("Connection", "keep-alive")
                .setHeader("DNT", "1")
                .setHeader("Sec-Fetch-Dest", "document")
                .setHeader("Sec-Fetch-Mode", "navigate")
                .setHeader("Sec-Fetch-Site", "none")
                .setHeader("Sec-Fetch-User", "?1")
                .setHeader("Sec-GPC", "1")
                .setHeader("Upgrade-Insecure-Requests", "1")
                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:107.0) Gecko/20100101 Firefox/107.0")
                .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
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
                ResponseType.LIVE,
                ZonedDateTime.now());
    }
}
