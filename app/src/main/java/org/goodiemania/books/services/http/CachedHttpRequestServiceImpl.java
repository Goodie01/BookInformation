package org.goodiemania.books.services.http;

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
import org.goodiemania.books.services.misc.misc.HttpRequestService;
import org.goodiemania.odin.external.EntityManager;
import org.goodiemania.odin.external.Odin;

public class CachedHttpRequestServiceImpl implements HttpRequestService {
    private final HttpClient httpClient;
    private final EntityManager<StoredHttpRequest> em;

    public CachedHttpRequestServiceImpl() {
        httpClient = HttpClient.newBuilder()
                .build();

        Odin odin = Odin.create()
                .setObjectMapper(new ObjectMapper().registerModule(new JavaTimeModule()))
                .addPackageName("org.goodiemania.api.models")
                .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                .build();
        em = odin.createFor(StoredHttpRequest.class);
    }

    @Override
    public String get(final String uriString) {
        return findCachedResponse(uriString)
                .map(StoredHttpRequest::getResponse)
                .orElseGet(() -> makeAndSaveHttpRequest(uriString));
    }

    private Optional<StoredHttpRequest> findCachedResponse(final String uriString) {
        Optional<StoredHttpRequest> foundHttpRequest = em.getById(uriString)
                .filter(storedHttpRequest ->
                        storedHttpRequest.getRequestTime().isAfter(ZonedDateTime.now().minus(1, ChronoUnit.DAYS))
                );
        foundHttpRequest.ifPresent(storedHttpRequest -> System.out.println("Found a cached result, " + storedHttpRequest.getRequestTime() + ", for: " + uriString));

        return foundHttpRequest;
    }

    private String makeAndSaveHttpRequest(final String uriString) {
        String response = makeHttpRequest(uriString);
        em.save(StoredHttpRequest.of(uriString, response));
        System.out.println("Making a HTTP request to, " + uriString);

        return response;
    }

    private String makeHttpRequest(final String uriString) {
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
