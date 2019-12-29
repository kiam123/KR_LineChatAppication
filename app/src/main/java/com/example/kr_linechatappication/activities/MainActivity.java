package com.example.kr_linechatappication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kr_linechatappication.adapters.ChatAdapter;
import com.example.kr_linechatappication.adapters.SimpleFragmentPagerAdapter;
import com.example.kr_linechatappication.datas.ChatData;
import com.example.kr_linechatappication.R;
import com.example.kr_linechatappication.datas.UserInfo;
import com.example.kr_linechatappication.fragments.IconFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();
    SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;
    boolean iconFlag = false;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUserData();
        initView();
        setChatRecyclerView();
        setIconRecyclerView();

        chatAdapter.addItem(new ChatData("Takuma Lee","你好啊，Kr Lee 我是你的導師","",20));
        chatAdapter.addItem(new ChatData("Kr Lee","https://i.imgur.com/NUyttbnb.jpg","",21));
        chatAdapter.addItem(new ChatData("Kr Lee","你好， 我叫Kr Lee，很開心認識你，有幸在你的領導之下，可以變得更強","",10));
        chatAdapter.addItem(new ChatData("Kr Lee","https://i.imgur.com/NUyttbnb.jpg","",11));
        // Write a message to the database

    }

    public void getUserData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("kr-lee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.child("icons").getChildren()) {
                    UserInfo.getInstance().addIcon(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void onSendClick(View view) {
        chatAdapter.addItem(new ChatData("Kr Lee",edtReply.getText().toString(),"",10));
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference();

        String id = "kr-Lee";
        String friendId = "takuma-lee";
        String timesamp = Calendar.getInstance().getTimeInMillis()+"";
        myRef.child(id).child("friends").child(friendId).child("chatContent").child(timesamp).child("id").setValue("takuma-lee");
        myRef.child(id).child("friends").child(friendId).child("chatContent").child(timesamp).child("type").setValue("text");
        myRef.child(id).child("friends").child(friendId).child("chatContent").child(timesamp).child("textContent").setValue(edtReply.getText().toString());
        myRef.child(id).child("friends").child(friendId).child("chatContent").child(timesamp).child("imageUrl").setValue("");

        edtReply.setText("");
    }

    private void initView() {
        edtReply = (EditText) findViewById(R.id.edt_reply) ;
        chatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        iconViewPager = (ViewPager) findViewById(R.id.viewPager);
        iconTabLayout = (TabLayout) findViewById(R.id.iconTabLayout);
        iconLayout = (LinearLayout) findViewById(R.id.iconLayout);
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

    public View getTabView(int position, String icon) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_icon, null);
        TextView txt_title = (TextView) view.findViewById(R.id.textView);
        ImageView img_title = (ImageView) view.findViewById(R.id.imageView);
        Picasso.get().load(icon).into(img_title);

        return view;
    }

    public void onIconClick(View view) {
        if(!iconFlag) {
            iconLayout.setVisibility(View.VISIBLE);
            iconFlag = true;

//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference();
            final int size = UserInfo.getInstance().getIconList().size();
            for(int i=0;i < UserInfo.getInstance().getIconList().size();i++) {
                final String iconId = UserInfo.getInstance().getIconList().get(i);
                myRef.child("icon").child(iconId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int i=0;
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            if(simpleFragmentPagerAdapter.getItem().size() < size  && snapshot.getKey().equals("catHeaderUrl")) {
                                Log.v("testttting", snapshot.toString());
                                simpleFragmentPagerAdapter.addItem(new IconFragment(chatAdapter, iconId));
                                iconTabLayout.getTabAt(i).setCustomView(getTabView(i, snapshot.getValue().toString()));
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

}
