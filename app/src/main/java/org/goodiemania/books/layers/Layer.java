package org.goodiemania.books.layers;

import org.goodiemania.books.context.Context;

public interface Layer {
    /**
     * Each layer should update or focus on a single attribute of the book.
     * this method is the entry point where our service interacts with the layer
     *
     * <p>A single instance of each layer is created</p>
     *
     * @param context Current context of the book being looked up
     */
    void apply(Context context);
}
