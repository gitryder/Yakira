package com.realllydan.yakira.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

import javax.annotation.Nullable;

public class HomeFragment extends Fragment implements
        MemberListAdapter.OnMemberClickListener,
        MemberListAdapter.OnCallClickListener {

    private static final int CALL_PERMISSION_REQUEST_CODE = 1;
    private static final int CALL_INTENT_REQUEST_CODE = 2;
    private static final String TAG = "HomeFragment";

    private FloatingActionButton fabAddMember;
    private View view;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private HomeFragmentListener homeFragmentListener;
    private IMainActivity iMainActivity;
    private ArrayList<Member> memberArrayList = new ArrayList<>();
    private MemberListAdapter memberListAdapter;

    public HomeFragment() {

    }

    @Override
    public void onMemberClick(int pos) {
        //TODO: View and Edit Member details

        String underDevelopment = "This feature is under development ;)";
        Snackbar.make(view, underDevelopment, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCallClick(int pos) {
        String memberContactNumber = memberArrayList.get(pos).getContact();

        if (memberContactNumber != null) {

            if (isPhoneCallPermissionGranted()) {
                makePhoneCallToMember(memberContactNumber);
            } else {
                requestPhoneCallPermission();
            }

        } else {
            Snackbar.make(view, R.string.contact_null_error_message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(view, R.string.permission_granted_acknowledgement, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(view, R.string.permission_denied_acknowledgement, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iMainActivity = (IMainActivity) context;
        homeFragmentListener = (HomeFragmentListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
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
        memberListAdapter = new MemberListAdapter(memberArrayList, this, this);
        recyclerView.setAdapter(memberListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setupRecyclerViewData() {
        memberArrayList.clear();

        db.collection(Constants.Firestore.COLLECTION_MEMBERS)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        memberArrayList.clear();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Member member = documentSnapshot.toObject(Member.class);
                            Log.e(TAG, "onEvent: MEMBER DOCUMENT: " + documentSnapshot.getId());
                            memberArrayList.add(member);
                        }
                        memberListAdapter.notifyDataSetChanged();
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

    private boolean isPhoneCallPermissionGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPhoneCallPermission() {
        String[] permissionToRequest  = { Manifest.permission.CALL_PHONE };
        ActivityCompat.requestPermissions(getActivity(), permissionToRequest, CALL_PERMISSION_REQUEST_CODE);
    }

    private void makePhoneCallToMember(String memberContactNumber) {
        String dial = "tel:" + memberContactNumber;
        startActivityForResult(new Intent(Intent.ACTION_CALL, Uri.parse(dial)), CALL_INTENT_REQUEST_CODE);
    }

    public interface HomeFragmentListener {
        void onAddMemberButtonClicked();
    }
}
