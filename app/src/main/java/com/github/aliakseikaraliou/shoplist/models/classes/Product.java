package com.github.aliakseikaraliou.shoplist.models.classes;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;

public class Product implements IProduct {

    private String name;
    private String description;

    public Product(@NonNull final String name, @NonNull final String description) {
        this.name = name;
        this.description = description;
    }

    public Product(@NonNull final String name) {
        this(name, "");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.name);
        dest.writeString(this.description);
    }

    private Product(final Parcel in) {
        this.name = in.readString();
        this.description = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {

        @Override
        public Product createFromParcel(final Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(final int size) {
            return new Product[size];
        }
    };
}
