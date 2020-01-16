package org.goodiemania.books.layers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.goodiemania.books.context.Context;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.NewBookInformation;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.Author;
import org.goodiemania.models.books.BookData;
import org.goodiemania.models.books.DataSource;
import org.w3c.dom.NodeList;

public class AuthorLayer implements GoodReadsLayer {
    @Override
    public void applyGoodReads(final NewBookInformation bookInformation, final XmlDocument document) {
        Optional.of(document)
                .map(xmlDocument -> {
                    final Set<Author> authors = new HashSet<>();
                    NodeList nodeList = xmlDocument.getValue("/GoodreadsResponse/book/authors/author");

                    for (int i = 1; i <= nodeList.getLength(); i++) {
                        String name = xmlDocument.getValueAsString(
                                String.format("/GoodreadsResponse/book/authors/author[%d]/name", i));
                        String role = xmlDocument.getValueAsString(
                                String.format("/GoodreadsResponse/book/authors/author[%d]/role", i));

                        authors.add(new Author(name, role));
                    }
                    return authors;
                })
                .filter(authorSet -> !authorSet.isEmpty())
                .ifPresent(bookInformation::setAuthors);
    }

    private Optional<BookData<Set<Author>>> getGoogle(final Context context) {
        return context.getGoogleBooksResponse()
                .map(jsonNode -> {

                    final Set<Author> authors = new HashSet<>();
                    final Iterator<Map.Entry<String, JsonNode>> fields =
                            jsonNode.path("volumeInfo").path("authors").fields();

                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> next = fields.next();
                        authors.add(new Author(next.getValue().asText(), ""));
                    }

                    return authors;
                })
                .filter(authorSet -> !authorSet.isEmpty())
                .map(authors -> BookData.of(authors, DataSource.GOOGLE_BOOKS));
    }

    private Optional<BookData<Set<Author>>> getOpenLib(final Context context) {
        return context.getOpenLibrarySearchResponse()
                .map(jsonNode -> {

                    final Set<Author> authors = new HashSet<>();
                    final Iterator<Map.Entry<String, JsonNode>> fields =
                            jsonNode.path("details").path("authors").fields();

                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> next = fields.next();
                        authors.add(new Author(next.getValue().path("name").asText(), ""));
                    }
                    return authors;
                })
                .filter(authorSet -> !authorSet.isEmpty())
                .map(authors -> BookData.of(authors, DataSource.OPEN_LIBRARY));
    }

    //TODO we might have more luck using the actual library thing author response...
    private Optional<BookData<Set<Author>>> getLibThing(final Context context) {
        return context.getLibraryThingAuthor()
                .map(xmlDocument -> xmlDocument.getValueAsString("/response/ltml/item/author"))
                .map(s -> BookData.of(
                        Set.of(new Author(s, "")),
                        DataSource.LIBRARY_THING));
    }
}
