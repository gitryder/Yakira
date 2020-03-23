package com.realllydan.yakira.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realllydan.yakira.Constants;
import com.realllydan.yakira.IMainActivity;
import com.realllydan.yakira.R;
import com.realllydan.yakira.UserDetailsProvider;
import com.realllydan.yakira.Utils;
import com.realllydan.yakira.Utils.Toaster;
import com.realllydan.yakira.data.MemberListAdapter;
import com.realllydan.yakira.data.models.CallRecord;
import com.realllydan.yakira.data.models.Member;
import com.realllydan.yakira.data.models.User;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements
        MemberListAdapter.OnMemberClickListener,
        MemberListAdapter.OnCallClickListener,
        UserDetailsProvider.UserDetailsChangeListener {

    private static final int CALL_PERMISSION_REQUEST_CODE = 1;
    private static final int CALL_INTENT_REQUEST_CODE = 2;

    private FloatingActionButton fabAddMember;
    private View view;
    private Context context;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    private HomeFragmentListener homeFragmentListener;
    private IMainActivity iMainActivity;
    private String userName;
    private Toaster toaster;
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
        String memberId = memberArrayList.get(pos).getMemberId();

        if (memberContactNumber != null) {

            if (isPhoneCallPermissionGranted()) {
                makePhoneCallToMember(memberContactNumber, memberId);
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
        this.context = context;
        iMainActivity = (IMainActivity) context;
        homeFragmentListener = (HomeFragmentListener) context;
    }

    @Override
    public void onDetailsChanged(User user) {
        if (user != null) {
            if (user.getAccountType().equals(User.TYPE_ADMIN)) {
                setupAddNewMemberButton();
            } else if (user.getAccountType().equals(User.TYPE_GENERAL)){
                hideAddNewMemberButton();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        fabAddMember = view.findViewById(R.id.fabAddMember);
        iMainActivity.setToolbarTitle(R.string.home_fragment_title);

        new UserDetailsProvider(this);

        setupUi();

        return view;
    }

    private void setupUi() {
        setupRecyclerView();
    }

    private void showAddNewMemberButton() {
        fabAddMember.setVisibility(View.VISIBLE);
    }
    private void hideAddNewMemberButton() {
        fabAddMember.setVisibility(View.GONE);
    }

    private void setupAddNewMemberButton() {
        showAddNewMemberButton();

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
        db.child(Constants.Firebase.MEMBERS)
                .orderByChild("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        memberArrayList.clear();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Member member = data.getValue(Member.class);
                            memberArrayList.add(member);
                        }

                        memberListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        String databaseErrorMessage = databaseError.getMessage();
                        Snackbar.make(view, databaseErrorMessage, Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private boolean isPhoneCallPermissionGranted() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPhoneCallPermission() {
        String[] permissionToRequest = {Manifest.permission.CALL_PHONE};
        ActivityCompat.requestPermissions(getActivity(), permissionToRequest, CALL_PERMISSION_REQUEST_CODE);
    }

    private void makePhoneCallToMember(String memberContactNumber, String memberId) {
        String dial = "tel:" + memberContactNumber;
        startActivityForResult(new Intent(Intent.ACTION_CALL, Uri.parse(dial)), CALL_INTENT_REQUEST_CODE);

        addCallRecordForCurrentCall(memberId);
    }

    private void addCallRecordForCurrentCall(String memberId) {
        String date = Utils.Time.getDate();
        String time = Utils.Time.getTime();
        String year = Utils.Time.getYear();
        String author = userName;

        CallRecord callRecord = new CallRecord(date, time, year, author);

        //TODO: Upload a call record
    }

    public interface HomeFragmentListener {
        void onAddMemberButtonClicked();
    }
}
