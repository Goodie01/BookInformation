package org.goodiemania.models.http;

import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import org.goodiemania.books.services.http.ResponseType;

public class HttpRequestResponse {
    private String body;
    private int status;
    private ResponseType responseType;
    private ZonedDateTime requestTime;

    public HttpRequestResponse() {
    }

    /**
     * Creates a new instance given the below params.
     *
     * @param body         String response body
     * @param status       HTTP status code
     * @param responseType Where did we get the response from?
     * @return A HttpRequestResponse containing the fields
     */
    public static HttpRequestResponse of(final String body, final int status, final ResponseType responseType, final ZonedDateTime requestTime) {
        HttpRequestResponse httpRequestResponse = new HttpRequestResponse();
        httpRequestResponse.setBody(body);
        httpRequestResponse.setStatus(status);
        httpRequestResponse.setResponseType(responseType);
        httpRequestResponse.setRequestTime(requestTime);

        return httpRequestResponse;
    }


    /**
     * Creates a new instance given the below params.
     *
     * @param httpResponse Http response to injest
     * @param responseType Where did we get the response from?
     * @return A HttpRequestResponse containing the fields
     */
    public static HttpRequestResponse of(final HttpResponse<String> httpResponse, final ResponseType responseType) {
        HttpRequestResponse httpRequestResponse = new HttpRequestResponse();
        httpRequestResponse.setBody(httpResponse.body());
        httpRequestResponse.setStatus(httpResponse.statusCode());
        httpRequestResponse.setResponseType(responseType);
        httpRequestResponse.setRequestTime(ZonedDateTime.now());

        return httpRequestResponse;
    }

    public HttpRequestResponse cached() {
        return HttpRequestResponse.of(body, status, ResponseType.CACHED, requestTime);
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

    public ZonedDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(final ZonedDateTime requestTime) {
        this.requestTime = requestTime;
    }
}
