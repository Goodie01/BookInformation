package org.goodiemania.books.services.misc.context;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import org.goodiemania.books.model.BookInformation;
import org.goodiemania.books.services.misc.xml.XmlDocument;

public class Context {
    private final String isbn;
    private final BookInformation bookInformation;

    private XmlDocument goodReadsResponse;
    private XmlDocument libraryThingResponse;
    private XmlDocument libraryThingAuthor;
    private JsonNode openLibrarySearchResponse;
    private JsonNode googleBooksResponse;

    Context(final String isbn,
            final JsonNode openLibraryResponse,
            final XmlDocument goodReadsResponse,
            final XmlDocument libraryThingResponse,
            final XmlDocument libraryThingAuthor,
            final JsonNode googleBooksResponse) {
        this.isbn = isbn;
        this.goodReadsResponse = goodReadsResponse;
        this.openLibrarySearchResponse = openLibraryResponse;
        this.libraryThingResponse = libraryThingResponse;
        this.libraryThingAuthor = libraryThingAuthor;
        this.googleBooksResponse = googleBooksResponse;
        this.bookInformation = new BookInformation();
    }

    public static ContextBuilder build() {
        return new ContextBuilder();
    }

    public Optional<XmlDocument> getGoodReadsResponse() {
        return Optional.ofNullable(this.goodReadsResponse);
    }

    public Optional<XmlDocument> getLibraryThingResponse() {
        return Optional.ofNullable(this.libraryThingResponse);
    }

    public Optional<XmlDocument> getLibraryThingAuthor() {
        return Optional.ofNullable(this.libraryThingAuthor);
    }

    public Optional<JsonNode> getOpenLibrarySearchResponse() {
        return Optional.ofNullable(this.openLibrarySearchResponse);
    }

    public Optional<JsonNode> getGoogleBooksResponse() {
        return Optional.ofNullable(this.googleBooksResponse);
    }

    public BookInformation getBookInformation() {
        return bookInformation;
    }

    public String getIsbn() {
        return isbn;
    }
}
