package org.goodiemania.books.layers;

import java.util.List;
import java.util.Optional;
import org.goodiemania.books.model.BookData;
import org.goodiemania.books.model.DataSource;

public final class BookDataFinder {
    private final List<DataSource> preferenceOrder;

    public BookDataFinder(final List<DataSource> preferenceOrder) {
        this.preferenceOrder = preferenceOrder;
    }

    /**
     * Takes a list of found BookData, searches through it looking for the data given a preference list.
     *
     * @param bookDataList List of all book data found
     * @param <T>          The type of the data, eg  set of authors, title, ISBN, etc
     * @return Optional containing data from your preferred source
     */
    public <T> Optional<T> processBookData(
            final List<BookData<T>> bookDataList) {
        for (DataSource source : preferenceOrder) {
            Optional<T> data = bookDataList.stream()
                    .filter(bookData -> bookData.getSources().contains(source))
                    .findFirst()
                    .map(BookData::getData);

            if (data.isPresent()) {
                return data;
            }
        }

        return Optional.empty();
    }
}
