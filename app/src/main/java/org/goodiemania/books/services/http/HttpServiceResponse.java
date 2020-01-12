package org.goodiemania.books.services.http;

public interface HttpServiceResponse {
    String getResponse();

    int getStatus();

    long getTime();

    ResponseType getResponseType();
}
