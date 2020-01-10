package org.goodiemania.api.models;

import java.time.ZonedDateTime;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;

@Entity
public class StoredHttpRequest {
    @Id
    private String url;
    private String response;
    private ZonedDateTime requestTime;

    public StoredHttpRequest() {
    }

    public StoredHttpRequest(final String url, final String response, final ZonedDateTime now) {
        this.url = url;
        this.response = response;
        this.requestTime = now;
    }

    public static StoredHttpRequest of(final String url, final String response) {
        return new StoredHttpRequest(url, response, ZonedDateTime.now());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(final String response) {
        this.response = response;
    }

    public ZonedDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(final ZonedDateTime requestTime) {
        this.requestTime = requestTime;
    }
}
