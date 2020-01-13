package org.goodiemania.models.books;

import java.util.HashSet;
import java.util.Set;

public class BookData<T> {
    private final T data;
    private final Set<DataSource> sources;

    private BookData(final T data, final Set<DataSource> dataSources) {
        this.data = data;
        this.sources = dataSources;
    }

    /**
     * Creates a instance of BookData with the associated data and data source.
     *
     * @param data   The Data of the BookData
     * @param source The DataSource of the associated Data
     * @param <T>    Type of the Data
     * @return BookData with the associated data and source
     */
    public static <T> BookData<T> of(final T data, final DataSource source) {
        HashSet<DataSource> dataSources = new HashSet<>();
        dataSources.add(source);
        return new BookData<>(data, dataSources);
    }

    public T getData() {
        return data;
    }

    public Set<DataSource> getSources() {
        return sources;
    }

    @Override
    public String toString() {
        return String.format("%s %s", data.toString(), sources);
    }
}
