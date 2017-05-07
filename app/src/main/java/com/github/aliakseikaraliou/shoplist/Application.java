package com.github.aliakseikaraliou.shoplist;

import android.content.Intent;

import com.github.aliakseikaraliou.shoplist.db.DbHelper;
import com.github.aliakseikaraliou.shoplist.services.FirebaseMessagingService;

public class Application extends android.app.Application {

    private final DbHelper dbHelper;

    public Application() {
        dbHelper = new DbHelper(this, 1);

    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }
}
