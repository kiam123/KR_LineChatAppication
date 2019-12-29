package com.example.kr_linechatappication.fragments;


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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class IconFragment extends Fragment {
    RecyclerView iconRecyclerView;
    IconAdapter iconAdapter;
    ChatAdapter chatAdapter;
    String iconId;

    public IconFragment(ChatAdapter chatAdapter, String iconId) {
        this.chatAdapter = chatAdapter;
        this.iconId = iconId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_icon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        setTestCase();
    }

    public void initView(View view) {
        iconRecyclerView = (RecyclerView) view.findViewById(R.id.iconRecyclerView);
        iconAdapter = new IconAdapter(getActivity(), chatAdapter);
        iconRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        iconRecyclerView.setAdapter(iconAdapter);
    }

    public void setTestCase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("icon").child(iconId).child("icons").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    iconAdapter.addItem(new IconData(snapshot.getValue().toString(),1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
