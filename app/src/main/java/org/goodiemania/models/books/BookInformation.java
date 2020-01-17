package org.goodiemania.models.books;

import java.util.Set;

public class BookInformation {
    private Title title;
    private Set<Author> authors;
    private String description;
    private Isbn10 isbn10;
    private Isbn13 isbn13;
    private Image image;
    private DataSource source;

    public BookInformation() {
    }

    /**
     * Creates a BookInformation with the provided source.
     *
     * @param source The provided source
     * @return A BookInformation containing the provided source
     */
    public static BookInformation fromSource(final DataSource source) {
        BookInformation bookInformation = new BookInformation();
        bookInformation.setSource(source);
        return bookInformation;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(final Title title) {
        this.title = title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(final Set<Author> authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Isbn10 getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(final Isbn10 isbn10) {
        this.isbn10 = isbn10;
    }

    public Isbn13 getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(final Isbn13 isbn13) {
        this.isbn13 = isbn13;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(final Image image) {
        this.image = image;
    }

    public DataSource getSource() {
        return source;
    }

    public void setSource(final DataSource source) {
        this.source = source;
    }
}
