package com.example.mapxplorer.User;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GetCurrentUserFromDB {
    private static final String TAG = "MainActivity";
    private static FirebaseDatabase mDatabase;
    private static DatabaseReference mDbRef;
    private static String userId;
    public static void getUser(){
        mDatabase = FirebaseDatabase.getInstance();
        mDbRef = mDatabase.getReference("Users/name");
        mDbRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError error) {
// Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
