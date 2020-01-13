package org.goodiemania.models.books;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class Author {
    private final String name;
    private final String description;

    public Author(final String name, final String description) {
        this.name = StringUtils.trim(Objects.requireNonNull(name).replaceAll("\\s+", " "));
        this.description = StringUtils.trim(Objects.requireNonNull(description).replaceAll("\\s+", " "));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Author author = (Author) o;

        if (!name.equals(author.name)) {
            return false;
        }
        return description.equals(author.description);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + (StringUtils.isBlank(description) ? "" : " (" + description + ")");
    }
}
