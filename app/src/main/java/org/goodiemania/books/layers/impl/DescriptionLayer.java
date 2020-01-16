package org.goodiemania.books.layers.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.goodiemania.books.context.Context;
import org.goodiemania.books.layers.Layer;
import org.goodiemania.models.books.BookData;
import org.goodiemania.models.books.DataSource;

public class DescriptionLayer implements Layer {
    @Override
    public void apply(final Context context) {
        List<BookData<String>> descrList = new ArrayList<>();
        getGood(context).ifPresent(setBookData ->
                LayerHelper.processBookData(descrList, setBookData));
        getGoogle(context).ifPresent(setBookData ->
                LayerHelper.processBookData(descrList, setBookData));
        getOpenLib(context).ifPresent(setBookData ->
                LayerHelper.processBookData(descrList, setBookData));
        getLibThing(context).ifPresent(setBookData ->
                LayerHelper.processBookData(descrList, setBookData));

        context.getBookInformation().setDescription(descrList);
    }

    private Optional<BookData<String>> getGood(final Context context) {
        return context.getGoodReadsResponse()
                .map(goodReadsResponse -> goodReadsResponse.getValueAsString("/GoodreadsResponse/book/description"))
                .map(s -> BookData.of(s, DataSource.GOOD_READS));
    }

    //TODO write me.
    private Optional<BookData<String>> getGoogle(final Context context) {
        return Optional.empty();
    }

    private Optional<BookData<String>> getOpenLib(final Context context) {
        return Optional.empty();
    }

    private Optional<BookData<String>> getLibThing(final Context context) {
        return Optional.empty();
    }
}