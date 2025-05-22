package com.example.pro_test;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String userID = getIntent().getStringExtra("userID");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putString("userID", userID);

            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, homeFragment)
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                Bundle bundle = new Bundle();
                bundle.putString("userID", userID);

                selFragment = new HomeFragment();
                selFragment.setArguments(bundle);

            } else if (itemId == R.id.nav_record) {
                selFragment = new RecordFragment();
            }

            if (selFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, selFragment)
                        .commit();
            }

            return true;
        });
    }
}

