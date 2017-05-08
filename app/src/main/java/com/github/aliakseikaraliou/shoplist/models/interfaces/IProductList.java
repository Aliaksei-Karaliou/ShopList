package com.github.aliakseikaraliou.shoplist.models.interfaces;

import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Objects;

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

    boolean remove(IProduct o);
}
