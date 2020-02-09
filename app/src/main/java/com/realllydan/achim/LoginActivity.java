package com.realllydan.achim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton btnLogin, btnRegister;
    private ProgressBar progressBar;
    private TextInputEditText etLoginEmail, etLoginPassword;

    private String email, password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnNavToRegister);
        progressBar = findViewById(R.id.progressBar);

        setupToolbar();
        setupProgressBar();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFieldsAndRegisterUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegisterActivity();
            }
        });
    }

    private void setupToolbar() {
        getSupportActionBar().setTitle(R.string.login_toolbar_title);
    }

    private void setupProgressBar() {
        hideProgressBar();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void validateFieldsAndRegisterUser() {
        showProgressBar();
        bindFieldDataToStrings();

        if (!areFieldsEmpty()) {
            loginUserWithValidatedCredentials();
        } else {
            hideProgressBar();
            makeToast(getString(R.string.error_unfilled_fields));
        }
    }

    private void loginUserWithValidatedCredentials() {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                hideProgressBar();
                if (task.isSuccessful()) {
                    navigateToMainActivity();
                    finish();
                } else {
                    makeToast(task.getException().getMessage());
                }
            }
        });

    }

    private void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, LoginActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivityIntent);
    }

    private boolean areFieldsEmpty() {
        return (email.isEmpty() || password.isEmpty());
    }

    private void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void bindFieldDataToStrings() {
        email = etLoginEmail.getText().toString().trim();
        password = etLoginPassword.getText().toString().trim();
    }

    private void navigateToRegisterActivity() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

}
