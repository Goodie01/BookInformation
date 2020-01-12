package org.goodiemania.books.layers;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.goodiemania.books.model.Author;
import org.goodiemania.books.model.BookData;
import org.goodiemania.books.model.DataSource;
import org.goodiemania.books.services.context.Context;
import org.w3c.dom.NodeList;

public class AuthorLayer implements Layer {
    @Override
    public void apply(final Context context) {
        List<BookData<Set<Author>>> authorList = new ArrayList<>();
        getGood(context).ifPresent(setBookData ->
                LayerHelper.processBookData(authorList, setBookData));
        getGoogle(context).ifPresent(setBookData ->
                LayerHelper.processBookData(authorList, setBookData));
        getOpenLib(context).ifPresent(setBookData ->
                LayerHelper.processBookData(authorList, setBookData));
        getLibThing(context).ifPresent(setBookData ->
                LayerHelper.processBookData(authorList, setBookData));

        context.getBookInformation().setAuthors(authorList);
    }

    private Optional<BookData<Set<Author>>> getGood(final Context context) {
        return context.getGoodReadsResponse()
                .map(xmlDocument -> {
                    final Set<Author> authors = new HashSet<>();
                    NodeList nodeList = xmlDocument.getValue("/GoodreadsResponse/book/authors/author");

                    for (int i = 1; i <= nodeList.getLength(); i++) {
                        String name = xmlDocument.getValueAsString(
                                String.format("/GoodreadsResponse/book/authors/author[%d]/name", i));
                        String role = xmlDocument.getValueAsString(
                                String.format("/GoodreadsResponse/book/authors/author[%d]/role", i));

                        authors.add(new Author(name, role));
                    }
                    return authors;
                })
                .filter(authorSet -> !authorSet.isEmpty())
                .map(authors -> BookData.of(authors, DataSource.GOOD_READS));
    }

    private Optional<BookData<Set<Author>>> getGoogle(final Context context) {
        return context.getGoogleBooksResponse()
                .map(jsonNode -> {

                    final Set<Author> authors = new HashSet<>();
                    final Iterator<Map.Entry<String, JsonNode>> fields =
                            jsonNode.path("volumeInfo").path("authors").fields();

                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> next = fields.next();
                        authors.add(new Author(next.getValue().asText(), ""));
                    }

                    return authors;
                })
                .filter(authorSet -> !authorSet.isEmpty())
                .map(authors -> BookData.of(authors, DataSource.GOOGLE_BOOKS));
    }

    private Optional<BookData<Set<Author>>> getOpenLib(final Context context) {
        return context.getOpenLibrarySearchResponse()
                .map(jsonNode -> {

                    final Set<Author> authors = new HashSet<>();
                    final Iterator<Map.Entry<String, JsonNode>> fields =
                            jsonNode.path("details").path("authors").fields();

                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> next = fields.next();
                        authors.add(new Author(next.getValue().path("name").asText(), ""));
                    }
                    return authors;
                })
                .filter(authorSet -> !authorSet.isEmpty())
                .map(authors -> BookData.of(authors, DataSource.OPEN_LIBRARY));
    }

    //TODO we might have more luck using the actual library thing author response...
    private Optional<BookData<Set<Author>>> getLibThing(final Context context) {
        return context.getLibraryThingAuthor()
                .map(xmlDocument -> xmlDocument.getValueAsString("/response/ltml/item/author"))
                .map(s -> BookData.of(
                        Set.of(new Author(s, "")),
                        DataSource.LIBRARY_THING));
    }
}
