package com.github.aliakseikaraliou.shoplist.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.github.aliakseikaraliou.shoplist.models.interfaces.IShopListProduct;
import com.github.aliakseikaraliou.shoplist.parsers.html.EurooptGipermallParser;
import com.github.aliakseikaraliou.shoplist.ui.UiConstants;
import com.github.aliakseikaraliou.shoplist.ui.activities.ShopListActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopListService extends IntentService {

    private static final String URL_TEMPLATE = "https://e-dostavka.by/search/?searchtext=%s&page=%d&ajax=0";
    private static String SERVICE_NAME = "com.github.aliakseikaraliou.shoplist.services.shoplistservice";

    public ShopListService() {
        super(SERVICE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        if (intent != null) {
            final String title = intent.getStringExtra(UiConstants.Strings.PRODUCT_TITLE);
            try {
                final String url = String.format(Locale.US, URL_TEMPLATE, title, 1);
                final Document page = Jsoup.connect(url).get();
                final List<IShopListProduct> productList = new EurooptGipermallParser().parseHtml(page);
                final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
                final Intent sendIntent = new Intent(UiConstants.Receivers.SHOPLIST_NAME);
                sendIntent.putParcelableArrayListExtra(UiConstants.Strings.SHOP_LIST, (ArrayList<? extends Parcelable>) productList);
                broadcastManager.sendBroadcast(sendIntent);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
}