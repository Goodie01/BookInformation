package org.goodiemania.models;

import java.util.List;
import org.goodiemania.models.books.BookInformation;

public class BookResponse {
    private List<BookInformation> infoList;

    /**
     * Creates a instance of a BookResponse with the provided list of book information.
     *
     * @param infoList List to be contained in the response
     * @return BookResponse containing the provided list
     */
    public static BookResponse of(final List<BookInformation> infoList) {
        BookResponse bookResponse = new BookResponse();
        bookResponse.setInfoList(infoList);

        return bookResponse;
    }

    public List<BookInformation> getInfoList() {
        return infoList;
    }

    public void setInfoList(final List<BookInformation> infoList) {
        this.infoList = infoList;
    }
}
