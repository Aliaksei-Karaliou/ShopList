package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
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
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class ShopListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<IShopListProduct>> {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<IShopListProduct> shopListProducts;
    private static final int LOADER_ID = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
//        final String title = getIntent().getStringExtra(UiConstants.Strings.PRODUCT_TITLE);
        final String title = "Молоко";
        progressBar = (ProgressBar) findViewById(R.id.activity_shoplist_progress);

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
        final Bundle bundle = new Bundle();
        bundle.putString(UiConstants.Strings.PRODUCT_TITLE, title);
        loaderManager.initLoader(LOADER_ID, bundle, this).forceLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_shoplist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.menu_shoplist_save) {
            final List<Integer> checkedItems = ((ShopListAdapter) recyclerView.getAdapter()).getCheckedItems();
            final Collection<IShopListProduct> checkedProducts = new ArrayList<>();
            for (final Integer checkedItem : checkedItems) {
                checkedProducts.add(shopListProducts.get(checkedItem));
            }
            final Intent intent = new Intent();
            intent.putParcelableArrayListExtra(UiConstants.Strings.SHOP_LIST, (ArrayList<? extends Parcelable>) checkedProducts);
            setResult(UiConstants.Ids.SHOP_LIST, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(UiConstants.Ids.SHOP_LIST);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public Loader<List<IShopListProduct>> onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<List<IShopListProduct>>(this) {

            private static final String URL_TEMPLATE = "https://e-dostavka.by/search/?searchtext=%s&page=%d&ajax=0";

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar = (ProgressBar) findViewById(R.id.activity_shoplist_progress);
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public List<IShopListProduct> loadInBackground() {
                int i = 0;
                final List<IShopListProduct> result = new ArrayList<>();
                final String title = args.getString(UiConstants.Strings.PRODUCT_TITLE);
                try {
                    while (true) {
                        i++;
                        final String url = String.format(Locale.US, URL_TEMPLATE, title, i);
                        final Document page = Jsoup.connect(url).get();
                        final List<IShopListProduct> productList = new EurooptGipermallParser().parseHtml(page);
                        if (shopListProducts.contains(productList.get(0))) {
                            break;
                        }
                        result.addAll(productList);
                        ShopListActivity.this.shopListProducts.addAll(productList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.getAdapter().notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
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
    }


    @Override
    public void onLoaderReset(final Loader<List<IShopListProduct>> loader) {
    }
}
