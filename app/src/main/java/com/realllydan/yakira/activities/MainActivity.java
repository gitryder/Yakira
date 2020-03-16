package com.realllydan.yakira.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.realllydan.yakira.IMainActivity;
import com.realllydan.yakira.R;
import com.realllydan.yakira.fragments.AddMemberFragment;
import com.realllydan.yakira.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.HomeFragmentListener,
        IMainActivity {

    private int flFragmentContainerId = R.id.fragmentContainer;

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
        inflateHomeFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUi();
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
