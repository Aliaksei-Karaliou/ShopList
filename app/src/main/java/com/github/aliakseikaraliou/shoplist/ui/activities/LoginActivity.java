package com.github.aliakseikaraliou.shoplist.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.aliakseikaraliou.shoplist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        final EditText emailEditText = (EditText) findViewById(R.id.activity_login_email);
        final EditText passwordEditText = (EditText) findViewById(R.id.activity_login_password);

        final Button submitButton = (Button) findViewById(R.id.activity_login_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                auth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.activity_login_firebasefail, Toast.LENGTH_SHORT).show();
                        } else {
                            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.activity_login_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                onBackPressed();
            }
        });
    }
}
