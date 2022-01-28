package com.example.mapxplorer.Introduction;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.mapxplorer.R;

public class OnceDialogIntroduction extends AppCompatActivity {
    View view;
    static ViewPager viewPager;
    @SuppressLint("InflateParams") @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.introduction_dialog_once, null, false);

        viewPager = view.findViewById(R.id.vpPager);
        PagerTabStrip tabStrip = view.findViewById(R.id.pager_header);
        tabStrip.setBackgroundColor(Color.RED);
        tabStrip.setTextColor(Color.WHITE);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int colorFrom = tabStrip.getSolidColor();
                int[] colors = new int[]{ Color.RED, Color.BLUE , Color.parseColor("#009c24")};
                int colorTo = colors[position];

                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(250);
                colorAnimation.addUpdateListener(animator ->  tabStrip.setBackgroundColor((int) animator.getAnimatedValue()));
                colorAnimation.start();
            }

            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override public void onPageScrollStateChanged(int state) { }
        });
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        viewPager.setAdapter(new MyPagerAdapter(fragmentManager));

        setContentView(view);
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private final int NUM_ITEMS = 3;

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
                    return new IntroductionFragments.First();
                case 1:
                    return new IntroductionFragments.Second();
                case 2:
                    return new IntroductionFragments.Third();
                default:
                    return null;
            }

        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Переваги";
            }
            else if(position == 1){
                return "Можливості";
            }
            else if(position == 2){
                return "Інше";
            }
            else return "NULL";
        }

    }
}
