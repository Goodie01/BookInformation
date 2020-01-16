package org.goodiemania.books.layers.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.context.Context;
import org.goodiemania.books.layers.BookInformation;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.Layer;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.BookData;
import org.goodiemania.models.books.DataSource;
import org.goodiemania.models.books.Title;


public class TitleLayer implements Layer, GoodReadsLayer {
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

    @Override
    public void applyGoodReads(final BookInformation bookInformation, final XmlDocument document) {
        Optional.of(document)
                .map(xmlDocument -> xmlDocument.getValueAsString("/GoodreadsResponse/book/title"))
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(Title::of)
                .ifPresent(bookInformation::setTitle);
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
