package com.zxqy.xunilaidian.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zxqy.xunilaidian.R;

public class CallPageActivity extends AppCompatActivity {


    public static void launchActivity(Context context) {
        Intent intent =  new Intent(context, CallPageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_come_call);
    }
}