package com.github.aliakseikaraliou.shoplist.models.interfaces;

import android.os.Parcelable;

public interface IProduct extends Parcelable {

    Long getId();

    void setId(long id);

    String getName();

    void setName(final String name);

    String getDescription();

    void setDescription(final String description);

    double getPrice();

    void setPrice(final double price);

    double getQuantity();

    void setQuantity(final double quantity);
}
