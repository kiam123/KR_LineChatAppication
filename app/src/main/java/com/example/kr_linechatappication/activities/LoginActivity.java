package com.example.kr_linechatappication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kr_linechatappication.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtAccount;
    TextView txtPassword;
    String account, password;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setToolbar();
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtAccount = (TextView) findViewById(R.id.txtAccount);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
    }

    private void setToolbar() {
        toolbar.setTitle("KRLineChatAppication");
    }

    public void onLoginClick(View view) {
        account = txtAccount.getText().toString();
        password = txtPassword.getText().toString();

        myRef.child("users").child(account).child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals(password)) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    intent.putExtra("account", account);
                    //TODO 需要把朋友刪掉
                    if(account.equals("kr-lee")) {
                        intent.putExtra("friend", "takuma-lee");
                    } else {
                        intent.putExtra("friend", "kr-lee");
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "賬號或密碼錯誤", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }
}
