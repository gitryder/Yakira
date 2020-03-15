package com.realllydan.yakira;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.realllydan.yakira.data.MemberListAdapter;
import com.realllydan.yakira.data.models.Member;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MemberListAdapter.OnMemberClickListener {

    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private ArrayList<Member> memberArrayList = new ArrayList<>();

    @Override
    public void onMemberClick(int pos) {
        String clickedMemberName = memberArrayList.get(pos).getName();
        Snackbar.make(mCoordinatorLayout, clickedMemberName, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDrawerLayout = findViewById(R.id.mainDrawerLayout);
        mCoordinatorLayout = findViewById(R.id.mainCoordinatorLayout);

        setupUi();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                logoutUser();
            default:
                break;
        }

        return false;
    }

    private void setupUi() {
        setupToolbar();
        setupNavigationDrawer();
        setupRecyclerView();
    }

    private void setupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.main_toolbar_title);
    }

    private void setupNavigationDrawer() {
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close
        );

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void setupRecyclerView() {
        setupRecyclerViewData();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MemberListAdapter memberListAdapter = new MemberListAdapter(memberArrayList, this);
        recyclerView.setAdapter(memberListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerViewData() {
        memberArrayList.add(new Member("Danyl Fernandes", "Team"));
        memberArrayList.add(new Member("Joel Fernandes", "Team"));
        memberArrayList.add(new Member("Carol Fernandes", "General"));
        memberArrayList.add(new Member("John Doe", "General"));
        memberArrayList.add(new Member("Sarah Williams", "General"));
        memberArrayList.add(new Member("Joe Bider", "Team"));
        memberArrayList.add(new Member("Sean Parker", "General"));
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
}
