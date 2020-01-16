package org.goodiemania.books.layers.impl;

import java.util.Optional;
import org.goodiemania.books.context.Context;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.NewBookInformation;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.BookData;

public class DescriptionLayer implements GoodReadsLayer {
    @Override
    public void applyGoodReads(final NewBookInformation bookInformation, final XmlDocument document) {
        Optional.of(document)
                .map(goodReadsResponse -> goodReadsResponse.getValueAsString("/GoodreadsResponse/book/description"))
                .ifPresent(bookInformation::setDescription);
    }

    //TODO write me.
    private Optional<BookData<String>> getGoogle(final Context context) {
        return Optional.empty();
    }

    private Optional<BookData<String>> getOpenLib(final Context context) {
        return Optional.empty();
    }

    private Optional<BookData<String>> getLibThing(final Context context) {
        return Optional.empty();
    }
}
