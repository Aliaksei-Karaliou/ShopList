package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;
import com.github.aliakseikaraliou.shoplist.parsers.html.EurooptGipermallParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ShopListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<IProduct>> {

    private static final String URL_TEMPLATE = "https://e-dostavka.by/search/?searchtext=%s&page=%d&ajax=0";
    private String title;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        title = "Молоко";

        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<List<IProduct>> onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<List<IProduct>>(this) {

            @Override
            public List<IProduct> loadInBackground() {
                try {
                    final String url = String.format(Locale.US, URL_TEMPLATE, title, 1);
                    final Document page = Jsoup.connect(url).get();
                    return new EurooptGipermallParser().parseHtml(page);
                } catch (final IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<List<IProduct>> loader, final List<IProduct> data) {
        final StringBuilder builder = new StringBuilder();
    }

    @Override
    public void onLoaderReset(final Loader<List<IProduct>> loader) {

    }

}
