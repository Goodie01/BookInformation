package org.goodiemania.books.layers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.model.BookData;
import org.goodiemania.books.model.DataSource;
import org.goodiemania.books.model.Isbn13;
import org.goodiemania.books.services.context.Context;

public class Isbn13Layer implements Layer {
    @Override
    public void apply(final Context context) {
        List<BookData<Isbn13>> descrList = new ArrayList<>();
        getGoodReads(context).ifPresent(setBookData ->
                LayerHelper.processBookData(descrList, setBookData));
        getGoogleBooks(context).ifPresent(setBookData ->
                LayerHelper.processBookData(descrList, setBookData));
        getOpenLibrary(context).ifPresent(setBookData ->
                LayerHelper.processBookData(descrList, setBookData));
        getFromSearchParam(context).ifPresent(setBookData ->
                LayerHelper.processBookData(descrList, setBookData));

        context.getBookInformation().setIsbn13(descrList);
    }

    private Optional<BookData<Isbn13>> getFromSearchParam(final Context context) {
        if (context.getIsbn().length() == 13) {
            Isbn13 isbnData = new Isbn13(context.getIsbn());
            return Optional.of(BookData.of(isbnData, DataSource.SEARCH));
        }
        return Optional.empty();
    }

    private Optional<BookData<Isbn13>> getGoodReads(final Context context) {
        return context.getGoodReadsResponse()
                .map(xmlDocument -> xmlDocument.getValueAsString("/GoodreadsResponse/book/isbn13"))
                .filter(StringUtils::isNotBlank)
                .map(Isbn13::new)
                .map(isbn13 -> BookData.of(isbn13, DataSource.GOOD_READS));
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
