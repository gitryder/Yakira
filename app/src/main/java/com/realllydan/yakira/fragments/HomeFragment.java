package com.realllydan.yakira.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.realllydan.yakira.Constants;
import com.realllydan.yakira.IMainActivity;
import com.realllydan.yakira.R;
import com.realllydan.yakira.activities.LoginActivity;
import com.realllydan.yakira.activities.MainActivity;
import com.realllydan.yakira.data.MemberListAdapter;
import com.realllydan.yakira.data.models.Member;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements
        MemberListAdapter.OnMemberClickListener,
        MemberListAdapter.OnCallClickListener {

    private static final String TAG = "HomeFragment";

    private FloatingActionButton fabAddMember;
    private View view;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private HomeFragmentListener homeFragmentListener;
    private IMainActivity iMainActivity;
    private ArrayList<Member> memberArrayList = new ArrayList<>();

    public HomeFragment() {

    }

    @Override
    public void onMemberClick(int pos) {
        String clickedMemberName = memberArrayList.get(pos).getName();
        Snackbar.make(view, clickedMemberName, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCallClick(int pos) {
        String clickedMemberName = memberArrayList.get(pos).getName();
        Snackbar.make(view, "Calling " + clickedMemberName + "...", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iMainActivity = (IMainActivity) context;
        homeFragmentListener = (HomeFragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();

        iMainActivity.setToolbarTitle(R.string.home_fragment_title);

        setupUi();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth != null) {
            if (mAuth.getCurrentUser() == null) {
                navigateToLoginActivity();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
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

    private void setupUi() {
        setupAddNewMemberButton();
        setupRecyclerView();
    }

    private void setupAddNewMemberButton() {
        fabAddMember = view.findViewById(R.id.fabAddMember);
        fabAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragmentListener.onAddMemberButtonClicked();
            }
        });
    }

    private void setupRecyclerView() {
        setupRecyclerViewData();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        MemberListAdapter memberListAdapter = new MemberListAdapter(memberArrayList, this, this);
        recyclerView.setAdapter(memberListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupRecyclerViewData() {
        memberArrayList.clear();

        /*
        Dummy Data

        memberArrayList.add(new Member("Danyl Fernandes", "Team"));
        memberArrayList.add(new Member("Joel Fernandes", "Team"));
        memberArrayList.add(new Member("Carol Fernandes", "General"));
        memberArrayList.add(new Member("John Doe", "General", "Mar 16", "Cara"));
        memberArrayList.add(new Member("Sarah Williams", "General"));
        memberArrayList.add(new Member("Joe Bider", "Team", "Jan 4", "Peter"));
        memberArrayList.add(new Member("Sean Parker", "General"));
        */

        db.collection(Constants.FIRESTORE_COLLECTION_MEMBERS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot data : queryDocumentSnapshots) {
                            Member member = data.toObject(Member.class);
                            Log.d(TAG, "onSuccess: Member Name: ");
                        }
                    }
                });
    }

    private void logoutUser() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        navigateToLoginActivity();
    }

    private void navigateToLoginActivity() {
        Intent loginActivityIntent = new Intent(getActivity(), LoginActivity.class);
        loginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginActivityIntent);
    }

    public interface HomeFragmentListener {
        void onAddMemberButtonClicked();
    }
}
