package org.goodiemania.books.layers;

import com.fasterxml.jackson.databind.JsonNode;
import org.goodiemania.models.books.BookInformation;

public interface GoogleBooksLayer {
    /**
     * Each layer should update or focus on a single attribute of the book.
     * this method is the entry point where our service interacts with the layer
     *
     * <p>A single instance of each layer is created</p>
     *
     * @param bookInformation book information object to mutate
     * @param document        XML document representing the Good Reads response
     */
    void applyGoogleBooks(final BookInformation bookInformation, final JsonNode document);
}
