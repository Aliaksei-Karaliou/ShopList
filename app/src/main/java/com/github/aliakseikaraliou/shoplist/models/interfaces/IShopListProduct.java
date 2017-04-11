package com.github.aliakseikaraliou.shoplist.models.interfaces;

import android.os.Parcelable;

import java.util.List;

public interface IShopListProduct extends Parcelable {
    IProduct getProduct();

    String getImageUrl();

}
