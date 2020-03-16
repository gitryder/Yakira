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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.realllydan.yakira.Constants;
import com.realllydan.yakira.R;
import com.realllydan.yakira.data.models.User;

public class RegisterActivity extends AppCompatActivity {

    private MaterialButton btnRegister;
    private ProgressBar progressBar;
    private Toolbar mToolbar;
    private TextInputLayout tilRegisterConfirmPassword, tilRegisterAccountType;
    private TextInputEditText etRegisterName, etRegisterEmail, etRegisterPassword, etRegisterConfirmPassword, etRegisterAccountType;
    private String name, email, password, confirmPassword, accountType;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

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
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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

    private void addUserDetailsToDatabase() {
        User user = new User(name, email, accountType);

        db.collection(Constants.FIRESTORE_COLLECTION_USERS).add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        hideProgressBar();
                        makeToast(getString(R.string.msg_register_success));
                        logoutUser();
                    }
                });
    }

    private void registerUserWithValidatedDetails() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        addUserDetailsToDatabase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressBar();
                        makeToast(e.getMessage());
                    }
                });
    }
}
