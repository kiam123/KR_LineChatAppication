package com.example.kr_linechatappication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kr_linechatappication.adapters.ChatAdapter;
import com.example.kr_linechatappication.adapters.SimpleFragmentPagerAdapter;
import com.example.kr_linechatappication.R;
import com.example.kr_linechatappication.datas.ChatData2;
import com.example.kr_linechatappication.datas.UserInfo;
import com.example.kr_linechatappication.fragments.IconFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    ChatAdapter chatAdapter;
    LinearLayoutManager chatLinearLayoutManager;
    EditText edtReply;
    ViewPager iconViewPager;
    TabLayout iconTabLayout;
    LinearLayout iconLayout;
    Toolbar toolbar;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();
    SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;
    boolean iconFlag = false;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("error", "wtf");

        getUserData();
        initView();
        setToolbar();
        setChatRecyclerView();
        setIconRecyclerView();
        getChatMessenge();
    }

    public void getUserData() {
        UserInfo.getInstance().setName("kr-lee");
        UserInfo.getInstance().setFriend("takuma-lee");

        myRef.child("users").child(UserInfo.getInstance().getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.child("icons").getChildren()) {
                    UserInfo.getInstance().addIcon(snapshot.getValue().toString());

                }
                for(DataSnapshot snapshot: dataSnapshot.child("friends").getChildren()) {
                    UserInfo.getInstance().addFriend(snapshot.getValue().toString());
                }

                if(dataSnapshot.hasChild("headerImage")) {
                    UserInfo.getInstance().setHeaderImage(dataSnapshot.child("headerImage").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        myRef.child("users").child(UserInfo.getInstance().getFriend()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("headerImage")) {
                    UserInfo.getInstance().setFriendHeaderImage(dataSnapshot.child("headerImage").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void initView() {
        edtReply = (EditText) findViewById(R.id.edt_reply) ;
        chatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        iconViewPager = (ViewPager) findViewById(R.id.viewPager);
        iconTabLayout = (TabLayout) findViewById(R.id.iconTabLayout);
        iconLayout = (LinearLayout) findViewById(R.id.iconLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setToolbar() {
        toolbar.setTitle("KRLineChatAppication");
    }

    private void setChatRecyclerView() {
        chatLinearLayoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(chatLinearLayoutManager);
        chatAdapter = new ChatAdapter(this, chatRecyclerView);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    public void setIconRecyclerView() {
        simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),fragmentArrayList, this);
        iconViewPager.setAdapter(simpleFragmentPagerAdapter);
        iconTabLayout.setupWithViewPager(iconViewPager);
    }

    public void getChatMessenge() {
        final ArrayList<String> chatId = new ArrayList<String>();
        myRef.child("users").child(UserInfo.getInstance().getName()).child("friends").child(UserInfo.getInstance().getFriend()).child("chatId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatAdapter.clearItem();
                chatId.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    chatId.add(snapshot.getValue().toString());
                }
                for(int i=0;i < chatId.size();i++) {
                    myRef.child("chatInfo").child(chatId.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ChatData2 chatData = dataSnapshot.getValue(ChatData2.class);
                            chatAdapter.addItem(chatData);
//                            Log.v("ImageURLl", chatData.getImageUrl());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


    }

    public void onSendClick(View view) {
        String id = UserInfo.getInstance().getName();
        String friendId = UserInfo.getInstance().getFriend();
        String timesamp = Calendar.getInstance().getTimeInMillis()+"";

        ChatData2 chatData = new ChatData2();
        chatData.setImageUrl("");
        chatData.setMessenge(edtReply.getText().toString());
        chatData.setReceiver(UserInfo.getInstance().getFriend());
        chatData.setSender(UserInfo.getInstance().getName());
        chatData.setType("text");

        myRef.child("chatInfo").child(timesamp).setValue(chatData);
        myRef.child("users").child(id).child("friends").child(friendId).child("chatId").child(chatAdapter.getItemCount()+"").setValue(timesamp);
        myRef.child("users").child(friendId).child("friends").child(id).child("chatId").child(chatAdapter.getItemCount()+"").setValue(timesamp);
        edtReply.setText("");
    }

    public View getTabView(String icon) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_icon, null);
        ImageView imgIconHeader = (ImageView) view.findViewById(R.id.imageView);
//        TextView txt_title = (TextView) view.findViewById(R.id.textView);

        Glide.with(this).load(icon).into(imgIconHeader);
        return view;
    }

    public void onIconClick(View view) {
        if(!iconFlag) {
            closeKeyBoard();

            iconLayout.setVisibility(View.VISIBLE);
            iconFlag = true;

            final int size = UserInfo.getInstance().getIconList().size();
            for(int i=0;i < UserInfo.getInstance().getIconList().size();i++) {
                final String iconId = UserInfo.getInstance().getIconList().get(i);
                myRef.child("icon").child(iconId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i=0;
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            if(simpleFragmentPagerAdapter.getItem().size() < size  && snapshot.getKey().equals("catHeaderUrl")) {
                                simpleFragmentPagerAdapter.addItem(new IconFragment(chatAdapter, iconId));
                                iconTabLayout.getTabAt(i).setCustomView(getTabView(snapshot.getValue().toString()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        } else {
            iconLayout.setVisibility(View.GONE);
            iconFlag = false;
        }
    }

    public void onEditTextClick(View view) {
        iconLayout.setVisibility(View.GONE);
        iconFlag = false;
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
