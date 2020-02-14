package com.realllydan.yakira;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.realllydan.yakira.data.User;

public class RegisterActivity extends AppCompatActivity {

    private MaterialButton btnRegister;
    private ProgressBar progressBar;
    private TextInputLayout tilRegisterConfirmPassword, tilRegisterAccountType;
    private TextInputEditText etRegisterName, etRegisterEmail, etRegisterPassword, etRegisterConfirmPassword, etRegisterAccountType;
    private String name, email, password, confirmPassword, accountType;

    private FirebaseAuth mAuth;
    private DatabaseReference mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseDatabase.getInstance().getReference();

        tilRegisterConfirmPassword = findViewById(R.id.tilRegisterConfirmPassword);
        tilRegisterAccountType = findViewById(R.id.tilRegisterAccountType);
        etRegisterName = findViewById(R.id.etRegisterName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);
        etRegisterAccountType = findViewById(R.id.etRegisterAccountType);
        progressBar = findViewById(R.id.progressBar);
        btnRegister = findViewById(R.id.btnRegister);

        setupToolbar();
        setupProgressBar();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFieldsAndRegisterUser();
            }
        });
    }

    private void setupToolbar() {
        getSupportActionBar().setTitle(R.string.register_toolbar_title);
    }

    private void setupProgressBar() {
        hideProgressBar();
    }

    private void validateFieldsAndRegisterUser() {
        showProgressBar();
        bindFieldDataToStrings();

        if (!areFieldsEmpty()) {
            if (!arePasswordsMatching()) {
                hideProgressBar();
                tilRegisterConfirmPassword.setError(getString(R.string.error_unmatched_passwords));
            } else if (!isAccountTypeValid()) {
                hideProgressBar();
                tilRegisterAccountType.setError(getString(R.string.error_invalid_account_type));
            } else {
                registerUserWithValidatedDetails();
            }
        } else {
            hideProgressBar();
            makeToast(getString(R.string.error_unfilled_fields));
        }
    }

    private void bindFieldDataToStrings() {
        name = etRegisterName.getText().toString().trim();
        email = etRegisterEmail.getText().toString().trim();
        password = etRegisterPassword.getText().toString().trim();
        confirmPassword = etRegisterConfirmPassword.getText().toString().trim();
        accountType = etRegisterAccountType.getText().toString().trim();
    }

    private boolean areFieldsEmpty() {
        return (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                || accountType.isEmpty());
    }

    private boolean arePasswordsMatching() {
        return (confirmPassword.equals(password));
    }

    private boolean isAccountTypeValid() {
        return (accountType.equals("0") || accountType.equals("1"));
    }

    private void makeToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void logoutUser() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        navigateToLoginActivity();
    }

    private void navigateToLoginActivity() {
        Intent loginActivityIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginActivityIntent);
    }

    private void registerUserWithValidatedDetails() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(name, email, accountType);

                    mDb.child(getString(R.string.users_database_child)).child(name).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            hideProgressBar();
                            if (task.isSuccessful()) {
                                makeToast(getString(R.string.msg_register_success));
                                logoutUser();
                            } else {
                                makeToast(getString(R.string.error_register_failure));
                            }
                        }
                    });
                } else {
                    hideProgressBar();
                    makeToast(task.getException().getMessage());
                }
            }
        });
    }
}
