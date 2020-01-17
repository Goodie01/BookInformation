package org.goodiemania.books.layers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.GoogleBooksLayer;
import org.goodiemania.books.layers.LibraryThingLayer;
import org.goodiemania.books.layers.OpenLibraryLayer;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.Author;
import org.goodiemania.models.books.BookInformation;
import org.w3c.dom.NodeList;

public class AuthorLayer implements GoodReadsLayer, LibraryThingLayer, GoogleBooksLayer, OpenLibraryLayer {
    @Override
    public void applyGoodReads(final BookInformation bookInformation, final XmlDocument document) {
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

    @Override
    public void applyGoogleBooks(final BookInformation bookInformation, final JsonNode document) {
        Optional.of(document)
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
                .ifPresent(bookInformation::setAuthors);
    }

    @Override
    public void applyLibraryThing(final BookInformation bookInformation, final XmlDocument bookDocument) {
        Optional.of(bookDocument)
                .map(xmlDocument -> xmlDocument.getValueAsString("/response/ltml/item/author"))
                .map(s -> Set.of(new Author(s, "")))
                .ifPresent(bookInformation::setAuthors);
    }

    @Override
    public void applyOpenLibrary(final BookInformation bookInformation, final JsonNode document) {
        Optional.of(document)
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
                .ifPresent(bookInformation::setAuthors);
    }
}
