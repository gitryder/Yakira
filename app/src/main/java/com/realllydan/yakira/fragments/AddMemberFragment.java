package com.realllydan.yakira.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.realllydan.yakira.Constants;
import com.realllydan.yakira.IMainActivity;
import com.realllydan.yakira.R;
import com.realllydan.yakira.Utils.Toaster;
import com.realllydan.yakira.data.models.Member;

import java.util.ArrayList;

public class AddMemberFragment extends Fragment {

    private ProgressBar progressBar;
    private View view;
    private Context context;

    private IMainActivity iMainActivity;
    private Toaster toaster;

    //testing
    private ArrayList<Member> allMembers = new ArrayList<>();

    public AddMemberFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        iMainActivity = (IMainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_member, container, false);
        setupUi();

        toaster = new Toaster(context);

        return view;
    }

    private void setupUi() {
        iMainActivity.setToolbarTitle(R.string.add_member_fragment_title);
        setupProgressBar();
        setupAddMemberButton();
    }

    private void setupProgressBar() {
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void setupAddMemberButton() {
        MaterialButton bAddMember = view.findViewById(R.id.bAddMember);

        bAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMemberToDatabase();
            }
        });
    }

    private Member getDataFromFieldsAsMemberObject() {
        //Setup Fields
        TextInputLayout tilName = view.findViewById(R.id.tilName);
        TextInputLayout tilPhone = view.findViewById(R.id.tilPhone);
        ChipGroup cgMemberType = view.findViewById(R.id.cgMemberType);
        Chip selectedChip = view.findViewById(cgMemberType.getCheckedChipId());

        //Get Data from the Fields
        String memberName = tilName.getEditText().getText().toString().trim();
        String memberPhone = tilPhone.getEditText().getText().toString().trim();
        String memberType = selectedChip.getText().toString();

        //Validate the data and return the corresponding result
        if (!memberName.isEmpty() && !memberPhone.isEmpty()) {
            return new Member(memberName, memberPhone, memberType);
        } else {
            return null;
        }
    }

    private void addAllDataToTestingArrayList() {

    }

    private void addMemberToDatabase() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        Member member = getDataFromFieldsAsMemberObject();

        if (member != null) {
            showProgressBar();

            db.child(Constants.Firebase.MEMBERS)
                    .push()
                    .setValue(member)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                hideProgressBar();

                                iMainActivity.onFragmentBackPressed();
                            } else {
                                hideProgressBar();

                                toaster.displayAToast(task.getException().getMessage());
                                iMainActivity.onFragmentBackPressed();
                            }
                        }
                    });
        }
    }

}
