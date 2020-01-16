package org.goodiemania.books.layers;

import java.util.Set;
import org.goodiemania.models.books.Author;
import org.goodiemania.models.books.Image;
import org.goodiemania.models.books.Isbn10;
import org.goodiemania.models.books.Isbn13;
import org.goodiemania.models.books.Title;

public class NewBookInformation {
    private Title title;
    private Set<Author> authors;
    private String description;
    private Isbn10 isbn10;
    private Isbn13 isbn13;
    private Image image;

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
}
