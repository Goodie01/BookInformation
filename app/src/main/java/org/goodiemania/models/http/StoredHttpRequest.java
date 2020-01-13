package org.goodiemania.models.http;

import org.jdbi.v3.core.mapper.Nested;

public class StoredHttpRequest {
    private String url;
    private HttpRequestResponse response;

    /**
     * Creates a StoredHttpRequest given the 2 arguments.
     *
     * @param url      URL that was used to access this response
     * @param response The HTTP response
     * @return A object representing both
     */
    public static StoredHttpRequest of(final String url, final HttpRequestResponse response) {
        StoredHttpRequest storedHttpRequest = new StoredHttpRequest();
        storedHttpRequest.setUrl(url);
        storedHttpRequest.setResponse(response);
        return storedHttpRequest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Nested
    public HttpRequestResponse getResponse() {
        return response;
    }

    @Nested
    public void setResponse(final HttpRequestResponse response) {
        this.response = response;
    }
}
