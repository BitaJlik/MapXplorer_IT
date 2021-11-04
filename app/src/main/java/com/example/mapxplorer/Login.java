package com.example.mapxplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mapxplorer.User.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Map;
import java.util.Objects;

public class Login extends AppCompatActivity {
    Button regiter;
    Button login;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        constraintLayout = findViewById(R.id.loginLayout);
        regiter = findViewById(R.id.register);
        login = findViewById(R.id.login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        login.setOnClickListener(v -> login());
        regiter.setOnClickListener(v -> showRegisterCard());
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

            // LogIn User from DB

            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
            .addOnSuccessListener(authResult -> {
                Intent intent = new Intent(Login.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }).addOnFailureListener(e -> {
                Snackbar.make(constraintLayout,"Incorrect pass or email" + e.getMessage(),Snackbar.LENGTH_LONG).show();
            });


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

            firebaseAuth.createUserWithEmailAndPassword(Objects.requireNonNull(email.getText()).toString(),password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();
                        user.setName(Objects.requireNonNull(name.getText()).toString());
                        user.setEmail(email.getText().toString());
                        user.setPassword(password.getText().toString());
                        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user).addOnSuccessListener(unused -> {
                            Snackbar.make(constraintLayout,"Success",Snackbar.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                        });
                    });
        });

        dialog.show();
    }


}