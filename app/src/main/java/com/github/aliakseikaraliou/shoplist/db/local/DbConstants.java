package com.github.aliakseikaraliou.shoplist.db.local;

public interface DbConstants {

    interface ProductList {

        String TABLE_NAME = "PRODUCTLIST";
        String TITLE = "title";
    }

    interface Product {

        String TABLE_NAME = "PRODUCT";
        String NAME = "name";
        String DESCRIPTION = "description";
        String PRICE = "price";
        String QUANTITY = "quantity";
    }

    interface Product_ProductList {

        String TABLE_NAME = "PRODUCT_PRODUCTLIST";
        String PRODUCT_LIST_ID = "PRODUCTLIST_id";
        String PRODUCT_ID = "PRODUCT_id";
    }
}
