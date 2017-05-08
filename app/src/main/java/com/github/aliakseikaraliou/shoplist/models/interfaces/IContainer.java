package com.github.aliakseikaraliou.shoplist.models.interfaces;

public interface IContainer {

    IProductList getProductList();

    IUser getSender();

    IUser getReceiver();
}
