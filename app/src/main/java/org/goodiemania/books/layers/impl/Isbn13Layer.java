package org.goodiemania.books.layers.impl;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.context.Context;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.NewBookInformation;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.BookData;
import org.goodiemania.models.books.DataSource;
import org.goodiemania.models.books.Isbn13;

public class Isbn13Layer implements GoodReadsLayer {
    @Override
    public void applyGoodReads(final NewBookInformation bookInformation, final XmlDocument document) {
        Optional.of(document)
                .map(xmlDocument -> xmlDocument.getValueAsString("/GoodreadsResponse/book/isbn13"))
                .filter(StringUtils::isNotBlank)
                .map(Isbn13::new)
                .ifPresent(bookInformation::setIsbn13);
    }

    private Optional<BookData<Isbn13>> getFromSearchParam(final Context context) {
        if (context.getIsbn().length() == 13) {
            Isbn13 isbnData = new Isbn13(context.getIsbn());
            return Optional.of(BookData.of(isbnData, DataSource.SEARCH));
        }
        return Optional.empty();
    }

    private Optional<BookData<Isbn13>> getGoogleBooks(final Context context) {
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
                        return StringUtils.equals("ISBN_13", type);
                    })
                    .map(jsonNode -> jsonNode.at(
                            String.format("/volumeInfo/industryIdentifiers/%d/identifier", currentCount))
                            .textValue());

            if (isbnValue.isPresent()) {
                Isbn13 isbn = new Isbn13(isbnValue.get());
                return Optional.of(BookData.of(isbn, DataSource.GOOGLE_BOOKS));
            }

            count++;
        }
        return Optional.empty();
    }

    private Optional<BookData<Isbn13>> getOpenLibrary(final Context context) {
        return context.getOpenLibrarySearchResponse()
                .map(jsonNode -> jsonNode.at("/details/isbn_13/0").textValue())
                .filter(StringUtils::isNotBlank)
                .map(Isbn13::new)
                .map(isbn13 -> BookData.of(isbn13, DataSource.OPEN_LIBRARY));
    }
}
