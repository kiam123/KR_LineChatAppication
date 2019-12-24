package com.example.kr_linechatappication.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.kr_linechatappication.R;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {
    Context mContext;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();
    TabLayout iconTabLayout;

    public SimpleFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList, Context context, TabLayout iconTabLayout) {
        super(fm);
        this.fragmentArrayList = fragmentArrayList;
        this.mContext = context;
        this.iconTabLayout = iconTabLayout;
    }

    public void addItem(Fragment fragment, Drawable icon) {
        fragmentArrayList.add(fragment);
        notifyDataSetChanged();
    }

    public int getIconArrayListSize(){
        return fragmentArrayList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

}
