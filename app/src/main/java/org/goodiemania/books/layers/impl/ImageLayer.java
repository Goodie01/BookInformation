package org.goodiemania.books.layers.impl;

import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.context.Context;
import org.goodiemania.books.layers.GoodReadsLayer;
import org.goodiemania.books.layers.NewBookInformation;
import org.goodiemania.books.services.xml.XmlDocument;
import org.goodiemania.models.books.BookData;
import org.goodiemania.models.books.DataSource;
import org.goodiemania.models.books.Image;

/**
 * Created by Macro303 on 2020-Jan-06.
 */
public class ImageLayer implements GoodReadsLayer {
    @Override
    public void applyGoodReads(final NewBookInformation bookInformation, final XmlDocument document) {
        Optional.of(document)
                .map(xmlDocument -> {
                    var imageUrl = xmlDocument.getValueAsString("/GoodreadsResponse/book/image_url");
                    if (imageUrl.equalsIgnoreCase("https://s.gr-assets.com/assets/nophoto/book/111x148-bcc042a9c91a29c1d680899eff700a03.png")) {
                        return "";
                    }
                    var id = xmlDocument.getValueAsString("/GoodreadsResponse/book/id");
                    return goodReadsUrlProcessing(imageUrl, id);
                })
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(Image::new)
                .ifPresent(bookInformation::setImage);
    }

    private String goodReadsUrlProcessing(final String url, final String id) {
        var regex = "^.*/(.*?).jpg$";
        var matcher = Pattern.compile(regex).matcher(url);
        if (!matcher.find()) {
            return url;
        }
        return url.replace(matcher.group(1), id);
    }

    private Optional<BookData<Image>> getGoogle(final Context context) {
        return context.getGoogleBooksResponse()
                .map(jsonNode -> jsonNode.at("/volumeInfo/imageLinks/thumbnail").textValue())
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(s -> BookData.of(new Image(s), DataSource.GOOGLE_BOOKS));
    }

    private Optional<BookData<Image>> getOpenLib(final Context context) {
        return context.getOpenLibrarySearchResponse()
                .map(jsonNode -> jsonNode.at("/thumnail_url").textValue())
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(s -> BookData.of(new Image(s), DataSource.OPEN_LIBRARY));
    }
}