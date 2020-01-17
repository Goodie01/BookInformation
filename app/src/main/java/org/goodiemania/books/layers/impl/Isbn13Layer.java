package org.goodiemania.books.layers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.GoogleBooksLayer;
import org.goodiemania.books.layers.OpenLibraryLayer;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.BookInformation;
import org.goodiemania.models.books.Isbn13;

public class Isbn13Layer implements GoodReadsLayer, GoogleBooksLayer, OpenLibraryLayer {
    @Override
    public void applyGoodReads(final BookInformation bookInformation, final XmlDocument document) {
        Optional.of(document)
                .map(xmlDocument -> xmlDocument.getValueAsString("/GoodreadsResponse/book/isbn13"))
                .filter(StringUtils::isNotBlank)
                .map(Isbn13::new)
                .ifPresent(bookInformation::setIsbn13);
    }

    @Override
    public void applyGoogleBooks(final BookInformation bookInformation, final JsonNode document) {
        int count = 0;

        while (true) {
            int currentCount = count;
            Boolean noIdentifierFound = Optional.of(document)
                    .map(jsonNode -> jsonNode.at(String.format("/volumeInfo/industryIdentifiers/%d", currentCount)).isEmpty())
                    .orElse(true);
            if (noIdentifierFound) {
                return;
            }

            Optional<String> isbnValue = Optional.of(document)
                    .filter(jsonNode -> {
                        String type = jsonNode.at(String.format("/volumeInfo/industryIdentifiers/%d/type", currentCount)).textValue();
                        return StringUtils.equals("ISBN_13", type);
                    })
                    .map(jsonNode -> jsonNode.at(String.format("/volumeInfo/industryIdentifiers/%d/identifier", currentCount)).textValue());

            if (isbnValue.isPresent()) {
                bookInformation.setIsbn13(new Isbn13(isbnValue.get()));
                return;
            }

            count++;
        }
    }

    @Override
    public void applyOpenLibrary(final BookInformation bookInformation, final JsonNode document) {
        Optional.of(document)
                .map(jsonNode -> jsonNode.at("/details/isbn_13/0").textValue())
                .filter(StringUtils::isNotBlank)
                .map(Isbn13::new)
                .ifPresent(bookInformation::setIsbn13);
    }
}
