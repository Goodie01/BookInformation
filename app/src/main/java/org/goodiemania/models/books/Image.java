package org.goodiemania.models.books;

import java.util.Objects;

/**
 * Created by Macro303 on 2020-Jan-06.
 */
public class Image {
    private String value;

    public Image(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Image)) {
            return false;
        }

        return value.equals(((Image) o).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "Image{"
                + "value='" + value + '\''
                + '}';
    }
}