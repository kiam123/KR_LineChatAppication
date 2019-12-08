package com.example.kr_linechatappication.Activitity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.kr_linechatappication.Adapter.ChatAdapter;
import com.example.kr_linechatappication.Data.ChatData;
import com.example.kr_linechatappication.R;

public class MainActivity extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    ChatAdapter chatAdapter;
    EditText edtReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtReply = (EditText) findViewById(R.id.edt_reply) ;
        chatRecyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(this);
        chatRecyclerView.setAdapter(chatAdapter);
//
        chatAdapter.addItem(new ChatData("Takuma Lee","你好啊，Kr Lee 我是你的導師","",2));
        chatAdapter.addItem(new ChatData("Kr Lee","你好， 我叫Kr Lee，很開心認識你，有幸在你的領導之下，可以變得更強","",1));
    }

    public void onSendClick(View view) {

        chatAdapter.addItem(new ChatData("Kr Lee",edtReply.getText().toString(),"",1));
    }
}
