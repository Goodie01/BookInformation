package org.goodiemania.books.services.misc;

import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.services.http.HttpRequestService;
import org.goodiemania.books.services.http.HttpServiceResponse;
import org.goodiemania.books.services.http.ResponseType;
import org.goodiemania.books.services.http.impl.HttpServiceResponseImpl;

public class HttpRequestServiceMock implements HttpRequestService {
    //These are going to be terrible terrible giant loads of string. I'm sorry.
    private static final String GOOD_READS_RESP = "<error>Page not found</error>";
    private static final String LIBRARY_THING_RESP = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<response stat=\"ok\"><ltml xmlns=\"http://www.librarything.com/\" version=\"1.1\"><item id=\"23798478\" type=\"work\"><author id=\"4797844\" authorcode=\"mcadootyson\">Tyson Mcadoo</author><title>Subtract Book - By Tyson Mcadoo</title><rating>0</rating><url>http://www.librarything.com/work/23798478</url><commonknowledge/></item><legal>By using this data you agree to the LibraryThing API terms of service.</legal></ltml></response>\n";
    //NODO this is a invalid author ID resp.... I need a example of a valid author response
    private static final String LIBRARY_THING_AUTHOR_RESP = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<response stat=\"fail\"><err code=\"106\">Could not determine author from supplied arguments</err></response>\n";
    public static final String GOOGLE_BOOKS_RESP = "{\n"
            + " \"kind\": \"books#volumes\",\n"
            + " \"totalItems\": 0\n"
            + "}";
    public static final String OPEN_LIBRARY_RESP = "{}";

    public String getRespString(final String uriString) {
        if (StringUtils.startsWith(uriString, "https://www.goodreads.com/")) {
            return GOOD_READS_RESP;
        } else if (StringUtils.startsWith(uriString, "https://www.librarything.com/services/rest/1.1/?method=librarything.ck.getwork")) {
          return LIBRARY_THING_RESP;
        }else if (StringUtils.startsWith(uriString, "https://www.librarything.com/services/rest/1.1/?method=librarything.ck.getauthor")) {
          return LIBRARY_THING_AUTHOR_RESP;
        }else if (StringUtils.startsWith(uriString, "https://www.googleapis.com")) {
          return GOOGLE_BOOKS_RESP;
        }else if (StringUtils.startsWith(uriString, "https://openlibrary.org")) {
          return OPEN_LIBRARY_RESP;
        }
        return "";
    }

    @Override
    public HttpServiceResponse get(final String uriString, final boolean cachedResponseAllowed) {
        return HttpServiceResponseImpl.of(getRespString(uriString), 200,200, ResponseType.TEST);
    }
}
