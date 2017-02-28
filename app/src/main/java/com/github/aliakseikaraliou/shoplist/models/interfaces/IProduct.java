package com.github.aliakseikaraliou.shoplist.models.interfaces;

import android.os.Parcelable;

public interface IProduct extends Parcelable {

    String getName();

    void setName(final String name);

    String getDescription();

    void setDescription(final String description);
}
