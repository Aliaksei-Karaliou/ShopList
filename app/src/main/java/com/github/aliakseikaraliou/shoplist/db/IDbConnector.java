package com.github.aliakseikaraliou.shoplist.db;

public interface IDbConnector<T> {

    long put(T item);
}
