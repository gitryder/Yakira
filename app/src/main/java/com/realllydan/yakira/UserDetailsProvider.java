package com.realllydan.yakira;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.realllydan.yakira.data.models.User;

public class UserDetailsProvider {

    private UserDetailsChangeListener userDetailsChangeListener;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public UserDetailsProvider(UserDetailsChangeListener userDetailsChangeListener) {
        this.userDetailsChangeListener = userDetailsChangeListener;

        retrieveUserDetailsFromFirebase();
    }

    private void retrieveUserDetailsFromFirebase() {
        db.child(Constants.Firebase.USERS)
                .child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        userDetailsChangeListener.onDetailsChanged(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        userDetailsChangeListener.onDetailsChanged(null);
                    }
                });
    }

    public interface UserDetailsChangeListener {
        void onDetailsChanged(User user);
    }
}
