package com.github.aliakseikaraliou.shoplist.models.interfaces;

import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public interface IProduct extends Parcelable {

    String getName();

    void setName(final String name);

    String getDescription();

    void setDescription(final String description);

    double getPrice();

    void setPrice(final double price);

    double getQuantity();

    void setQuantity(final double quantity);
}
