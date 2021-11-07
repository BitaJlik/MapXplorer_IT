package com.example.mapxplorer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mapxplorer.Market.Market;
import com.example.mapxplorer.User.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Objects;

public class Login extends AppCompatActivity {
    Button regiter;
    Button login;
    Button viewer;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ValueEventListener listener = new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot snapuser : snapshot.getChildren()){ // UiD User
                    User user = snapuser.getValue(User.class);
                    if(user == null) break;
                    DataBase.users.add(user); // adding in LOCAL DB
                    ArrayList<Market> markets = new ArrayList<>(); // Markets for user
                    // getting from ONLINE DB from user markets
                    for(DataSnapshot dataSnapshot : snapuser.child("markets").getChildren()){
                        Market market = dataSnapshot.getValue(Market.class);
                        markets.add(market);
                    }
                    user.setMarkets(markets);
                }
                for(int i = 0;i < DataBase.users.size();i++){
                    System.out.println(DataBase.users.get(i));
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        };
        DataBase.reference.addValueEventListener(listener);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        constraintLayout = findViewById(R.id.loginLayout);
        regiter = findViewById(R.id.register);
        login = findViewById(R.id.login);
        viewer = findViewById(R.id.viewer);

        login.setOnClickListener(v -> login());
        regiter.setOnClickListener(v -> showRegisterCard());
        viewer.setOnClickListener(v -> nextActivity());


    }
    private void login(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("LogIn");
        dialog.setMessage("Input on these inputs");
        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.login_window,null);
        dialog.setView(registerWindow);

        MaterialEditText email = registerWindow.findViewById(R.id.emailInput);
        MaterialEditText password = registerWindow.findViewById(R.id.passwordInput);

        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());
        dialog.setPositiveButton("Confirm", (dialogInterface, which) -> {
            if(TextUtils.isEmpty(email.toString())){
                Snackbar.make(constraintLayout,"Input email",Snackbar.LENGTH_SHORT).show();
                return;
            }

            if(Objects.requireNonNull(password.getText()).toString().length() < 5){
                Snackbar.make(constraintLayout,"Input password more than 5 symbols",Snackbar.LENGTH_SHORT).show();
                return;
            }
            // LogIn User from DB
            DataBase.auth.signInWithEmailAndPassword(
                    Objects.requireNonNull(email.getText()).toString(),password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        for(User user : DataBase.users){
                            if(user.getEmail().equals(email.getText().toString())){
                                DataBase.ActiveSessionUser = user;
                                break;
                            }
                        }
                        Intent intent = new Intent(Login.this, MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> Snackbar.make(constraintLayout,"Incorrect pass or email",Snackbar.LENGTH_LONG).show());
        });
        dialog.show();
    }
    private void showRegisterCard() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Registration");
        dialog.setMessage("Input on these inputs");

        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_window,null);
        dialog.setView(registerWindow);

        MaterialEditText email = registerWindow.findViewById(R.id.emailInput);
        MaterialEditText password = registerWindow.findViewById(R.id.passwordInput);
        MaterialEditText name = registerWindow.findViewById(R.id.nameInput);

        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());

        dialog.setPositiveButton("Confirm", (dialogInterface, which) -> {
            // checking inputs
            {
                if (TextUtils.isEmpty(email.toString())) {
                    Snackbar.make(constraintLayout, "Input email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name.toString())) {
                    Snackbar.make(constraintLayout, "Input name", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (Objects.requireNonNull(password.getText()).toString().length() < 5) {
                    Snackbar.make(constraintLayout, "Input password more than 5 symbols", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }

            // Registration user in DataBase
            DataBase.auth.createUserWithEmailAndPassword(
                Objects.requireNonNull(email.getText()).toString(),password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();
                        user.setName(Objects.requireNonNull(name.getText()).toString());
                        user.setEmail(email.getText().toString());
                        user.setPassword(password.getText().toString());
                        DataBase.reference.child(Objects.requireNonNull(
                                FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user)
                                    .addOnSuccessListener(unused -> {
                                Snackbar.make(constraintLayout,
                                        "Success register\nVerify Email please",Snackbar.LENGTH_SHORT).show();
                                //FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                            });
                    });
        });
        dialog.show();
    }
    private void nextActivity(){
        // Viewer
        DataBase.auth.signInAnonymously();
        Intent intent = new Intent(Login.this,MapsActivity.class);
        finish();
        startActivity(intent);
    }
}