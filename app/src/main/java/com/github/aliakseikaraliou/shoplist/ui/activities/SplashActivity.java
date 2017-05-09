package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.github.aliakseikaraliou.shoplist.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            final Button loginButton = (Button) findViewById(R.id.activity_splash_login);
            final Button registerButton = (Button) findViewById(R.id.activity_splash_register);
        } else {
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
