package org.goodiemania.books.services.http;

public interface HttpRequestService {
    HttpServiceResponse get(String uriString, final boolean cachedResponseAllowed);
}
