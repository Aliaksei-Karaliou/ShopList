package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;
import com.github.aliakseikaraliou.shoplist.parsers.html.EurooptGipermallParsel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ShopListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<IProductList> {

    private String title;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        title = "Молоко";

        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<IProductList> onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<IProductList>(this) {

            @Override
            public IProductList loadInBackground() {
                try {
                    final Document page = Jsoup.connect("https://gipermall.by/search/?searchtext=" + title).get();
                    return new EurooptGipermallParsel().parseHtml(page.outerHtml());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<IProductList> loader, final IProductList data) {

    }

    @Override
    public void onLoaderReset(final Loader<IProductList> loader) {

    }

}
