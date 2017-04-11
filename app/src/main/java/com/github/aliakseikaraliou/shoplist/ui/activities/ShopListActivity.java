package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IShopListProduct;
import com.github.aliakseikaraliou.shoplist.parsers.html.EurooptGipermallParser;
import com.github.aliakseikaraliou.shoplist.ui.UiConstants;
import com.github.aliakseikaraliou.shoplist.ui.adapters.ShopListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<IShopListProduct>> {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<IShopListProduct> shopListProducts;
    private static int LOADER_ID = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        final String title = getIntent().getStringExtra(UiConstants.Strings.PRODUCT_TITLE);

        progressBar.setVisibility(View.VISIBLE);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = ((RecyclerView) findViewById(R.id.activity_shoplist_recycler));
        shopListProducts = new ArrayList<>();
        final ShopListAdapter adapter = new ShopListAdapter(this, shopListProducts);
        recyclerView.setAdapter(adapter);
        final RecyclerView.LayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new GridLayoutManager(this, 3);
        }
        recyclerView.setLayoutManager(layoutManager);

        final LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Loader<List<IShopListProduct>> onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<List<IShopListProduct>>(this) {

            private static final String URL_TEMPLATE = "https://e-dostavka.by/search/?searchtext=%s&page=%d&ajax=0";
            private String title;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar = (ProgressBar) findViewById(R.id.activity_shoplist_progress);
                progressBar.setVisibility(View.VISIBLE);
                final String title = args.getString(UiConstants.Strings.PRODUCT_TITLE);
            }

            @Override
            public List<IShopListProduct> loadInBackground() {
                int i = 0;
                final List<IShopListProduct> result = new ArrayList<>();
                try {
                    while (true) {
                        i++;
                        final String url = String.format(Locale.US, URL_TEMPLATE, title, i);
                        final Document page = Jsoup.connect(url).get();
                        final List<IShopListProduct> productList = new EurooptGipermallParser().parseHtml(page);
                        if (shopListProducts.contains(productList.get(0))) {
                            break;
                        }
                        result.addAll(shopListProducts);
                    }
                    return result;
                } catch (final Exception e) {
                    throw new IllegalArgumentException();
                }
            }
        };
    }

    @Override
    public void onLoadFinished(final Loader<List<IShopListProduct>> loader, final List<IShopListProduct> data) {
        shopListProducts = new ArrayList<>(data);
    }

    @Override
    public void onLoaderReset(final Loader<List<IShopListProduct>> loader) {
    }

    private class ShopListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final List<IShopListProduct> shopList = intent.getParcelableArrayListExtra(UiConstants.Strings.SHOP_LIST);
            if (shopListProducts.isEmpty()) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            final int size = shopListProducts.size();
            shopListProducts.addAll(shopList);
            recyclerView.getAdapter().notifyItemInserted(size);
        }
    }
}
