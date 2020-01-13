package org.goodiemania.models.books;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class Title {
    private final String seriesTitle;
    private final String title;
    private final String subTitle;

    private Title(final String seriesTitle, final String title, final String subTitle) {
        this.seriesTitle = Objects.requireNonNull(seriesTitle);
        this.title = Objects.requireNonNull(title);
        this.subTitle = Objects.requireNonNull(subTitle);
    }

    /**
     * Creates a title given the parameters.
     *
     * @param seriesTitle The books series title
     * @param title       The books main title
     * @param subTitle    The books sub title
     * @return A object containing all 3
     */
    public static Title of(final String seriesTitle, final String title, final String subTitle) {
        return new Title(
                seriesTitle == null ? "" : seriesTitle,
                title == null ? "" : title,
                subTitle == null ? "" : subTitle);
    }

    public static Title of(final String title) {
        return Title.of("", title, "");
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    private String to() {
        String finalTitle = title;

        if (!StringUtils.contains(subTitle, finalTitle)
                && !StringUtils.contains(finalTitle, subTitle)
                && StringUtils.isNotBlank(subTitle)) {
            finalTitle = String.format("%s: %s", finalTitle, subTitle);
        }

        if (!(StringUtils.contains(seriesTitle, finalTitle)
                || StringUtils.contains(finalTitle, seriesTitle)
                || StringUtils.contains(seriesTitle, title)
                || StringUtils.contains(title, seriesTitle))
                && StringUtils.isNotBlank(seriesTitle)) {
            finalTitle = String.format("%s: %s", seriesTitle, finalTitle);
        }

        return finalTitle;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Title otherTitle = (Title) o;

        if (!seriesTitle.equals(otherTitle.seriesTitle)) {
            return false;
        }
        if (!title.equals(otherTitle.title)) {
            return false;
        }
        return subTitle.equals(otherTitle.subTitle);
    }

    @Override
    public int hashCode() {
        int result = seriesTitle.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + subTitle.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return seriesTitle + "|" + title + "|" + subTitle;
    }
}
