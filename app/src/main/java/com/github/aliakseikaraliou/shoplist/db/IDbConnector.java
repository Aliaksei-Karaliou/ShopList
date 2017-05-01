package com.github.aliakseikaraliou.shoplist.db;

import java.util.List;

public interface IDbConnector<T> {

    long put(T item);

    List<T> getAll();
}
