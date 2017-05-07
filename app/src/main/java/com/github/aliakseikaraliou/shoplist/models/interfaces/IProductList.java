package com.github.aliakseikaraliou.shoplist.models.interfaces;

import android.os.Parcelable;

import java.util.List;
import java.util.Objects;

public interface IProductList extends Parcelable {

    String getTitle();

    void setTitle(final String title);

    Long getId();

    List<IProduct> getList();

    void setId(final long id);

    String toJson();

    void add(int position, IProduct product);

    boolean add(IProduct product);

    IProduct remove(int position);

    int size();

    IProduct get(int position);

    boolean remove(Object o);

    boolean isEmpty();
}
