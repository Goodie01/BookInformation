package org.goodiemania.books.services.http.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.HttpServiceResponse;
import org.goodiemania.books.services.http.ResponseType;
import org.goodiemania.books.services.misc.TimerResponse;
import org.goodiemania.books.services.misc.TimerService;
import org.goodiemania.dao.StoredHttpRequestDao;
import org.goodiemania.models.http.HttpRequestResponse;
import org.goodiemania.models.http.HttpServiceResponseImpl;
import org.goodiemania.models.http.StoredHttpRequest;

public class CachedHttpRequestServiceImpl implements HttpRequestService {
    private final HttpClient httpClient;
    private final StoredHttpRequestDao requestDao;
    private final TimerService timerService;

    /**
     * Creates a new instance of the CachedHttpRequestServiceImpl.
     */
    public CachedHttpRequestServiceImpl(final HttpClient httpClient, final StoredHttpRequestDao requestDao, final TimerService timerService) {
        this.httpClient = httpClient;
        this.requestDao = requestDao;
        this.timerService = timerService;
    }

    @Override
    public HttpServiceResponse get(final String uriString, final boolean cachedResponseAllowed) {
        TimerResponse<HttpRequestResponse> timedRequest = timerService.time(() -> {
            Optional<HttpRequestResponse> cachedResponse = Optional.empty();

            if (cachedResponseAllowed) {
                cachedResponse = findCachedResponse(uriString);
            }

            return cachedResponse.orElseGet(() -> makeAndSaveHttpRequest(uriString));
        });

        return HttpServiceResponseImpl.of(
                timedRequest.getResponse().getBody(),
                timedRequest.getResponse().getStatus(),
                timedRequest.getTime(),
                timedRequest.getResponse().getResponseType(),
                timedRequest.getResponse().getRequestTime());
    }


    private Optional<HttpRequestResponse> findCachedResponse(final String uriString) {
        Optional<StoredHttpRequest> foundHttpRequest = requestDao.getByUrl(uriString)
                .filter(storedHttpRequest ->
                        storedHttpRequest.getResponse().getRequestTime().isAfter(ZonedDateTime.now().minus(1, ChronoUnit.DAYS))
                );
        foundHttpRequest.ifPresent(storedHttpRequest ->
                System.out.println("Found a cached result, " + storedHttpRequest.getResponse().getRequestTime() + ", for: " + uriString)
        );

        return foundHttpRequest.map(StoredHttpRequest::getResponse);
    }

    private HttpRequestResponse makeAndSaveHttpRequest(final String uriString) {
        URI uri = URI.create(uriString);
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(uri)
                .setHeader("User-Agent", "Jerome Java bot: https://gitlab.com/Goodie_/BookInformation")
                .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            HttpRequestResponse response = HttpRequestResponse.of(httpResponse, ResponseType.LIVE);
            requestDao.save(StoredHttpRequest.of(uriString, response.cached()));
            System.out.println("Making a HTTP request to, " + uriString);
            return response;
        } catch (InterruptedException | IOException e) {
            throw new IllegalStateException(uriString, e);
        }
    }
}
