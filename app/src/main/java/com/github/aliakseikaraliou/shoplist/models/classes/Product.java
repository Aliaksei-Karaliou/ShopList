package com.github.aliakseikaraliou.shoplist.models.classes;

import android.os.Parcel;

import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;

import java.util.Objects;

public final class Product implements IProduct {

    private String name;
    private String description;
    private double price;
    private double quantity = 1;
    private Long id;

    private Product(final String name, final String description, final double price, final double quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final long id) {
        if (this.id == null) {
            this.id = id;
        }
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

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(final double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof IProduct && name.equals(((IProduct) obj).getName());
    }

    public static class Builder {

        private String name;
        private String description = "";
        private double price = 0;
        private double quantity = 1;

        public Builder(final String name) {
            this.name = name;
        }

        public Builder setName(final String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(final String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(final Double price) {
            this.price = price;
            return this;
        }

        public Builder setQuantity(final double quantity) {
            this.quantity = quantity;
            return this;
        }

        public IProduct build() {
            return new Product(name, description, price, quantity);
        }
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

    public static final Creator<IProduct> CREATOR = new Creator<IProduct>() {

        @Override
        public IProduct createFromParcel(final Parcel source) {
            return new Product(source);
        }

        @Override
        public IProduct[] newArray(final int size) {
            return new Product[size];
        }
    };
}
