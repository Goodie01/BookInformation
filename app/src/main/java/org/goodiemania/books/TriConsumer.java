package org.goodiemania.books;

@FunctionalInterface
public interface TriConsumer<T, U, E> {
    void accept(T var1, U var2, E var3);
}
