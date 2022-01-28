package com.example.mapxplorer.Auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.R;
import com.example.mapxplorer.User.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class RegisterFragment extends Fragment {
    static MaterialEditText email;
    static MaterialEditText password;
    static MaterialEditText name;
    MaterialButton register;
    @SuppressLint("StaticFieldLeak")
    static View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_window, container, false);
        super.onCreate(savedInstanceState);

        email = view.findViewById(R.id.emailInput);
        password = view.findViewById(R.id.passwordInput);
        name = view.findViewById(R.id.nameInput);

        register = view.findViewById(R.id.btn_register);
        register.setOnClickListener(v -> {
            if(check()){
                // Registration user in DataBase
                DataBase.auth.createUserWithEmailAndPassword(
                        Objects.requireNonNull(email.getText()).toString(), Objects.requireNonNull(password.getText()).toString())
                        .addOnSuccessListener(authResult -> {
                            User user = new User();
                            user.setName(Objects.requireNonNull(name.getText()).toString());
                            user.setEmail(email.getText().toString());
                            user.setPassword(password.getText().toString());
                            DataBase.reference.child(Objects.requireNonNull(
                                    FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(user)
                                    .addOnSuccessListener(unused -> {
                                        user.setId(FirebaseAuth.getInstance().getUid());
                                        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                        Snackbar.make(view, getStr(R.string.success_reg), Snackbar.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                    });
                        });
            }
        });

        return view;
    }
    private boolean check(){

        if (Objects.requireNonNull(email.getText()).toString().isEmpty()) {
            email.setError(getStr(R.string.input_email));
            return false;
        }
        else if(!email.getText().toString().contains("@") && email.getText().toString().length() > 5){
            email.setError(getStr(R.string.input_email));
            return false;
        }
        if (Objects.requireNonNull(name.getText()).toString().isEmpty()) {
            name.setError(getStr(R.string.input_name));
            return false;
        }
        else if(name.getText().toString().length() < 4){
            name.setError(getStr(R.string.input_name));
            return false;
        }
        if (Objects.requireNonNull(password.getText()).toString().length() < 5) {
            password.setError(getStr(R.string.password_less));
            return false;
        }

        return true;
    }
    public static void clear(){
        if(view != null){
            email.setText("");
            password.setText("");
            name.setText("");

        }
    }
    public String getStr(int Id){ return requireContext().getString(Id); }
}
