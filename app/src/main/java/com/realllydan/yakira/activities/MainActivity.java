package com.realllydan.yakira.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.realllydan.yakira.IMainActivity;
import com.realllydan.yakira.R;
import com.realllydan.yakira.fragments.AddMemberFragment;
import com.realllydan.yakira.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.HomeFragmentListener,
        IMainActivity {

    private int flFragmentContainerId = R.id.fragmentContainer;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    public void onAddMemberButtonClicked() {
        inflateAddMemberFragment();
    }

    @Override
    public void setToolbarTitle(int title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onFragmentBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth != null) {
            if (mAuth.getCurrentUser() == null) {
                navigateToLoginActivity();
            } else {
                inflateHomeFragment();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
            default:
                break;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        disallowBackPressIfHomeFragment();
    }

    private void setupUi() {
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void disallowBackPressIfHomeFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(flFragmentContainerId);

        if (!(fragment instanceof HomeFragment)) {
            super.onBackPressed();
        }
    }

    private void logoutUser() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        navigateToLoginActivity();
    }

    private void navigateToLoginActivity() {
        Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginActivityIntent);
    }

    private void inflateHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(flFragmentContainerId, new HomeFragment())
                .addToBackStack(null)
                .commit();
    }

    private void inflateAddMemberFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(flFragmentContainerId, new AddMemberFragment())
                .addToBackStack(null)
                .commit();
    }
}
