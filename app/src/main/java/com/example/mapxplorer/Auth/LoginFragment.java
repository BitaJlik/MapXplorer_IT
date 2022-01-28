package com.example.mapxplorer.Auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.MainActivity;
import com.example.mapxplorer.MyCustomMapFragment;
import com.example.mapxplorer.R;
import com.example.mapxplorer.User.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class LoginFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    static View view;
    MaterialButton login;
    TextView forgot;
    static MaterialEditText email;
    static MaterialEditText password;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_window, container, false);
        super.onCreate(savedInstanceState);
        email = view.findViewById(R.id.emailInput);
        password = view.findViewById(R.id.passwordInput);

        login = view.findViewById(R.id.btn_login);
        login.setOnClickListener(v1 -> {
            View v = view;
            assert v != null;
            if (TextUtils.isEmpty(email.toString())) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Snackbar.make(v, getStr(R.string.input_email), Snackbar.LENGTH_LONG)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .show();
            }
            else {
                if (Objects.requireNonNull(password.getText()).toString().length() < 5) {
                    password.setError(getStr(R.string.password_less));
                    password.setText("");
                }
                else {
                    // LogIn User from DB
                    DataBase.auth.signInWithEmailAndPassword(
                            Objects.requireNonNull(email.getText()).toString(), password.getText().toString())
                            .addOnSuccessListener(authResult -> {
                                if (!Objects.requireNonNull(DataBase.auth.getCurrentUser()).isEmailVerified()) {
                                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                    Snackbar.make(v, getStr(R.string.verify_email), Snackbar.LENGTH_LONG)
                                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                            .show();
                                    return;
                                }
                                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                for (User user : DataBase.users) {
                                    if (user.getEmail().equals(email.getText().toString())) {
                                        user.setId(FirebaseAuth.getInstance().getUid());
                                        DataBase.ActiveSessionUser = user;
                                        break;
                                    }
                                }
                                MainActivity.setHeader();
                                MainActivity.isViewMap = true;
                                MainActivity.view.setCheckedItem(R.id.nav_Map);
                                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment, new MyCustomMapFragment()).commit();
                            })
                            .addOnFailureListener(e -> {
                                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                Snackbar.make(v, getStr(R.string.incorrect_login), Snackbar.LENGTH_LONG)
                                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                        .show();
                            });
                }
            }
        });

        forgot = view.findViewById(R.id.forgot);
        forgot.setOnClickListener(v -> {
            BottomDialogForgot forgot = BottomDialogForgot.newInstance();
            forgot.show(getChildFragmentManager(),"forgot");
            System.out.println("FORGOT");
        });


        return view;
    }

    public static void clear(){
        if(view != null){
            email.setText("");
            password.setText("");
        }
    }

    public String getStr(int Id){
        return requireContext().getString(Id);
    }
}
