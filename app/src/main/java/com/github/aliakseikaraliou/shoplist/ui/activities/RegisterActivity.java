package com.github.aliakseikaraliou.shoplist.ui.activities;

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

import static android.util.Patterns.*;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        final EditText emailEditText = (EditText) findViewById(R.id.activity_register_email);
        final EditText passwordEditText = (EditText) findViewById(R.id.activity_register_password);
        final EditText confirmEditText = (EditText) findViewById(R.id.activity_register_passwordConfirm);

        final Button registerButton = (Button) findViewById(R.id.activity_register_submit);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                if (!passwordEditText.getText().toString().equals(confirmEditText.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, R.string.activity_login_passwordfail, Toast.LENGTH_SHORT).show();
                } else if (!EMAIL_ADDRESS.matcher(emailEditText.getText()).matches()) {
                    Toast.makeText(RegisterActivity.this, R.string.activity_login_emailfail, Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull final Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, R.string.activity_login_firebasefail, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.activity_register_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });
    }
}
