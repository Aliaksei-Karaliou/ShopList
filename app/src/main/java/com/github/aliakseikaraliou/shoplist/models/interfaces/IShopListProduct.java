package com.github.aliakseikaraliou.shoplist.models.interfaces;

import android.os.Parcelable;

public interface IShopListProduct extends Parcelable {
    IProduct getProduct();

    String getImageUrl();

}
