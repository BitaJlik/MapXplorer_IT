package com.example.mapxplorer;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.mapxplorer.User.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class LoginFragment extends Fragment {
    View view;
    Button regiter;
    Button login;
    ConstraintLayout constraintLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login, container, false);
        super.onCreate(savedInstanceState);

        constraintLayout = view.findViewById(R.id.loginLayout);
        regiter = view.findViewById(R.id.register);
        login = view.findViewById(R.id.login);

        login.setOnClickListener(v -> login());
        regiter.setOnClickListener(v -> showRegisterCard());
        return view;
    }


    private void login() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
        dialog.setTitle("LogIn");
        dialog.setMessage("Input on these inputs");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View registerWindow = inflater.inflate(R.layout.login_window, null);
        dialog.setView(registerWindow);

        MaterialEditText email = registerWindow.findViewById(R.id.emailInput);
        MaterialEditText password = registerWindow.findViewById(R.id.passwordInput);

        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss());
        dialog.setPositiveButton("Confirm", (dialogInterface, which) -> {
            if (TextUtils.isEmpty(email.toString())) {
                Snackbar.make(constraintLayout, "Input email", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (Objects.requireNonNull(password.getText()).toString().length() < 5) {
                Snackbar.make(constraintLayout, "Input password more than 5 symbols", Snackbar.LENGTH_SHORT).show();
                return;
            }
            // LogIn User from DB


            DataBase.auth.signInWithEmailAndPassword(
                    Objects.requireNonNull(email.getText()).toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        if (!Objects.requireNonNull(DataBase.auth.getCurrentUser()).isEmailVerified()) {
                            Snackbar.make(constraintLayout, "Please, verify email", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        for (User user : DataBase.users) {
                            if (user.getEmail().equals(email.getText().toString())) {
                                DataBase.ActiveSessionUser = user;
                                break;
                            }
                        }
                        MapsActivity.setHeader();
                        MapsActivity.isViewMap = true;
                        MapsActivity.view.setCheckedItem(R.id.nav_Map);
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment, MapsActivity.fragment).commit();
                    }).addOnFailureListener(e -> Snackbar.make(constraintLayout, "Incorrect pass or email", Snackbar.LENGTH_LONG).show());
        });
        dialog.show();
    }

    private void showRegisterCard() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());

        dialog.setTitle("Registration");
        dialog.setMessage("Input on these inputs");

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View registerWindow = inflater.inflate(R.layout.register_window, null);
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
                    Objects.requireNonNull(email.getText()).toString(), password.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        User user = new User();
                        user.setName(Objects.requireNonNull(name.getText()).toString());
                        user.setEmail(email.getText().toString());
                        user.setPassword(password.getText().toString());
                        DataBase.reference.child(Objects.requireNonNull(
                                FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user)
                                .addOnSuccessListener(unused -> {
                                    Snackbar.make(constraintLayout,
                                            "Success register\nVerify Email please", Snackbar.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                });
                    });
        });
        dialog.show();
    }
}
