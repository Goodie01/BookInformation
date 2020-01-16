package org.goodiemania.books.layers;

import org.goodiemania.books.services.xml.XmlDocument;

public interface GoodReadsLayer {
    /**
     * Each layer should update or focus on a single attribute of the book.
     * this method is the entry point where our service interacts with the layer
     * <p>
     * A single instance of each layer is created
     *
     * @param document XML document representing the Good Reads response
     */
    void applyGoodReads(final BookInformation bookInformation, final XmlDocument document);
}
