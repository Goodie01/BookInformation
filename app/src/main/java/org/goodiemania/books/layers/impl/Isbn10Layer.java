package org.goodiemania.books.layers.impl;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.context.Context;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.NewBookInformation;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.BookData;
import org.goodiemania.models.books.DataSource;
import org.goodiemania.models.books.Isbn10;

public class Isbn10Layer implements GoodReadsLayer {
    @Override
    public void applyGoodReads(final NewBookInformation bookInformation, final XmlDocument document) {
        Optional.of(document)
                .map(xmlDocument -> xmlDocument.getValueAsString("/GoodreadsResponse/book/isbn"))
                .filter(StringUtils::isNotBlank)
                .map(Isbn10::new)
                .ifPresent(bookInformation::setIsbn10);
    }

    private Optional<BookData<Isbn10>> getFromSearchParam(final Context context) {
        if (context.getIsbn().length() == 10) {
            Isbn10 isbnData = new Isbn10(context.getIsbn());
            return Optional.of(BookData.of(isbnData, DataSource.SEARCH));

        }
        return Optional.empty();
    }

    private Optional<BookData<Isbn10>> getGoogleBooks(final Context context) {
        int count = 0;

        while (true) {
            int currentCount = count;
            Boolean noIdentifierFound = context.getGoogleBooksResponse()
                    .map(jsonNode -> jsonNode.at(
                            String.format("/volumeInfo/industryIdentifiers/%d", currentCount)).isEmpty())
                    .orElse(true);
            if (noIdentifierFound) {
                break;
            }

            Optional<String> isbnValue = context.getGoogleBooksResponse()
                    .filter(jsonNode -> {
                        String type = jsonNode.at(
                                String.format("/volumeInfo/industryIdentifiers/%d/type", currentCount))
                                .textValue();
                        return StringUtils.equals("ISBN_10", type);
                    })
                    .map(jsonNode -> jsonNode.at(
                            String.format("/volumeInfo/industryIdentifiers/%d/identifier", currentCount))
                            .textValue());

            if (isbnValue.isPresent()) {
                Isbn10 isbn = new Isbn10(isbnValue.get());
                return Optional.of(BookData.of(isbn, DataSource.GOOGLE_BOOKS));
            }

            count++;
        }
        return Optional.empty();
    }

    private Optional<BookData<Isbn10>> getOpenLibrary(final Context context) {
        return context.getOpenLibrarySearchResponse()
                .map(jsonNode -> jsonNode.at("/details/isbn_10/0").textValue())
                .filter(StringUtils::isNotBlank)
                .map(Isbn10::new)
                .map(isbn10 -> BookData.of(isbn10, DataSource.OPEN_LIBRARY));
    }
}
