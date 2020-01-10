package org.goodiemania.books.model;

import java.util.List;
import java.util.Set;

public class BookInformation {
    private List<BookData<Title>> title;
    private List<BookData<Set<Author>>> authors;
    private List<BookData<String>> description;
    private List<BookData<Isbn10>> isbn10;
    private List<BookData<Isbn13>> isbn13;
    private List<BookData<Image>> image;

    public List<BookData<Title>> getTitle() {
        return title;
    }

    public void setTitle(final List<BookData<Title>> title) {
        this.title = title;
    }

    public List<BookData<Set<Author>>> getAuthors() {
        return authors;
    }

    public void setAuthors(final List<BookData<Set<Author>>> authors) {
        this.authors = authors;
    }

    public List<BookData<String>> getDescription() {
        return description;
    }

    public void setDescription(final List<BookData<String>> description) {
        this.description = description;
    }

    public List<BookData<Isbn10>> getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(final List<BookData<Isbn10>> isbn10) {
        this.isbn10 = isbn10;
    }

    public List<BookData<Isbn13>> getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(final List<BookData<Isbn13>> isbn13) {
        this.isbn13 = isbn13;
    }

    @Override
    public String toString() {
        return String.format("BookInformation{title='%s', authors=%s, isbn10='%s', isbn13='%s'}",
                title, authors, isbn10, isbn13);
    }

    public List<BookData<Image>> getImage() {
        return image;
    }

    public void setImage(final List<BookData<Image>> image) {
        this.image = image;
    }
}
