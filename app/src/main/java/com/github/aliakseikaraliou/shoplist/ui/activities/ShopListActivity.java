package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.github.aliakseikaraliou.shoplist.R;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IShopListProduct;
import com.github.aliakseikaraliou.shoplist.services.ShopListService;
import com.github.aliakseikaraliou.shoplist.ui.UiConstants;
import com.github.aliakseikaraliou.shoplist.ui.adapters.ShopListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShopListActivity extends AppCompatActivity {

    private Intent serviceIntent;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private List<IShopListProduct> shopListProducts;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);
        final String title = getIntent().getStringExtra(UiConstants.Strings.PRODUCT_TITLE);
        serviceIntent = new Intent(this, ShopListService.class);
        serviceIntent.putExtra(UiConstants.Strings.PRODUCT_TITLE, title);
        startService(serviceIntent);

        progressBar = ((ProgressBar) findViewById(R.id.activity_shoplist_progress));
        progressBar.setVisibility(View.VISIBLE);

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

        final LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UiConstants.Receivers.SHOPLIST_NAME);
        localBroadcastManager.registerReceiver(new ShopListReceiver(), intentFilter);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(UiConstants.Strings.SHOP_LIST, (ArrayList<? extends Parcelable>) shopListProducts);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final List<IShopListProduct> restoredList = savedInstanceState.getParcelableArrayList(UiConstants.Strings.SHOP_LIST);
        if (shopListProducts.isEmpty() && restoredList != null) {
            shopListProducts.addAll(restoredList);
            recyclerView.getAdapter().notifyItemInserted(0);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceIntent);
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
