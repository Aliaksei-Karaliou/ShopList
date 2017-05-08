package com.github.aliakseikaraliou.shoplist.db.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.aliakseikaraliou.shoplist.db.local.DbConstants;

public class DbHelper extends SQLiteOpenHelper {

    private static final String SHOPLIST_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS \'" + DbConstants.ProductList.TABLE_NAME + "\' (\'id\'INTEGER PRIMARY KEY AUTOINCREMENT, \'" + DbConstants.ProductList.TITLE + "\' TEXT);";
    private static final String DB_NAME = "Shoplist";
    private static final String PRODUCT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS \'" + DbConstants.Product.TABLE_NAME + "\' (\'id\' INTEGER PRIMARY KEY AUTOINCREMENT, \'" + DbConstants.Product.NAME + "\' TEXT,\'" + DbConstants.Product.DESCRIPTION + "\' TEXT, \'" + DbConstants.Product.PRICE + "\' FLOAT, \'" + DbConstants.Product.QUANTITY + "\' FLOAT);";
    private static final String PRODUCT_PRODUCTLIST_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS \'" + DbConstants.Product_ProductList.TABLE_NAME + "\' (\'" + DbConstants.Product_ProductList.PRODUCT_LIST_ID + "\' INTEGER, \'" + DbConstants.Product_ProductList.PRODUCT_ID + "\' INTEGER)";

    public DbHelper(final Context context, final int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(SHOPLIST_TABLE_CREATE);
        db.execSQL(PRODUCT_TABLE_CREATE);
        db.execSQL(PRODUCT_PRODUCTLIST_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }
}
