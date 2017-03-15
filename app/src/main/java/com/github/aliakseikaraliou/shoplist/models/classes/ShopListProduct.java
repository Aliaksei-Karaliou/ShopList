package com.github.aliakseikaraliou.shoplist.models.classes;

import android.os.Parcel;

import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IShopListProduct;

public class ShopListProduct implements IShopListProduct {

    private final IProduct product;
    private final String imageUrl;

    public ShopListProduct(final IProduct product, final String imageUrl) {
        this.product = product;
        this.imageUrl = imageUrl;
    }

    @Override
    public IProduct getProduct() {
        return product;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof IShopListProduct && product.equals(((IShopListProduct) obj).getProduct());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(this.product, flags);
        dest.writeString(this.imageUrl);
    }

    private ShopListProduct(final Parcel in) {
        this.product = in.readParcelable(IProduct.class.getClassLoader());
        this.imageUrl = in.readString();
    }

    public static final Creator<ShopListProduct> CREATOR = new Creator<ShopListProduct>() {

        @Override
        public ShopListProduct createFromParcel(final Parcel source) {
            return new ShopListProduct(source);
        }

        @Override
        public ShopListProduct[] newArray(int size) {
            return new ShopListProduct[size];
        }
    };
}
