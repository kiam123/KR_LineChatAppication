package com.example.kr_linechatappication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.kr_linechatappication.R;

public class RegisterActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        setToolbar();
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setToolbar() {
        toolbar.setTitle("註冊");
    }
}
