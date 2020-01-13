package org.goodiemania.books.services.http;

import java.time.ZonedDateTime;

public interface HttpServiceResponse {
    String getResponse();

    int getStatus();

    long getTime();

    ResponseType getResponseType();

    ZonedDateTime getRequestTime();
}
