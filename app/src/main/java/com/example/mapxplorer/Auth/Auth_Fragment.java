package com.example.mapxplorer.Auth;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.mapxplorer.MainActivity;
import com.example.mapxplorer.R;

public class Auth_Fragment extends Fragment {
    View view;
    FragmentPagerAdapter adapterViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.auth_fragment, container, false);
        super.onCreate(savedInstanceState);
        ViewPager vpPager = view.findViewById(R.id.vpPager);
        PagerTabStrip tabStrip = view.findViewById(R.id.pager_header);
        tabStrip.setTextColor(Color.WHITE);
        tabStrip.setBackgroundColor(getResources().getColor(R.color.main_color));
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) { }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == 0){
                    LoginFragment.clear();
                }
                if(state == 1){
                    RegisterFragment.clear();
                }
            }
        });
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        return view;
    }


    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private final int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new LoginFragment();
                case 1:
                    return new RegisterFragment();
                default:
                    return null;
            }

        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Login";
            }
            else if(position == 1){
                return "Register";
            }
            else return "NULL";
        }

    }
}
