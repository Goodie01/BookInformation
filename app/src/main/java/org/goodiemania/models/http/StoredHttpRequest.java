package org.goodiemania.models.http;

import java.time.ZonedDateTime;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;

@Entity
public class StoredHttpRequest {
    @Id
    private String url;
    private HttpRequestResponse response;

    public StoredHttpRequest() {
    }

    private StoredHttpRequest(final String url, final HttpRequestResponse response, final ZonedDateTime now) {
        this.url = url;
        this.response = response;
    }

    public static StoredHttpRequest of(final String url, final HttpRequestResponse response) {
        return new StoredHttpRequest(url, response, ZonedDateTime.now());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public HttpRequestResponse getResponse() {
        return response;
    }

    public void setResponse(final HttpRequestResponse response) {
        this.response = response;
    }
}
