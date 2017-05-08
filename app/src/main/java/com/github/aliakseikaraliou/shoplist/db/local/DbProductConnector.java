package com.github.aliakseikaraliou.shoplist.db.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.aliakseikaraliou.shoplist.Application;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;

import java.util.List;

public class DbProductConnector implements IDbConnector<IProduct> {

    private final SQLiteDatabase database;

    public DbProductConnector(final Context context) {
        final DbHelper dbHelper = ((Application) context.getApplicationContext()).getDbHelper();
        database = dbHelper.getWritableDatabase();
    }

    public DbProductConnector(final SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public long put(final IProduct item) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.Product.NAME, item.getName());
        contentValues.put(DbConstants.Product.DESCRIPTION, item.getDescription());
        contentValues.put(DbConstants.Product.PRICE, item.getPrice());
        contentValues.put(DbConstants.Product.QUANTITY, item.getQuantity());
        final long raw = database.insert(DbConstants.Product.TABLE_NAME, null, contentValues);
        item.setId(raw);
        return raw;
    }

    @Override
    public List<IProduct> getAll() {
        // not needed
        return null;
    }

    @Override
    public int remove(final IProduct item) {
        final Long id = item.getId();
        if (id != null) {
            final int delete = database.delete(DbConstants.Product.TABLE_NAME, "id=" + id, null);
            new StringBuilder();
            return delete;
        } else {
            return 0;
        }
    }

}
