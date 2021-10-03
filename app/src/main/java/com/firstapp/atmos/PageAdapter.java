package com.firstapp.atmos;

import android.content.Context;
import android.view.View;

import com.firstapp.atmos.Networking.WeatherDataService;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {

    Fragment first;
    Fragment second;

    public PageAdapter(@NonNull FragmentActivity fragmentActivity, Fragment first, Fragment second) {
        super(fragmentActivity);
        this.first = first;
        this.second = second;
    }

    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:

                return this.first;

            case 1:
                return this.second;
        }
        return this.first;
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
