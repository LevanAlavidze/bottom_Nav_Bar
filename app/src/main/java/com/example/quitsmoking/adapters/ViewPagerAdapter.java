package com.example.quitsmoking.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quitsmoking.fragments.Healing_Fragment;
import com.example.quitsmoking.fragments.Home_Fragment;
import com.example.quitsmoking.fragments.Settings_Fragment;


public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Home_Fragment();
            case 1:
                return new Healing_Fragment();
            case 2:
                return new Settings_Fragment();
            default:
                return new Home_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of fragments in the ViewPager
    }
}
