package org.goodiemania.books.layers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.GoogleBooksLayer;
import org.goodiemania.books.layers.LibraryThingLayer;
import org.goodiemania.books.layers.OpenLibraryLayer;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.BookInformation;
import org.goodiemania.models.books.Title;


public class TitleLayer implements GoodReadsLayer, LibraryThingLayer, GoogleBooksLayer, OpenLibraryLayer {
    @Override
    public void applyGoodReads(final BookInformation bookInformation, final XmlDocument document) {
        Optional.of(document)
                .map(xmlDocument -> xmlDocument.getValueAsString("/GoodreadsResponse/book/title"))
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(Title::of)
                .ifPresent(bookInformation::setTitle);
    }


    @Override
    public void applyGoogleBooks(final BookInformation bookInformation, final JsonNode document) {
        Optional.of(document)
                .map(jsonNode -> {
                    final String title = StringUtils.trim(jsonNode.at("/volumeInfo/title").textValue());
                    final String subtitle = StringUtils.trim(jsonNode.at("/volumeInfo/subtitle").textValue());

                    return Title.of("", title, subtitle);
                })
                .ifPresent(bookInformation::setTitle);
    }

    @Override
    public void applyLibraryThing(final BookInformation bookInformation, final XmlDocument bookDocument) {
        Optional.of(bookDocument)
                .map(xmlDocument -> xmlDocument.getValueAsString("/response/ltml/item/title"))
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(Title::of)
                .ifPresent(bookInformation::setTitle);
    }

    @Override
    public void applyOpenLibrary(final BookInformation bookInformation, final JsonNode document) {
        Optional.of(document)
                .map(jsonNode -> {
                    final String seriesTitle = StringUtils.trim(jsonNode.at("/details/series/0").textValue());
                    final String title = StringUtils.trim(jsonNode.at("/details/subtitle").textValue());
                    final String subtitle = StringUtils.trim(jsonNode.at("/details/title").textValue());

                    return Title.of(seriesTitle, title, subtitle);
                })
                .ifPresent(bookInformation::setTitle);
    }
}
