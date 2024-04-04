package com.sr_developers.sowetodrycleaners;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.sr_developers.sowetodrycleaners.helperclass.FragmentHelper;

public class OrdersActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tableLayout;
    private FragmentHelper fragmentHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getSupportActionBar().setTitle("All  Orders");

        viewPager = findViewById(R.id.ViewPagerID);
        tableLayout = findViewById(R.id.LoginTabLayoutID);
        fragmentHelper = new FragmentHelper(getSupportFragmentManager());
        viewPager.setAdapter(fragmentHelper);
        tableLayout.setupWithViewPager(viewPager);

    }

}