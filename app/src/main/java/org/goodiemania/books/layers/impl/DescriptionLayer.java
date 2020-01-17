package org.goodiemania.books.layers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.GoogleBooksLayer;
import org.goodiemania.books.layers.LibraryThingLayer;
import org.goodiemania.books.layers.OpenLibraryLayer;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.Author;
import org.goodiemania.models.books.BookInformation;
import org.goodiemania.models.books.Title;

public class DescriptionLayer implements GoodReadsLayer, LibraryThingLayer, GoogleBooksLayer, OpenLibraryLayer {
    @Override
    public void applyGoodReads(final BookInformation bookInformation, final XmlDocument document) {
        Optional.of(document)
                .map(goodReadsResponse -> goodReadsResponse.getValueAsString("/GoodreadsResponse/book/description"))
                .ifPresent(bookInformation::setDescription);
    }

    @Override
    public void applyLibraryThing(final BookInformation bookInformation, final XmlDocument bookDocument) {
        Optional.of(bookDocument)
                .map(xmlDocument -> xmlDocument.getValueAsString(
                        "/response/ltml/item/commonknowledge/fieldList/field[@name='description']/versionList/version/factList/fact"
                ))
                .map(StringUtils::trim)
                .map(s -> StringUtils.removeStart(s, "![CDATA["))
                .map(s -> StringUtils.removeEnd(s, "]]>"))
                .map(StringUtils::trim)
                .ifPresent(bookInformation::setDescription);
    }

    @Override
    public void applyGoogleBooks(final BookInformation bookInformation, final JsonNode document) {
        Optional.of(document)
                .map(jsonNode -> StringUtils.trim(jsonNode.at("/volumeInfo/description").textValue()))
                .ifPresent(bookInformation::setDescription);
    }

    @Override
    public void applyOpenLibrary(final BookInformation bookInformation, final JsonNode document) {
        Optional.of(document)
                .map(jsonNode -> StringUtils.trim(jsonNode.at("/details/description").textValue()))
                .ifPresent(bookInformation::setDescription);
    }
}
