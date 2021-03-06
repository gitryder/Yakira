package com.realllydan.yakira.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.realllydan.yakira.Utils.Toaster;
import com.realllydan.yakira.R;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton btnLogin, btnRegister;
    private ProgressBar progressBar;
    private Toolbar mToolbar;
    private TextInputEditText etLoginEmail, etLoginPassword;

    private String email, password;
    private Toaster toaster = new Toaster(this);
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
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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
            toaster.displayAToast(R.string.error_unfilled_fields);
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
                    toaster.displayAToast(task.getException().getMessage());
                }
            }
        });

    }

    private void navigateToMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainActivityIntent);
    }

    private boolean areFieldsEmpty() {
        return (email.isEmpty() || password.isEmpty());
    }

    private void bindFieldDataToStrings() {
        email = etLoginEmail.getText().toString().trim();
        password = etLoginPassword.getText().toString().trim();
    }

    private void navigateToRegisterActivity() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

}
