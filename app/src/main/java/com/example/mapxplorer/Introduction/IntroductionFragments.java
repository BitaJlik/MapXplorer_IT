package com.example.mapxplorer.Introduction;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.mapxplorer.R;

import java.util.Objects;

import at.markushi.ui.CircleButton;

public  class IntroductionFragments {

    private static final int millis = 3000;

    public static class First extends Fragment {
        View view;
        TextView text;
        CircleButton button;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.introduction_item, container, false);
            view.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.intro_first, requireActivity().getTheme()));

            text = view.findViewById(R.id.text);

            button = view.findViewById(R.id.button);
            button.setColor(Color.parseColor("#a9a3ff"));
            button.setOnClickListener(v -> {
                OnceDialogIntroduction.viewPager.setCurrentItem(1);

            });
            new CountDownTimer(millis, 1000) {
                public void onTick(long millisUntilFinished) { }
                public void onFinish() { button.setPressed(true); }
            }.start();

            // Edit only this for text
            text.setText("Простий \n Зручний \n Швидкий! ");
            text.setTextSize(24);

            return view;
        }
    }
    public static class Second extends Fragment {
        View view;
        TextView text;
        CircleButton button;
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.introduction_item, container, false);
            view.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.intro_second, requireActivity().getTheme()));

            text = view.findViewById(R.id.text);
            text.setTextColor(Color.WHITE);
            button = view.findViewById(R.id.button);
            button.setColor(Color.parseColor("#a3ffb8"));
            button.setOnClickListener(v -> OnceDialogIntroduction.viewPager.setCurrentItem(2));
            new CountDownTimer(millis, 1000) {
                public void onTick(long millisUntilFinished) { }
                public void onFinish() { button.setPressed(true); }
            }.start();

            // Edit only this for text
            text.setText("У вашому розпорядженні особливий додаток, розроблений для перегляду всіх магазинів та товару");
            text.setTextSize(24);

            return view;
        }
    }
    public static class Third extends Fragment {
        View view;
        TextView text;
        CircleButton button;
        @SuppressLint("UseCompatLoadingForDrawables") @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.introduction_item, container, false);
            view.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.intro_third, requireActivity().getTheme()));


            text = view.findViewById(R.id.text);
            button = view.findViewById(R.id.button);
            button.setColor(Color.GRAY);
            button.setImageResource(R.drawable.check);
            button.setOnClickListener(v -> requireActivity().finish());
            new CountDownTimer(millis, 1000) {
                public void onTick(long millisUntilFinished) { }
                public void onFinish() { button.setPressed(true); }
            }.start();

            // Edit only this for text
            text.setText("Можливості зашкалюють, так само як і емоції розробників після релізу :)");
            text.setTextSize(24);

            return view;
        }
    }


}
