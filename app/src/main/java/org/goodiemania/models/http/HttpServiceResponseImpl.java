package org.goodiemania.models.http;

import java.time.ZonedDateTime;
import org.goodiemania.books.services.http.HttpServiceResponse;
import org.goodiemania.books.services.http.ResponseType;

public class HttpServiceResponseImpl implements HttpServiceResponse {
    private final String response;
    private final int status;
    private final long time;
    private final ResponseType responseType;
    private final ZonedDateTime requestTime;

    private HttpServiceResponseImpl(
            final String response,
            final int status,
            final long time,
            final ResponseType responseType,
            final ZonedDateTime requestTime) {
        this.response = response;
        this.status = status;
        this.time = time;
        this.responseType = responseType;
        this.requestTime = requestTime;
    }

    public static HttpServiceResponse of(
            final String response,
            final int status,
            final long time,
            final ResponseType responseType,
            final ZonedDateTime requestTime) {
        return new HttpServiceResponseImpl(response, status, time, responseType, requestTime);
    }

    @Override
    public String getResponse() {
        return response;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public ResponseType getResponseType() {
        return responseType;
    }

    @Override
    public ZonedDateTime getRequestTime() {
        return requestTime;
    }
}
