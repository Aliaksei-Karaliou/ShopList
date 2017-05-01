package com.github.aliakseikaraliou.shoplist.models.interfaces;

import android.os.Parcelable;

import java.util.List;

public interface IProductList extends List<IProduct>, Parcelable {

    String getTitle();

    void setTitle(final String title);

   Long getId();

    void setId(final long id);
}
