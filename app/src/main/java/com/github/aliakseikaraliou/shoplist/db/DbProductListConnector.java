package com.github.aliakseikaraliou.shoplist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.aliakseikaraliou.shoplist.Application;
import com.github.aliakseikaraliou.shoplist.models.classes.Product;
import com.github.aliakseikaraliou.shoplist.models.classes.ProductList;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProduct;
import com.github.aliakseikaraliou.shoplist.models.interfaces.IProductList;

import java.util.ArrayList;
import java.util.List;

public class DbProductListConnector implements IDbConnector<IProductList> {

    private final SQLiteDatabase database;
    private final DbProductConnector productConnector;

    public DbProductListConnector(final Context context) {
        final DbHelper dbHelper = ((Application) context.getApplicationContext()).getDbHelper();
        database = dbHelper.getWritableDatabase();
        productConnector = new DbProductConnector(database);
        Log.i("MYSQL:", "DbProductListConnector: constructor");
    }

    @Override
    public long put(final IProductList item) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.ProductList.TITLE, item.getTitle());
        Log.i("MYSQL:", "DbProductListConnector: put");
        final long productListRow = database.insert(DbConstants.ProductList.TABLE_NAME, null, contentValues);
        item.setId(productListRow);
        for (final IProduct product : item) {
            final long productRow = productConnector.put(product);

            final ContentValues connectionCV = new ContentValues();
            connectionCV.put(DbConstants.Product_ProductList.PRODUCT_LIST_ID, productListRow);
            connectionCV.put(DbConstants.Product_ProductList.PRODUCT_ID, productRow);
            database.insert(DbConstants.Product_ProductList.TABLE_NAME, null, connectionCV);
        }
        return productListRow;
    }

    @Override
    public List<IProductList> getAll() {
        final Cursor cursor = database.query(DbConstants.ProductList.TABLE_NAME, null, null, null, null, null, null);

        final int titleIndex = cursor.getColumnIndex(DbConstants.ProductList.TITLE);
        final int idIndex = cursor.getColumnIndex("id");

        final List<IProductList> productLists = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                final String title = cursor.getString(titleIndex);
                final int id = cursor.getInt(idIndex);
                final IProductList productList = new ProductList(title);
                productList.setId(id);

                //get products
                final Cursor connectionCursor = database.query(DbConstants.Product_ProductList.TABLE_NAME, new String[]{DbConstants.Product_ProductList.PRODUCT_ID}, DbConstants.Product_ProductList.PRODUCT_LIST_ID + '=' + id, null, null, null, null);

                if (connectionCursor.moveToFirst()) {
                    final int productIndex = connectionCursor.getColumnIndex(DbConstants.Product_ProductList.PRODUCT_ID);
                    do {
                        final long productId = connectionCursor.getInt(productIndex);
                        final Cursor productCursor = database.query(DbConstants.Product.TABLE_NAME, null, "id=" + productId, null, null, null, null);
                        if (productCursor.moveToFirst()) {
                            final int productIdIndex = productCursor.getColumnIndex("id");
                            final int nameIndex = productCursor.getColumnIndex(DbConstants.Product.NAME);
                            final int descriptionIndex = productCursor.getColumnIndex(DbConstants.Product.DESCRIPTION);
                            final int priceIndex = productCursor.getColumnIndex(DbConstants.Product.PRICE);
                            final int quantityIndex = productCursor.getColumnIndex(DbConstants.Product.QUANTITY);

                            final IProduct product = new Product.Builder(productCursor.getString(nameIndex))
                                    .setDescription(productCursor.getString(descriptionIndex))
                                    .setPrice(productCursor.getDouble(priceIndex))
                                    .setQuantity(productCursor.getDouble(quantityIndex))
                                    .build();
                            product.setId(productIdIndex);
                            productList.add(product);
                        }
                        productCursor.close();
                    }
                    while (connectionCursor.moveToNext());
                }
                connectionCursor.close();
                productLists.add(productList);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return productLists;
    }

    @Override
    public int remove(final IProductList item) {
        final Long id = item.getId();
        for (final IProduct product : item) {
            productConnector.remove(product);
            database.delete(DbConstants.Product_ProductList.TABLE_NAME, DbConstants.Product_ProductList.PRODUCT_LIST_ID + "=" + id, null);
        }
        if (id != null) {
            return database.delete(DbConstants.ProductList.TABLE_NAME, "id=" + id, null);
        }
        return 0;
    }
}
