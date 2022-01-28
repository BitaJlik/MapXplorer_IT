package com.example.mapxplorer;

import static com.example.mapxplorer.MainActivity.fragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mapxplorer.Auth.BottomDialogForgot;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class Settings extends Fragment {
    View view;
    SwitchCompat darkTheme_switch;
    TextView darkTheme_text;
    public static boolean isSwitch = false;

    MaterialButton aboutDev;
    @SuppressLint("InflateParams") @Nullable @Override public View onCreateView(LayoutInflater inflater,
                                                 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        inflater.getContext().setTheme(R.style.Theme_MapXplorer_Day);
        view = inflater.inflate(R.layout.settings_fragment,null,false);
        darkTheme_switch = view.findViewById(R.id.dark_switch);
        darkTheme_switch.setChecked(MainActivity.isDarkTheme);

        darkTheme_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MainActivity.isDarkTheme = !MainActivity.isDarkTheme;
            isSwitch = true;
            if(MainActivity.isDarkTheme){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        darkTheme_text = view.findViewById(R.id.dark_text);
        darkTheme_text.setText(R.string.dark);
        aboutDev = view.findViewById(R.id.aboutDev);
        aboutDev.setOnClickListener(v -> {
            BottomAboutDev aboutDev = BottomAboutDev.newInstance();
            aboutDev.show(getChildFragmentManager(),"aboutDev");
        });
        return view;
    }


    public static class BottomAboutDev extends BottomSheetDialogFragment {

        public static BottomAboutDev newInstance() {
            return new BottomAboutDev();
        }

        @SuppressLint("StaticFieldLeak") @Nullable @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            
            View view = inflater.inflate(R.layout.about_devs, container,false);

            MaterialTextView about_bit = view.findViewById(R.id.about_BitaJlik);
            about_bit.setText("Vitali Tkach \n Main developer\n Tech lead");
            MaterialTextView about_max = view.findViewById(R.id.about_Max);
            about_max.setText("Maxim Zaharchuk \n Main  ");
            MaterialTextView about_den = view.findViewById(R.id.about_Denis);
            about_den.setText("Denis Sayetskiy \n Support developer");
            view.setOnClickListener(v -> {
                Objects.requireNonNull(getDialog()).hide();
            });
            Objects.requireNonNull(getDialog()).setOnShowListener(dialog -> {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
                assert bottomSheetInternal != null;
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            });

            return view;
        }
    }
}
