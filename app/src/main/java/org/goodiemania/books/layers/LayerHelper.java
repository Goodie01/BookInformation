package org.goodiemania.books.layers;

import java.util.List;
import java.util.Optional;
import org.goodiemania.models.books.BookData;

public final class LayerHelper {
    /**
     * Takes a found piece of data and attempts to match it to the existing data, if no match is found adds it to the list.
     *
     * @param bookDataList  List of all data found so far
     * @param foundBookData The newly found data
     * @param <T>           The type of the data, eg  set of authors, title, ISBN, etc
     */
    public static <T> void processBookData(
            final List<BookData<T>> bookDataList,
            final BookData<T> foundBookData) {
        if (bookDataList.isEmpty()) {
            bookDataList.add(foundBookData);
        } else {
            Optional<BookData<T>> any = bookDataList.stream()
                    .filter(bookData -> bookData.getData().equals(foundBookData.getData()))
                    .findAny();
            any.ifPresentOrElse(
                    existingBookData -> existingBookData.getSources().addAll(foundBookData.getSources()),
                    () -> bookDataList.add(foundBookData)
            );
        }
    }
}
