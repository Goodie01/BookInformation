package org.goodiemania.books.layers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.model.BookData;
import org.goodiemania.books.model.DataSource;
import org.goodiemania.books.model.Title;
import org.goodiemania.books.services.context.Context;


public class TitleLayer implements Layer {
    @Override
    public void apply(final Context context) {
        List<BookData<Title>> titleList = new ArrayList<>();
        getGood(context).ifPresent(setBookData ->
                LayerHelper.processBookData(titleList, setBookData));
        getGoogle(context).ifPresent(setBookData ->
                LayerHelper.processBookData(titleList, setBookData));
        getOpenLib(context).ifPresent(setBookData ->
                LayerHelper.processBookData(titleList, setBookData));
        getLibThing(context).ifPresent(setBookData ->
                LayerHelper.processBookData(titleList, setBookData));

        context.getBookInformation().setTitle(titleList);
    }

    private Optional<BookData<Title>> getGood(final Context context) {
        return context.getGoodReadsResponse()
                .map(xmlDocument -> xmlDocument.getValueAsString("/GoodreadsResponse/book/title"))
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(s -> BookData.of(Title.of(s), DataSource.GOOD_READS));
    }

    private Optional<BookData<Title>> getGoogle(final Context context) {
        return context.getGoogleBooksResponse()
                .map(jsonNode -> {
                    final String title = StringUtils.trim(jsonNode.at("/volumeInfo/title").textValue());
                    final String subtitle = StringUtils.trim(jsonNode.at("/volumeInfo/subtitle").textValue());

                    return Title.of("", title, subtitle);
                })
                .map(title -> BookData.of(title, DataSource.GOOGLE_BOOKS));
    }

    private Optional<BookData<Title>> getOpenLib(final Context context) {
        return context.getOpenLibrarySearchResponse()
                .map(jsonNode -> {
                    final String seriesTitle = StringUtils.trim(jsonNode.at("/details/series/0").textValue());
                    final String title = StringUtils.trim(jsonNode.at("/details/subtitle").textValue());
                    final String subtitle = StringUtils.trim(jsonNode.at("/details/title").textValue());

                    return Title.of(seriesTitle, title, subtitle);
                })
                .map(title -> BookData.of(title, DataSource.OPEN_LIBRARY));
    }

    private Optional<BookData<Title>> getLibThing(final Context context) {
        return context.getLibraryThingResponse()
                .map(xmlDocument -> xmlDocument.getValueAsString("/response/ltml/item/title"))
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(s -> BookData.of(Title.of(s), DataSource.LIBRARY_THING));
    }
}
