package org.goodiemania.books.layers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.books.model.BookData;
import org.goodiemania.books.model.DataSource;
import org.goodiemania.books.model.Image;
import org.goodiemania.books.services.misc.context.Context;

/**
 * Created by Macro303 on 2020-Jan-06.
 */
public class ImageLayer implements Layer {
    @Override
    public void apply(final Context context) {
        List<BookData<Image>> imageList = new ArrayList<>();
        getGood(context).ifPresent(data ->
                LayerHelper.processBookData(imageList, data));
        getGoogle(context).ifPresent(data ->
                LayerHelper.processBookData(imageList, data));
        getOpenLib(context).ifPresent(data ->
                LayerHelper.processBookData(imageList, data));

        context.getBookInformation().setImage(imageList);
    }

    private Optional<BookData<Image>> getGood(final Context context) {
        return context.getGoodReadsResponse()
                .map(xmlDocument -> {
                    var imageUrl = xmlDocument.getValueAsString("/GoodreadsResponse/book/image_url");
                    if (imageUrl.equalsIgnoreCase("https://s.gr-assets.com/assets/nophoto/book/111x148-bcc042a9c91a29c1d680899eff700a03.png")) {
                        return "";
                    }
                    var id = xmlDocument.getValueAsString("/GoodreadsResponse/book/id");
                    return imageUrl(imageUrl, id);
                })
                .map(StringUtils::trim)
                .filter(StringUtils::isNotBlank)
                .map(s -> BookData.of(new Image(s), DataSource.GOOD_READS));
    }


    private String imageUrl(final String url, final String id) {
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