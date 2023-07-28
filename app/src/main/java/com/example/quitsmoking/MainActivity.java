package com.example.quitsmoking;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quitsmoking.adapters.ViewPagerAdapter;
import com.example.quitsmoking.fragments.Healing_Fragment;
import com.example.quitsmoking.fragments.Home_Fragment;
import com.example.quitsmoking.fragments.Settings_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private Fragment currentFragment;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frameLayout);
        currentFragment = new Home_Fragment();
        setNewFragment(currentFragment);
        viewPager = findViewById(R.id.viewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Change the selected item in the BottomNavigationView
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                // Update the currentFragment based on the selected page
                if (position == 0) {
                    currentFragment = new Home_Fragment();
                } else if (position == 1) {
                    currentFragment = new Healing_Fragment();
                } else if (position == 2) {
                    currentFragment = new Settings_Fragment();
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home && !(currentFragment instanceof Home_Fragment)) {
                viewPager.setCurrentItem(0, true); // Select the first fragment in the ViewPager
                return true;
            } else if (itemId == R.id.healing && !(currentFragment instanceof Healing_Fragment)) {
                viewPager.setCurrentItem(1, true); // Select the second fragment in the ViewPager
                return true;
            } else if (itemId == R.id.settings && !(currentFragment instanceof Settings_Fragment)) {
                viewPager.setCurrentItem(2, true); // Select the third fragment in the ViewPager
                return true;
            }
            return false;
        });
    }

    private void setNewFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        if (currentFragment instanceof Home_Fragment) {
            if (fragment instanceof Healing_Fragment) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (fragment instanceof Settings_Fragment) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else if (currentFragment instanceof Healing_Fragment) {
            if (fragment instanceof Home_Fragment) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (fragment instanceof Settings_Fragment) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        } else if (currentFragment instanceof Settings_Fragment) {
            if (fragment instanceof Home_Fragment) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (fragment instanceof Healing_Fragment) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        }

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}