package com.github.aliakseikaraliou.shoplist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.aliakseikaraliou.shoplist.Application;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;

public class DbProductListConnector implements IDbConnector<IProductList> {

    private final SQLiteDatabase database;

    public DbProductListConnector(final Context context) {
        final DbHelper dbHelper = ((Application) context.getApplicationContext()).getDbHelper();
        database = dbHelper.getWritableDatabase();
        Log.i("MYSQL:", "DbProductListConnector: constructor");
    }

    @Override
    public long put(final IProductList item) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.ShopList.TITLE, item.getTitle());
        Log.i("MYSQL:", "DbProductListConnector: put");
        return database.insert(DbConstants.ShopList.TABLE_NAME, null, contentValues);
    }
}
