package com.github.aliakseikaraliou.shoplist;

import com.github.aliakseikaraliou.shoplist.db.local.DbHelper;

public class Application extends android.app.Application {

    private final DbHelper dbHelper;

    public Application() {
        dbHelper = new DbHelper(this, 1);

    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }
}
