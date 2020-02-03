package com.example.kr_linechatappication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kr_linechatappication.adapters.ChatAdapter;
import com.example.kr_linechatappication.adapters.SimpleFragmentPagerAdapter;
import com.example.kr_linechatappication.R;
import com.example.kr_linechatappication.datas.ChatData;
import com.example.kr_linechatappication.datas.UserInfo;
import com.example.kr_linechatappication.fragments.IconFragment;
import com.example.kr_linechatappication.notifications.APIService;
import com.example.kr_linechatappication.notifications.Client;
import com.example.kr_linechatappication.notifications.Data;
import com.example.kr_linechatappication.notifications.MyResponse;
import com.example.kr_linechatappication.notifications.Sender;
import com.example.kr_linechatappication.notifications.Token;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    APIService apiService;
    private String account;
    private String friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("error", "wtf");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult ->
                updateToken(instanceIdResult.getToken()));

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        getUserData();
        initView();
        setToolbar();
        setChatRecyclerView();
        setIconRecyclerView();
        getChatMessenge();
    }

    public void updateToken(String token) {
        account = getIntent().getStringExtra("account");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(account).setValue(token1);
    }

    public void getUserData() {
        //TODO 之後擴充可能需要把朋友刪掉
        account = getIntent().getStringExtra("account");
        friend = getIntent().getStringExtra("friend");
        UserInfo.getInstance().setName(account);
        UserInfo.getInstance().setFriend(friend);

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
                            ChatData chatData = dataSnapshot.getValue(ChatData.class);
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
        String msg = edtReply.getText().toString();

        ChatData chatData = new ChatData();
        chatData.setImageUrl("");
        chatData.setMessenge(msg);
        chatData.setReceiver(UserInfo.getInstance().getFriend());
        chatData.setSender(UserInfo.getInstance().getName());
        chatData.setType("text");

        myRef.child("chatInfo").child(timesamp).setValue(chatData);
        myRef.child("users").child(id).child("friends").child(friendId).child("chatId").child(chatAdapter.getItemCount()+"").setValue(timesamp);
        myRef.child("users").child(friendId).child("friends").child(id).child("chatId").child(chatAdapter.getItemCount()+"").setValue(timesamp);

        sendNotification(friendId, id, msg);
        edtReply.setText("");

    }

    public void sendNotification(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(receiver, R.mipmap.ic_launcher, username + ": " + message, "Chat",
                            UserInfo.getInstance().getName());

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
