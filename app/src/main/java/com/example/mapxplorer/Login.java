package com.example.mapxplorer;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mapxplorer.User.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {
    Button regiter;
    Button login;
    Button viewer;
    ConstraintLayout constraintLayout;
    ValueEventListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listener = new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot snapshot) {
                for( DataSnapshot snapuser : snapshot.getChildren()){
                    DataBase.users.add(snapuser.getValue(User.class));
                    ArrayList<Market> markets = new ArrayList<>();
                    System.out.println(markets);
                    for(DataSnapshot dataSnapshot : snapuser.child("markets").getChildren()){
                        markets.add(dataSnapshot.getValue(Market.class));
                    }
                    System.out.println("after" + markets);
                    DataBase.users.get(DataBase.users.size()-1).setMarkets(markets);
                    DataBase.online_markets.addAll(markets);
                }
                System.out.println( DataBase.users.toString());
            }

            @Override public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        constraintLayout = findViewById(R.id.loginLayout);
        regiter = findViewById(R.id.register);
        login = findViewById(R.id.login);
        viewer = findViewById(R.id.viewer);
        DataBase.auth = FirebaseAuth.getInstance();
        DataBase.database = FirebaseDatabase.getInstance();
        DataBase.reference = DataBase.database.getReference("Users");
        DataBase.reference.addValueEventListener(listener);

        login.setOnClickListener(v -> login());
        regiter.setOnClickListener(v -> showRegisterCard());
        viewer.setOnClickListener(v -> nextActivity());

        MaterialEditText email = new MaterialEditText(this) ;
        email.setText("qq@g.com");
        MaterialEditText password = new MaterialEditText(this);
        password.setText("qweqwe");


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

        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
            dialog1.dismiss();
        });
        dialog.setPositiveButton("Confirm", (dialogInterface, which) -> {
            if(TextUtils.isEmpty(email.toString())){
                Snackbar.make(constraintLayout,"Input email",Snackbar.LENGTH_SHORT).show();
                return;
            }

            if(Objects.requireNonNull(password.getText()).toString().length() < 5){
                Snackbar.make(constraintLayout,"Input password more than 5 symbols",Snackbar.LENGTH_SHORT).show();
                return;
            }
            DataBase.online_markets = new ArrayList<>();
            // LogIn User from DB
            DataBase.auth.signInWithEmailAndPassword(
                    Objects.requireNonNull(email.getText()).toString(),password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        DataBase.reference.addValueEventListener(listener);
                        for(User user : DataBase.users){
                            if(user.getEmail().equals(email.getText().toString())){
                                DataBase.user = user;
                                break;
                            }
                        }
                        Intent intent = new Intent(Login.this, MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> {
                Snackbar.make(constraintLayout,"Incorrect pass or email",Snackbar.LENGTH_LONG).show();
            });
        });
        dialog.show();
    }
    private void showRegisterCard() {
        System.out.println("register");
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registration");
        dialog.setMessage("Input on these inputs");
        LayoutInflater inflater = LayoutInflater.from(this);
        View registerWindow = inflater.inflate(R.layout.register_window,null);
        dialog.setView(registerWindow);

        MaterialEditText email = registerWindow.findViewById(R.id.emailInput);
        MaterialEditText password = registerWindow.findViewById(R.id.passwordInput);
        MaterialEditText name = registerWindow.findViewById(R.id.nameInput);

        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
           dialog1.dismiss();
        });

        dialog.setPositiveButton("Confirm", (dialogInterface, which) -> {
            // checking inputs
            if(TextUtils.isEmpty(email.toString())){
                Snackbar.make(constraintLayout,"Input email",Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(name.toString())){
                Snackbar.make(constraintLayout,"Input name",Snackbar.LENGTH_SHORT).show();
                return;
            }
            if(Objects.requireNonNull(password.getText()).toString().length() < 5){
                Snackbar.make(constraintLayout,"Input password more than 5 symbols",Snackbar.LENGTH_SHORT).show();
                return;
            }

            // Registration user in DataBase

            DataBase.auth.createUserWithEmailAndPassword(Objects.requireNonNull(email.getText()).toString(),password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();
                        user.setName(Objects.requireNonNull(name.getText()).toString());
                        user.setEmail(email.getText().toString());
                        user.setPassword(password.getText().toString());
                        ArrayList<Market> markets = new ArrayList<>();
                        user.setMarkets(markets);
                        DataBase.reference.child(Objects.requireNonNull(
                                FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user)
                                .addOnSuccessListener(unused -> {
                            Snackbar.make(constraintLayout,"Success",Snackbar.LENGTH_SHORT).show();
                            //FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                        });
                    });
        });
        dialog.show();
    }
    private void nextActivity(){
        DataBase.auth.signInAnonymously();
        Intent intent = new Intent(Login.this,MapsActivity.class);
        finish();
        startActivity(intent);
    }
}