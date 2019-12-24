package com.example.kr_linechatappication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kr_linechatappication.adapters.ChatAdapter;
import com.example.kr_linechatappication.adapters.SimpleFragmentPagerAdapter;
import com.example.kr_linechatappication.datas.ChatData;
import com.example.kr_linechatappication.R;
import com.example.kr_linechatappication.fragments.BlankFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    ImageView imgIcon;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<Fragment>();
    SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setChatRecyclerView();
        setIconRecyclerView();

        chatAdapter.addItem(new ChatData("Takuma Lee","你好啊，Kr Lee 我是你的導師","",20));
        chatAdapter.addItem(new ChatData("Kr Lee","https://i.imgur.com/NUyttbnb.jpg","",21));
        chatAdapter.addItem(new ChatData("Kr Lee","你好， 我叫Kr Lee，很開心認識你，有幸在你的領導之下，可以變得更強","",10));
        chatAdapter.addItem(new ChatData("Kr Lee","https://i.imgur.com/NUyttbnb.jpg","",11));
        // Write a message to the database

    }

    public void onSendClick(View view) {
        chatAdapter.addItem(new ChatData("Kr Lee",edtReply.getText().toString(),"",10));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

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
//        iconRecyclerView = (RecyclerView) findViewById(R.id.iconRecyclerView);
        iconViewPager = (ViewPager) findViewById(R.id.viewPager);
        iconTabLayout = (TabLayout) findViewById(R.id.iconTabLayout);
//        imgIcon = (ImageView) findViewById(R.id.imgIcon);
        iconLayout = (LinearLayout) findViewById(R.id.iconLayout);
    }

    private void setChatRecyclerView() {
        chatLinearLayoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(chatLinearLayoutManager);
        chatAdapter = new ChatAdapter(this, chatRecyclerView);
        chatRecyclerView.setAdapter(chatAdapter);
    }

    public void setIconRecyclerView() {
        simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),fragmentArrayList, this, iconTabLayout);
        iconViewPager.setAdapter(simpleFragmentPagerAdapter);
        iconTabLayout.setupWithViewPager(iconViewPager);

        Drawable myIcon = getResources().getDrawable(R.drawable.ic_launcher);
        simpleFragmentPagerAdapter.addItem(new BlankFragment(chatAdapter), myIcon);
        simpleFragmentPagerAdapter.addItem(new BlankFragment(chatAdapter), myIcon);
        simpleFragmentPagerAdapter.addItem(new BlankFragment(chatAdapter), myIcon);
        simpleFragmentPagerAdapter.addItem(new BlankFragment(chatAdapter), myIcon);
        simpleFragmentPagerAdapter.addItem(new BlankFragment(chatAdapter), myIcon);
        simpleFragmentPagerAdapter.addItem(new BlankFragment(chatAdapter), myIcon);
        simpleFragmentPagerAdapter.addItem(new BlankFragment(chatAdapter), myIcon);
        simpleFragmentPagerAdapter.addItem(new BlankFragment(chatAdapter), myIcon);

        setupTabIcons();
    }

    //初始化tab
    private void setupTabIcons() {
        try {
            for(int i=0;i < simpleFragmentPagerAdapter.getIconArrayListSize();i++) {
                iconTabLayout.getTabAt(i).setCustomView(getTabView(i));
            }
        }catch (Exception exception) {
            Log.v("tesst", exception.toString());
        }

    }

    //自定義tab
    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_icon, null);//自定義layout設定tab（部落格，記錄，待吃，個人）裡面的設計
        TextView txt_title = (TextView) view.findViewById(R.id.textView);
//        txt_title.setText(titles.get(position));
        ImageView img_title = (ImageView) view.findViewById(R.id.imageView);
        Picasso.get().load(R.drawable.ic_launcher).into(img_title);

        return view;
    }

    public void onIconClick(View view) {
//        Toast.makeText(this,"1",Toast.LENGTH_LONG).show();
        iconLayout.setVisibility(View.VISIBLE);
    }
}
