package org.goodiemania.books.layers;

import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.BookInformation;

public interface LibraryThingLayer {
    /**
     * Each layer should update or focus on a single attribute of the book.
     * this method is the entry point where our service interacts with the layer
     *
     * <p>A single instance of each layer is created</p>
     *
     * @param bookInformation book information object to mutate
     * @param document        XML document representing the Good Reads response
     */
    void applyLibraryThing(final BookInformation bookInformation, final XmlDocument document);
}
