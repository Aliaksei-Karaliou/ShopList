package com.github.aliakseikaraliou.shoplist.db.firebase;

import com.google.firebase.database.DatabaseError;

public interface OnDataChanged<T> {

    void onChange(T data);

    void onError(DatabaseError error);
}
