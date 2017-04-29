package com.greenDaoGenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class Generator {

    private static final String PACKAGE_NAME = "com.github.aliakseikaraliou.shoplist.db";
    private static final int DB_VERSION = 1;
    private static final String PRODUCT = "Product";
    private static final String PRODUCT_NAME = "Name";
    private static final String PRODUCT_DESCRIPTION = "Description";
    private static final String PRODUCT_PRICE = "Price";
    private static final String PRODUCT_QUANTITY = "Quantity";
    private static final String DB_OUTPUT = "./app/src/main/java";
    private static final String PRODUCT_LIST = "ProductList";
    private static final String PRODUCT_LIST_NAME = "Name";

    public static void main(final String[] args) throws Exception {
        final Schema schema = new Schema(DB_VERSION, PACKAGE_NAME);

        generateProductEntity(schema);
        generateProductListEntity(schema);

        final DaoGenerator daoGenerator = new DaoGenerator();
        daoGenerator.generateAll(schema, DB_OUTPUT);

    }

    private static Entity generateProductListEntity(final Schema schema) {
        final Entity productListEntity = schema.addEntity(PRODUCT_LIST);
        productListEntity.addIdProperty();
        productListEntity.addStringProperty(PRODUCT_LIST_NAME);
        return productListEntity;
    }

    private static Entity generateProductEntity(final Schema schema) {
        final Entity productEntity = schema.addEntity(PRODUCT);

        productEntity.addIdProperty();
        productEntity.addStringProperty(PRODUCT_NAME).notNull();
        productEntity.addStringProperty(PRODUCT_DESCRIPTION);
        productEntity.addFloatProperty(PRODUCT_PRICE);
        productEntity.addIntProperty(PRODUCT_QUANTITY);
        return productEntity;
    }
}