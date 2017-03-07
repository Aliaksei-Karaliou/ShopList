package com.github.aliakseikaraliou.shoplist.models.classes;

import android.os.Parcel;

import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;

public final class Product implements IProduct {

    private String name;
    private String description;
    private Number price;
    private Number quantity = 1;

    private Product(final String name, final String description, final Number price, final Number quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
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

    public Number getPrice() {
        return price;
    }

    public void setPrice(final Number price) {
        this.price = price;
    }

    public Number getQuantity() {
        return quantity;
    }

    public void setQuantity(final Number quantity) {
        this.quantity = quantity;
    }

    public static class Builder {

        private String name;
        private String description = "";
        private Double price;
        private Number quantity = 1;

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

        public Builder setQuantity(final Number quantity) {
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
        public Product createFromParcel(final Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(final int size) {
            return new Product[size];
        }
    };
}
