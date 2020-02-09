package com.realllydan.achim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        setupToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
        if (mAuth != null) {
            if (mAuth.getCurrentUser() == null) {
                navigateToLoginActivity();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Log.d(TAG, "onOptionsItemSelected: logout");
                logoutUser();
            default:
                break;
        }

        return false;
    }

    private void setupToolbar() {
        Log.d(TAG, "setupToolbar: called");
        getSupportActionBar().setTitle(R.string.main_toolbar_title);
    }

    private void logoutUser() {
        Log.d(TAG, "logoutUser: logged out");
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        navigateToLoginActivity();
    }

    private void navigateToLoginActivity() {
        Log.d(TAG, "navigateToLoginActivity: called");
        Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginActivityIntent);
    }
}
