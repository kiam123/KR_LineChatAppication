package com.example.kr_linechatappication.fragments;


import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kr_linechatappication.R;
import com.example.kr_linechatappication.adapters.ChatAdapter;
import com.example.kr_linechatappication.adapters.IconAdapter;
import com.example.kr_linechatappication.datas.IconData;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    RecyclerView iconRecyclerView;
    IconAdapter iconAdapter;
    ChatAdapter chatAdapter;

    public BlankFragment(ChatAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    public void initView(View view) {
        iconRecyclerView = (RecyclerView) view.findViewById(R.id.iconRecyclerView);
        iconAdapter = new IconAdapter(getActivity(), chatAdapter);
        iconRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        iconRecyclerView.setAdapter(iconAdapter);

        iconAdapter.addItem(new IconData("https://i.imgur.com/NUyttbnb.jpg",1));
        iconAdapter.addItem(new IconData("https://i.imgur.com/NUyttbnb.jpg",1));
        iconAdapter.addItem(new IconData("https://i.imgur.com/NUyttbnb.jpg",1));
        iconAdapter.addItem(new IconData("https://i.imgur.com/NUyttbnb.jpg",1));
        iconAdapter.addItem(new IconData("https://i.imgur.com/NUyttbnb.jpg",1));
        iconAdapter.addItem(new IconData("https://i.imgur.com/NUyttbnb.jpg",1));
    }

}
