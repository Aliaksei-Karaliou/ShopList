package com.github.aliakseikaraliou.shoplist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final String SHOPLIST_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS \'" + DbConstants.ShopList.TABLE_NAME + "\' (\'id\'INTEGER PRIMARY KEY AUTOINCREMENT, \'" + DbConstants.ShopList.TITLE + "\' TEXT);";
    private static final String DB_NAME = "Shoplist";
    private static final String TAG = "MYSQL";

    public DbHelper(final Context context, final int version) {
        super(context, DB_NAME, null, version);
        Log.i(TAG, SHOPLIST_TABLE_CREATE);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        Log.i(TAG, "onCreate");
        db.execSQL(SHOPLIST_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }
}
