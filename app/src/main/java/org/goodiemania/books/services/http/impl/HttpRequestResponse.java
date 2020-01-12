package org.goodiemania.books.services.http.impl;

import org.goodiemania.books.services.http.ResponseType;

public class HttpRequestResponse {
    private String body;
    private int status;
    private ResponseType responseType;

    public HttpRequestResponse() {
    }

    public static HttpRequestResponse of(final String body, final int status, final ResponseType responseType) {
        HttpRequestResponse httpRequestResponse = new HttpRequestResponse();
        httpRequestResponse.setBody(body);
        httpRequestResponse.setStatus(status);
        httpRequestResponse.setResponseType(responseType);

        return httpRequestResponse;
    }

    public HttpRequestResponse cached() {
        return HttpRequestResponse.of(body, status, ResponseType.CACHED);
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(final ResponseType responseType) {
        this.responseType = responseType;
    }
}
