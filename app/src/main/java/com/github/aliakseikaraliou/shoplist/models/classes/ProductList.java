package com.github.aliakseikaraliou.shoplist.models.classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ProductList implements IProductList {

    private String title;
    private final List<IProduct> productList;
    private Long id = null;

    public ProductList(final String title) {
        this.title = title;
        this.productList = new ArrayList<>();
    }

    public ProductList(final String title, final List<IProduct> productList) {
        this.title = title;
        this.productList = new ArrayList<>(productList);
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
    public int size() {
        return productList.size();
    }

    @Override
    public boolean isEmpty() {
        return productList.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return productList.contains(o);
    }

    @NonNull
    @Override
    public Iterator<IProduct> iterator() {
        return productList.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return productList.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull final T[] a) {
        return a;
    }

    @Override
    public boolean add(final IProduct product) {
        return productList.add(product);
    }

    @Override
    public boolean remove(final Object o) {
        return productList.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull final Collection<?> c) {
        return productList.containsAll(c);
    }

    @Override
    public boolean addAll(@NonNull final Collection<? extends IProduct> c) {
        return productList.addAll(c);
    }

    @Override
    public boolean addAll(final int index, @NonNull final Collection<? extends IProduct> c) {
        return productList.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NonNull final Collection<?> c) {
        return productList.removeAll(c);
    }

    @Override
    public boolean retainAll(@NonNull final Collection<?> c) {
        return productList.retainAll(c);
    }

    @Override
    public void clear() {
        productList.clear();
    }

    @Override
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
    public IProduct set(final int index, final IProduct element) {
        return productList.set(index, element);
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
    public int indexOf(final Object o) {
        return productList.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return productList.lastIndexOf(o);
    }

    @Override
    public ListIterator<IProduct> listIterator() {
        return productList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<IProduct> listIterator(final int index) {
        return productList.listIterator(index);
    }

    @NonNull
    @Override
    public List<IProduct> subList(final int fromIndex, final int toIndex) {
        return productList.subList(fromIndex, toIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.title);
        dest.writeTypedList(this.productList);
    }

    private ProductList(final Parcel in) {
        this.title = in.readString();
        this.productList = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Parcelable.Creator<ProductList> CREATOR = new Parcelable.Creator<ProductList>() {

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

