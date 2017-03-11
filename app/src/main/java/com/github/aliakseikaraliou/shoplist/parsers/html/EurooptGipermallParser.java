package com.github.aliakseikaraliou.shoplist.parsers.html;

import com.github.aliakseikaraliou.shoplist.models.classes.Product;
import com.github.aliakseikaraliou.shoplist.models.classes.ShopListProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IShopListProduct;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class EurooptGipermallParser {

    public List<IShopListProduct> parseHtml(final Document html) {
        final Elements select = html.select("div[class^=products_card trademark_]");
        final List<IShopListProduct> productList = new ArrayList<>();
        final int itemsCountToRun = Math.min(select.size(), 7);
        for (int i = 0; i < itemsCountToRun; i++) {
            final Element element = select.get(i);
            //get title
            final String title = element.select("div[class=title]").select("a").first().text();

            final Element priceElement = element.select("div[class=price]").first();
            final double price = this.getPriceFromString(priceElement.text());
            final IProduct product = new Product.Builder(title)
                    .setPrice(price)
                    .build();
            final String imgSrc = element.select("img[class=retina_redy]").attr("src");
            final IShopListProduct shopListProduct = new ShopListProduct(product, imgSrc);
            productList.add(shopListProduct);
        }
        return productList;
    }

    private double getPriceFromString(final String stringPrice) {
        final String price = stringPrice.replace("р", "").replace("к.", "").split(" ")[0];
        return Double.parseDouble(price);
    }
}
