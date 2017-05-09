package com.github.aliakseikaraliou.shoplist.models.interfaces;

import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.List;

public interface IProductList extends Parcelable {

    String getTitle();

    void setTitle(final String title);

    List<IProduct> getList();

    @Exclude
    String getId();

    @Exclude
    void setId(final String id);

    void add(int position, IProduct product);

    boolean add(IProduct product);

    IProduct remove(int position);

    int size();

    IProduct get(int position);

    IProduct set(final int index, final IProduct element);
}
