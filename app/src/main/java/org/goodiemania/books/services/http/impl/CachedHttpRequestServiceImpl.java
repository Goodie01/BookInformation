package org.goodiemania.books.services.http.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.goodiemania.api.models.StoredHttpRequest;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.HttpServiceResponse;
import org.goodiemania.books.services.http.ResponseType;
import org.goodiemania.books.services.misc.TimerResponse;
import org.goodiemania.books.services.misc.TimerService;
import org.goodiemania.odin.external.EntityManager;
import org.goodiemania.odin.external.Odin;

public class CachedHttpRequestServiceImpl implements HttpRequestService {
    private final HttpClient httpClient;
    private final EntityManager<StoredHttpRequest> em;
    private final TimerService timerService;

    public CachedHttpRequestServiceImpl() {
        httpClient = HttpClient.newBuilder()
                .build();

        Odin odin = Odin.create()
                .setObjectMapper(new ObjectMapper().registerModule(new JavaTimeModule()))
                .addPackageName("org.goodiemania.api.models")
                .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                .build();
        em = odin.createFor(StoredHttpRequest.class);
        timerService = new TimerService();
    }

    @Override
    public HttpServiceResponse get(final String uriString, final boolean cachedResponseAllowed) {
        TimerResponse<HttpRequestResponse> timedEventResponse = timerService.time(() -> {
            Optional<HttpRequestResponse> cachedResponse = Optional.empty();

            if (cachedResponseAllowed) {
                cachedResponse = findCachedResponse(uriString);
            }

            return cachedResponse.orElseGet(() -> makeAndSaveHttpRequest(uriString));
        });


        return HttpServiceResponseImpl.of(
                timedEventResponse.getResponse().getBody(),
                timedEventResponse.getResponse().getStatus(),
                timedEventResponse.getTime(),
                timedEventResponse.getResponse().getResponseType());
    }


    private Optional<HttpRequestResponse> findCachedResponse(final String uriString) {
        Optional<StoredHttpRequest> foundHttpRequest = em.getById(uriString)
                .filter(storedHttpRequest ->
                        storedHttpRequest.getRequestTime().isAfter(ZonedDateTime.now().minus(1, ChronoUnit.DAYS))
                );
        foundHttpRequest.ifPresent(storedHttpRequest -> System.out.println("Found a cached result, " + storedHttpRequest.getRequestTime() + ", for: " + uriString));

        return foundHttpRequest.map(StoredHttpRequest::getResponse);
    }

    private HttpRequestResponse makeAndSaveHttpRequest(final String uriString) {
        URI uri = URI.create(uriString);
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(uri)
                .setHeader("User-Agent", "Jerome Java bot: https://gitlab.com/Goodie_/jerome")
                .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            HttpRequestResponse response = HttpRequestResponse.of(httpResponse.body(), httpResponse.statusCode(), ResponseType.LIVE);
            em.save(StoredHttpRequest.of(uriString, response.cached()));
            System.out.println("Making a HTTP request to, " + uriString);
            return response;
        } catch (InterruptedException | IOException e) {
            throw new IllegalStateException(uriString, e);
        }
    }
}
