package com.example.mapxplorer.Auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class BottomDialogForgot extends BottomSheetDialogFragment {

    public static BottomDialogForgot newInstance() {
        return new BottomDialogForgot();
    }


    @SuppressLint("StaticFieldLeak") @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.forgot_pass, container,false);

        MaterialButton button = view.findViewById(R.id.btn_login_forgot);
        TextInputEditText inputEditText = view.findViewById(R.id.emailInput);
        inputEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
        button.setOnClickListener(v -> {
            if(Objects.requireNonNull(inputEditText.getText()).toString().isEmpty()) return;
            if(Objects.requireNonNull(inputEditText.getText()).toString().length() <= 5) return;
            DataBase.auth.sendPasswordResetEmail(Objects.requireNonNull(inputEditText.getText()).toString());
        });


        return view;
    }
}
