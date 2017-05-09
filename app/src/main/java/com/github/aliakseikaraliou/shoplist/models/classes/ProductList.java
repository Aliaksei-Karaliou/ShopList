package com.github.aliakseikaraliou.shoplist.models.classes;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProductList implements IProductList {

    private String title;
    private List<IProduct> productList;
    @Exclude
    private transient String id;

    @SuppressWarnings("unused")
    public ProductList() {
        this("");
    }

    public ProductList(final String title) {
        this.title = title;
        this.productList = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public ProductList(final String title, final List<IProduct> productList) {
        this.title = title;
        this.productList = new ArrayList<>(productList);
    }

    @Override
    public List<IProduct> getList() {
        return productList;
    }

    public void setList(@SuppressWarnings("TypeMayBeWeakened") final List<Product> productList) {
        final List<IProduct> arrayList = new ArrayList<>();
        arrayList.addAll(productList);
        this.productList = arrayList;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    @Exclude
    public String getId() {
        return id;
    }

    @Override
    @Exclude
    public void setId(@NonNull final String id) {
        if (this.id == null) {
            this.id = id;
        }
    }

//    public String toJson() {
//        final Gson gson = new GsonBuilder().create();
//        final ProductList products = new ProductList(this.title, this.productList);
//        return gson.toJson(products);
//    }

    @Override
    public int size() {
        return productList.size();
    }

    @Override
    public boolean add(final IProduct product) {
        return productList.add(product);
    }

    @Override
    public IProduct set(final int index, final IProduct element) {
        return getList().set(index, element);
    }

    public boolean equals(final Object o) {
        return o instanceof IProductList && productList.equals(o);
    }

    @Override
    public int hashCode() {
        return productList.hashCode();
    }

    @Override
    public IProduct get(final int index) {
        return productList.get(index);
    }

    @Override
    public void add(final int index, final IProduct element) {
        productList.add(index, element);
    }

    @Override
    public IProduct remove(final int index) {
        return productList.remove(index);
    }

    @Override
    public boolean addAll(@NonNull final Collection<? extends IProduct> c) {
        return getList().addAll(c);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.title);
        dest.writeTypedList(this.productList);
        dest.writeString(this.id);
    }

    @SuppressWarnings("unchecked")
    private ProductList(final Parcel in) {
        this.title = in.readString();
        this.productList = new ArrayList<>();
        this.productList.addAll(in.createTypedArrayList(Product.CREATOR));
        this.id = in.readString();
    }

    public static final Creator<ProductList> CREATOR = new Creator<ProductList>() {
        @Override
        public ProductList createFromParcel(final Parcel source) {
            return new ProductList(source);
        }

        @Override
        public ProductList[] newArray(final int size) {
            return new ProductList[size];
        }
    };
}

