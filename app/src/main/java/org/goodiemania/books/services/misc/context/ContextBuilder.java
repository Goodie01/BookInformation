package org.goodiemania.books.services.misc.context;

import com.fasterxml.jackson.databind.JsonNode;
import org.goodiemania.books.services.misc.xml.XmlDocument;

public class ContextBuilder {
    private String isbn;
    private JsonNode openLibraryResponse;
    private XmlDocument goodReadsResponse;
    private XmlDocument libraryThingResponse;
    private XmlDocument libraryThingAuthor;
    private JsonNode googleBooksResponse;

    ContextBuilder() {
    }

    public ContextBuilder setIsbn(final String isbn) {
        this.isbn = isbn;
        return this;
    }

    public ContextBuilder setOpenLibraryResponse(final JsonNode openLibraryResponse) {
        this.openLibraryResponse = openLibraryResponse;
        return this;
    }

    public ContextBuilder setGoodReadsResponse(final XmlDocument goodReadsResponse) {
        this.goodReadsResponse = goodReadsResponse;
        return this;
    }

    public ContextBuilder setLibraryThingResponse(final XmlDocument libraryThingResponse) {
        this.libraryThingResponse = libraryThingResponse;
        return this;
    }

    public ContextBuilder setLibraryThingAuthor(final XmlDocument libraryThingAuthor) {
        this.libraryThingAuthor = libraryThingAuthor;
        return this;
    }

    public ContextBuilder setGoogleBooksResponse(final JsonNode googleBooksResponse) {
        this.googleBooksResponse = googleBooksResponse;
        return this;
    }

    /**
     * Builds a context instance.
     *
     * @return Returns the built context
     */
    public Context build() {
        return new Context(isbn,
                openLibraryResponse,
                goodReadsResponse,
                libraryThingResponse,
                libraryThingAuthor,
                googleBooksResponse);
    }
}